package com.averon.sistema.empresa;

import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zul.Window;

import com.averon.modelo.EmpresaUsuario;
import com.averon.modelo.Funcionario;
import com.averon.modelo.SucursalUsuario;
import com.averon.util.ParamsLocal;
import com.averon.util.TemplateViewModelLocal;
import com.doxacore.components.finder.FinderModel;
import com.doxacore.modelo.Tipo;
import com.doxacore.modelo.Usuario;
import com.doxacore.util.UtilStaticMetodos;



public class FuncionarioVM extends TemplateViewModelLocal{
	
	private List<Object[]> lFuncionarios;
	private List<Object[]> lFuncionariosOri;
	private Funcionario funcionarioSelected;
	private Usuario usuarioSelected;
	
	private boolean opCrearFuncionario;
	private boolean opEditarFuncionario;
	private boolean opBorrarFuncionario;

	private boolean editar = false;

	@Init(superclass = true)
	public void initFuncionarioVM() {

		this.inicializarFiltros();
		this.cargarFuncionarios();
		

	}

	@AfterCompose(superclass = true)
	public void afterComposeFuncionarioVM() {

	}

	
	@Override
	protected void inicializarOperaciones() {
		this.opCrearFuncionario = this.operacionHabilitada(ParamsLocal.OP_CREAR_FUNCIONARIO);
		this.opEditarFuncionario = this.operacionHabilitada(ParamsLocal.OP_EDITAR_FUNCIONARIO);
		this.opBorrarFuncionario = this.operacionHabilitada(ParamsLocal.OP_BORRAR_FUNCIONARIO);
		
	}
	
	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[6];

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lFuncionarios")
	public void filtrarFuncionario() {

		this.lFuncionarios = this.filtrarListaObject(this.filtroColumns, this.lFuncionariosOri);

	}

	private void cargarFuncionarios() {

		String sql = this.um.getSql("funcionario/listaFuncionarios.sql").replace("?1", this.getCurrentEmpresa().getEmpresaid()+"");
		this.lFuncionarios = this.reg.sqlNativo(sql);
		this.lFuncionariosOri = this.lFuncionarios;
		
		this.filtrarFuncionario();

	}
	
	//seccion modal
	
	private Window modal;
	
	@Command
	public void modalFuncionarioAgregar() {

		if (!this.opCrearFuncionario)
			return;

		this.editar = false;
		this.modalFuncionario(-1);

	}

	@Command
	public void modalFuncionario(@BindingParam("funcionarioid") long funcionarioid) {

		this.inicializarFinders();

		if (funcionarioid != -1) {

			if (!this.opEditarFuncionario)
				return;

			this.editar = true;

			this.funcionarioSelected = this.reg.getObjectById(Funcionario.class.getName(), funcionarioid);
			
			if (this.funcionarioSelected.getUsuario() != null) {
				
				this.usuarioSelected = this.funcionarioSelected.getUsuario();
				
			}else {
				
				this.usuarioSelected = new Usuario();
				
			}

		} else {

			this.funcionarioSelected = new Funcionario();
			this.funcionarioSelected.getPersona().setEmpresa(getCurrentEmpresa());
			this.usuarioSelected = new Usuario();
		}

		modal = (Window) Executions.createComponents("/sistema/zul/empresa/funcionarioModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	@Command
	@NotifyChange("lFuncionarios")
	public void guardar() {
		
		boolean generar_EU_SU = false;
		
		if (this.usuarioSelected.getAccount() != null) {
			
			this.usuarioSelected.setEmail(this.funcionarioSelected.getPersona().getEmail());
			this.usuarioSelected.setFullName(this.funcionarioSelected.getPersona().getFullName());
			
			if (this.usuarioSelected.getPassword() != null || this.usuarioSelected.getPassword().matches("^[a-fA-F0-9]{64}$") ) {
				
				this.usuarioSelected.setPassword(UtilStaticMetodos.getSHA256(this.usuarioSelected.getPassword()));
				
			}
			
			if (this.funcionarioSelected.isActivo() == false) {
				
				this.usuarioSelected.setActivo(this.funcionarioSelected.isActivo());
				
			}
			
			if (this.funcionarioSelected.getUsuario() == null) {
				
				generar_EU_SU = true;
				
			}
			
			this.funcionarioSelected.setUsuario(this.usuarioSelected);

		}

		this.funcionarioSelected = this.save(this.funcionarioSelected);
		
		if (generar_EU_SU) {
			
			EmpresaUsuario eu = new EmpresaUsuario();
			eu.setUsuario(this.funcionarioSelected.getUsuario());
			eu = this.save(eu);
			
			SucursalUsuario su = new SucursalUsuario();
			su.setUsuario(this.funcionarioSelected.getUsuario());
			su.setSucursal(getCurrentSucursal());
			
			su = this.save(su);
			
		}

		if (editar) {

			Notification.show("Funcionario Actualizada.");

			this.editar = false;

		} else {
			
			
			Notification.show("Los datos de la Nueva Funcionario fueron agragadas.");
		}
		
		this.cargarFuncionarios();

		this.modal.detach();

	}
	
	private FinderModel documentoTipoFinder;
	private FinderModel funcionarioTipoFinder;

	@NotifyChange("*")
	public void inicializarFinders() {

		String sqlDocumentoTipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1", ParamsLocal.SIGLA_TIPOTIPO_DOCUMENTO );
		documentoTipoFinder = new FinderModel("Documento", sqlDocumentoTipo);
		
		String sqlFuncionarioTipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1", ParamsLocal.SIGLA_TIPOTIPO_FUNCIONARIO );
		funcionarioTipoFinder = new FinderModel("Funcionario", sqlFuncionarioTipo);

	}

	public void generarFinders(@BindingParam("finder") String finder) {

		if (finder.compareTo(this.documentoTipoFinder.getNameFinder()) == 0) {

			this.documentoTipoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.documentoTipoFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.funcionarioTipoFinder.getNameFinder()) == 0) {

			this.funcionarioTipoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.funcionarioTipoFinder, "listFinder");

			return;
		}

	}

