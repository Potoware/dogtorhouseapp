package com.dogtorhouse.app.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dogtorhouse.app.entity.Rol;
import com.dogtorhouse.app.repository.RolRepository;
import com.dogtorhouse.app.service.IRolService;

@Service
public class RolService implements IRolService{

	@Autowired
	private RolRepository rolRepository;
	@Override
	public List<Rol> findAll() {
		// TODO Auto-generated method stub
		return (List<Rol>) rolRepository.findAll();
	}

	@Override
	public Rol save(Rol rol) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Rol> findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}

}
