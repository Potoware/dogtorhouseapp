package com.dogtorhouse.app.service;

import java.util.List;
import java.util.Optional;

import com.dogtorhouse.app.entity.Cliente;
import com.dogtorhouse.app.util.criteria.CriterioCliente;

public interface IClienteService {

	public List<Cliente> findAll();

	public Cliente save(Cliente cliente);

	public Optional<Cliente> findById(Long id);

	public void deleteById(Long id);

	List<Cliente> findAllCriterioCliente(CriterioCliente criterio);

	Boolean validoEmail(String email);
}
