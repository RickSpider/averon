package com.averon.sistema.main;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.calendar.Calendars;
import org.zkoss.calendar.event.CalendarsEvent;
import org.zkoss.calendar.impl.SimpleCalendarModel;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Window;

import com.averon.modelo.Agendamiento;
import com.averon.seachModel.FuncionarioSearchModel;
import com.averon.seachModel.PersonaSearchModel;
import com.averon.seachModel.ServicioSearchModel;
import com.averon.seachModel.TipoSearchModel;
import com.averon.sistema.main.calendar.CalendarItem;
import com.averon.util.ParamsLocal;
import com.averon.util.TemplateViewModelLocal;
import com.doxacore.modelo.Tipo;




public class CalendarVM extends TemplateViewModelLocal{

	private SimpleCalendarModel calendarModel;
	
	
	private long empresaid;
	
	private ListModelArray<PersonaSearchModel> lPersonasSearchModel;
	private ListModelArray<FuncionarioSearchModel> lFuncionariosSearchModel;
	private ListModelArray<ServicioSearchModel> lServiciosSearchModel;
	private ListModelArray<TipoSearchModel> lEstadosTiposSearchModel;
	
	private PersonaSearchModel personaSearchModelSelected;
	private FuncionarioSearchModel funcionarioSearchModelSelected;
	private ServicioSearchModel servicioSearchModelSelected;
	private TipoSearchModel estadoTipoSearchModelSelected;
	
	
	@Wire
	private Calendars calendars;
	
	
	@Init(superclass = true)
	public void initCalendarVM() throws ParseException {
		
		//this.calendarModel = new SimpleCalendarModel();
		this.empresaid = this.getCurrentEmpresa().getEmpresaid();
		
		
	}
	
	@AfterCompose(superclass = true)
	public void afterComposeCalendarVM(@ContextParam(ContextType.VIEW) Component view) throws ParseException {
		
		//esto permite usar el id con el linsten para el metodo
		Selectors.wireEventListeners(view, this);
		//esto permite usar el @wire
		Selectors.wireComponents(view, this, false);
		this.calendars.setBeginTime(6);
		this.calendars.setTimeslots(4);		
		this.dataCharger();
	}
	
	private void generarSearchModels() {
	    this.lPersonasSearchModel = crearSearchModel(
	        this.um.getSql("persona/buscarPersona.sql").replace("?1", String.valueOf(this.empresaid)),
	        o -> new PersonaSearchModel(
	        	Long.parseLong(o[0].toString()),
	            Long.parseLong(o[1].toString()),
	            o[2].toString(),
	            o[4].toString()
	        )
	    );

	    this.lFuncionariosSearchModel = crearSearchModel(
	        this.um.getSql("funcionario/buscarFuncionarios.sql").replace("?1", String.valueOf(this.empresaid)),
	        o -> new FuncionarioSearchModel(
	        	Long.parseLong(o[0].toString()),
	            Long.parseLong(o[1].toString()),
	            o[2].toString(),
	            o[3].toString()
	        )
	    );

	    this.lServiciosSearchModel = crearSearchModel(
	        this.um.getSql("servicio/buscarServicio.sql").replace("?1", String.valueOf(this.empresaid)), 
	        o -> new ServicioSearchModel(
	        	Long.parseLong(o[0].toString()),
	            Long.parseLong(o[1].toString()),
	            o[2].toString(),
	            o[3].toString()
	        )
	    );
	    
	   
	    
	   this.lEstadosTiposSearchModel = crearSearchModel(
		        this.um.getCoreSql("buscarTiposPorSiglaTipotipo.sql").replace("?1", "AGENDAMIENTOESTADO"), 
		        o -> new TipoSearchModel(
		        	Long.parseLong(o[0].toString()),
		            o[1].toString()
		        )
		    );
	}
	
	private <T> ListModelArray<T> crearSearchModel(String sql, java.util.function.Function<Object[], T> mapper) {
	    List<Object[]> resultados = this.reg.sqlNativo(sql);
	    List<T> lista = new ArrayList<>(resultados.size());

	    for (Object[] fila : resultados) {
	        lista.add(mapper.apply(fila));
	    }

	    ListModelArray<T> modelo = new ListModelArray<>(lista);
	    return modelo;
	}
	
	

	
	
	
	
