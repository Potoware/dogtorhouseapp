package com.dogtorhouse.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dogtorhouse.app.entity.Cliente;
import com.dogtorhouse.app.entity.Veterinario;

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Long> {
	@Query(value = "SELECT v FROM Cliente v WHERE v.email = :email")
	Optional<Cliente> findByEmail(@Param("email") String email);

}