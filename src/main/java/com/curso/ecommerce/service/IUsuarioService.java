package com.curso.ecommerce.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.curso.ecommerce.model.Usuario;

@Service
public interface IUsuarioService {
	// metodo que permite obtener un usuario de la BD
	// optional valida si el usuario existe
	Optional<Usuario> findById(Integer id);

	Usuario save(Usuario usuario);
	Optional<Usuario> findByEmail (String email);
}
