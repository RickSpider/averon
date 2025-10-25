package com.averon.sistema.abm;

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

import com.averon.modelo.Persona;
import com.averon.util.ParamsLocal;
import com.averon.util.TemplateViewModelLocal;
import com.doxacore.components.finder.FinderModel;
import com.doxacore.modelo.Tipo;



public class PersonaVM extends TemplateViewModelLocal{
	
	private List<Object[]> lPersonas;
	private List<Object[]> lPersonasOri;
	private Persona personaSelected;
	
	private boolean opCrearPersona;
	private boolean opEditarPersona;
	private boolean opBorrarPersona;

	private boolean editar = false;

	@Init(superclass = true)
	public void initPersonaVM() {

		this.inicializarFiltros();
		this.cargarPersonas();
		
	}

	@AfterCompose(superclass = true)
	public void afterComposePersonaVM() {
		
	}

	
	@Override
	protected void inicializarOperaciones() {
		this.opCrearPersona = this.operacionHabilitada(ParamsLocal.OP_CREAR_PERSONA);
		this.opEditarPersona = this.operacionHabilitada(ParamsLocal.OP_EDITAR_PERSONA);
		this.opBorrarPersona = this.operacionHabilitada(ParamsLocal.OP_BORRAR_PERSONA);
		
	}
	
	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[7];

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lPersonas")
	public void filtrarPersona() {

		this.lPersonas = this.filtrarListaObject(this.filtroColumns, this.lPersonasOri);

	}

	private void cargarPersonas() {

		String sql = this.um.getSql("persona/listaPersona.sql").replace("?1", this.getCurrentEmpresa().getEmpresaid()+"");
		this.lPersonas = this.reg.sqlNativo(sql);
		this.lPersonasOri = this.lPersonas;
		
		this.filtrarPersona();

	}
	
	//seccion modal
	
	private Window modal;
	
	@Command
	public void modalPersonaAgregar() {

		if (!this.opCrearPersona)
			return;

		this.editar = false;
		this.modalPersona(-1);

	}

	@Command
	public void modalPersona(@BindingParam("personaid") long personaid) {

		this.inicializarFinders();

		if (personaid != -1) {

			if (!this.opEditarPersona)
				return;

			this.editar = true;

			this.personaSelected = this.reg.getObjectById(Persona.class.getName(), personaid);

		} else {

			this.personaSelected = new Persona();
			//this.personaSelected.setEmpresa(getCurrentEmpresa());
			
		}
		
		
		modal = (Window) Executions.createComponents("/sistema/zul/abm/personaModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	@Command
	@NotifyChange("lPersonas")
	public void guardar() {

		this.personaSelected = this.save(this.personaSelected);

		this.cargarPersonas();

		this.modal.detach();

		if (editar) {

			Notification.show("Persona Actualizado.");

			this.editar = false;

		} else {

			Notification.show("Los datos deL Persona fueron agragados.");
		}

	}
	
	private FinderModel documentoTipoFinder;

	@NotifyChange("*")
	public void inicializarFinders() {

		String sqlDocumentoTipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1", ParamsLocal.SIGLA_TIPOTIPO_DOCUMENTO );
		documentoTipoFinder = new FinderModel("Documento", sqlDocumentoTipo);

	}

	public void generarFinders(@BindingParam("finder") String finder) {

		if (finder.compareTo(this.documentoTipoFinder.getNameFinder()) == 0) {

			this.documentoTipoFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.documentoTipoFinder, "listFinder");

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

	}

	@Command
	@NotifyChange("*")
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.documentoTipoFinder.getNameFinder()) == 0) {

			this.personaSelected.setDocumentoTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			return;
		}

	}
	
	
	@Command
	public void exportarExcel() {
		
		String [] hd1 =  {"ID Interno","Nombre/RazonSocial","DOC#/RUC", "EMAIL", "TELEFONO", "FECHA_NACIMIENTO"};
		this.exportarExcelGenerico("ListaPersonas", "ListaPersonas", hd1, lPersonas);
		
	}
	
	
	
	

	public List<Object[]> getlPersonas() {
		return lPersonas;
	}

	public void setlPersonas(List<Object[]> lPersonas) {
		this.lPersonas = lPersonas;
	}

	public Persona getPersonaSelected() {
		return personaSelected;
	}

	public void setPersonaSelected(Persona personaSelected) {
		this.personaSelected = personaSelected;
	}

	public boolean isOpCrearPersona() {
		return opCrearPersona;
	}

	public void setOpCrearPersona(boolean opCrearPersona) {
		this.opCrearPersona = opCrearPersona;
	}

	public boolean isOpEditarPersona() {
		return opEditarPersona;
	}

	public void setOpEditarPersona(boolean opEditarPersona) {
		this.opEditarPersona = opEditarPersona;
	}

	public boolean isOpBorrarPersona() {
		return opBorrarPersona;
	}

	public void setOpBorrarPersona(boolean opBorrarPersona) {
		this.opBorrarPersona = opBorrarPersona;
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
	
	

}
