package com.averon.sistema.main;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;

import com.averon.modelo.Empresa;
import com.averon.util.TemplateViewModelLocal;

public class BienvenidaVM extends TemplateViewModelLocal{
	
	private Empresa empresaSelected;
	
	@Init(superclass = true)
	public void initBienvenidaVM() {

		empresaSelected = this.getCurrentEmpresa();
		
		if (this.empresaSelected.getLogopath() == null) {
			
			this.empresaSelected.setLogopath("/logo.png");
			
		}
	
	}

	@AfterCompose(superclass = true)
	public void afterComposeBienvenidaVM() {
		
	}


	@Override
	protected void inicializarOperaciones() {
		// TODO Auto-generated method stub
		
	}

	public Empresa getEmpresaSelected() {
		return empresaSelected;
	}

	public void setEmpresaSelected(Empresa empresaSelected) {
		this.empresaSelected = empresaSelected;
	}
	
	

}
