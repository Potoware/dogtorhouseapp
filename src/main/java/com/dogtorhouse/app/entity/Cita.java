package com.dogtorhouse.app.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
public class Cita{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message="Es necesario seleccionar el paciente")
	@ManyToOne
	private Paciente paciente;
	
	@NotNull(message="Es necesario seleccionar el veterinario")
	@ManyToOne
	private Veterinario veterinario;
	
	@NotNull(message="Es necesario ingresar la fecha y hora de la cita")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm")
	private LocalDateTime fechaHora;
	
	@NotEmpty(message="Debe ingresar el tipo de cita")
	private String tipo;
	
	private Date fechaBaja;
	private String motivoBaja;
	private String comentarioCancelar;
	private String estado;
	
	@Column(nullable = true)
    private String diagnostico;

    @Column(nullable = true)
    private String tratamiento;
    
    @Column(nullable = true)
    @ElementCollection
    private List<String> medicacion;
    
    @Column(nullable = true)
    private Date fechaAtencion;
	
	public Date getFechaBaja() {
		return fechaBaja;
	}
	
	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}
	
	public String getMotivoBaja() {
		return motivoBaja;
	}
	
	public void setMotivoBaja(String motivoBaja) {
		this.motivoBaja = motivoBaja;
	}


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Paciente getPaciente() {
		return paciente;
	}
	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}
	public Veterinario getVeterinario() {
		return veterinario;
	}
	public void setVeterinario(Veterinario veterinario) {
		this.veterinario = veterinario;
	}
	public LocalDateTime getFechaHora() {
		return fechaHora;
	}
	public void setFechaHora(LocalDateTime fechaHora) {
		this.fechaHora = fechaHora;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getComentarioCancelar() {
		return comentarioCancelar;
	}

	public void setComentarioCancelar(String comentarioCancelar) {
		this.comentarioCancelar = comentarioCancelar;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	public String getTratamiento() {
		return tratamiento;
	}

	public void setTratamiento(String tratamiento) {
		this.tratamiento = tratamiento;
	}

	public List<String> getMedicacion() {
		return medicacion;
	}

	public void setMedicacion(List<String> medicacion) {
		this.medicacion = medicacion;
	}

	public Date getFechaAtencion() {
		return fechaAtencion;
	}

	public void setFechaAtencion(Date fechaAtencion) {
		this.fechaAtencion = fechaAtencion;
	}
	
	
	
	

}