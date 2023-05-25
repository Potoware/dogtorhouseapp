package com.dogtorhouse.app.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dogtorhouse.app.entity.Cita;
import com.dogtorhouse.app.entity.Cliente;
import com.dogtorhouse.app.entity.Paciente;
import com.dogtorhouse.app.entity.Veterinario;
import com.dogtorhouse.app.repository.CitaRepository;
import com.dogtorhouse.app.service.impl.CitaService;
import com.dogtorhouse.app.service.impl.ClienteService;
import com.dogtorhouse.app.service.impl.PacienteService;
import com.dogtorhouse.app.service.impl.VeterinarioService;
import com.dogtorhouse.app.util.Constantes;
import com.dogtorhouse.app.util.criteria.CriterioCita;
import com.dogtorhouse.app.util.criteria.CriterioCliente;

@Controller
@SessionAttributes("cita")
public class CitaController extends BaseController {

	@Autowired
	private CitaService citaService;
	@Autowired
	private PacienteService pacienteService;

	@Autowired
	private VeterinarioService veterinarioService;
	
	@Autowired
	private ClienteService clienteService;

	@RequestMapping(value = { "/dogtorhouse/citas" })
	public String listarCitas(Map<String, Object> model, HttpSession session) {
		if (!usuarioLogueado((Veterinario) session.getAttribute("veterinarioSesion"))) {
			return "redirect:/login";
		}
		init(model, session);
		model.put("criterio", new CriterioCita());
		List<Cita> citas = citaService.findAll();
		model.put("citas", citas);
		List<Cliente> clientes = clienteService.findAll();
		List<Paciente> pacientes = pacienteService.findAll();
		List<Veterinario> veterinarios = veterinarioService.findAll();
		model.put("clientes",clientes);
		model.put("pacientes",pacientes);
		model.put("veterinarios", veterinarios);
		return "dogtorhouse/citas/listaCitas";
	}

	@RequestMapping(value = "/dogtorhouse/citas", method = RequestMethod.POST)
	public String listarCitasFiltro(@Valid CriterioCita criterio, Map<String, Object> model, HttpSession session) {
		if (!usuarioLogueado((Veterinario) session.getAttribute("veterinarioSesion"))) {
			return "redirect:/login";
		}
		init(model, session);
		model.put("criterio", new CriterioCita());
		List<Cita> citas = citaService.findAllCriterioCita(criterio);
		List<Cliente> clientes = clienteService.findAll();
		List<Paciente> pacientes = pacienteService.findAll();
		List<Veterinario> veterinarios = veterinarioService.findAll();
		model.put("clientes",clientes);
		model.put("pacientes",pacientes);
		model.put("veterinarios", veterinarios);
		super.mostrarMensajeInformativo("Se encontraron " + citas.size() + " registros", model);

		model.put("citas", citas);
		return "dogtorhouse/citas/listaCitas";
	}

	@RequestMapping(value = "/dogtorhouse/citas/cita")
	public String crearCitaForm(Map<String, Object> model, HttpSession session) {
		if (!usuarioLogueado((Veterinario) session.getAttribute("veterinarioSesion"))) {
			return "redirect:/login";
		}
		init(model, session);
		Cita cita = new Cita();
		List<Paciente> pacientes = pacienteService.findAll();
		List<Veterinario> veterinarios = veterinarioService.findAll();

		model.put("cita", cita);
		model.put("pacientes", pacientes);
		model.put("veterinarios", veterinarios);
		model.put("operacion", "Crear Cita");

		return "dogtorhouse/citas/citaForm";

	}

	@RequestMapping(value = "/dogtorhouse/citas/cita", method = RequestMethod.POST)
	public String guardarCita(@Valid Cita cita, BindingResult result, Model model,
			@RequestParam(value = "origen", defaultValue = "") String origen, SessionStatus status,
			RedirectAttributes redirectAttributes, HttpSession session,
			@RequestParam(value = "medicacion", required = false) List<String> medicacionList) {
		if (!usuarioLogueado((Veterinario) session.getAttribute("veterinarioSesion"))) {
			return "redirect:/login";
		}
		if (result.hasErrors()) {
			mostrarMensajeError("Hay errores en el formulario", null, model);
			init(model, session);
			return "dogtorhouse/citas/citaForm";
		}
		Cita auxCita = citaService.save(cita);
		status.setComplete();

		switch (origen) {
		case Constantes.REAGENDAR_CITA:
			cita.setEstado(Constantes.CODIGO_ESTADO_CITA_ACTIVA);
			mostrarMensajeCorrecto("Cita reprogramada correctamente", redirectAttributes);
			return "redirect:/dogtorhouse/citas/cita/" + cita.getId();
		case Constantes.CANCELAR_CITA:
			if (cita.getMotivoBaja().equals(Constantes.ELIMINAR_BEAN_ERROR)) {
				cita.setEstado(Constantes.CODIGO_ESTADO_CITA_BAJA);
				cita.setFechaBaja(new Date());
			} else {
				cita.setEstado(Constantes.CODIGO_ESTADO_CITA_CANCELADA);
			}
			citaService.save(cita);
			mostrarMensajeCorrecto("Cita cancelada correctamente", redirectAttributes);
			return "redirect:/dogtorhouse/citas/cita/" + cita.getId();
		case Constantes.ATENDER_CITA:
			
			cita.setEstado(cita.getEstado());
			cita.setFechaAtencion(new Date());
			if (medicacionList != null) {
				medicacionList.removeIf(ml->ml.equals("[]"));
				for (int i = 0; i < medicacionList.size(); i++) {
				    String medicacion = medicacionList.get(i);
				    
				    medicacion = medicacion.replaceAll("\\[", ""); // Eliminar caracteres "["
				    medicacion = medicacion.replaceAll("\\]", ""); // Eliminar caracteres "]"
				    medicacion = medicacion.replaceAll("Eliminar", ""); // Eliminar palabra "Eliminar"
				    medicacion = medicacion.trim(); // Eliminar espacios al inicio y final
				    
				    medicacionList.set(i, medicacion); // Reemplazar el elemento modificado en la lista
				}

				cita.setMedicacion(medicacionList);
				
				
			}
			citaService.save(cita);
			mostrarMensajeCorrecto("Atención añadida correctamente", redirectAttributes);
			return "redirect:/dogtorhouse/citas/atenderCita/" + cita.getId();
			
		default:
			cita.setEstado(Constantes.CODIGO_ESTADO_CITA_ACTIVA);
			citaService.save(cita);

		

			mostrarMensajeCorrecto("Cita guardada correctamente", redirectAttributes);
			return "redirect:/dogtorhouse/citas/cita/" + cita.getId();
		}
	}

