package com.averon.sistema.abm;

import java.util.Date;
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

import com.averon.modelo.Ajuste;
import com.averon.modelo.AjusteDetalle;
import com.averon.modelo.Deposito;
import com.averon.modelo.Producto;
import com.averon.modelo.ProductoDeposito;
import com.averon.util.ParamsLocal;
import com.averon.util.TemplateViewModelLocal;
import com.doxacore.components.finder.FinderModel;
import com.doxacore.modelo.Tipo;



public class AjusteVM extends TemplateViewModelLocal{
	
	private List<Object[]> lAjustes;
	private List<Object[]> lAjustesOri;
	private Ajuste ajusteSelected;

	private boolean opCrearAjuste;
	private boolean opEditarAjuste;
	private boolean opBorrarAjuste;
	
	private Producto productoSelected;

	private boolean editar = false;

	@Init(superclass = true)
	public void initAjusteVM() {

		this.cargarAjustes();
		this.inicializarFiltros();
	}

	@AfterCompose(superclass = true)
	public void afterComposeAjusteVM() {
		
	}

	
	@Override
	protected void inicializarOperaciones() {
		this.opCrearAjuste = this.operacionHabilitada(ParamsLocal.OP_CREAR_AJUSTE);
		this.opEditarAjuste = this.operacionHabilitada(ParamsLocal.OP_EDITAR_AJUSTE);
		this.opBorrarAjuste = this.operacionHabilitada(ParamsLocal.OP_BORRAR_AJUSTE);
		
	}
	
	private String filtroColumns[];

	private void inicializarFiltros() {

		this.filtroColumns = new String[4];

		for (int i = 0; i < this.filtroColumns.length; i++) {

			this.filtroColumns[i] = "";

		}

	}

	@Command
	@NotifyChange("lAjustes")
	public void filtrarAjuste() {

		this.lAjustes = this.filtrarListaObject(this.filtroColumns, this.lAjustesOri);

	}

	private void cargarAjustes() {

		String sql = this.um.getSql("ajuste/listaAjuste.sql")
				.replace("?1", this.getCurrentEmpresa().getEmpresaid()+"");
		this.lAjustes = this.reg.sqlNativo(sql);
		this.lAjustesOri = this.lAjustes;

	}
	
	//seccion modal
	
	private Window modal;
	
	@Command
	public void modalAjusteAgregar() {

		if (!this.opCrearAjuste)
			return;

		this.editar = false;
		this.modalAjuste(-1);

	}

