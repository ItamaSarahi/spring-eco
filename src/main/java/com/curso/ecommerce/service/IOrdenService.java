package com.curso.ecommerce.service;

import java.util.List;

import com.curso.ecommerce.model.Orden;

public interface IOrdenService {

	//lista de orden para obtener todas las ordenes
	List <Orden> findAll();
	
	//metodo que retorna una orden
	Orden save (Orden orden);
	String generarNumeroOrden();
}
