package com.dogtorhouse.app.service;

import java.util.List;
import java.util.Optional;

import com.dogtorhouse.app.entity.Paciente;
import com.dogtorhouse.app.util.criteria.CriterioPaciente;

public interface IPacienteService {

	public List<Paciente> findAll();

	public Paciente save(Paciente paciente);

	public Optional<Paciente> findById(Long id);

	public void deleteById(Long id);

	public List<Paciente> findAllCriterioPaciente(CriterioPaciente criterio);
}
