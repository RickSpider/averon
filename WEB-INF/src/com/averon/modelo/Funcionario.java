package com.averon.modelo;

import java.io.Serializable;
import java.util.Date;

import com.doxacore.modelo.Tipo;
import com.doxacore.modelo.Usuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "funcionarios")
public class Funcionario extends ModeloERPInterno implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6195824852079768299L;

	public Funcionario() {
	}

	public Funcionario(Long funcionarioid) {
		this.funcionarioid = funcionarioid;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long funcionarioid;

	@ManyToOne
	@JoinColumn(name = "funcionarioTipoId", nullable = false)
	private Tipo funcionarioTipo;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "usuarioid", unique = true, nullable = true)
	private Usuario usuario;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "personaid", unique = true)
	private Persona persona = new Persona();

	@Temporal(TemporalType.DATE)
	private Date fechaIngreso = new Date();

	private boolean activo = true;

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

	public Long getFuncionarioid() {
		return funcionarioid;
	}

	public void setFuncionarioid(long funcionarioid) {
		this.funcionarioid = funcionarioid;
	}

	public Tipo getFuncionarioTipo() {
		return funcionarioTipo;
	}

	public void setFuncionarioTipo(Tipo funcionarioTipo) {
		this.funcionarioTipo = funcionarioTipo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public void setFuncionarioid(Long funcionarioid) {
		this.funcionarioid = funcionarioid;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

}
