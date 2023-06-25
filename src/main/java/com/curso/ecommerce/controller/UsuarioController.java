package com.curso.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IOrdenService;
import com.curso.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

import org.slf4j.*;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	// objeto para acceder al crus
	@Autowired
	private IUsuarioService usuarioService;

	// variable de tipo log
	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);

	@Autowired
	private IOrdenService ordenService;

	// metodo que permita mostrar la pagina del registro
	@GetMapping("/registro")
	public String create() {
		return "usuario/registro";
	}

	// guardar el usuario en la base de datoa
	@PostMapping("/save")

	public String save(Usuario usuario) {
		// hacer una impresion de los datos del usuario
		LOGGER.info("Usuario registro: {}", usuario);

		usuario.setTipo("USER");
		usuarioService.save(usuario);
		return "redirect:/";
	}

	// obtener la vista del usuario
	@GetMapping("/login")
	public String login() {
		return "usuario/login";
	}

	// metodo para acceder
	@PostMapping("/acceder")
	public String acceder(Usuario usuario, HttpSession session) {

		// obtener usuario que tenga dicho email
		Optional<Usuario> user = usuarioService.findByEmail(usuario.getEmail());

		if (user.isPresent()) {
			session.setAttribute("idusuario", user.get().getId());

			// si el usuario es administrador
			if (user.get().getTipo().equals("Administrador")) {
				return "redirect:/administrador";
			} else {
				return "redirect:/";
			}
		} else {
			LOGGER.info("Usuario no existe");
		}

		return "redirect:/";
	}

	// metodo para obtener compras
	@GetMapping("/compras")
	public String obtenerCompras(Model model, HttpSession session) {
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		List<Orden> ordenes = ordenService.findByUsuario(usuario);
		model.addAttribute("ordenes", ordenes);
		return "usuario/compras";
	}

	// metodo para obtener el detalle de la compra mediante el id
	@GetMapping("/detalle/{id}")
	// se le pasa como argumento una path variable para
	public String detalleCompra(@PathVariable Integer id, HttpSession session, Model model) {
		// se pasa la sesion. para validar si es que la sesion existe
		LOGGER.info("Id de la orden : {}",id);
		
		Optional<Orden> orden= ordenService.findById(id);
		
		model.addAttribute("detalles",orden.get().getDetalle());
		model.addAttribute("sesion", session.getAttribute("idusuario"));

		return "usuario/detallecompra";
	}
	
	//metodo para poder cerrar la sesion del usuario, recibe la sesion 
	@GetMapping("/cerrar")
	public String cerrarSesion(HttpSession session) {
		session.removeAttribute("idusuario");
		return "redirect:/";
	}
}
