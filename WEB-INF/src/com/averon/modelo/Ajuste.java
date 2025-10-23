package com.averon.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.doxacore.modelo.Tipo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "Ajustes")
public class Ajuste extends ModeloERP implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7521428690577037050L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ajusteid;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;
	
	@Column(columnDefinition="TEXT")
	private String comentario;
	
	@ManyToOne
	@JoinColumn(name = "depositoid", nullable = false)
	private Deposito Deposito;
	
	@ManyToOne
	@JoinColumn(name = "ajustetipoid", nullable = false)
	private Tipo ajusteTipo;
	
	
	@OneToMany(mappedBy = "ajuste", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.EAGER)
	private List<AjusteDetalle> detalles = new ArrayList<AjusteDetalle>();
	
	
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

	public Long getAjusteid() {
		return ajusteid;
	}

	public void setAjusteid(Long ajusteid) {
		this.ajusteid = ajusteid;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Deposito getDeposito() {
		return Deposito;
	}

	public void setDeposito(Deposito deposito) {
		Deposito = deposito;
	}

	public List<AjusteDetalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<AjusteDetalle> detalles) {
		this.detalles = detalles;
	}

	public Tipo getAjusteTipo() {
		return ajusteTipo;
	}

	public void setAjusteTipo(Tipo ajusteTipo) {
		this.ajusteTipo = ajusteTipo;
	}
	
	

}
