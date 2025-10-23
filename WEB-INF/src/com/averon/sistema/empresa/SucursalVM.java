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


import com.averon.modelo.Sucursal;
import com.averon.modelo.SucursalUsuario;
import com.averon.util.ParamsLocal;
import com.averon.util.TemplateViewModelLocal;
import com.doxacore.components.finder.FinderModel;
import com.doxacore.modelo.Tipo;
import com.doxacore.modelo.Usuario;



public class SucursalVM extends TemplateViewModelLocal{
	
	private List<Object[]> lSucursales;
	private List<Object[]> lSucursalesOri;
	private Sucursal sucursalSelected;
	private Usuario usuarioSelected;
	
	private boolean opCrearSucursal;
	private boolean opEditarSucursal;
	private boolean opBorrarSucursal;

	private boolean editar = false;

	@Init(superclass = true)
	public void initSucursalVM() {

		this.inicializarFiltros();
		this.cargarSucursales();
		
	}

	@AfterCompose(superclass = true)
	public void afterComposeSucursalVM() {

	}

	
	@Override
	protected void inicializarOperaciones() {
		this.opCrearSucursal = this.operacionHabilitada(ParamsLocal.OP_CREAR_SUCURSAL);
		this.opEditarSucursal = this.operacionHabilitada(ParamsLocal.OP_EDITAR_SUCURSAL);
		this.opBorrarSucursal = this.operacionHabilitada(ParamsLocal.OP_BORRAR_SUCURSAL);
		
	}
	
	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[3];

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lSucursales")
	public void filtrarSucursal() {

		this.lSucursales = this.filtrarListaObject(this.filtroColumns, this.lSucursalesOri);

	}

	private void cargarSucursales() {

		String sql = this.um.getSql("sucursal/listaSucursal.sql").replace("?1", this.getCurrentEmpresa().getEmpresaid()+"");
		this.lSucursales = this.reg.sqlNativo(sql);
		this.lSucursalesOri = this.lSucursales;
		
		this.filtrarSucursal();

	}
	
	//seccion modal
	
	private Window modal;
	
	@Command
	public void modalSucursalAgregar() {

		if (!this.opCrearSucursal)
			return;

		this.editar = false;
		this.modalSucursal(-1);

	}

