package com.averon.modelo;

import java.io.Serializable;
import java.util.List;

import com.doxacore.modelo.Tipo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Sucursales")
public class Sucursal extends ModeloERPInterno implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3991360098257704777L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sucursalid;
	
	private String nombre;
	private String establecimiento;
	private String direccion;
	
	@ManyToOne
	@JoinColumn(name = "comprobantetipoid")
	private Tipo comprobanteTipo;
	
	@OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<SucursalUsuario> usuarios;
	
	@OneToMany(mappedBy = "sucursal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Deposito> depositos;

	public Long getSucursalid() {
		return sucursalid;
	}

	public void setSucursalid(Long sucursalid) {
		this.sucursalid = sucursalid;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEstablecimiento() {
		return establecimiento;
	}

	public void setEstablecimiento(String establecimiento) {
		this.establecimiento = establecimiento;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public List<SucursalUsuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<SucursalUsuario> usuarios) {
		this.usuarios = usuarios;
	}

	public List<Deposito> getDepositos() {
		return depositos;
	}

	public void setDepositos(List<Deposito> depositos) {
		this.depositos = depositos;
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

	public Tipo getComprobanteTipo() {
		return comprobanteTipo;
	}

	public void setComprobanteTipo(Tipo comprobanteTipo) {
		this.comprobanteTipo = comprobanteTipo;
	}
	
	

}