	@Override
	protected void inicializarOperaciones() {
		// TODO Auto-generated method stub
	}
	
	@NotifyChange("*")
	private void dataCharger() throws ParseException {
		
		//Calendar calendar = Calendar.getInstance();
		this.calendarModel = new SimpleCalendarModel();
		
		/*String sqlSuscripcion = this.um.getSql("calendar/listDatosSuscripcion.sql").replace("?1", this.getCurrentEmpresa().getEmpresaid()+"");
		
		List<Object[]> ldatos = this.reg.sqlNativo(sqlSuscripcion);
		
	//	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		if (ldatos != null && ldatos.size() > 0) {
			
			for(Object[] x : ldatos) {
				
				CalendarItem ci = new CalendarItem();
				ci.setLocked(true);
				
				Date date = (Date) x[1];
				date = um.modificarHorasMinutosSegundos(date, 8, 0, 0, 0);
				calendar.setTime(date);
				ci.setBeginDate(calendar.getTime());
				
				date = (Date) x[2];
				date = um.modificarHorasMinutosSegundos(date, 9, 0, 0, 0);
				
				calendar.setTime(date);
				ci.setEndDate(calendar.getTime());
				
				ci.setTitle(x[3].toString());
				ci.setContent(x[4].toString());
				ci.setStyle("background-color: #FF0000; color: #FFFFFF;");
				this.calendarModel.add(ci);
			}
			
		}*/
		
		String sqlAgendamiento = this.um.getSql("calendar/listDatosAgendamiento.sql").replace("?1", this.empresaid+"").replace("?2", this.getCurrentSucursal().getSucursalid()+"");
		List<Object[]> lAgendamietos = this.reg.sqlNativo(sqlAgendamiento);
		
		if (lAgendamietos != null && lAgendamietos.size() > 0) {
			
			for(Object[] x : lAgendamietos) {
				
				
				CalendarItem ci = new CalendarItem();
				//ci.setLocked(true);
				
				ci.setAgendamientoid(Long.parseLong(x[0].toString()));
				
				ci.setBeginDate((Date) x[1]);
				
				ci.setEndDate((Date) x[2]);
				
				if (x[7].toString().compareTo(ParamsLocal.SIGLA_TIPO_AGENDAMIENTO_SERVICIO) == 0) {
					
					ci.setTitle(x[5].toString());
					ci.setContent(x[6].toString());
					
				}else if (x[7].toString().compareTo(ParamsLocal.SIGLA_TIPO_AGENDAMIENTO_RECORDATORIO) == 0) {
					
					ci.setTitle(x[3].toString());
					ci.setContent(x[4].toString());
					
				}
				
				ci.setStyle("background-color: "+x[8].toString()+"; color: #FFFFFF;");
				
				this.calendarModel.add(ci);
			}
			
		}
		
		/*String sqlCumpleaños = this.um.getSql("calendar/listDatosCumpleaños.sql").replace("?1", this.empresaid+"");
		List<Object[]> lCumpleaños = this.reg.sqlNativo(sqlCumpleaños);
		
		if (lCumpleaños != null && lCumpleaños.size() > 0) {
			
			calendar = Calendar.getInstance();
			int currentYear = calendar.get(Calendar.YEAR);
			
			for(Object[] x : lCumpleaños) {
				
				CalendarItem ci = new CalendarItem();
				ci.setLocked(true);
				
				calendar.setTime((Date) x[1]);
				
				calendar.set(Calendar.HOUR, 8);
				calendar.set(Calendar.YEAR, currentYear);
				
				ci.setBeginDate(calendar.getTime());
				calendar.add(Calendar.HOUR,1);
				ci.setEndDate(calendar.getTime());
				
				ci.setTitle("Cumpleaños");
				
				ci.setContent(x[0].toString());
				
				ci.setStyle("background-color: #ff8000; color: #FFFFFF");
				
				this.calendarModel.add(ci);
				
			}
			
		}*/
		
		this.calendars.setModel(calendarModel);
		
	}
	
	private Window modal;
	private Agendamiento agendamientoSelected;
	private boolean editar = false;
	

