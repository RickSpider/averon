package com.averon.modelo;

import java.io.Serializable;
import java.util.Date;

import com.doxacore.modelo.Tipo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "Personas")
public class Persona extends ModeloERPInterno implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7296818978299953444L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long personaid;

	@ManyToOne
	@JoinColumn(name = "documentotipoid")
	private Tipo documentoTipo;

	private String documentoNum;

	private String ruc;

	private String razonSocial;

	private String email;

	@Column(columnDefinition = "TEXT")
	private String direccion;

	private String telefono;

	private String nombre;
	private String apellido;

	@Temporal(TemporalType.DATE)
	private Date fechaNacimiento;

	public Persona() {
	}

	public Persona(Long personaid) {
		this.personaid = personaid;
	}

	public String getFullName() {

		return this.nombre + " " + this.getApellido();

	}

	public Tipo getDocumentoTipo() {
		return documentoTipo;
	}

	public void setDocumentoTipo(Tipo documentoTipo) {
		this.documentoTipo = documentoTipo;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public Long getPersonaid() {
		return personaid;
	}

	public void setPersonaid(Long personaid) {
		this.personaid = personaid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getDocumentoNum() {
		return documentoNum;
	}

	public void setDocumentoNum(String documentoNum) {
		this.documentoNum = documentoNum;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
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

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

}
