package com.curso.ecommerce.controller;

import java.util.List;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IUsuarioService;
import com.curso.ecommerce.service.ProductoService;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {
	@Autowired
	private ProductoService productoservice;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IOrdenService ordenService;

	// private final Logger log = LoggerFactory.getLogger(ProductoController.class);

	// funcionalidad para mostrar el producto en home

	@GetMapping("")
	public String home(Model model) {
		List<Producto> productos = productoservice.findAll();
		model.addAttribute("productos", productos);
		return "administrador/home";
	}

	// obtener todos los usuarios
	@GetMapping("/usuarios")
	public String usuarios(Model model) {
		model.addAttribute("usuarios", usuarioService.findAll());
		return "administrador/usuarios";
	}

	@GetMapping("/ordenes")
	public String ordenes(Model model) {
		model.addAttribute("ordenes", ordenService.findAll());
		return "administrador/ordenes";
	}

	// metodo para mapear en detalles de compra
	@GetMapping("/detalle/{id}")
	public String detalle(Model model, @PathVariable Integer id) {
		Orden orden = ordenService.findById(id).get();

		model.addAttribute("detalles", orden.getDetalle());
		return "administrador/detalleorden";
	}

}