	@Listen(CalendarsEvent.ON_ITEM_CREATE + " = #calendars; " + CalendarsEvent.ON_ITEM_EDIT + "  = #calendars")
	//@Command
	public void agendamientoModal(@BindingParam("event") CalendarsEvent event) {
		
		event.stopClearGhost();
		
		System.out.println("========HOrarios======");
		
		System.out.println(event.getBeginDate());
		System.out.println(event.getEndDate());
		
		
		
		/*System.out.println(event.getData().toString());
		System.out.println(TimeZone.getDefault());*/

		
		this.funcionarioSearchModelSelected = null;
		this.personaSearchModelSelected = null;	
		this.servicioSearchModelSelected = null;	
		 
		CalendarItem ci = (CalendarItem) event.getCalendarItem();
		if (ci == null) {
			
			editar = false;
			
			this.agendamientoSelected = new Agendamiento();
			
			this.agendamientoSelected.setInicio(this.um.calcularFecha(event.getBeginDate(), Calendar.HOUR, this.calendars.getBeginTime()));
			this.agendamientoSelected.setFin(this.um.calcularFecha(event.getEndDate(), Calendar.HOUR, this.calendars.getBeginTime()));
			this.agendamientoSelected.setSucursal(this.getCurrentSucursal());
			this.agendamientoSelected.setAgendamientoTipo(this.reg.getObjectBySigla(Tipo.class, ParamsLocal.SIGLA_TIPO_AGENDAMIENTO_SERVICIO));
			this.agendamientoSelected.setEstado(this.reg.getObjectBySigla(Tipo.class, ParamsLocal.SIGLA_TIPO_AGENDAMIENTOESTADO_PENDIENTE));
			
			this.estadoTipoSearchModelSelected = new TipoSearchModel(this.agendamientoSelected.getEstado());
			
			
			
		}else {
			
			editar = true;
			
			this.agendamientoSelected = this.reg.getObjectById(Agendamiento.class.getName(), ci.getAgendamientoid()	); 		
			this.personaSearchModelSelected = new PersonaSearchModel(this.agendamientoSelected.getPersona());
			
			//this.funcionarioSearchModelSelected = new FuncionarioSearchModel(this.agendamientoSelected.getFuncionario());
			
			if (this.agendamientoSelected.getFuncionario() != null) {
			    this.funcionarioSearchModelSelected = new FuncionarioSearchModel(this.agendamientoSelected.getFuncionario());
			} 
						
			this.servicioSearchModelSelected = new ServicioSearchModel(this.agendamientoSelected.getServicio());
			this.estadoTipoSearchModelSelected = new TipoSearchModel(this.agendamientoSelected.getEstado());
		}
		
		generarSearchModels();
	
		modal = (Window) Executions.createComponents("/sistema/zul/main/agendamientoModal.zul", this.mainComponent,
				null);
		Selectors.wireComponents(modal, this, false);
		modal.doModal();
	}
	
	//listen to the calendar-update of event data, usually send when user drag the event data 
	@Listen(CalendarsEvent.ON_ITEM_UPDATE + " = #calendars")
	@NotifyChange("*")
    public void updateAgendamiento(@BindingParam("event")CalendarsEvent event) throws ParseException {
    	
    	//System.out.println("entre en el update");
        CalendarItem ci = (CalendarItem) event.getCalendarItem();
        
       // ci.setBeginDate(event.getBeginDate());
       // ci.setEndDate(event.getEndDate());
        
       // calendarModel.update(ci);
        
        this.agendamientoSelected = this.reg.getObjectById(Agendamiento.class.getName(), ci.getAgendamientoid()	); 	
        this.agendamientoSelected.setInicio(event.getBeginDate());
        this.agendamientoSelected.setFin(event.getEndDate());
        
       // System.out.println(this.agendamientoSelected.getAgendamientoid()+ " "+this.agendamientoSelected.getInicio()+" "+this.agendamientoSelected.getFin());
        
        this.save(this.agendamientoSelected);
        
        this.dataCharger();
        
    }
	
