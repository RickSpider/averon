package com.averon.seachModel;

import com.averon.modelo.Producto;
import com.doxacore.modelo.Tipo;

public class TipoSearchModel {

	private long id;
	private String nombre;

	public TipoSearchModel(long id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}
	
	public TipoSearchModel(Tipo t) {
		
		this.id = t.getTipoid();
		this.nombre = t.getTipo();
		
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
	
	public Tipo getTipo() {
		
		Tipo out = new Tipo();
		out.setTipoid(this.id);
		
		return out;
		
	}

	@Override
	public String toString() {
		return this.nombre;
	}

}
