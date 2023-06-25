package com.curso.ecommerce.service;

import java.util.List;
import java.util.Optional;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Usuario;

public interface IOrdenService {

	//lista de orden para obtener todas las ordenes
	List <Orden> findAll();
	
	Optional<Orden> findById(Integer id);
	//metodo que retorna una orden
	Orden save (Orden orden);
	String generarNumeroOrden();
	List <Orden> findByUsuario(Usuario usuario);
	

}
