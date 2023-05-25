package com.dogtorhouse.app.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dogtorhouse.app.entity.Cliente;
import com.dogtorhouse.app.entity.Veterinario;
import com.dogtorhouse.app.repository.VeterinarioRepository;
import com.dogtorhouse.app.service.IVeterinarioService;
import com.dogtorhouse.app.util.Utilidades;
import com.dogtorhouse.app.util.criteria.CriterioCliente;
import com.dogtorhouse.app.util.criteria.CriterioVeterinario;

@Service
public class VeterinarioService implements IVeterinarioService{
	@Autowired
  private  VeterinarioRepository veterinarioRepository;

@Override
public List<Veterinario> findAll() {
	// TODO Auto-generated method stub
	return ((List<Veterinario>) veterinarioRepository.findAll()).stream()
			.filter(veterinario->Objects.isNull(veterinario.getFechaBaja()))
			.collect(Collectors.toList());
}

@Override
public Veterinario save(Veterinario veterinario) {
	// TODO Auto-generated method stub
	return veterinarioRepository.save(veterinario);
}

@Override
public Optional<Veterinario> findById(Long id) {
	// TODO Auto-generated method stub
	return veterinarioRepository.findById(id).filter(veterinario->Objects.isNull(veterinario.getFechaBaja()));
}

@Override
public void deleteById(Long id) {
	//Si no es por error se hace una baja logica
	Optional<Veterinario> veterinario = veterinarioRepository.findById(id);
	if(!veterinario.isPresent()) {
		//throws mas adelante
		return;
	}
	
	veterinario.get().setFechaBaja(new Date());
	veterinarioRepository.save(veterinario.get());
	
}

@Override
public List<Veterinario> findAllCriterioVeterinario(CriterioVeterinario criterio) {
	
	return ((List<Veterinario>) veterinarioRepository.findAll()).stream()
		.filter(veterinario -> Utilidades.isBlankOrNull(criterio.getIdentificacion()) || veterinario.getIdentificacion().contains(criterio.getIdentificacion()))
		//.filter(veterinario ->criterio.getActivo() || veterinario.getActivo().equals(criterio.getActivo()))
		.filter(veterinario -> Utilidades.isBlankOrNull(criterio.getNombres()) || veterinario.getNombres().toLowerCase().contains(criterio.getNombres().toLowerCase()))
		.filter(veterinario -> Utilidades.isBlankOrNull(criterio.getApellidos())|| veterinario.getApellidos().toLowerCase().contains(criterio.getApellidos().toLowerCase()))
		.filter(veterinario -> Utilidades.isBlankOrNull(criterio.getEmail()) || veterinario.getEmail().toLowerCase().contains(criterio.getEmail().toLowerCase()))
		.filter(veterinario ->Objects.isNull(veterinario.getFechaBaja()))
		.collect(Collectors.toList());

}

@Override
public Boolean validoEmail(String email) {
	return veterinarioRepository.findByEmail(email).isEmpty();
	
	
}

@Override
public Optional<Veterinario> findByEmail(String email) {
	// TODO Auto-generated method stub
	return veterinarioRepository.findByEmail(email);
}
 
}