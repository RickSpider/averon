package com.averon.sistema.abm;

import java.util.List;
import java.util.Map;

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

import com.averon.modelo.Producto;
import com.averon.util.ParamsLocal;
import com.averon.util.TemplateViewModelLocal;
import com.doxacore.components.finder.FinderModel;
import com.doxacore.modelo.Tipo;



public class ServicioVM extends TemplateViewModelLocal{
	
	private List<Object[]> lServicios;
	private List<Object[]> lServiciosOri;
	private Producto servicioSelected;
	
	private Tipo tipoServicio;
	
	private boolean opCrearServicio;
	private boolean opEditarServicio;
	private boolean opBorrarServicio;

	private boolean editar = false;

	@Init(superclass = true)
	public void initServicioVM() {
		
		this.tipoServicio = this.reg.getObjectBySigla(Tipo.class, ParamsLocal.SIGLA_TIPO_PRODUCTO_SERVCIO);

		this.inicializarFiltros();
		this.cargarServicios();
		
	}

	@AfterCompose(superclass = true)
	public void afterComposeServicioVM() {
		
	}

	
	@Override
	protected void inicializarOperaciones() {
		this.opCrearServicio = this.operacionHabilitada(ParamsLocal.OP_CREAR_SERVICIO);
		this.opEditarServicio = this.operacionHabilitada(ParamsLocal.OP_EDITAR_SERVICIO);
		this.opBorrarServicio = this.operacionHabilitada(ParamsLocal.OP_BORRAR_SERVICIO);
		
	}
	
	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[4];

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lServicios")
	public void filtrarServicio() {

		this.lServicios = this.filtrarListaObject(this.filtroColumns, this.lServiciosOri);

	}

	private void cargarServicios() {

		String sql = this.um.getSql("servicio/listaServicio.sql")
				.replace("?1", this.getCurrentEmpresa().getEmpresaid()+"")
				.replace("?2", this.tipoServicio.getTipoid()+"");
		this.lServicios = this.reg.sqlNativo(sql);
		this.lServiciosOri = this.lServicios;

		this.filtrarServicio();
	}
	
	//seccion modal
	
	private Window modal;
	
	@Command
	public void modalServicioAgregar() {

		if (!this.opCrearServicio)
			return;

		this.editar = false;
		this.modalServicio(-1);

	}

