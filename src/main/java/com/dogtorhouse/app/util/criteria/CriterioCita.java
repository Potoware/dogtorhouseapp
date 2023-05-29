package com.dogtorhouse.app.util.criteria;

public class CriterioCita {

	private Long id;
	private Long paciente;
	private Long cliente;
	private Long veterinario;
	private String fechaInicio;
	private String fechaFin;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPaciente() {
		return paciente;
	}

	public void setPaciente(Long paciente) {
		this.paciente = paciente;
	}

	public Long getCliente() {
		return cliente;
	}

	public void setCliente(Long cliente) {
		this.cliente = cliente;
	}

	public Long getVeterinario() {
		return veterinario;
	}

	public void setVeterinario(Long veterinario) {
		this.veterinario = veterinario;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

}
