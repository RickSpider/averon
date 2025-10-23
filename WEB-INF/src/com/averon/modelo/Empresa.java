package com.averon.modelo;

import java.io.Serializable;

import com.doxacore.modelo.Modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "Empresas")
public class Empresa extends Modelo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5170783905879426636L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long empresaid;
	
	@Column(nullable = false)
	private String razonSocial;
	
	@Column(nullable = false, unique = true)
	private String ruc;
	
	private String logopath;
	
	private byte[] logo;
	
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

	public Long getEmpresaid() {
		return empresaid;
	}

	public void setEmpresaid(Long empresaid) {
		this.empresaid = empresaid;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public String getLogopath() {
		return logopath;
	}

	public void setLogopath(String logopath) {
		this.logopath = logopath;
	}

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
	}

	
	
}
