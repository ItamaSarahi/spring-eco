package com.curso.ecommerce.controller;

import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.repository.ProductoRepository;
import com.curso.ecommerce.service.ProductoService;

import org.slf4j.Logger;

@Controller
@RequestMapping("/")

public class HomeController {
	@Autowired
	private ProductoService productoservice;
	
	//variable de tipo logger
	private final Logger log=LoggerFactory.getLogger(HomeController.class);
	
	@GetMapping("")
	public String home(Model model) {
		model.addAttribute("productos", productoservice.findAll());	
		return "/usuario/home";
	}
	
	
	//metodo del boton del producto
	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id) {
		log.info("Id producto enviado como parametro{}",id);
		return "usuario/productohome";
	}
	
	
}
