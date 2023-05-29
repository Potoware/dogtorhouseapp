package com.dogtorhouse.app.service;

import java.util.List;
import java.util.Optional;

import com.dogtorhouse.app.entity.Rol;

public interface IRolService {
	public List<Rol> findAll();

	public Rol save(Rol rol);

	public Optional<Rol> findById(Long id);

	public void deleteById(Long id);
}
