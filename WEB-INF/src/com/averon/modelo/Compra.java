package com.averon.modelo;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.ColumnDefault;

import com.doxacore.modelo.Tipo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Compras")
public class Compra extends ModeloERP implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -545111790742841417L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long compraid;

	private Date fecha;
	
	@Column(nullable = false)
	private Long timbrado;
	@Column(nullable = false)
	private String comprobanteNum;
	
	@ManyToOne
	@JoinColumn(name = "comprobantetipoid", nullable = false)
	private Tipo comprobanteTipo;
	
	@ManyToOne
	@JoinColumn(name = "condicionTipoid", nullable = false)
	private Tipo condicionTipo;	
	
	@ManyToOne
	@JoinColumn(name = "monedaTipoid", nullable = false)
	private Tipo monedaTipo;
	
	private double monedaCambio = 1;
	
	@ColumnDefault("false")
	private boolean comprobanteElectronico;
	
	private String cdc;
	
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

}
