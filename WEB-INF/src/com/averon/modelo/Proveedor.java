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
@Table(name = "Proveedores")
public class Proveedor extends ModeloERPInterno implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7257474942560642556L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long proveedorid;
	
	@ManyToOne
	@JoinColumn(name = "personaid")
	private Persona persona;

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

	public Long getProveedorid() {
		return proveedorid;
	}

	public void setProveedorid(Long proveedorid) {
		this.proveedorid = proveedorid;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}
	
	

}
