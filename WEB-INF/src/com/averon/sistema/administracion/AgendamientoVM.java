package com.averon.sistema.administracion;

import java.util.Date;
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

import com.averon.modelo.Agendamiento;
import com.averon.modelo.Funcionario;
import com.averon.modelo.Persona;
import com.averon.modelo.Producto;
import com.averon.util.ParamsLocal;
import com.averon.util.TemplateViewModelLocal;
import com.doxacore.components.finder.FinderModel;
import com.doxacore.modelo.Tipo;
import com.doxacore.modelo.Usuario;



public class AgendamientoVM extends TemplateViewModelLocal{
	
	private List<Object[]> lAgendamientos;
	private List<Object[]> lAgendamientosOri;
	private Agendamiento agendamientoSelected;
	
	private boolean opCrearAgendamiento;
	private boolean opEditarAgendamiento;
	private boolean opBorrarAgendamiento;

	private boolean editar = false;

	@Init(superclass = true)
	public void initAgendamientoVM() {

		this.inicializarFiltros();
		this.cargarAgendamientos();
		

	}

	@AfterCompose(superclass = true)
	public void afterComposeAgendamientoVM() {

	}

	
	@Override
	protected void inicializarOperaciones() {
		
		this.opCrearAgendamiento = this.operacionHabilitada(ParamsLocal.OP_CREAR_AGENDAMIENTO);
		this.opEditarAgendamiento = this.operacionHabilitada(ParamsLocal.OP_EDITAR_AGENDAMIENTO);
		this.opBorrarAgendamiento = this.operacionHabilitada(ParamsLocal.OP_BORRAR_AGENDAMIENTO);
		
	}
	
	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[3];

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lAgendamientos")
	public void filtrarAgendamiento() {

		this.lAgendamientos = this.filtrarListaObject(this.filtroColumns, this.lAgendamientosOri);

	}

	private void cargarAgendamientos() {

		String sql = this.um.getSql("agendamiento/listaAgendamiento.sql")
				.replace("?1", this.getCurrentEmpresa().getEmpresaid()+"")
				.replace("?2", this.getCurrentSucursal().getSucursalid()+"");
		
		this.lAgendamientos = this.reg.sqlNativo(sql);
		this.lAgendamientosOri = this.lAgendamientos;

	}
	
	//seccion modal
	
	private Window modal;
	
	@Command
	public void modalAgendamientoAgregar() {

		if (!this.opCrearAgendamiento)
			return;

		this.editar = false;
		this.modalAgendamiento(-1);

	}

	@Command
	public void modalAgendamiento(@BindingParam("agendamientoid") long agendamientoid) {

		this.inicializarFinders();
		
		if (agendamientoid != -1) {

			if (!this.opEditarAgendamiento)
				return;

			this.editar = true;

			this.agendamientoSelected = this.reg.getObjectById(Agendamiento.class.getName(), agendamientoid);

		} else {

			this.agendamientoSelected = new Agendamiento();
			this.agendamientoSelected.setSucursal(this.getCurrentSucursal());
			
			this.agendamientoSelected.setInicio(this.um.modificarHorasMinutosSegundos(new Date(), 9, 0, 0, 0));
			this.agendamientoSelected.setFin(this.um.modificarHorasMinutosSegundos(new Date(), 10, 0, 0, 0));
			this.agendamientoSelected.setSucursal(this.getCurrentSucursal());
			this.agendamientoSelected.setAgendamientoTipo(this.reg.getObjectBySigla(Tipo.class, ParamsLocal.SIGLA_TIPO_AGENDAMIENTO_SERVICIO));

		}
		
		this.habilitarGrupos();

		modal = (Window) Executions.createComponents("/sistema/zul/administracion/agendamientoModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	private Boolean[] gruposDisable = {false, false};
	
	@Command
	@NotifyChange("lAgendamientos")
	public void guardar() {

		this.save(this.agendamientoSelected);

		this.modal.detach();

		if (editar) {

			Notification.show("Agendamiento Actualizado.");

			this.editar = false;

		} else {

			Notification.show("Los datos del Nuevo Agendamiento fueron agragados.");
		}
		
		this.cargarAgendamientos();

	}
	
	private FinderModel funcionarioFinder;
	private FinderModel personaFinder;
	private FinderModel servicioFinder;
	private FinderModel agendamientoTipoFinder;

	@NotifyChange("*")
	public void inicializarFinders() {
		
		String buscarTiposPorSiglaTipotipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql");
		
		String sqlAgendamientoTipo = buscarTiposPorSiglaTipotipo.replace("?1", ParamsLocal.SIGLA_TIPOTIPO_AGENDAMIENTO );
		agendamientoTipoFinder = new FinderModel("agendamientoTipo",sqlAgendamientoTipo );

		String sqlFuncionario = this.um.getSql("funcionario/buscarFuncionarios.sql").replace("?1", this.getCurrentEmpresa().getEmpresaid()+"");
		funcionarioFinder = new FinderModel("Funcionario", sqlFuncionario);
		
		String sqlPersona = this.um.getSql("persona/buscarPersona.sql").replace("?1", this.getCurrentEmpresa().getEmpresaid()+"");
		personaFinder = new FinderModel("Persona", sqlPersona);
		
		String sqlServicio = this.um.getSql("servicio/buscarServicio.sql")
				.replace("?1", this.getCurrentEmpresa().getEmpresaid()+"");
		servicioFinder = new FinderModel("Servicio", sqlServicio);
		
	}

	public void generarFinders(@BindingParam("finder") String finder) {

		if (finder.compareTo(this.funcionarioFinder.getNameFinder()) == 0) {

			this.funcionarioFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.funcionarioFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.personaFinder.getNameFinder()) == 0) {

			this.personaFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.personaFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.agendamientoTipoFinder.getNameFinder()) == 0) {

			this.agendamientoTipoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.agendamientoTipoFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.servicioFinder.getNameFinder()) == 0) {

			this.servicioFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.servicioFinder, "listFinder");

