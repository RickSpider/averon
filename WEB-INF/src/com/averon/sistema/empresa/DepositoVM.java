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

import com.averon.modelo.Deposito;
import com.averon.modelo.Sucursal;
import com.averon.util.ParamsLocal;
import com.averon.util.TemplateViewModelLocal;
import com.doxacore.components.finder.FinderModel;



public class DepositoVM extends TemplateViewModelLocal{
	
	private List<Object[]> lDepositos;
	private List<Object[]> lDepositosOri;
	private Deposito depositoSelected;
	
	private boolean opCrearDeposito;
	private boolean opEditarDeposito;
	private boolean opBorrarDeposito;

	private boolean editar = false;

	@Init(superclass = true)
	public void initDepositoVM() {

		this.inicializarFiltros();
		this.cargarDepositos();
		

	}

	@AfterCompose(superclass = true)
	public void afterComposeDepositoVM() {

	}

	
	@Override
	protected void inicializarOperaciones() {
		this.opCrearDeposito = this.operacionHabilitada(ParamsLocal.OP_CREAR_DEPOSITO);
		this.opEditarDeposito = this.operacionHabilitada(ParamsLocal.OP_EDITAR_DEPOSITO);
		this.opBorrarDeposito = this.operacionHabilitada(ParamsLocal.OP_BORRAR_DEPOSITO);
		
	}
	
	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[4];

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lDepositos")
	public void filtrarDeposito() {

		this.lDepositos = this.filtrarListaObject(this.filtroColumns, this.lDepositosOri);

	}

	private void cargarDepositos() {

		String sql = this.um.getSql("deposito/listaDeposito.sql").replace("?1", this.getCurrentEmpresa().getEmpresaid()+"");
		this.lDepositos = this.reg.sqlNativo(sql);
		this.lDepositosOri = this.lDepositos;
		
		this.filtrarDeposito();

	}
	
	//seccion modal
	
	private Window modal;
	
	@Command
	public void modalDepositoAgregar() {

		if (!this.opCrearDeposito)
			return;

		this.editar = false;
		this.modalDeposito(-1);

	}

	@Command
	public void modalDeposito(@BindingParam("depositoid") long depositoid) {

		this.inicializarFinders();
		
		if (depositoid != -1) {

			if (!this.opEditarDeposito)
				return;

			this.editar = true;

			this.depositoSelected = this.reg.getObjectById(Deposito.class.getName(), depositoid);

		} else {

			this.depositoSelected = new Deposito();
			//this.depositoSelected.setEmpresa(getCurrentEmpresa());
			this.depositoSelected.setSucursal(getCurrentSucursal());
			

		}

		modal = (Window) Executions.createComponents("/sistema/zul/empresa/depositoModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	@Command
	@NotifyChange("lDepositos")
	public void guardar() {

		this.depositoSelected = this.save(this.depositoSelected);

		this.cargarDepositos();

		this.modal.detach();

		if (editar) {

			Notification.show("Deposito Actualizada.");

			this.editar = false;

		} else {

			Notification.show("Los datos de la Nueva Deposito fueron agragados.");
		}

	}
	
	public FinderModel getSucursalFinder() {
		return sucursalFinder;
	}

	public void setSucursalFinder(FinderModel sucursalFinder) {
		this.sucursalFinder = sucursalFinder;
	}

	private FinderModel sucursalFinder;

	@NotifyChange("*")
	public void inicializarFinders() {
		
		String sqlSucursal = this.um.getSql("sucursal/buscarSucursal.sql")
				.replace("?1", this.getCurrentEmpresa().getEmpresaid()+"");
		
		sucursalFinder = new FinderModel("SucursalUsuario", sqlSucursal);
		
		
	}

	public void generarFinders(@BindingParam("finder") String finder) {

		if (finder.compareTo(this.sucursalFinder.getNameFinder()) == 0) {

			this.sucursalFinder.generarListFinder();
			BindUtils.postNotifyChange(null, null, this.sucursalFinder, "listFinder");

			return;
		}
		

	}

	@Command
	public void finderFilter(@BindingParam("filter") String filter, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.sucursalFinder.getNameFinder()) == 0) {

			this.sucursalFinder.setListFinder(this.filtrarListaObject(filter, this.sucursalFinder.getListFinderOri()));
			BindUtils.postNotifyChange(null, null, this.sucursalFinder, "listFinder");

			return;
		}
		
	
	}

	@Command
	@NotifyChange({"sucursalUsuarioSelected"})
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.sucursalFinder.getNameFinder()) == 0) {

			this.depositoSelected.setSucursal(this.reg.getObjectById(Sucursal.class.getName(), id));
			return;
			
		}


	}

	public List<Object[]> getlDepositos() {
		return lDepositos;
	}

	public void setlDepositos(List<Object[]> lDepositos) {
		this.lDepositos = lDepositos;
	}

	public Deposito getDepositoSelected() {
		return depositoSelected;
	}

	public void setDepositoSelected(Deposito depositoSelected) {
		this.depositoSelected = depositoSelected;
	}

	public boolean isOpCrearDeposito() {
		return opCrearDeposito;
	}

	public void setOpCrearDeposito(boolean opCrearDeposito) {
		this.opCrearDeposito = opCrearDeposito;
	}

	public boolean isOpEditarDeposito() {
		return opEditarDeposito;
	}

	public void setOpEditarDeposito(boolean opEditarDeposito) {
		this.opEditarDeposito = opEditarDeposito;
	}

	public boolean isOpBorrarDeposito() {
		return opBorrarDeposito;
	}

	public void setOpBorrarDeposito(boolean opBorrarDeposito) {
		this.opBorrarDeposito = opBorrarDeposito;
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
	
	

}
