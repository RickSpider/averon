package com.averon.seachModel;

import com.averon.modelo.Persona;

public class PersonaSearchModel {

	private long id;
	private long idInterno;
	private String nombreCompleto;
	private String docnum;

	public PersonaSearchModel(long id, long idInterno ,String nombreCompleto, String docnum) {

		this.id = id;
		this.idInterno = idInterno;
		this.nombreCompleto = nombreCompleto;
		this.docnum = docnum;
	}
	
	public PersonaSearchModel(Persona p) {

		this.id = p.getPersonaid();
		this.idInterno = p.getIdInterno();
		this.docnum = (p.getRuc() != null && !p.getRuc().trim().isEmpty()) ? p.getRuc() : p.getDocumentoNum();

		this.nombreCompleto = (p.getRazonSocial() != null && !p.getRazonSocial().trim().isEmpty()) ? p.getRazonSocial()
				: p.getNombre()+" "+p.getApellido();
	}
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getDocnum() {
		return docnum;
	}

	public void setDocnum(String docnum) {
		this.docnum = docnum;
	}

	public Persona getPersona() {

		return new Persona(this.id);

	}

	public long getIdInterno() {
		return idInterno;
	}

	public void setIdInterno(long idInterno) {
		this.idInterno = idInterno;
	}

	@Override
	public String toString() {
		return this.idInterno + " - " + this.nombreCompleto + " - " + this.docnum;
	}

}
