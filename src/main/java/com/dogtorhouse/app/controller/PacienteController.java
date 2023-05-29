package com.dogtorhouse.app.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dogtorhouse.app.entity.Cliente;
import com.dogtorhouse.app.entity.Paciente;
import com.dogtorhouse.app.entity.Veterinario;
import com.dogtorhouse.app.service.impl.ClienteService;
import com.dogtorhouse.app.service.impl.PacienteService;
import com.dogtorhouse.app.util.Base64Utils;
import com.dogtorhouse.app.util.Constantes;
import com.dogtorhouse.app.util.criteria.CriterioPaciente;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@SessionAttributes("paciente")
public class PacienteController extends BaseController {

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private PacienteService pacienteService;

	private Base64Utils base64Utils = new Base64Utils();

	@RequestMapping(value = "/dogtorhouse/pacientes")
	public String listarPacientes(Map<String, Object> model, HttpSession session) {
		if (!usuarioLogueado((Veterinario) session.getAttribute("veterinarioSesion"))) {
			return "redirect:/login";
		}
		super.init(model, session);
		cargarListas(model);
		model.put("criterio", new CriterioPaciente());
		List<Paciente> pacientes = pacienteService.findAll();
		List<Cliente> clientes = clienteService.findAll();
		model.put("pacientes", pacientes);
		model.put("clientes", clientes);
		return "dogtorhouse/pacientes/listaPacientes";
	}

	@RequestMapping(value = "/dogtorhouse/pacientes", method = RequestMethod.POST)
	public String listarPacientesFiltro(CriterioPaciente criterio, Map<String, Object> model, HttpSession session) {
		if (!usuarioLogueado((Veterinario) session.getAttribute("veterinarioSesion"))) {
			return "redirect:/login";
		}
		super.init(model, session);
		cargarListas(model);
		model.put("criterio", new CriterioPaciente());
		List<Paciente> pacientes = pacienteService.findAllCriterioPaciente(criterio);
		super.mostrarMensajeInformativo("Se encontraron " + pacientes.size() + " registros", model);
		model.put("pacientes", pacientes);
		return "dogtorhouse/pacientes/listaPacientes";
	}

	@RequestMapping(value = "/dogtorhouse/pacientes/paciente")
	public String crearPacienteForm(Map<String, Object> model, HttpSession session) {
		if (!usuarioLogueado((Veterinario) session.getAttribute("veterinarioSesion"))) {
			return "redirect:/login";
		}

		super.init(model, session);
		this.cargarListas(model);
		Paciente paciente = new Paciente();
		List<Cliente> clientes = clienteService.findAll();

		model.put("paciente", paciente);
		model.put("clientes", clientes);
		model.put("operacion", "Crear Paciente");

		return "dogtorhouse/pacientes/pacienteForm";

	}

	@RequestMapping(value = "/dogtorhouse/pacientes/paciente", method = RequestMethod.POST)
	public String guardarPaciente(@Valid Paciente paciente, BindingResult result, Model model, SessionStatus status,
			RedirectAttributes redirectAttributes, HttpSession session,
			@RequestParam(value = "file", required = false) MultipartFile file) {
		if (!usuarioLogueado((Veterinario) session.getAttribute("veterinarioSesion"))) {
			return "redirect:/login";
		}
		super.init(model, session);
		this.cargarListas(model);
		if (result.hasErrors()) {
			super.mostrarMensajeError("Hay errores en el formulario", null, model);
			return "dogtorhouse/pacientes/pacienteForm";
		}
		if (!file.isEmpty()) {
			try {
				// Obtener los bytes de la foto
				if (!file.isEmpty()) {
					byte[] fotoBytes = file.getBytes();
					paciente.setFoto(fotoBytes);
				}
			} catch (IOException e) {
				e.printStackTrace();
				// Manejar la excepción de lectura de la foto
			}
		}
		pacienteService.save(paciente);
		status.setComplete();
		super.mostrarMensajeCorrecto("Paciente guardado correctamente", redirectAttributes);
		return "redirect:/dogtorhouse/pacientes";

	}

	@RequestMapping(value = "/dogtorhouse/pacientes/paciente/{idPaciente}")
	public String editarPacienteForm(@PathVariable(value = "idPaciente") Long id, Map<String, Object> model,
			RedirectAttributes redirectAttributes, HttpSession session) {
		if (!usuarioLogueado((Veterinario) session.getAttribute("veterinarioSesion"))) {
			return "redirect:/login";
		}
		super.init(model, session);
		this.cargarListas(model);
		List<Cliente> clientes = clienteService.findAll();
		Optional<Paciente> pacienteOptional = pacienteService.findById(id);

		if (!pacienteOptional.isPresent()) {
			super.mostrarMensajeError("El paciente id " + id + " no existe", redirectAttributes, null);
			return "redirect:/dogtorhouse/pacientes";
		}

		byte[] fotoBytes = pacienteOptional.get().getFoto();
		model.put("base64Utils", base64Utils);
		// Agregar los bytes de la foto al modelo
		model.put("fotoBytes", fotoBytes);
		model.put("paciente", pacienteOptional.get());
		model.put("clientes", clientes);
		model.put("operacion", "Editar Paciente");

		// ruta del html del form
		return "dogtorhouse/pacientes/pacienteForm";
	}

	@RequestMapping(value = "/dogtorhouse/pacientes/paciente/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttributes, Model model,
			HttpSession session) {
		if (!usuarioLogueado((Veterinario) session.getAttribute("veterinarioSesion"))) {
			return "redirect:/login";
		}
		super.init(model, session);
		this.cargarListas(model);
		Optional<Paciente> paciente = pacienteService.findById(id);
		if (!paciente.isPresent()) {
			super.mostrarMensajeError("El paciente id " + id + " no existe", redirectAttributes, null);
			return "redirect:/dogtorhouse/pacientes";
		}

		pacienteService.deleteById(id);
		super.mostrarMensajeCorrecto("Paciente eliminado correctamente", redirectAttributes);
		return "redirect:/dogtorhouse/pacientes";

	}

	@SuppressWarnings("unchecked")
	protected void cargarListas(Object model) {
		List<Cliente> clientes = clienteService.findAll();

		if (model instanceof Model) {

			((Model) model).addAttribute("clientes", clientes);
			((Model) model).addAttribute("especiesRazas", Constantes.listaEspeciesRazas);
			((Model) model).addAttribute("especies", Constantes.listaEspeciesRazas.keySet());
		} else if (model instanceof Map<?, ?>) {
			((Map<String, Object>) model).put("clientes", clientes);
			((Map<String, Object>) model).put("especiesRazas", Constantes.listaEspeciesRazas);
			((Map<String, Object>) model).put("especies", Constantes.listaEspeciesRazas.keySet());

		}
	}

}