	@Command
	@NotifyChange("*")
	public void guardarAgendamiento() throws ParseException {
		
		if (this.personaSearchModelSelected == null) {
			
			this.mensajeInfo("Debes seleccionar una Persona.");
			return;
			
		}
		
		if (this.servicioSearchModelSelected == null) {
			
			this.mensajeInfo("Debes seleccionar un Servicio.");
			return;
			
		}
		
		if (this.estadoTipoSearchModelSelected == null) {
			
			this.mensajeInfo("Debes seleccionar un Estado.");
			return;
			
		}
		
		this.agendamientoSelected.setPersona(this.personaSearchModelSelected.getPersona());
		this.agendamientoSelected.setServicio(this.servicioSearchModelSelected.getServicio());
		this.agendamientoSelected.setEstado(this.estadoTipoSearchModelSelected.getTipo());
		
		agendamientoSelected.setFuncionario(
			    Optional.ofNullable(funcionarioSearchModelSelected)
			            .map(FuncionarioSearchModel::getFuncionario)
			            .orElse(null)
			);
		
		this.save(this.agendamientoSelected);
		
		 Notification.show(editar 
			        ? "Agendamiento editado." 
			        : "Agendamiento guardado."
			    );
		
		 editar = false;
		
		
		this.dataCharger();
		
		this.modal.detach();
		
	}
	
	

	public SimpleCalendarModel getCalendarModel() {
		return calendarModel;
	}

	public void setCalendarModel(SimpleCalendarModel calendarModel) {
		this.calendarModel = calendarModel;
	}

	public Agendamiento getAgendamientoSelected() {
		return agendamientoSelected;
	}

	public void setAgendamientoSelected(Agendamiento agendamientoSelected) {
		this.agendamientoSelected = agendamientoSelected;
	}

	public ListModelArray<PersonaSearchModel> getlPersonasSearchModel() {
		return lPersonasSearchModel;
	}

	public void setlPersonasSearchModel(ListModelArray<PersonaSearchModel> lPersonasSearchModel) {
		this.lPersonasSearchModel = lPersonasSearchModel;
	}

	public ListModelArray<FuncionarioSearchModel> getlFuncionariosSearchModel() {
		return lFuncionariosSearchModel;
	}

	public void setlFuncionariosSearchModel(ListModelArray<FuncionarioSearchModel> lFuncionariosSearchModel) {
		this.lFuncionariosSearchModel = lFuncionariosSearchModel;
	}

	public ListModelArray<ServicioSearchModel> getlServiciosSearchModel() {
		return lServiciosSearchModel;
	}

	public void setlServiciosSearchModel(ListModelArray<ServicioSearchModel> lServiciosSearchModel) {
		this.lServiciosSearchModel = lServiciosSearchModel;
	}

	public PersonaSearchModel getPersonaSearchModelSelected() {
		return personaSearchModelSelected;
	}

	public void setPersonaSearchModelSelected(PersonaSearchModel personaSearchModelSelected) {
		this.personaSearchModelSelected = personaSearchModelSelected;
	}

	public FuncionarioSearchModel getFuncionarioSearchModelSelected() {
		return funcionarioSearchModelSelected;
	}

	public void setFuncionarioSearchModelSelected(FuncionarioSearchModel funcionarioSearchModelSelected) {
		this.funcionarioSearchModelSelected = funcionarioSearchModelSelected;
	}

	public ServicioSearchModel getServicioSearchModelSelected() {
		return servicioSearchModelSelected;
	}

	public void setServicioSearchModelSelected(ServicioSearchModel servicioSearchModelSelected) {
		this.servicioSearchModelSelected = servicioSearchModelSelected;
	}

	public ListModelArray<TipoSearchModel> getlEstadosTiposSearchModel() {
		return lEstadosTiposSearchModel;
	}

	public void setlEstadosTiposSearchModel(ListModelArray<TipoSearchModel> lEstadosTiposSearchModel) {
		this.lEstadosTiposSearchModel = lEstadosTiposSearchModel;
	}

	public TipoSearchModel getEstadoTipoSearchModelSelected() {
		return estadoTipoSearchModelSelected;
	}

	public void setEstadoTipoSearchModelSelected(TipoSearchModel estadoTipoSearchModelSelected) {
		this.estadoTipoSearchModelSelected = estadoTipoSearchModelSelected;
	}
	
	

}
