package com.dogtorhouse.app.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dogtorhouse.app.entity.Cita;
import com.dogtorhouse.app.entity.Cliente;
import com.dogtorhouse.app.entity.Paciente;
import com.dogtorhouse.app.entity.Veterinario;
import com.dogtorhouse.app.service.IClienteService;
import com.dogtorhouse.app.service.impl.CitaService;
import com.dogtorhouse.app.service.impl.ClienteService;
import com.dogtorhouse.app.service.impl.PacienteService;
import com.dogtorhouse.app.service.impl.VeterinarioService;
import com.dogtorhouse.app.util.Base64Utils;
import com.dogtorhouse.app.util.Constantes;
import com.dogtorhouse.app.util.Mensaje;
import com.dogtorhouse.app.util.criteria.CriterioCita;
import com.dogtorhouse.app.util.criteria.CriterioCliente;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@SessionAttributes("cliente")
public class ClienteController extends BaseController{

	@Autowired
	private  ClienteService clienteService;
	@Autowired
	private PacienteService pacienteService;
	
	private Base64Utils base64Utils = new Base64Utils();
	
	@RequestMapping(value="/dogtorhouse/clientes")
	public String listarClientes(Map<String,Object> model,HttpSession session) {
		if(!usuarioLogueado((Veterinario)session.getAttribute("veterinarioSesion"))){
			return "redirect:/dogtorhouse/login";
		}
		super.init(model, session);
		model.put("criterio",new CriterioCliente());
		model.put("casa", "casa");
		List<Cliente> clientes = clienteService.findAll();
		model.put("clientes", clientes);
		return "dogtorhouse/clientes/listaClientes";
	}
	
	@RequestMapping(value="/dogtorhouse/clientes", method = RequestMethod.POST)
	public String listarClientesFiltro(CriterioCliente criterio,Map<String,Object> model,HttpSession session) {
		if(!usuarioLogueado((Veterinario)session.getAttribute("veterinarioSesion"))){
			return "redirect:/dogtorhouse/login";
		}
		super.init(model, session);
		model.put("criterio",new CriterioCliente());
		List<Cliente> clientes = clienteService.findAllCriterioCliente(criterio);
		super.mostrarMensajeInformativo("Se encontraron "+clientes.size()+" registros", model);
		model.put("clientes", clientes);
		return "dogtorhouse/clientes/listaClientes";
	}
	
	@RequestMapping(value="/dogtorhouse/clientes/cliente")
	public String crearClienteForm(Map <String, Object> model,HttpSession session) {
		if(!usuarioLogueado((Veterinario)session.getAttribute("veterinarioSesion"))){
			return "redirect:/dogtorhouse/login";
		}
		super.init(model, session);
		
		Cliente cliente = new Cliente();
		model.put("base64Utils", base64Utils);
		
		model.put("cliente", cliente);
		model.put("operacion", "Crear Cliente");
		
		return "dogtorhouse/clientes/clienteForm";
		
	}
	
	@RequestMapping(value="/dogtorhouse/clientes/cliente", method = RequestMethod.POST)
	public String guardarCliente(@Valid Cliente cliente, BindingResult result, Model model, SessionStatus status, RedirectAttributes redirectAttributes,HttpSession session,@RequestParam(value="file",required = false) MultipartFile file)  {
		if(!usuarioLogueado((Veterinario)session.getAttribute("veterinarioSesion"))){
			return "redirect:/dogtorhouse/login";
		}
		super.init(model, session);
		if(result.hasErrors()) {
			super.mostrarMensajeError("Hay errores en el formulario", null,model);
			return "dogtorhouse/clientes/clienteForm";
		}
		
		if((cliente.getId()==null || !clienteService.findById(cliente.getId()).get().getEmail().equals(cliente.getEmail())) && !clienteService.validoEmail(cliente.getEmail().trim().toLowerCase())) {
			super.mostrarMensajeError("El correo "+cliente.getEmail()+" ya esta registrado", null,model);
			return "dogtorhouse/clientes/clienteForm";
		}
		
		
		if (!file.isEmpty()) {
	        try {
	            // Obtener los bytes de la foto
	        	if (!file.isEmpty()) {
	                byte[] fotoBytes = file.getBytes();
	                cliente.setFoto(fotoBytes);
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	            // Manejar la excepción de lectura de la foto
	        }
	    }
		
		System.out.println("fecha:"+ cliente.getFechaNacimiento());
	
		clienteService.save(cliente);
		status.setComplete();
		super.mostrarMensajeCorrecto("Cliente guardado correctamente", redirectAttributes);
		
		return "redirect:/dogtorhouse/clientes";
		
	}
	
	@RequestMapping(value="/dogtorhouse/clientes/cliente/{idCliente}")
	public String editarClienteForm(@PathVariable(value="idCliente") Long id, Map <String, Object> model,RedirectAttributes redirectAttributes,HttpSession session) {
		if(!usuarioLogueado((Veterinario)session.getAttribute("veterinarioSesion"))){
			return "redirect:/dogtorhouse/login";
		}
		super.init(model, session);
		
		List<Paciente> pacientes = pacienteService.findAll();
		Optional<Cliente> clienteOptional = clienteService.findById(id);
		
		if(!clienteOptional.isPresent()) {
			super.mostrarMensajeError("El cliente id "+id+" no existe", redirectAttributes,null);
			return "redirect:/dogtorhouse/clientes";
		}
		
		byte[] fotoBytes = clienteOptional.get().getFoto();
		model.put("base64Utils", base64Utils);
	    // Agregar los bytes de la foto al modelo
	    model.put("fotoBytes", fotoBytes);

		model.put("cliente", clienteOptional.get());
		model.put("pacientes", pacientes);
		model.put("operacion", "Editar Cliente");
		
		//ruta del html del form
		return "dogtorhouse/clientes/clienteForm";
	}
	
	@RequestMapping(value="/dogtorhouse/clientes/cliente/eliminar/{id}")
	public String eliminar(@PathVariable(value="id") Long id,RedirectAttributes redirectAttributes,Model model,HttpSession session) {
		if(!usuarioLogueado((Veterinario)session.getAttribute("veterinarioSesion"))){
			return "redirect:/dogtorhouse/login";
		}
		super.init(model, session);
		Optional<Cliente> cliente = clienteService.findById(id);
		if(!cliente.isPresent()) {
			super.mostrarMensajeError("El cliente id "+id+" no existe", redirectAttributes,null);
			return "redirect:/dogtorhouse/clientes";
		}
		
		clienteService.deleteById(id);
		super.mostrarMensajeCorrecto("Cliente eliminado correctamente", redirectAttributes);
		
		return "redirect:/dogtorhouse/clientes";

	}

}