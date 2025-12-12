package com.averon.inicio;

import org.zkoss.zk.ui.Sessions;

import com.averon.modelo.EmpresaUsuario;
import com.averon.modelo.SucursalUsuario;
import com.doxacore.login.UsuarioCredencial;
import com.doxacore.modelo.Usuario;
import com.doxacore.util.Register;

public class Inicio {


	public void beforeLogin() {
		
	}

	public void afterLogin() {
		
		System.out.println("=========== Iiciando Sistema Averon ================");
		
		
		Register reg = new Register();
		UsuarioCredencial usuarioCredencial = (UsuarioCredencial) Sessions.getCurrent().getAttribute("userCredential");
		
		Usuario currentUser = reg.getObjectByColumn(Usuario.class, "account",
				usuarioCredencial.getAccount());
		
		String campo[] = {"usuario", "actual"};
		Object[] value = {currentUser, true};
		
		EmpresaUsuario eu =  reg.getObjectByColumns(EmpresaUsuario.class, campo, value);
		
		usuarioCredencial.setExtra(eu.getEmpresa().getRazonSocial());		
		Sessions.getCurrent().setAttribute("userCredential", usuarioCredencial);
		
		if  (eu != null) {
			Sessions.getCurrent().setAttribute("empresaid", eu.getEmpresa().getEmpresaid());			
		}else {
			Sessions.getCurrent().removeAttribute("empresaid");
		}
		
		
		SucursalUsuario su = reg.getObjectByColumns(SucursalUsuario.class, new String[]{"empresa","usuario", "actual"}, new Object[]{eu.getEmpresa(),eu.getUsuario(), true});
		
		if (su != null) {
			Sessions.getCurrent().setAttribute("sucursalid", su.getSucursal().getSucursalid());
		}else {
			Sessions.getCurrent().removeAttribute("sucursalid");
		}
		
		
		
	}
	
}
