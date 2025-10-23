package com.averon.seachModel;

import com.averon.modelo.Producto;

public class ServicioSearchModel {

	private long id;
	private long idInterno;
	private String nombre;
	private String precio;

	public ServicioSearchModel(long id, long idInterno, String nombre, String precio) {
		super();
		this.id = id;
		this.idInterno = idInterno;
		this.nombre = nombre;
		this.precio = precio;
	}
	
	public ServicioSearchModel(Producto s) {
		
		this.id = s.getProductoid();
		this.idInterno = s.getIdInterno();
		this.nombre = s.getNombre();
		this.precio = String.valueOf(s.getPrecioVenta());
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}
	

	public long getIdInterno() {
		return idInterno;
	}

	public void setIdInterno(long idInterno) {
		this.idInterno = idInterno;
	}
	
	public Producto getServicio() {
		
		return new Producto(this.id);
		
	}

	@Override
	public String toString() {
		return this.nombre;
	}

}
