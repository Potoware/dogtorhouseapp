package com.dogtorhouse.app.security.service.impl;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.dogtorhouse.app.security.service.ICifradoService;

@Service
public class CifradoService implements ICifradoService {

	@Override
	public String cifrarContrasenia(String contrasenia) {
		return BCrypt.hashpw(contrasenia, BCrypt.gensalt(10));
	}

	@Override
	public boolean verificarContrasenia(String contrasenia, String hash) {
		return BCrypt.checkpw(contrasenia, hash);
	}

}