package com.averon.seachModel;

import com.averon.modelo.Funcionario;

public class FuncionarioSearchModel {
	
	private long id;
	private long idInterno;
	private String nombreCompleto;
	private String docnum;
	
	public FuncionarioSearchModel(long id, long idInterno, String nombreCompleto, String docnum) {
		super();
		this.id = id;
		this.idInterno = idInterno;
		this.nombreCompleto = nombreCompleto;
		this.docnum = docnum;
	}
	
	public FuncionarioSearchModel(Funcionario f) {
		super();
		
		this.id = f.getFuncionarioid();
		this.idInterno = f.getIdInterno();
		this.docnum = (f.getPersona().getRuc() != null && !f.getPersona().getRuc().trim().isEmpty()) ? f.getPersona().getRuc() : f.getPersona().getDocumentoNum();

		this.nombreCompleto = (f.getPersona().getRazonSocial() != null && !f.getPersona().getRazonSocial().trim().isEmpty()) ? f.getPersona().getRazonSocial()
				: (f.getPersona().getNombre()+" "+f.getPersona().getApellido());

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
	
	public long getIdInterno() {
		return idInterno;
	}

	public void setIdInterno(long idInterno) {
		this.idInterno = idInterno;
	}
	
	public Funcionario getFuncionario() {
		
		return new Funcionario(this.id);
		
	}
	
	@Override
	public String toString() {
		return this.id+" - "+this.nombreCompleto;
	}
	
	
	

}
