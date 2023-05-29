package com.dogtorhouse.app.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dogtorhouse.app.entity.Cita;
import com.dogtorhouse.app.entity.Paciente;

@Repository
public interface CitaRepository extends CrudRepository<Cita, Long> {
	List<Cita> findByPacienteOrderByFechaHoraAsc(Paciente paciente);

}