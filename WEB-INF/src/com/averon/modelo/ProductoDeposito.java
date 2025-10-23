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
@Table(name = "ProductosDepositos")
public class ProductoDeposito extends ModeloERP implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -951729552354015722L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productodepositoid;
	
	@ManyToOne
	@JoinColumn(name = "Productoid", nullable = false)
	private Producto producto;
	
	@ManyToOne
	@JoinColumn(name = "depositoid", nullable = false)
	private Deposito deposito;
	
	private double cantidad = 0;
	
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

	public Long getProductodepositoid() {
		return productodepositoid;
	}

	public void setProductodepositoid(Long productodepositoid) {
		this.productodepositoid = productodepositoid;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Deposito getDeposito() {
		return deposito;
	}

	public void setDeposito(Deposito deposito) {
		this.deposito = deposito;
	}

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	
	

}