			return;
		}

	}

	@Command
	public void finderFilter(@BindingParam("filter") String filter, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.agendamientoTipoFinder.getNameFinder()) == 0) {

			this.agendamientoTipoFinder.setListFinder(this.filtrarListaObject(filter, this.agendamientoTipoFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.agendamientoTipoFinder, "listFinder");

			return;
		}
		
		
		if (finder.compareTo(this.funcionarioFinder.getNameFinder()) == 0) {

			this.personaFinder.setListFinder(this.filtrarListaObject(filter, this.funcionarioFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.funcionarioFinder, "listFinder");

			return;
		}
		

		if (finder.compareTo(this.personaFinder.getNameFinder()) == 0) {

			this.personaFinder.setListFinder(this.filtrarListaObject(filter, this.personaFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.personaFinder, "listFinder");

			return;
		}
		
		if (finder.compareTo(this.servicioFinder.getNameFinder()) == 0) {

			this.servicioFinder.setListFinder(this.filtrarListaObject(filter, this.servicioFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.servicioFinder, "listFinder");

			return;
		}


	}

	@Command
	@NotifyChange("*")
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {
		
		if (finder.compareTo(this.agendamientoTipoFinder.getNameFinder()) == 0) {

			this.agendamientoSelected.setAgendamientoTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			
			this.habilitarGrupos();
			
			return;
		}
		

		if (finder.compareTo(this.funcionarioFinder.getNameFinder()) == 0) {

			this.agendamientoSelected.setFuncionario(this.reg.getObjectById(Funcionario.class.getName(), id));
			return;
		}
		
		if (finder.compareTo(this.personaFinder.getNameFinder()) == 0) {

			this.agendamientoSelected.setPersona(this.reg.getObjectById(Persona.class.getName(), id));
			return;
		}
		
		if (finder.compareTo(this.servicioFinder.getNameFinder()) == 0) {

			this.agendamientoSelected.setServicio(this.reg.getObjectById(Producto.class.getName(), id));
			return;
		}

	}
	
	public void habilitarGrupos() {
		
		this.gruposDisable[0] = false;
		this.gruposDisable[1] = false;
		
		if (this.agendamientoSelected.getAgendamientoTipo().getSigla().compareTo(ParamsLocal.SIGLA_TIPO_AGENDAMIENTO_SERVICIO) == 0) {
			
			this.gruposDisable[0] = true;
			
		}else if (this.agendamientoSelected.getAgendamientoTipo().getSigla().compareTo(ParamsLocal.SIGLA_TIPO_AGENDAMIENTO_RECORDATORIO) == 0) {
			
			this.gruposDisable[1] = true;
			
		}
		
	}
	
	
	public List<Object[]> getlAgendamientos() {
		return lAgendamientos;
	}

	public void setlAgendamientos(List<Object[]> lAgendamientos) {
		this.lAgendamientos = lAgendamientos;
	}

	public Agendamiento getAgendamientoSelected() {
		return agendamientoSelected;
	}

	public void setAgendamientoSelected(Agendamiento agendamientoSelected) {
		this.agendamientoSelected = agendamientoSelected;
	}

	public boolean isOpCrearAgendamiento() {
		return opCrearAgendamiento;
	}

	public void setOpCrearAgendamiento(boolean opCrearAgendamiento) {
		this.opCrearAgendamiento = opCrearAgendamiento;
	}

	public boolean isOpEditarAgendamiento() {
		return opEditarAgendamiento;
	}

	public void setOpEditarAgendamiento(boolean opEditarAgendamiento) {
		this.opEditarAgendamiento = opEditarAgendamiento;
	}

	public boolean isOpBorrarAgendamiento() {
		return opBorrarAgendamiento;
	}

	public void setOpBorrarAgendamiento(boolean opBorrarAgendamiento) {
		this.opBorrarAgendamiento = opBorrarAgendamiento;
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

	public FinderModel getFuncionarioFinder() {
		return funcionarioFinder;
	}

	public void setFuncionarioFinder(FinderModel funcionarioFinder) {
		this.funcionarioFinder = funcionarioFinder;
	}

	public FinderModel getPersonaFinder() {
		return personaFinder;
	}

	public void setPersonaFinder(FinderModel personaFinder) {
		this.personaFinder = personaFinder;
	}

	public Boolean[] getGruposDisable() {
		return gruposDisable;
	}

	public void setGruposDisable(Boolean[] gruposDisable) {
		this.gruposDisable = gruposDisable;
	}

	public FinderModel getAgendamientoTipoFinder() {
		return agendamientoTipoFinder;
	}

	public void setAgendamientoTipoFinder(FinderModel agendamientoTipoFinder) {
		this.agendamientoTipoFinder = agendamientoTipoFinder;
	}

	public FinderModel getServicioFinder() {
		return servicioFinder;
	}

	public void setServicioFinder(FinderModel servicioFinder) {
		this.servicioFinder = servicioFinder;
	}
	
	

}
