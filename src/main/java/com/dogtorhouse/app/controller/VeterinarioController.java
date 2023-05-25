package com.dogtorhouse.app.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dogtorhouse.app.entity.Rol;
import com.dogtorhouse.app.entity.Veterinario;
import com.dogtorhouse.app.security.service.ICifradoService;
import com.dogtorhouse.app.service.IRolService;
import com.dogtorhouse.app.service.impl.VeterinarioService;
import com.dogtorhouse.app.util.Base64Utils;
import com.dogtorhouse.app.util.criteria.CriterioVeterinario;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@SessionAttributes("veterinario")
public class VeterinarioController extends BaseController {

	@Autowired
	private VeterinarioService veterinarioService;
	@Autowired
	private ICifradoService cifradoService;
	@Autowired
	private IRolService rolService;
	private Base64Utils base64Utils = new Base64Utils();

	@RequestMapping(value = "/dogtorhouse/veterinarios")
	public String listarVeterinarios(Map<String, Object> model, HttpSession session) {
		if (!usuarioLogueado((Veterinario) session.getAttribute("veterinarioSesion"))) {
			return "redirect:/login";
		}
		super.init(model, session);
		model.put("criterio", new CriterioVeterinario());
		List<Veterinario> veterinarios = veterinarioService.findAll();
		model.put("veterinarios", veterinarios);
		return "dogtorhouse/veterinarios/listaVeterinarios";
	}

	@RequestMapping(value = "/dogtorhouse/veterinarios", method = RequestMethod.POST)
	public String listarVeterinariosFiltro(@Valid CriterioVeterinario criterio, Map<String, Object> model,
			HttpSession session) {
		if (!usuarioLogueado((Veterinario) session.getAttribute("veterinarioSesion"))) {
			return "redirect:/login";
		}
		super.init(model, session);
		model.put("criterio", new CriterioVeterinario());
		List<Veterinario> veterinarios = veterinarioService.findAllCriterioVeterinario(criterio);
		super.mostrarMensajeInformativo("Se encontraron " + veterinarios.size() + " registros", model);
		model.put("veterinarios", veterinarios);
		return "dogtorhouse/veterinarios/listaVeterinarios";
	}

	@RequestMapping(value = "/dogtorhouse/veterinarios/veterinario")
	public String crearVeterinarioForm(Map<String, Object> model, HttpSession session) {
		if (!usuarioLogueado((Veterinario) session.getAttribute("veterinarioSesion"))) {
			return "redirect:/login";
		}
		super.init(model, session);
		Veterinario veterinario = new Veterinario();

		model.put("veterinario", veterinario);
		model.put("operacion", "Crear Veterinario");

		return "dogtorhouse/veterinarios/veterinarioForm";

	}

