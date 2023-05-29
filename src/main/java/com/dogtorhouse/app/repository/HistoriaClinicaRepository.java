package com.dogtorhouse.app.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dogtorhouse.app.entity.HistoriaClinica;
import com.dogtorhouse.app.entity.Paciente;

@Repository
public interface HistoriaClinicaRepository extends CrudRepository<HistoriaClinica, Long> {
	List<HistoriaClinica> findByPaciente(Paciente paciente);
}