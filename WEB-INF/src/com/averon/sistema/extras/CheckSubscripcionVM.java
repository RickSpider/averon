package com.averon.sistema.extras;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zul.Window;

import com.averon.modelo.Empresa;
import com.averon.modelo.Persona;
import com.doxacore.util.Register;
import com.doxacore.util.UtilMetodos;

public class CheckSubscripcionVM {

	private String documento;
	private String vencimiento;
	private Empresa empresaSelected;
	
	protected Register reg;
	protected Component mainComponent;
	protected UtilMetodos um;
	
	@Init
	public void initCheckSubscripcionVM(@ContextParam(ContextType.VIEW) Component view, @ExecutionParam("arg") String arg) {

		this.reg = new Register();
		this.um = new UtilMetodos();
		this.mainComponent = view;
		
		
		
	}

	@AfterCompose
	public void afterComposeCheckSubscripcionVM() {
		
		this.empresaSelected = this.reg.getObjectById(Empresa.class.getName(), Long.parseLong(Executions.getCurrent().getParameter("empresaid")));

	}
	
	private Window modal;
	
	@Command
	@NotifyChange("*")
	public void verificar() {
		
		if (this.documento.length()<=6) {
			
			Notification.show("Documento no valido" , "error", null,null, 4000 ,false);
			return;
			
		}
		
		String sqlSuscripcion = "select s.suscripcionid, s.vencimiento from suscripciones s\r\n"
				+ "join personas p on p.personaid = s.personaid\r\n"
				+ "where s.empresaid = "+this.empresaSelected.getEmpresaid()+"\n"
				+ "and p.documentonum like '%"+documento+"%'\r\n"
				;
		
		List<Object[]> s = this.reg.sqlNativo(sqlSuscripcion);
		
		if (s.size() <= 0) {
			
			Notification.show("No existe el documento, favor verificar" , "error", null,null, 4000 ,false);
			return;
		
		}else {
			
			String sql ="select s.suscripcionid, s.vencimiento from suscripciones s\r\n"
					+ "join personas p on p.personaid = s.personaid\r\n"
					+ "where s.empresaid = "+this.empresaSelected.getEmpresaid()+"\n"
					+ "and p.documentonum like '%"+documento+"%'\r\n"
					+ "and s.vencimiento <= current_date;";

			List<Object[]> sVencidos = this.reg.sqlNativo(sql);
			
			if (sVencidos.size() > 0) {
				
				//Notification.show("Suscripcion Vencida!!!!" , "error", null,null, 4000 ,false);
				
				//SimpleDateFormat sdf;
				
				this.vencimiento = new SimpleDateFormat("dd/MM/YYYY").format((Date) sVencidos.get(0)[1]);
				
				modal = (Window) Executions.createComponents("/sistema/zul/extras/sucripcionNoPagaModal.zul", this.mainComponent, null);
				Selectors.wireComponents(modal, this, false);
				modal.doModal();
				
			}else {
				
				//Notification.show("Suscripcion al dia." , "info", null,null, 4000 ,false);
				
				modal = (Window) Executions.createComponents("/sistema/zul/extras/sucripcionPagaModal.zul", this.mainComponent, null);
				Selectors.wireComponents(modal, this, false);
				modal.doModal();
				
			
				
			}

		}
		
		
		this.documento = "";
		
		
	}


	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public Empresa getEmpresaSelected() {
		return empresaSelected;
	}

	public void setEmpresaSelected(Empresa empresaSelected) {
		this.empresaSelected = empresaSelected;
	}

	public String getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(String vencimiento) {
		this.vencimiento = vencimiento;
	}
	
	
	
}
