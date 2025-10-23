package com.averon.modelo;

import java.io.Serializable;

import com.doxacore.modelo.Modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Marcas")
public class Marca extends ModeloERPInterno implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5219485396337153495L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long marcaid;
	
	private String nombre;

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

	public Long getMarcaid() {
		return marcaid;
	}

	public void setMarcaid(Long marcaid) {
		this.marcaid = marcaid;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	

}
