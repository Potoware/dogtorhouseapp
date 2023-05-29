package com.dogtorhouse.app.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dogtorhouse.app.entity.Cliente;
import com.dogtorhouse.app.repository.ClienteRepository;
import com.dogtorhouse.app.service.IClienteService;
import com.dogtorhouse.app.util.Utilidades;
import com.dogtorhouse.app.util.criteria.CriterioCliente;

@Service
public class ClienteService implements IClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Override
	public List<Cliente> findAll() {
		return ((List<Cliente>) clienteRepository.findAll()).stream()
				.filter(cliente -> Objects.isNull(cliente.getFechaBaja())).collect(Collectors.toList());
	}

	@Override
	public Cliente save(Cliente cliente) {
		// TODO Auto-generated method stub
		return clienteRepository.save(cliente);
	}

	@Override
	public Optional<Cliente> findById(Long id) {
		// TODO Auto-generated method stub
		return clienteRepository.findById(id).filter(cliente -> Objects.isNull(cliente.getFechaBaja()));
	}

	@Override
	public void deleteById(Long id) {
		// Si no es por error se hace una baja logica
		Optional<Cliente> cliente = clienteRepository.findById(id);

		cliente.get().setFechaBaja(new Date());
		clienteRepository.save(cliente.get());

	}

	@Override
	public List<Cliente> findAllCriterioCliente(CriterioCliente criterio) {
		return ((List<Cliente>) clienteRepository.findAll()).stream()
				.filter(cliente -> Utilidades.isBlankOrNull(criterio.getIdentificacion())
						|| cliente.getIdentificacion().contains(criterio.getIdentificacion()))
				.filter(cliente -> Utilidades.isBlankOrNull(criterio.getNombres())
						|| cliente.getNombres().toLowerCase().contains(criterio.getNombres().toLowerCase()))
				.filter(cliente -> Utilidades.isBlankOrNull(criterio.getApellidos())
						|| cliente.getApellidos().toLowerCase().contains(criterio.getApellidos().toLowerCase()))
				.filter(cliente -> Utilidades.isBlankOrNull(criterio.getEmail())
						|| cliente.getEmail().toLowerCase().contains(criterio.getEmail().toLowerCase()))
				.filter(cliente -> Objects.isNull(cliente.getFechaBaja())).collect(Collectors.toList());
	}

	@Override
	public Boolean validoEmail(String email) {
		return clienteRepository.findByEmail(email).isEmpty();

	}

}