	@Command
	public void modalSucursal(@BindingParam("sucursalid") long sucursalid) {
		
	
		this.usuarioSelected = null;

		if (sucursalid != -1) {

			if (!this.opEditarSucursal)
				return;

			this.editar = true;

			this.sucursalSelected = this.reg.getObjectById(Sucursal.class.getName(), sucursalid);

		} else {

			this.sucursalSelected = new Sucursal();
			//this.sucursalSelected.setEmpresa(getCurrentEmpresa());

		}

		modal = (Window) Executions.createComponents("/sistema/zul/empresa/sucursalModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();
		
		this.inicializarFinders();

	}
	
	@Command
	@NotifyChange("lSucursales")
	public void guardar() {

		this.sucursalSelected = this.save(this.sucursalSelected);

		this.cargarSucursales();

		this.modal.detach();

		if (editar) {

			Notification.show("Sucursal Actualizada.");

			this.editar = false;

		} else {

			Notification.show("Los datos de la Nueva Sucursal fueron agragados.");
		}

	}
	
	
	
	
	private FinderModel empresaUsuarioFinder;
	private FinderModel comprobanteTipoFinder;

	@NotifyChange("*")
	public void inicializarFinders() {
		
		String buscarTiposPorSiglaTipotipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql");

		String sqlComprobanteTipo = buscarTiposPorSiglaTipotipo.replace("?1", ParamsLocal.SIGLA_TIPOTIPO_COMPROBANTE );
		comprobanteTipoFinder = new FinderModel("Comprobante", sqlComprobanteTipo);

		String sqlUsuario = this.um.getSql("empresaUsuario/buscarEmpresaUsuario.sql").replace("?1", this.getCurrentEmpresa().getEmpresaid()+"" );
		empresaUsuarioFinder = new FinderModel("Usuario", sqlUsuario);
		
	}

	public void generarFinders(@BindingParam("finder") String finder) {

		
		if (finder.compareTo(this.comprobanteTipoFinder.getNameFinder()) == 0) {

			this.comprobanteTipoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.comprobanteTipoFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.empresaUsuarioFinder.getNameFinder()) == 0) {

			this.empresaUsuarioFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.empresaUsuarioFinder, "listFinder");

			return;
		}
		
		
		

	}

	@Command
	public void finderFilter(@BindingParam("filter") String filter, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.comprobanteTipoFinder.getNameFinder()) == 0) {

			this.comprobanteTipoFinder.setListFinder(this.filtrarListaObject(filter, this.comprobanteTipoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.comprobanteTipoFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.empresaUsuarioFinder.getNameFinder()) == 0) {

			this.empresaUsuarioFinder.setListFinder(this.filtrarListaObject(filter, this.empresaUsuarioFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.empresaUsuarioFinder, "listFinder");

			return;
		}
	
	}

	@Command
	@NotifyChange({"*"})
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.comprobanteTipoFinder.getNameFinder()) == 0) {

			this.sucursalSelected.setComprobanteTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			return;
			
		}
		
		if (finder.compareTo(this.empresaUsuarioFinder.getNameFinder()) == 0) {

			this.usuarioSelected = this.reg.getObjectById(Usuario.class.getName(), id);
			return;
			
		}

	}
	
	@Command
	@NotifyChange({"sucursalSelected", "usuarioSelected"})
	public void agregarUsuario() {
		
		SucursalUsuario su = new SucursalUsuario();
		
		su.setEmpresa(this.sucursalSelected.getEmpresa());
		su.setSucursal(this.sucursalSelected);
		su.setUsuario(usuarioSelected);
		
		this.sucursalSelected.getUsuarios().add(su);
		
		this.usuarioSelected = null;
		
	}

	public List<Object[]> getlSucursales() {
		return lSucursales;
	}

	public void setlSucursales(List<Object[]> lSucursales) {
		this.lSucursales = lSucursales;
	}

	public Sucursal getSucursalSelected() {
		return sucursalSelected;
	}

	public void setSucursalSelected(Sucursal sucursalSelected) {
		this.sucursalSelected = sucursalSelected;
	}

	public boolean isOpCrearSucursal() {
		return opCrearSucursal;
	}

	public void setOpCrearSucursal(boolean opCrearSucursal) {
		this.opCrearSucursal = opCrearSucursal;
	}

	public boolean isOpEditarSucursal() {
		return opEditarSucursal;
	}

	public void setOpEditarSucursal(boolean opEditarSucursal) {
		this.opEditarSucursal = opEditarSucursal;
	}

	public boolean isOpBorrarSucursal() {
		return opBorrarSucursal;
	}

	public void setOpBorrarSucursal(boolean opBorrarSucursal) {
		this.opBorrarSucursal = opBorrarSucursal;
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

	public FinderModel getEmpresaUsuarioFinderFinder() {
		return empresaUsuarioFinder;
	}

	public void setEmpresaUsuarioFinder(FinderModel empresaUsuarioFinder) {
		this.empresaUsuarioFinder = empresaUsuarioFinder;
	}

	public Usuario getUsuarioSelected() {
		return usuarioSelected;
	}

	public void setUsuarioSelected(Usuario usuarioSelected) {
		this.usuarioSelected = usuarioSelected;
	}

	public FinderModel getEmpresaUsuarioFinder() {
		return empresaUsuarioFinder;
	}

	public FinderModel getComprobanteTipoFinder() {
		return comprobanteTipoFinder;
	}

	public void setComprobanteTipoFinder(FinderModel comprobanteTipoFinder) {
		this.comprobanteTipoFinder = comprobanteTipoFinder;
	}
	
	

}
