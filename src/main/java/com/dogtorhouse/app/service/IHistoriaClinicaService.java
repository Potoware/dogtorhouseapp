package com.dogtorhouse.app.service;

import java.util.List;
import java.util.Optional;

import com.dogtorhouse.app.entity.HistoriaClinica;
import com.dogtorhouse.app.entity.Paciente;
import com.dogtorhouse.app.util.criteria.CriterioHC;

public interface IHistoriaClinicaService {

	public List<HistoriaClinica> findAll();

	public Optional<HistoriaClinica> findById(Long id);

	public void deleteById(Long id);

	public List<HistoriaClinica> findByPaciente(Paciente paciente);

	List<HistoriaClinica> findByCriterioHC(CriterioHC criterio);

}
