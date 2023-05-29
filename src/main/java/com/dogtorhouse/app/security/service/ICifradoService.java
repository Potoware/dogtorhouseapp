package com.dogtorhouse.app.security.service;

public interface ICifradoService {

	String cifrarContrasenia(String contrasenia);

	boolean verificarContrasenia(String contrasenia, String hash);

}
