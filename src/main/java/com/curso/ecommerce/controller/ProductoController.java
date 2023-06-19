package com.curso.ecommerce.controller;

import java.util.Optional;

import org.slf4j.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/productos")
public class ProductoController {

	// variable de prueba (?
	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);

	// objeto de tipo producto
	@Autowired
	private ProductoService productoService;

	@GetMapping("")
	// lleva la vista de producto hacia la vista show
	public String show(Model model) {
		// Esto envia a la variable productos y posterior lo recibimos a la vista
		model.addAttribute("productos", productoService.findAll());
		return "productos/show";
	}

	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}

	@PostMapping("/save")
	public String save(Producto producto) {
		// Recibira un objeto de tipo producto, es un logger
		LOGGER.info("Este es el objeto producto{}", producto);
		Usuario u = new Usuario(1, "", "", "", "", "", "", "");
		producto.setUsuario(u);
		productoService.save(producto);

		// peticion directamente a productos. Lo que cargara es la vista show
		return "redirect:/productos";
	}

	// metodo para editar el producto
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {

		// Objeto de tipo producto, se va a almacenar el objeto buscadp
		Producto producto = new Producto();

		Optional<Producto> optionalProducto = productoService.get(id);
		// Trae de vuelta el producto buscado
		producto = optionalProducto.get();

		// Esto es una variable de logeo
		LOGGER.info("Producto buscado: {}", producto);

		// model lleva datos desde el backend hasta la vista
		model.addAttribute("producto", producto);
		return "productos/edit";
	}

	// metodo para actulizar, recibe como parametro un objeto de tipo producto
	@PostMapping("/update")
	public String update(Producto producto) {
		productoService.Update(producto);
		return "redirect:/productos";

	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {
		productoService.Delete(id);
		return "redirect:/productos";
	}
	

}
