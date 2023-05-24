package com.dogtorhouse.app.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dogtorhouse.app.entity.Cliente;
import com.dogtorhouse.app.entity.Paciente;
import com.dogtorhouse.app.repository.PacienteRepository;
import com.dogtorhouse.app.service.IPacienteService;
import com.dogtorhouse.app.util.Utilidades;
import com.dogtorhouse.app.util.criteria.CriterioCliente;
import com.dogtorhouse.app.util.criteria.CriterioPaciente;

@Service
public class PacienteService implements IPacienteService{
	@Autowired
  private  PacienteRepository pacienteRepository;

	@Override
	public List<Paciente> findAll() {
		// TODO Auto-generated method stub
		return ((List<Paciente>) pacienteRepository.findAll()).stream()
				.filter(paciente->Objects.isNull(paciente.getFechaBaja()))
				.collect(Collectors.toList());
	}

	@Override
	public Paciente save(Paciente paciente) {
		// TODO Auto-generated method stub
		return pacienteRepository.save(paciente);
	}

	@Override
	public Optional<Paciente> findById(Long id) {
		// TODO Auto-generated method stub
		return pacienteRepository.findById(id).filter(paciente->Objects.isNull(paciente.getFechaBaja()));
	}

	@Override
	public void deleteById(Long id) {
		Optional<Paciente> paciente = pacienteRepository.findById(id);
		paciente.get().setFechaBaja(new Date());
		pacienteRepository.save(paciente.get());
		
	}
	
	@Override
	public List<Paciente> findAllCriterioPaciente(CriterioPaciente criterio) {
		System.out.println(criterio.getNombre());
		return ((List<Paciente>) pacienteRepository.findAll()).stream()
			.filter(paciente -> criterio.getId() == null || paciente.getId().equals(criterio.getId()))
			.filter(paciente -> Utilidades.isBlankOrNull(criterio.getEspecie())|| paciente.getEspecie().toLowerCase().contains(criterio.getEspecie().toLowerCase()))
			.filter(paciente -> Utilidades.isBlankOrNull(criterio.getNombre())|| paciente.getNombre().toLowerCase().contains(criterio.getNombre().toLowerCase()))
			.filter(paciente -> Utilidades.isBlankOrNull(criterio.getRaza()) || paciente.getRaza().toLowerCase().contains(criterio.getRaza().toLowerCase()))
			.filter(paciente -> criterio.getCliente() == null || paciente.getCliente().getId().equals(criterio.getCliente()))
			.filter(paciente ->Objects.isNull(paciente.getFechaBaja()))
			.collect(Collectors.toList());
	
	}
 
	  
	  
	}
