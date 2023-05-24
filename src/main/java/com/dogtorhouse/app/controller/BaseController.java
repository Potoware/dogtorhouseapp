package com.dogtorhouse.app.controller;

import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.dogtorhouse.app.entity.Veterinario;
import com.dogtorhouse.app.util.Mensaje;

public class BaseController {

	private final String TIPO_MENSAJE_ERROR = "error";
	private final String TIPO_MENSAJE_CORRECTO = "correcto";
	private final String TIPO_MENSAJE_INFORMATIVO ="informativo";
	
	public boolean usuarioLogueado(Veterinario veterinario){
		return (veterinario != null 
				&& veterinario.getId()!= null
				&& veterinario.getId()>0);
	}
	
	protected void mostrarMensajeCorrecto(String texto, RedirectAttributes redirect) {
		redirect.addFlashAttribute("mensaje", new Mensaje(TIPO_MENSAJE_CORRECTO,texto));
	}
	
	protected void mostrarMensajeError(String texto, RedirectAttributes redirect,Model model) {
		if(redirect!=null)
			redirect.addFlashAttribute("mensaje", new Mensaje(TIPO_MENSAJE_ERROR,texto));
		else
			model.addAttribute("mensaje", new Mensaje(TIPO_MENSAJE_ERROR,texto));
	}
	
	protected void mostrarMensajeInformativo(String texto, Model model) {
		model.addAttribute("mensaje", new Mensaje(TIPO_MENSAJE_INFORMATIVO,texto));
	}
	
	protected void mostrarMensajeInformativo(String texto, RedirectAttributes redirect) {
		redirect.addFlashAttribute("mensaje", new Mensaje(TIPO_MENSAJE_INFORMATIVO,texto));
	}
	
	protected void mostrarMensajeInformativo(String texto, Map<String,Object> model) {
		model.put("mensaje", new Mensaje(TIPO_MENSAJE_INFORMATIVO,texto));
	}
	
	@SuppressWarnings("unchecked")
	protected void init(Object model,HttpSession session) {
		
		if (model instanceof Map<?,?>) {
			
			if(((Map<String, Object>) model).get("mensaje")!=null) {
				System.out.println((Mensaje) ((Map<String, Object>) model).get("mensaje"));
			}else {
				((Map<String, Object>) model).put("mensaje", new Mensaje());
			}
		}else if(model instanceof Model) {
			if(((Model) model).getAttribute("mensaje")!=null) {
				System.out.println((Mensaje) ((Model) model).getAttribute("mensaje"));
			}else {
				((Model) model).addAttribute("mensaje", new Mensaje());
			}
		}
		
	}
	

}