	@RequestMapping(value = "/dogtorhouse/veterinarios/veterinario", method = RequestMethod.POST)
	public String guardarVeterinario(@Valid Veterinario veterinario, BindingResult result, Model model,
			SessionStatus status, RedirectAttributes redirectAttributes, HttpSession session,
			@RequestParam(value = "origen", required = false) String origen,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "passwordConfirm", required = false) String passwordConfirm,
			@RequestParam("rolesValue") String rolesValue,
			@RequestParam(value = "file", required = false) MultipartFile file) {
		if (!usuarioLogueado((Veterinario) session.getAttribute("veterinarioSesion"))) {
			return "redirect:/login";
		}
		super.init(model, session);

		System.out.println(result.getAllErrors());
		if (result.hasErrors() && !"CONTRASENIA".equals(origen)) {
			super.mostrarMensajeError("Hay errores en el formulario", null, model);
			return "dogtorhouse/veterinarios/veterinarioForm";
		}

		if ((veterinario.getId() == null
				|| !veterinarioService.findById(veterinario.getId()).get().getEmail().equals(veterinario.getEmail()))
				&& !veterinarioService.validoEmail(veterinario.getEmail().trim().toLowerCase())) {
			super.mostrarMensajeError("El correo " + veterinario.getEmail() + " ya esta registrado", null, model);
			return "dogtorhouse/veterinarios/veterinarioForm";
		}

		if ("CONTRASENIA".equals(origen)) {
			if (!password.equals(passwordConfirm)) {
				super.mostrarMensajeError("Las contraseñas no coinciden", null, model);
				return "dogtorhouse/veterinarios/veterinarioForm";
			}

			if (password.length() < 8) {
				super.mostrarMensajeError("Las contraseña debe tener minimo 8 caracteres", null, model);
				return "dogtorhouse/veterinarios/veterinarioForm";
			}

			veterinario.setPassword(cifradoService.cifrarContrasenia(password));
		}

		if (rolesValue != null) {
			List<Rol> auxRoles = rolService.findAll();
			auxRoles.removeIf(rol -> !rol.getId().toString().equals(rolesValue));

			veterinario.setRoles(auxRoles.stream().collect(Collectors.toSet()));

		}
		
		if (!file.isEmpty()) {
	        try {
	            // Obtener los bytes de la foto
	        	if (!file.isEmpty()) {
	                byte[] fotoBytes = file.getBytes();
	                veterinario.setFoto(fotoBytes);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	            // Manejar la excepción de lectura de la foto
	        }
	    }

		if (veterinario.getId() == null)
			veterinario.setPassword(cifradoService.cifrarContrasenia(veterinario.getPassword()));

		veterinarioService.save(veterinario);
		status.setComplete();
		super.mostrarMensajeCorrecto("Veterinario guardado correctamente", redirectAttributes);

		return "redirect:/dogtorhouse/veterinarios";

	}

	@RequestMapping(value = "/dogtorhouse/veterinarios/veterinario/{idVeterinario}")
	public String editarVeterinarioForm(@PathVariable(value = "idVeterinario") Long id, Map<String, Object> model,
			RedirectAttributes redirectAttributes, HttpSession session) {
		if (!usuarioLogueado((Veterinario) session.getAttribute("veterinarioSesion"))) {
			super.mostrarMensajeError("Debe iniciar sesión para acceder a esta pagina", redirectAttributes, null);
			return "redirect:/login";
		}
		super.init(model, session);
		Optional<Veterinario> veterinarioOptional = veterinarioService.findById(id);

		if (!veterinarioOptional.isPresent()) {
			super.mostrarMensajeError("El veterinario id " + id + " no existe", redirectAttributes, null);

			return "redirect:/dogtorhouse/veterinarios";
		}

		byte[] fotoBytes = veterinarioOptional.get().getFoto();
		model.put("base64Utils", base64Utils);
		// Agregar los bytes de la foto al modelo
		model.put("fotoBytes", fotoBytes);

		model.put("veterinario", veterinarioOptional.get());
		model.put("idRol", veterinarioOptional.get().getRoles().stream().findFirst().map(Rol::getId).orElse(null));

		model.put("operacion", "Editar Veterinario");

		// ruta del html del form
		return "dogtorhouse/veterinarios/veterinarioForm";
	}

	@RequestMapping(value = "/dogtorhouse/veterinarios/veterinario/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes redirectAttributes, Model model,
			HttpSession session) {
		if (!usuarioLogueado((Veterinario) session.getAttribute("veterinarioSesion"))) {
			super.mostrarMensajeError("Debe iniciar sesión para acceder a esta pagina", redirectAttributes, null);
			return "redirect:/login";
		}
		super.init(model, session);
		Optional<Veterinario> veterinario = veterinarioService.findById(id);
		if (!veterinario.isPresent()) {
			super.mostrarMensajeError("El veterinario id " + id + " no existe", redirectAttributes, null);
			return "redirect:/dogtorhouse/veterinarios";
		}
		veterinarioService.deleteById(id);

		return "redirect:/dogtorhouse/veterinarios";

	}

}