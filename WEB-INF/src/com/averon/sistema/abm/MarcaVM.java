package com.averon.sistema.abm;

import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zul.Window;

import com.averon.modelo.Marca;
import com.averon.util.ParamsLocal;
import com.averon.util.TemplateViewModelLocal;



public class MarcaVM extends TemplateViewModelLocal{
	
	private List<Object[]> lMarcas;
	private List<Object[]> lMarcasOri;
	private Marca marcaSelected;
	
	private boolean opCrearMarca;
	private boolean opEditarMarca;
	private boolean opBorrarMarca;

	private boolean editar = false;

	@Init(superclass = true)
	public void initMarcaVM() {

		this.cargarMarcas();
		this.inicializarFiltros();

	}

	@AfterCompose(superclass = true)
	public void afterComposeMarcaVM() {

	}

	
	@Override
	protected void inicializarOperaciones() {
		this.opCrearMarca = this.operacionHabilitada(ParamsLocal.OP_CREAR_MARCA);
		this.opEditarMarca = this.operacionHabilitada(ParamsLocal.OP_EDITAR_MARCA);
		this.opBorrarMarca = this.operacionHabilitada(ParamsLocal.OP_BORRAR_MARCA);
		
	}
	
	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[3];

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lMarcas")
	public void filtrarMarca() {

		this.lMarcas = this.filtrarListaObject(this.filtroColumns, this.lMarcasOri);

	}

	private void cargarMarcas() {

		String sql = this.um.getSql("marca/listaMarca.sql").replace("?1", this.getCurrentEmpresa().getEmpresaid()+"");
		this.lMarcas = this.reg.sqlNativo(sql);
		this.lMarcasOri = this.lMarcas;

	}
	
	//seccion modal
	
	private Window modal;
	
	@Command
	public void modalMarcaAgregar() {

		if (!this.opCrearMarca)
			return;

		this.editar = false;
		this.modalMarca(-1);

	}

	@Command
	public void modalMarca(@BindingParam("depositoid") long depositoid) {

		
		if (depositoid != -1) {

			if (!this.opEditarMarca)
				return;

			this.editar = true;

			this.marcaSelected = this.reg.getObjectById(Marca.class.getName(), depositoid);

		} else {

			this.marcaSelected = new Marca();
			//this.depositoSelected.setEmpresa(getCurrentEmpresa());
		
			

		}

		modal = (Window) Executions.createComponents("/sistema/zul/abm/marcaModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	@Command
	@NotifyChange("lMarcas")
	public void guardar() {

		this.marcaSelected = this.save(this.marcaSelected);

		this.cargarMarcas();

		this.modal.detach();

		if (editar) {

			Notification.show("Marca Actualizada.");

			this.editar = false;

		} else {

			Notification.show("Los datos de la Nueva Marca fueron agragados.");
		}

	}
	
	public List<Object[]> getlMarcas() {
		return lMarcas;
	}

	public void setlMarcas(List<Object[]> lMarcas) {
		this.lMarcas = lMarcas;
	}

	public boolean isOpCrearMarca() {
		return opCrearMarca;
	}

	public void setOpCrearMarca(boolean opCrearMarca) {
		this.opCrearMarca = opCrearMarca;
	}

	public boolean isOpEditarMarca() {
		return opEditarMarca;
	}

	public void setOpEditarMarca(boolean opEditarMarca) {
		this.opEditarMarca = opEditarMarca;
	}

	public boolean isOpBorrarMarca() {
		return opBorrarMarca;
	}

	public void setOpBorrarMarca(boolean opBorrarMarca) {
		this.opBorrarMarca = opBorrarMarca;
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

	public Marca getMarcaSelected() {
		return marcaSelected;
	}

	public void setMarcaSelected(Marca marcaSelected) {
		this.marcaSelected = marcaSelected;
	}
	
	

}
