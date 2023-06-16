package com.curso.ecommerce.service;

import java.util.Optional;

import com.curso.ecommerce.model.Producto;

public interface ProductoService {
	 public Producto save(Producto producto);
	 //Busca si un producto existe
	 public Optional<Producto> get (Integer id);
	 
	 //Permite actualizar
	 public void Update(Producto producto);
	 
	 //pemite eliminar
	 public void Delete(Integer id);
	 
	 
}