	@RequestMapping(value = "/dogtorhouse/citas/cita/{idCita}")
	public String editarCitaForm(@PathVariable(value = "idCita") Long id, Map<String, Object> model,
			RedirectAttributes redirectAttributes, HttpSession session) {
		if (!usuarioLogueado((Veterinario) session.getAttribute("veterinarioSesion"))) {
			return "redirect:/login";
		}
		init(model, session);

		List<Paciente> pacientes = pacienteService.findAll();
		List<Veterinario> veterinarios = veterinarioService.findAll();
		Optional<Cita> citaOptional = citaService.findById(id);

		if (!citaOptional.isPresent()) {
			super.mostrarMensajeError("La cita id " + id + " no existe", redirectAttributes, null);
			return "redirect:/dogtorhouse/citas";
		}

		model.put("cita", citaOptional.get());
		model.put("veterinarios", veterinarios);
		model.put("pacientes", pacientes);
		if(citaOptional.get().getEstado().equals(Constantes.CODIGO_ESTADO_CITA_CANCELADA)) {
			model.put("operacion", "Detalle Cita");
		}else {
			model.put("operacion", "Editar Cita");
		}
		

		// ruta del html del form
		return "dogtorhouse/citas/citaForm";
	}

	@RequestMapping(value = "/dogtorhouse/citas/atenderCita/{idCita}")
	public String atenderCitaForm(@PathVariable(value = "idCita") Long id, Map<String, Object> model,
			RedirectAttributes redirectAttributes, HttpSession session) {
		if (!usuarioLogueado((Veterinario) session.getAttribute("veterinarioSesion"))) {
			return "redirect:/login";
		}
		init(model, session);

		List<Paciente> pacientes = pacienteService.findAll();
		List<Veterinario> veterinarios = veterinarioService.findAll();
		Optional<Cita> citaOptional = citaService.findById(id);

		if (!citaOptional.isPresent()) {
			super.mostrarMensajeError("La cita id " + id + " no existe", redirectAttributes, null);
			return "redirect:/dogtorhouse/citas";
		}

		model.put("cita", citaOptional.get());
		model.put("veterinarios", veterinarios);
		model.put("pacientes", pacientes);
		if(citaOptional.get().getEstado().equals(Constantes.CODIGO_ESTADO_CITA_FINALIZADA)) {
			model.put("operacion", "Detalle Cita");
		}else {
			model.put("operacion", "Atender Cita");
		}
		

		// ruta del html del form
		return "dogtorhouse/citas/citaForm";
	}

	@RequestMapping(value = "/dogtorhouse/citas/cita/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttributes, Model model,
			HttpSession session) {
		if (!usuarioLogueado((Veterinario) session.getAttribute("veterinarioSesion"))) {
			return "redirect:/login";
		}
		init(model, session);
		Optional<Cita> cita = citaService.findById(id);
		if (!cita.isPresent()) {
			super.mostrarMensajeError("La cita id " + id + " no existe", redirectAttributes, null);
			return "redirect:/dogtorhouse/citas";
		}

		citaService.deleteById(id);
		super.mostrarMensajeCorrecto("Cita eliminada correctamente", redirectAttributes);

		return "redirect:/dogtorhouse/citas";

	}

	@RequestMapping(value = "/dogtorhouse/reportes/reporte")
	public String reporte(RedirectAttributes redirectAttributes, Model model, HttpSession session) {
		if (!usuarioLogueado((Veterinario) session.getAttribute("veterinarioSesion"))) {
			return "redirect:/login";
		}
		init(model, session);
		return "dogtorhouse/reportes/reporte";

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void init(Object model, HttpSession session) {
		// TODO Auto-generated method stub
		super.init(model, session);
		if (model instanceof Model) {
			((Model) model).addAttribute("listaMotivosCancelacion", Constantes.motivosCancelacion);
			((Model) model).addAttribute("listaEstadosCita", Constantes.listaEstadosCita);
		} else if (model instanceof Map<?, ?>) {
			((Map<String, Object>) model).put("listaMotivosCancelacion", Constantes.motivosCancelacion);
			((Map<String, Object>) model).put("listaEstadosCita", Constantes.listaEstadosCita);
		}
	}

}