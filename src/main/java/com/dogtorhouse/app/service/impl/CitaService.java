package com.dogtorhouse.app.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dogtorhouse.app.entity.Cita;
import com.dogtorhouse.app.entity.Paciente;
import com.dogtorhouse.app.repository.CitaRepository;
import com.dogtorhouse.app.service.ICitaService;
import com.dogtorhouse.app.util.Constantes;
import com.dogtorhouse.app.util.Utilidades;
import com.dogtorhouse.app.util.criteria.CriterioCita;

@Service
public class CitaService implements ICitaService {
	@Autowired
	private  CitaRepository citaRepository;

	@Override
	public List<Cita> findAll() {
		return ((List<Cita>) citaRepository.findAll()).stream()
			.filter(cita -> Objects.isNull(cita.getFechaBaja()))
			.collect(Collectors.toList());
	}
	
	@Override
	public List<Cita> findAllCriterioCita(CriterioCita criterio) {

	    final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    final LocalDateTime fechaInicio;
	    final LocalDateTime fechaFin;
	    if (!Utilidades.isBlankOrNull(criterio.getFechaInicio()) && !Utilidades.isBlankOrNull(criterio.getFechaFin())) {
	        fechaInicio = LocalDate.parse(criterio.getFechaInicio(), dateFormatter).atStartOfDay();
	        fechaFin = LocalDate.parse(criterio.getFechaFin(), dateFormatter).atStartOfDay();
	    } else {
	        fechaInicio = null;
	        fechaFin = null;
	    }

	    return ((List<Cita>) citaRepository.findAll()).stream()
	            .filter(cita -> Objects.isNull(cita.getFechaBaja()))
	            .filter(cita -> criterio.getId() == null || cita.getId().equals(criterio.getId()))
	            .filter(cita -> criterio.getPaciente() == null || cita.getPaciente().getId().equals(criterio.getPaciente()))
	            .filter(cita -> criterio.getVeterinario() == null || cita.getVeterinario().getId().equals(criterio.getVeterinario()))
	            .filter(cita -> criterio.getCliente() == null || cita.getPaciente().getCliente().getId().equals(criterio.getCliente()))
	            .filter(cita -> fechaInicio == null || fechaFin == null || (cita.getFechaHora().compareTo(fechaInicio) >= 0 && cita.getFechaHora().compareTo(fechaFin) <= 0))
	            .collect(Collectors.toList());
	}


	@Override
	public Cita save(Cita cita) {
		return citaRepository.save(cita);
	}

	@Override
	public Optional<Cita> findById(Long id) {
		return citaRepository.findById(id).filter(cita->Objects.isNull(cita.getFechaBaja()));
	}

	@Override
	public void deleteById(Long id) {
		
		//Si no es por error se hace una baja logica
		Optional<Cita> cita = citaRepository.findById(id);
		if(!cita.isPresent()) {
			//throws mas adelante
			return;
		}
		
		cita.get().setFechaBaja(new Date());
		citaRepository.save(cita.get());
		
		
	}
	
	@Override
	public List<Cita> findByPaciente(Paciente paciente) {
		return (List<Cita>) citaRepository.findByPacienteOrderByFechaHoraAsc(paciente).stream()
				.filter(cita->Objects.isNull(cita.getFechaBaja()))
				.collect(Collectors.toList());
	}

}