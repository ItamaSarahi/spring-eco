package com.curso.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

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

}
