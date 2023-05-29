package com.dogtorhouse.app.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dogtorhouse.app.entity.HistoriaClinica;
import com.dogtorhouse.app.entity.Paciente;
import com.dogtorhouse.app.repository.HistoriaClinicaRepository;
import com.dogtorhouse.app.service.IHistoriaClinicaService;
import com.dogtorhouse.app.util.criteria.CriterioHC;

@Service
public class HistoriaClinicaService implements IHistoriaClinicaService {
	@Autowired
	private HistoriaClinicaRepository historiaClinicaRepository;

	@Override
	public List<HistoriaClinica> findAll() {
		// TODO Auto-generated method stub
		return ((List<HistoriaClinica>) historiaClinicaRepository.findAll()).stream()
				.filter(hc -> Objects.isNull(hc.getFechaBaja())).collect(Collectors.toList());
	}

	@Override
	public Optional<HistoriaClinica> findById(Long id) {
		// TODO Auto-generated method stub
		return historiaClinicaRepository.findById(id).filter(hc -> Objects.isNull(hc.getFechaBaja()));
	}

	@Override
	public void deleteById(Long id) {

		Optional<HistoriaClinica> optionalHC = historiaClinicaRepository.findById(id);
		if (!optionalHC.isPresent()) {
			return;
		}

		optionalHC.get().setFechaBaja(new Date());
		historiaClinicaRepository.save(optionalHC.get());

	}

	@Override
	public List<HistoriaClinica> findByPaciente(Paciente paciente) {
		// TODO Auto-generated method stub
		return (List<HistoriaClinica>) historiaClinicaRepository.findByPaciente(paciente);

	}

	@Override
	public List<HistoriaClinica> findByCriterioHC(CriterioHC criterio) {
		// TODO Auto-generated method stub
		return ((List<HistoriaClinica>) historiaClinicaRepository.findAll()).stream()
				.filter(hc -> Objects.isNull(hc.getFechaBaja()))
				.filter(hc -> criterio.getPaciente() == null
						|| hc.getPaciente().getId().equals(criterio.getPaciente().getId()))
				.filter(hc -> criterio.getCliente() == null
						|| hc.getPaciente().getCliente().getId().equals(criterio.getCliente().getId()))
				.collect(Collectors.toList());
	}

}