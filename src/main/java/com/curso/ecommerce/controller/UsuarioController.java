package com.curso.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.service.IUsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	// objeto para acceder al crus
	@Autowired
	IUsuarioService usuarioService;

	// metodo que permita mostrar la pagina del registro
	@GetMapping("/registro")
	public String create() {
		return "usuario/registro";
	}
}