	@Command
	public void modalServicio(@BindingParam("servicioid") long servicioid) {

		this.inicializarFinders();

		if (servicioid != -1) {

			if (!this.opEditarServicio)
				return;

			this.editar = true;

			this.servicioSelected = this.reg.getObjectById(Producto.class.getName(), servicioid);

		} else {

			this.servicioSelected = new Producto();
			this.servicioSelected.setProductoTipo(this.tipoServicio);
			this.servicioSelected.setEmpresa(getCurrentEmpresa());
			this.servicioSelected.setControlStock(false);
			this.servicioSelected.setMonedaVentaTipo(this.reg.getObjectBySigla(Tipo.class, ParamsLocal.SIGLA_TIPO_MONEDA_GUARANIES));
			this.servicioSelected.setIvaTipo(this.reg.getObjectBySigla(Tipo.class, ParamsLocal.SIGLA_TIPO_IVA_10));
			this.servicioSelected.setUnidadMedidaTipo(this.reg.getObjectBySigla(Tipo.class, ParamsLocal.SIGLA_TIPO_UNIDADMEDIDA_UNIDAD));
			
		}

		modal = (Window) Executions.createComponents("/sistema/zul/abm/servicioModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	@Command
	@NotifyChange("lServicios")
	public void guardar() {

		this.servicioSelected = this.save(this.servicioSelected);

		this.modal.detach();

		if (editar) {

			Notification.show("Servicio Actualizado.");

			this.editar = false;

		} else {

			Notification.show("Los datos deL Servicio fueron agragados.");
		}
		
		this.cargarServicios();

	}
	
	private FinderModel ivaTipoFinder;
	private FinderModel monedaTipoFinder;
	private FinderModel unidadMedidaTipoFinder;

	@NotifyChange("*")
	public void inicializarFinders() {

		String sqlIvaTipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1", ParamsLocal.SIGLA_TIPOTIPO_IVA );
		ivaTipoFinder = new FinderModel("IVA", sqlIvaTipo);
		
		String sqlmonedaTipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1", ParamsLocal.SIGLA_TIPOTIPO_MONEDA );
		monedaTipoFinder = new FinderModel("MONEDA", sqlmonedaTipo);
		
		String sqlunidadMedidaTipo = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1", ParamsLocal.SIGLA_TIPOTIPO_UNIDADMEDIDA );
		unidadMedidaTipoFinder = new FinderModel("UNIDADMEDIDA", sqlunidadMedidaTipo);

	}

	public void generarFinders(@BindingParam("finder") String finder) {
		
		Map<String, FinderModel> finders = Map.of(
				this.ivaTipoFinder.getNameFinder(), this.ivaTipoFinder,
				this.monedaTipoFinder.getNameFinder(), this.monedaTipoFinder,
				this.unidadMedidaTipoFinder.getNameFinder(), this.unidadMedidaTipoFinder
						
				);
		
		 FinderModel selectedFinder = finders.get(finder);
		    if (selectedFinder != null) {
		        selectedFinder.generarListFinder();
		        BindUtils.postNotifyChange(null, null, selectedFinder, "listFinder");
		    }


	}

	@Command
	public void finderFilter(@BindingParam("filter") String filter, @BindingParam("finder") String finder) {

		Map<String, FinderModel> finders = Map.of(
				this.ivaTipoFinder.getNameFinder(), this.ivaTipoFinder,
				this.monedaTipoFinder.getNameFinder(), this.monedaTipoFinder,
				this.unidadMedidaTipoFinder.getNameFinder(), this.unidadMedidaTipoFinder
						
				);

		    FinderModel selectedFinder = finders.get(finder);
		    if (selectedFinder != null) {
		        selectedFinder.setListFinder(this.filtrarListaObject(filter, selectedFinder.getListFinderOri()));
		        BindUtils.postNotifyChange(null, null, selectedFinder, "listFinder");
		    }

	}

	@Command
	@NotifyChange("*")
	public void onSelectetItemFinder(@BindingParam("id") Long id, @BindingParam("finder") String finder) {

		if (finder.compareTo(this.ivaTipoFinder.getNameFinder()) == 0) {

			this.servicioSelected.setIvaTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			return;
		}

		
		if (finder.compareTo(this.monedaTipoFinder.getNameFinder()) == 0) {

			this.servicioSelected.setMonedaVentaTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			return;
		}
		
		if (finder.compareTo(this.monedaTipoFinder.getNameFinder()) == 0) {

			this.servicioSelected.setUnidadMedidaTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			return;
		}


	}
	
	
	public FinderModel getIvaTipoFinder() {
		return ivaTipoFinder;
	}

	public void setIvaTipoFinder(FinderModel ivaTipoFinder) {
		this.ivaTipoFinder = ivaTipoFinder;
	}

	public List<Object[]> getlServicios() {
		return lServicios;
	}

	public void setlServicios(List<Object[]> lServicios) {
		this.lServicios = lServicios;
	}

	public Producto getServicioSelected() {
		return servicioSelected;
	}

	public void setServicioSelected(Producto servicioSelected) {
		this.servicioSelected = servicioSelected;
	}

	public boolean isOpCrearServicio() {
		return opCrearServicio;
	}

	public void setOpCrearServicio(boolean opCrearServicio) {
		this.opCrearServicio = opCrearServicio;
	}

	public boolean isOpEditarServicio() {
		return opEditarServicio;
	}

	public void setOpEditarServicio(boolean opEditarServicio) {
		this.opEditarServicio = opEditarServicio;
	}

	public boolean isOpBorrarServicio() {
		return opBorrarServicio;
	}

	public void setOpBorrarServicio(boolean opBorrarServicio) {
		this.opBorrarServicio = opBorrarServicio;
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

	public FinderModel getMonedaTipoFinder() {
		return monedaTipoFinder;
	}

	public void setMonedaTipoFinder(FinderModel monedaTipoFinder) {
		this.monedaTipoFinder = monedaTipoFinder;
	}

	public Tipo getTipoServicio() {
		return tipoServicio;
	}

	public void setTipoServicio(Tipo tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	public FinderModel getUnidadMedidaTipoFinder() {
		return unidadMedidaTipoFinder;
	}

	public void setUnidadMedidaTipoFinder(FinderModel unidadMedidaTipoFinder) {
		this.unidadMedidaTipoFinder = unidadMedidaTipoFinder;
	}
	
	
	

}
