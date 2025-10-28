package com.averon.modelo;

import java.io.Serializable;
import java.util.Date;

import com.doxacore.modelo.Tipo;

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
@Table(name = "Agendamientos")
public class Agendamiento extends ModeloERPInterno implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1558949509519075985L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long agendamientoid;
	
	private String titulo;
	private String contenido;
	
	@ManyToOne
	@JoinColumn(name = "agendamientotipoid", nullable = false)
	private Tipo agendamientoTipo;
	
	@ManyToOne
	@JoinColumn(name = "sucursalid")
	private Sucursal sucursal;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date inicio;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fin;
	
	@ManyToOne
	@JoinColumn(name = "servicioid", nullable = true)
	private Producto servicio;
	
	@ManyToOne
	@JoinColumn(name = "funcionarioid", nullable = true)
	private Funcionario funcionario;
	
	@ManyToOne
	@JoinColumn(name = "personaid", nullable = true)
	private Persona persona;
	
	@ManyToOne
	@JoinColumn(name = "estadotipoid", nullable = true)
	private Tipo estado;

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

	public Long getAgendamientoid() {
		return agendamientoid;
	}

	public void setAgendamientoid(Long agendamientoid) {
		this.agendamientoid = agendamientoid;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFin() {
		return fin;
	}

	public void setFin(Date fin) {
		this.fin = fin;
	}

	public Sucursal getSucursal() {
		return sucursal;
	}

	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Tipo getAgendamientoTipo() {
		return agendamientoTipo;
	}

	public void setAgendamientoTipo(Tipo agendamientoTipo) {
		this.agendamientoTipo = agendamientoTipo;
	}

	public Producto getServicio() {
		return servicio;
	}

	public void setServicio(Producto servicio) {
		this.servicio = servicio;
	}

	public Tipo getEstado() {
		return estado;
	}

	public void setEstado(Tipo estado) {
		this.estado = estado;
	}
	
	

	
}
