package com.averon.modelo;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ajustesdetalles")
public class AjusteDetalle extends ModeloERP implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5575339569485678148L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ajustedetalleid;
	
	@ManyToOne
	@JoinColumn(name = "ajusteid", nullable = false)
	private Ajuste ajuste;
	
	@ManyToOne
	@JoinColumn(name = "productoid", nullable = false)
	private Producto producto;
	
	private double cantidad;
	
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

	public Long getAjustedetalleid() {
		return ajustedetalleid;
	}

	public void setAjustedetalleid(Long ajustedetalleid) {
		this.ajustedetalleid = ajustedetalleid;
	}

	public Ajuste getAjuste() {
		return ajuste;
	}

	public void setAjuste(Ajuste ajuste) {
		this.ajuste = ajuste;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	
	
	

}
