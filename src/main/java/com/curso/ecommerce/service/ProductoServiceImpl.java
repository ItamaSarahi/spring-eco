package com.curso.ecommerce.service;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.repository.ProductoRepository;

@Service
public class ProductoServiceImpl implements ProductoService{

	
	//Declarar un objeto de tipo Repositori
	@Autowired
	private ProductoRepository productoRepository;
	
	
	
	@Override
	public Producto save(Producto producto) {
		return productoRepository.save(producto);
	}

	@Override
	public Optional<Producto> get(Integer id) {
		return productoRepository.findById(id);
	}

	@Override
	public void Update(Producto producto) {
	   productoRepository.save(producto);
	}

	@Override
	public void Delete(Integer id) {
		productoRepository.deleteById(id);
		
	}

}
