package com.curso.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.curso.ecommerce.model.Usuario;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {
	// aceder a la base de datos y obtener los datos de dicho usuario.
	// comprobar que el correo se enuentre en la BD

	Optional<Usuario> findByEmail(String email);

}
