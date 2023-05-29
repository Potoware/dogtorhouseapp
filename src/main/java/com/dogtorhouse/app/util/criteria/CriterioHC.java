package com.dogtorhouse.app.util.criteria;

import com.dogtorhouse.app.entity.Cliente;
import com.dogtorhouse.app.entity.Paciente;

public class CriterioHC {

	private Long id;
	private Cliente cliente;
	private Paciente paciente;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

}
