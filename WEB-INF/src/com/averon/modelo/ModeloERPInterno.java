package com.averon.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ModeloERPInterno extends ModeloERP{

	@Column(insertable = false, updatable = false)
	private Long idInterno;

	public Long getIdInterno() {
		return idInterno;
	}

	public void setIdInterno(Long idInterno) {
		this.idInterno = idInterno;
	}

	
}
