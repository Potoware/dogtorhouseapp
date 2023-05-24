package com.dogtorhouse.app.service;

import java.util.List;
import java.util.Optional;

import com.dogtorhouse.app.entity.Cita;
import com.dogtorhouse.app.entity.Paciente;
import com.dogtorhouse.app.util.criteria.CriterioCita;

public interface ICitaService {

	public List<Cita> findAll();
	
	public Cita save(Cita cita);
	
	public Optional<Cita> findById(Long id);

	
	public List<Cita> findByPaciente(Paciente paciente);


	List<Cita> findAllCriterioCita(CriterioCita criterio);

	void deleteById(Long id);
}
