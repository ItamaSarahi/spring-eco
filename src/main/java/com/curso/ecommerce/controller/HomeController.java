package com.curso.ecommerce.controller;

import org.slf4j.LoggerFactory;
import java.util.*;

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
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IDetalleOrdenService;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IUsuarioService;
import com.curso.ecommerce.service.ProductoService;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;

@Controller
@RequestMapping("/")

public class HomeController {
	@Autowired
	private ProductoService productoservice;

	// variable para acceder al crud de la orden
	@Autowired
	private IOrdenService ordenService;

	// variable para acceder al crud del dettale de la orden
	@Autowired
	private IDetalleOrdenService detalleOrdenService;

	// objeto de tipo usuario
	@Autowired
	private IUsuarioService usuarioService;

	// para almacenar los detalles de la orden
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();

	// datos de la orden
	Orden orden = new Orden();


	@GetMapping("")
	public String home(Model model, HttpSession session) {
		model.addAttribute("productos", productoservice.findAll());

		// session
		model.addAttribute("sesion", session.getAttribute("idusuario"));

		return "usuario/home";
	}

	// metodo del boton del producto
	@GetMapping("productohome/{id}")
	public String productoHome(@PathVariable Integer id, Model model) {
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
		producto = optionalProducto.get();

		detalleOrden.setCantidad(cantidad);
		detalleOrden.setPrecio(producto.getPrecio());
		detalleOrden.setNombre(producto.getNombre());
		detalleOrden.setTotal(producto.getPrecio() * cantidad);
		detalleOrden.setProducto(producto);

		// validar que el producto no se añada 2 veces
		Integer idProducto = producto.getId();
		boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId() == idProducto);

		if (!ingresado) {
			// se añade cada producto hacia la lista
			detalles.add(detalleOrden);
		}

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
			if (detalleOrden.getProducto().getId() != id) {
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

	// metodo para llevar el carrito a cualquier lado

	@GetMapping("/getCart")
	public String getCart(Model model, HttpSession session) {
		// detalles y orden son globales a todos los metodos del controlador
		model.addAttribute("cart", detalles);
		model.addAttribute("Orden", orden);

		// sesion
		model.addAttribute("sesion", session.getAttribute("idusuario"));

		return "/usuario/carrito";
	}

	// resumen de la orden: para ver los datos direcion,nombre, correo, etc
	@GetMapping("/order")
	public String order(Model model, HttpSession session) {

		// obtener el id del usuario por la sesion:
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();

		// detalles y orden son globales a todos los metodos del controlador
		model.addAttribute("cart", detalles);
		model.addAttribute("Orden", orden);
		model.addAttribute("usuario", usuario);
		return "usuario/resumenorden";
	}

	// metodo que responde a una peticion de tipo getmapping
	@GetMapping("/saveOrder")
	public String saveOrder(HttpSession session) {
		// obtener la fecha actual
		Date fechaCreacion = new Date();
		orden.setFechaCreacion(fechaCreacion);

		// dar el numero de orden
		orden.setNumero(ordenService.generarNumeroOrden());

		// el usuario que hara referencia a esa orden, ya que el usuario debe de haberse
		// logeado.
		// obtener el id del usuario por la sesion:
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();

		orden.setUsuario(usuario);

		// ya con los datos, guardamos la orden
		ordenService.save(orden);

		// Tambien guardamos los detalles
		// Objeto de tipo detalle Orden que lee la lista de detalles
		for (DetalleOrden dt : detalles) {
			dt.setOrden(orden);
			detalleOrdenService.save(dt);
		}

		// limpiar los valores que tiene el detalle y la orden.
		orden = new Orden();
		detalles.clear();

		// hacemos redirect hacia la home
		return "redirect:/";

	}

	// metodo para la busqueda de los produtos
	@PostMapping("/search")
	public String searchProducto(@RequestParam String nombre, Model model) {
// filtra por un predicado: el predicado es lo que queremos obtener.
		// Obtiene los productos hace un string. A traves de una funcion anonima trae el
		// nombre del producto y el metodo contains le pasamos la secuencia de
		// caracteres, para que si el producto contiene alguna de esas partes nos
		// devuelva una lista
		List<Producto> productos = productoservice.findAll().stream().filter(p -> p.getNombre().contains(nombre))
				.collect(Collectors.toList());

		// parametros: nombre de la lista con la qe va a recibir
		model.addAttribute("productos", productos);

		return "usuario/home";
	}

}
