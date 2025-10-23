package com.averon.util;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Init;

import com.averon.modelo.Empresa;
import com.averon.modelo.EmpresaUsuario;
import com.averon.modelo.ModeloERP;
import com.averon.modelo.Sucursal;
import com.averon.modelo.SucursalUsuario;
import com.doxacore.TemplateViewModel;
import com.doxacore.modelo.Modelo;
import com.doxacore.report.ReportBigExcel;

public abstract class TemplateViewModelLocal extends TemplateViewModel{
	
	@Init(superclass = true)
	public void initTemplateViewModelLocal() {
		
	}

	@AfterCompose(superclass = true)
	public void afterComposeTemplateViewModelLocal() {
		
	}
	
	private EmpresaUsuario getCurrentEmpresaUsuario() {
		
		/*String campo[] = {"usuario", "actual"};
		Object[] value = {this.getCurrentUser(), true};*/
		
		return this.reg.getObjectByColumns(EmpresaUsuario.class, new String[]{"usuario", "actual"}, new Object[]{this.getCurrentUser(), true});
		
	}
	
	protected Empresa getCurrentEmpresa() {
		
		return this.getCurrentEmpresaUsuario().getEmpresa();
	}
	
	protected SucursalUsuario getCurrentSucursalUsuario() {
		
		EmpresaUsuario eu = this.getCurrentEmpresaUsuario();
		
		/*String campo[] = {"empresa","usuario", "actual"};
		Object[] value = {eu.getEmpresa(),eu.getUsuario(), true};*/
		
		SucursalUsuario su = this.reg.getObjectByColumns(SucursalUsuario.class, new String[]{"empresa","usuario", "actual"}, new Object[]{eu.getEmpresa(),eu.getUsuario(), true});
		
		return su;
		
	}
	
	protected Sucursal getCurrentSucursal() {
		
		return this.getCurrentSucursalUsuario().getSucursal();
	}
	
	protected <T extends ModeloERP> T save(T m) {
		
		if (m.getEmpresa() == null) {
			
			m.setEmpresa(getCurrentEmpresa());
			
		}

		return this.reg.saveObject(m, getCurrentUser().getAccount());

	}
	
	protected void exportarExcelGenerico(String nombreArchivo, String tituloPrincipal, String[] columnas,List<Object[]> datos) {


	    List<String[]> titulos = new ArrayList<>();
	    titulos.add(new String[]{tituloPrincipal});
	    titulos.add(new String[]{""}); // Espacio en blanco

	    // --- 2️⃣ Cabeceras dinámicas (opcional, puede venir vacía) ---
	    List<String[]> headersDatos = new ArrayList<>();
	    headersDatos.add(columnas);
	    
	    // --- 3️⃣ Procesar los datos ---
	    List<Object[]> detalles = new ArrayList<>();

	    for (Object[] fila : datos) {
	        Object[] o = new Object[fila.length-1];

	        for (int i = 0; i < fila.length-1; i++) {
	            o[i] = (fila[i+1] != null) ? fila[i+1].toString() : "";
	        }

	        detalles.add(o);
	    }

	    // --- 4️⃣ Exportar usando tu clase existente ---
	    ReportBigExcel re = new ReportBigExcel(nombreArchivo);
	    re.descargar(titulos, headersDatos, detalles);
	}
	

}
