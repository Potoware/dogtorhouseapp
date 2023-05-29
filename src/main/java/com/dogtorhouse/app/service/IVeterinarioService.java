package com.dogtorhouse.app.service;

import java.util.List;
import java.util.Optional;

import com.dogtorhouse.app.entity.Veterinario;
import com.dogtorhouse.app.util.criteria.CriterioVeterinario;

public interface IVeterinarioService {

	public List<Veterinario> findAll();

	public Veterinario save(Veterinario veterinario);

	public Optional<Veterinario> findById(Long id);

	public void deleteById(Long id);

	List<Veterinario> findAllCriterioVeterinario(CriterioVeterinario criterio);

	public Optional<Veterinario> findByEmail(String email);

	Boolean validoEmail(String email);
}
