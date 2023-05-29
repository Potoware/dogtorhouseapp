package com.dogtorhouse.app.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dogtorhouse.app.entity.Cliente;
import com.dogtorhouse.app.entity.Paciente;

@Repository
public interface PacienteRepository extends CrudRepository<Paciente, Long> {
	List<Paciente> findByCliente(Cliente cliente);
}