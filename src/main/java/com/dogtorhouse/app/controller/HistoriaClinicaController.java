package com.dogtorhouse.app.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dogtorhouse.app.entity.Cita;
import com.dogtorhouse.app.entity.Paciente;
import com.dogtorhouse.app.entity.Veterinario;
import com.dogtorhouse.app.service.impl.CitaService;
import com.dogtorhouse.app.service.impl.PacienteService;
import com.dogtorhouse.app.util.Base64Utils;
import com.dogtorhouse.app.util.Constantes;

import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes("cita")
public class HistoriaClinicaController extends BaseController {

	@Autowired
	private CitaService citaService;
	@Autowired
	private PacienteService pacienteService;
	
	private Base64Utils base64Utils = new Base64Utils();
	
	@RequestMapping(value = "/dogtorhouse/pacientes/hc/{idPaciente}")
	public String detalleHCPaciente(@PathVariable(value = "idPaciente") Long id, Map<String, Object> model,
			RedirectAttributes redirectAttributes, HttpSession session) {
		if (!usuarioLogueado((Veterinario) session.getAttribute("veterinarioSesion"))) {
			return "redirect:/login";
		}
		init(model, session);
		Optional<Paciente> pacienteOptional = pacienteService.findById(id);
		
		if (!pacienteOptional.isPresent()) {
			super.mostrarMensajeError("El paciente id " + id + " no existe", redirectAttributes, null);
			return "redirect:/dogtorhouse/pacientes";
		}
		
		List<Cita> citas = citaService.findByPaciente(pacienteOptional.get());
		citas.removeIf(cita->!cita.getEstado().equals("003"));
		
		byte[] fotoBytes = pacienteOptional.get().getFoto();
		byte[] fotoCBytes = pacienteOptional.get().getCliente().getFoto();
		model.put("base64Utils", base64Utils);
	    // Agregar los bytes de la foto al modelo
	    model.put("fotoBytes", fotoBytes);
	    
	    model.put("fotoCBytes", fotoCBytes);

		model.put("citas", citas);
		model.put("paciente", pacienteOptional.get());
		model.put("operacion", "Historia Clinica");
		
		return "dogtorhouse/hcs/hcDetail";
	}
	
}