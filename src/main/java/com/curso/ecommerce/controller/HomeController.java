package com.curso.ecommerce.controller;

import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.curso.ecommerce.model.DetalleOrden;
import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.repository.ProductoRepository;
import com.curso.ecommerce.service.ProductoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;

@Controller
@RequestMapping("/")

public class HomeController {
	@Autowired
	private ProductoService productoservice;

	// para almacenar los detalles de la orden
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();

	// datos de la orden
	Orden orden = new Orden();

	// variable de tipo logger
	private final Logger log = LoggerFactory.getLogger(HomeController.class);

	@GetMapping("")
	public String home(Model model) {
		model.addAttribute("productos", productoservice.findAll());
		return "usuario/home";
	}

	// metodo del boton del producto
	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
		log.info("Id producto enviado como parametro{}", id);
		Producto producto = new Producto();

		// Se buscara el producto en la base de datos a traves del producto service
		Optional<Producto> productoOptional = productoservice.get(id);
		producto = productoOptional.get();

		// lo enviamos hacia la vista
		model.addAttribute("producto", producto);

		return "usuario/productohome";
	}

	// metodo para el carrito
	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model) {
		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();

		// se pondra en 0 cada vez que entre a este metodo
		double sumaTotal = 0;

		Optional<Producto> optionalProducto = productoservice.get(id);

		log.info("El producto añadido: {}", optionalProducto.get());
		log.info("Cantidad: {}", cantidad);
		producto = optionalProducto.get();

		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(producto.getPrecio() * cantidad);
		detalleOrden.setProducto(producto);

		// se añade cada producto hacia la lista
		detalles.add(detalleOrden);
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("Orden", orden);

		return "usuario/carrito";
	}

	@GetMapping("/delete/cart/{id}")
	// metodo para quitar un producto del carrito
	public String deleteProductoCart(@PathVariable Integer id, Model model) {
		// lista nueva de productos
		List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>();

		// se quita el producto de la lista de detaller
		for (DetalleOrden detalleOrden : detalles) {
			// si encuentra un id si esta en detaller no lo añadira
			if (detalleOrden.getProducto().getId() != id ) {
				ordenesNueva.add(detalleOrden);
			}
		}

		
		// poner la nueva lista con los productos restantes
		detalles = ordenesNueva;

		double sumaTotal = 0;
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("Orden", orden);

		return "usuario/carrito";
	}

}
