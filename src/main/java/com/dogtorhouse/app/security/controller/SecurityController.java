package com.dogtorhouse.app.security.controller;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dogtorhouse.app.entity.Veterinario;
import com.dogtorhouse.app.security.service.impl.CifradoService;
import com.dogtorhouse.app.service.IVeterinarioService;
import com.dogtorhouse.app.util.Base64Utils;
import com.dogtorhouse.app.util.Mensaje;


@Controller
@SessionAttributes("veterinario")
public class SecurityController {
	
	@Autowired
	private IVeterinarioService veterinarioService;
	
	@Autowired
	private CifradoService cifradoService;
	private Base64Utils base64Utils = new Base64Utils();
	
	@RequestMapping(value ={"/login"})
	public String login(Map <String, Object> model,HttpSession session) {
		Veterinario veterinario = new Veterinario();
		if(((Model) model).getAttribute("mensaje")!=null) {
			System.out.println((Mensaje) ((Model) model).getAttribute("mensaje"));
		}else {
			((Model) model).addAttribute("mensaje", new Mensaje());
		}
		model.put("veterinario", veterinario);
		session.removeAttribute("veterinarioSesion");
		return "dogtorhouse/login";
		
	}
	
	@RequestMapping(value = "/login/verificar",method=RequestMethod.POST)
	public String validarLogin(Veterinario veterinario, Model model, SessionStatus status, HttpSession session, RedirectAttributes redirectAttributes) {
		boolean credencialesCorrectas = false;
		Optional<Veterinario> veterinarioConsultado = null;
		if(veterinario!=null && veterinario.getEmail()!=null && veterinario.getPassword()!=null) {
			
			veterinarioConsultado = veterinarioService.findByEmail(veterinario.getEmail());
			
			if(veterinarioConsultado.isPresent() 
					&& cifradoService.verificarContrasenia(veterinario.getPassword(),veterinarioConsultado.get().getPassword())
					) {
				credencialesCorrectas=true;
				veterinarioConsultado.get().setPassword(null);
				
				
			}
		}
		session.setAttribute("veterinarioSesion", veterinarioConsultado.isPresent()?veterinarioConsultado.get():null);
		byte[] fotoBytes = veterinarioConsultado.get().getFoto();
		session.setAttribute("base64UtilsV2", base64Utils);
		// Agregar los bytes de la foto al modelo
		session.setAttribute("fotoBytesPrf", fotoBytes);
		if(!credencialesCorrectas) {
			redirectAttributes.addFlashAttribute("mensaje",new Mensaje("error","El usuario o contraseña son incorrectos"));
			System.out.println("___XXX__XXX_X_X_asdasd");
			return "redirect:/login";
		}
		status.setComplete();
		return "redirect:/dogtorhouse/citas";
		
		
	}
	

}
