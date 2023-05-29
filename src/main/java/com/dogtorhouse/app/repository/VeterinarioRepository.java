
package com.dogtorhouse.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dogtorhouse.app.entity.Veterinario;

@Repository
public interface VeterinarioRepository extends CrudRepository<Veterinario, Long> {

	@Query(value = "SELECT v FROM Veterinario v WHERE v.email = :email")
	Optional<Veterinario> findByEmail(@Param("email") String email);

}