	@Command
	public void finderFilter(@BindingParam("filter") String filter, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.documentoTipoFinder.getNameFinder()) == 0) {

			this.documentoTipoFinder.setListFinder(this.filtrarListaObject(filter, this.documentoTipoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.documentoTipoFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.funcionarioTipoFinder.getNameFinder()) == 0) {

			this.funcionarioTipoFinder.setListFinder(this.filtrarListaObject(filter, this.funcionarioTipoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.funcionarioTipoFinder, "listFinder");

			return;
		}


	}

	@Command
	@NotifyChange("*")
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.documentoTipoFinder.getNameFinder()) == 0) {

			this.funcionarioSelected.getPersona().setDocumentoTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			return;
		}
		
		if (finder.compareTo(this.funcionarioTipoFinder.getNameFinder()) == 0) {

			this.funcionarioSelected.setFuncionarioTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			return;
		}


	}

	public List<Object[]> getlFuncionarios() {
		return lFuncionarios;
	}

	public void setlFuncionarios(List<Object[]> lFuncionarios) {
		this.lFuncionarios = lFuncionarios;
	}

	public Funcionario getFuncionarioSelected() {
		return funcionarioSelected;
	}

	public void setFuncionarioSelected(Funcionario funcionarioSelected) {
		this.funcionarioSelected = funcionarioSelected;
	}

	public boolean isOpCrearFuncionario() {
		return opCrearFuncionario;
	}

	public void setOpCrearFuncionario(boolean opCrearFuncionario) {
		this.opCrearFuncionario = opCrearFuncionario;
	}

	public boolean isOpEditarFuncionario() {
		return opEditarFuncionario;
	}

	public void setOpEditarFuncionario(boolean opEditarFuncionario) {
		this.opEditarFuncionario = opEditarFuncionario;
	}

	public boolean isOpBorrarFuncionario() {
		return opBorrarFuncionario;
	}

	public void setOpBorrarFuncionario(boolean opBorrarFuncionario) {
		this.opBorrarFuncionario = opBorrarFuncionario;
	}

	public boolean isEditar() {
		return editar;
	}

	public void setEditar(boolean editar) {
		this.editar = editar;
	}

	public String[] getFiltroColumns() {
		return filtroColumns;
	}

	public void setFiltroColumns(String[] filtroColumns) {
		this.filtroColumns = filtroColumns;
	}

	public FinderModel getDocumentoTipoFinder() {
		return documentoTipoFinder;
	}

	public void setDocumentoTipoFinder(FinderModel documentoTipoFinder) {
		this.documentoTipoFinder = documentoTipoFinder;
	}

	public FinderModel getFuncionarioTipoFinder() {
		return funcionarioTipoFinder;
	}

	public void setFuncionarioTipoFinder(FinderModel funcionarioTipoFinder) {
		this.funcionarioTipoFinder = funcionarioTipoFinder;
	}

	public Usuario getUsuarioSelected() {
		return usuarioSelected;
	}

	public void setUsuarioSelected(Usuario usuarioSelected) {
		this.usuarioSelected = usuarioSelected;
	}
	
	

}
