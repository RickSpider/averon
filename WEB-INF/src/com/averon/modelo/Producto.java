package com.averon.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Immutable;

import com.doxacore.modelo.Tipo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Productos")
public class Producto extends ModeloERPInterno implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8145698113910891097L;
	
	
	public Producto() {}
	public Producto(Long productoid) {
		super();
		this.productoid = productoid;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productoid;
	
	@Column(nullable = false, columnDefinition="TEXT")
	private String nombre;
	
	@ManyToOne
	@JoinColumn(name = "productotipoid", nullable = false)
	private Tipo productoTipo;
	
	private Double precioCompra;

	@Column(nullable = false)
	private Double precioVenta;
	
	@ManyToOne
	@JoinColumn(name = "monedaventatipoid", nullable = false)
	private Tipo monedaVentaTipo;
	
	@ManyToOne
	@JoinColumn(name = "ivatipoid", nullable = false)
	private Tipo ivaTipo;
	
	@ManyToOne
	@JoinColumn(name = "unidadmedidatipoid" , nullable = false)
	private Tipo unidadMedidaTipo;
	
	@ManyToOne
	@JoinColumn(name = "marcaid")
	private Marca marca;
	
	@OneToMany(mappedBy = "producto", fetch = FetchType.EAGER)
	private List<ProductoDeposito> inventario = new ArrayList<ProductoDeposito>();

	private boolean controlStock = true;

	public Long getProductoid() {
		return productoid;
	}

	public void setProductoid(Long productoid) {
		this.productoid = productoid;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getPrecioCompra() {
		return precioCompra;
	}

	public void setPrecioCompra(Double precioCompra) {
		this.precioCompra = precioCompra;
	}

	public Double getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(Double precioVenta) {
		this.precioVenta = precioVenta;
	}

	public Tipo getProductoTipo() {
		return productoTipo;
	}

	public void setProductoTipo(Tipo productoTipo) {
		this.productoTipo = productoTipo;
	}

	public Tipo getIvaTipo() {
		return ivaTipo;
	}

	public void setIvaTipo(Tipo ivaTipo) {
		this.ivaTipo = ivaTipo;
	}

	public boolean isControlStock() {
		return controlStock;
	}

	public void setControlStock(boolean controlStock) {
		this.controlStock = controlStock;
	}

	
	public Tipo getMonedaVentaTipo() {
		return monedaVentaTipo;
	}

	public void setMonedaVentaTipo(Tipo monedaVentaTipo) {
		this.monedaVentaTipo = monedaVentaTipo;
	}
	
	

	public List<ProductoDeposito> getInventario() {
		return inventario;
	}

	public void setInventario(List<ProductoDeposito> inventario) {
		this.inventario = inventario;
	}

	@Override
	public Object[] getArrayObjectDatos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStringDatos() {
		// TODO Auto-generated method stub
		return null;
	}

	public Tipo getUnidadMedidaTipo() {
		return unidadMedidaTipo;
	}

	public void setUnidadMedidaTipo(Tipo unidadMedidaTipo) {
		this.unidadMedidaTipo = unidadMedidaTipo;
	}

	public Marca getMarca() {
		return marca;
	}

	public void setMarca(Marca marca) {
		this.marca = marca;
	}
	
	
}