	@Command
	public void modalAjuste(@BindingParam("ajusteid") long ajusteid) {

		this.inicializarFinders();
		
		this.productoSelected = null;
		

		if (ajusteid != -1) {

			if (!this.opEditarAjuste)
				return;

			this.editar = true;

			this.ajusteSelected = this.reg.getObjectById(Ajuste.class.getName(), ajusteid);

		} else {

			this.ajusteSelected = new Ajuste();
			//this.ajusteSelected.setEmpresa(getCurrentEmpresa());
			this.ajusteSelected.setFecha(new Date());
			

		}

		modal = (Window) Executions.createComponents("/sistema/zul/abm/ajusteModal.zul", this.mainComponent, null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();

	}
	
	@Command
	@NotifyChange("lAjustes")
	public void guardar() {

		this.ajusteSelected = this.save(this.ajusteSelected);		

		this.modal.detach();
		
		for (AjusteDetalle ad : this.ajusteSelected.getDetalles()) {
			
			String[] campos = {"empresa","deposito","producto"};
			Object[] valores = {this.ajusteSelected.getEmpresa(), this.ajusteSelected.getDeposito(), ad.getProducto()};
			
			ProductoDeposito pd = this.reg.getObjectByColumns(ProductoDeposito.class, campos, valores);
						
			if (pd == null) {
			
				pd = new ProductoDeposito();
				//pd.setEmpresa(this.ajusteSelected.getEmpresa());
				pd.setDeposito(this.ajusteSelected.getDeposito());
				pd.setProducto(ad.getProducto());
				
				
			}
			
			if (this.ajusteSelected.getAjusteTipo().getSigla().compareTo("AJUSTE_POSITIVO") == 0) {
				
				pd.setCantidad(pd.getCantidad()+ad.getCantidad());
				
			}else if (this.ajusteSelected.getAjusteTipo().getSigla().compareTo("AJUSTE_NEGATIVO") == 0) {
				
				pd.setCantidad(pd.getCantidad()-ad.getCantidad());
				
			}
			
			this.save(pd);
			
		}
		
		if (editar) {

			Notification.show("Ajuste Actualizado.");

			this.editar = false;

		} else {

			Notification.show("Los datos deL Ajuste fueron agragados.");
		}
		
		
		
		this.cargarAjustes();

	}
	
	private FinderModel depositoFinder;
	private FinderModel ajusteTipoFinder;
	private FinderModel productoFinder;
	
	@NotifyChange("*")
	public void inicializarFinders() {
		
		Tipo tipoProducto  =this.reg.getObjectBySigla(Tipo.class, ParamsLocal.SIGLA_TIPO_PRODUCTO_PRODUCTO);;
		

		String sqlAjuste = this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1", ParamsLocal.SIGLA_TIPOTIPO_AJUSTE );
		ajusteTipoFinder = new FinderModel("ajusteTipo", sqlAjuste);
		
		String sqlDeposito = this.um.getSql("deposito/buscarDeposito.sql").replace("?1", this.getCurrentEmpresa().getEmpresaid()+"");
		depositoFinder = new FinderModel("deposito",sqlDeposito);

		String sqlProducto = this.um.getSql("producto/buscarProducto.sql")
				.replace("?1", this.getCurrentEmpresa().getEmpresaid()+"")
				.replace("?2", tipoProducto.getTipoid()+"");;
		productoFinder = new FinderModel("producto",sqlProducto);
	}
	
	public void generarFinders(@BindingParam("finder") String finder) {
		
		Map<String, FinderModel> finders = Map.of(
				this.ajusteTipoFinder.getNameFinder(), this.ajusteTipoFinder,
				this.depositoFinder.getNameFinder(), this.depositoFinder,
				this.productoFinder.getNameFinder(), this.productoFinder
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
	        this.ajusteTipoFinder.getNameFinder(), this.ajusteTipoFinder,
	        this.depositoFinder.getNameFinder(), this.depositoFinder,
	        this.productoFinder.getNameFinder(), this.productoFinder
	        
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

		if (finder.compareTo(this.ajusteTipoFinder.getNameFinder()) == 0) {

			this.ajusteSelected.setAjusteTipo(this.reg.getObjectById(Tipo.class.getName(), id));
			return;
		}
		
		if (finder.compareTo(this.depositoFinder.getNameFinder()) == 0) {

			this.ajusteSelected.setDeposito(this.reg.getObjectById(Deposito.class.getName(), id));
			return;
		}
		
		if (finder.compareTo(this.productoFinder.getNameFinder()) == 0) {

			this.productoSelected = (this.reg.getObjectById(Producto.class.getName(), id));
			return;
		}


	}
	
	
	
	@Command
	@NotifyChange("*")
	public void agregarProducto() {
		
		AjusteDetalle ad = new AjusteDetalle();
		ad.setEmpresa(getCurrentEmpresa());
		ad.setAjuste(ajusteSelected);
		ad.setCantidad(0);
		ad.setProducto(productoSelected);
		
		this.ajusteSelected.getDetalles().add(ad);
		this.productoSelected = null;
	}
	
	@Command
	@NotifyChange("*")
	public void borrarAjusteDetalle(@BindingParam("ajustedetalle") AjusteDetalle ajustedetalle) {
		
		this.ajusteSelected.getDetalles().remove(ajustedetalle);		
		
	}
	


	public List<Object[]> getlAjustes() {
		return lAjustes;
	}

	public void setlAjustes(List<Object[]> lAjustes) {
		this.lAjustes = lAjustes;
	}

	public Ajuste getAjusteSelected() {
		return ajusteSelected;
	}

	public void setAjusteSelected(Ajuste ajusteSelected) {
		this.ajusteSelected = ajusteSelected;
	}

	public boolean isOpCrearAjuste() {
		return opCrearAjuste;
	}

	public void setOpCrearAjuste(boolean opCrearAjuste) {
		this.opCrearAjuste = opCrearAjuste;
	}

	public boolean isOpEditarAjuste() {
		return opEditarAjuste;
	}

	public void setOpEditarAjuste(boolean opEditarAjuste) {
		this.opEditarAjuste = opEditarAjuste;
	}

	public boolean isOpBorrarAjuste() {
		return opBorrarAjuste;
	}

	public void setOpBorrarAjuste(boolean opBorrarAjuste) {
		this.opBorrarAjuste = opBorrarAjuste;
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

	public FinderModel getDepositoFinder() {
		return depositoFinder;
	}

	public void setDepositoFinder(FinderModel depositoFinder) {
		this.depositoFinder = depositoFinder;
	}

	public FinderModel getAjusteTipoFinder() {
		return ajusteTipoFinder;
	}

	public void setAjusteTipoFinder(FinderModel ajusteTipoFinder) {
		this.ajusteTipoFinder = ajusteTipoFinder;
	}

	public Producto getProductoSelected() {
		return productoSelected;
	}

	public void setProductoSelected(Producto productoSelected) {
		this.productoSelected = productoSelected;
	}

	public FinderModel getProductoFinder() {
		return productoFinder;
	}

	public void setProductoFinder(FinderModel productoFinder) {
		this.productoFinder = productoFinder;
	}
	

}
