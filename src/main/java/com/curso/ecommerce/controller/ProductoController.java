package com.curso.ecommerce.controller;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.curso.ecommerce.model.Producto;
import com.curso.ecommerce.model.Usuario;
import com.curso.ecommerce.service.ProductoService;
import com.curso.ecommerce.service.UploadFileService;

@Controller
@RequestMapping("/productos")
public class ProductoController {

	// variable de prueba (?
	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);

	// objeto de tipo producto
	@Autowired
	private ProductoService productoService;
	
	//variable para la imagen
	@Autowired
	private UploadFileService upload;

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
	public String save(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
		// Recibira un objeto de tipo producto, es un logger
		LOGGER.info("Este es el objeto producto{}", producto);
		Usuario u = new Usuario(1, "", "", "", "", "", "", "");
		producto.setUsuario(u);
		
		//logica para subir la imagen al servidor y guardarla
		
		//cuando la imagen sea cargada por primera vez. Entra a este 
		if(producto.getId()==null) {
			String nombreImagen=upload.saveImage(file);
			//aqui se guarda el nombre de la imagen del producto
			producto.setImagen(nombreImagen);
		}else {
	
		}
		
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
	public String update(Producto producto,@RequestParam("img") MultipartFile file) throws IOException {
		
		
		//cuando se modifica el producto, pero no se cambia la imagen
		if(file.isEmpty()) {
			Producto p= new Producto();
			p=productoService.get(producto.getId()).get();
			producto.setImagen(p.getImagen());
			
		//cuando tambien se cambia la imagen cuando se edita el producto
		}else {			
			//Eliminar desde el servidor la imagen
			Producto p = new Producto();
			p=productoService.get(producto.getId()).get();

			//Si la imagen no es la imagen por defecto se va a eliminar
			if(!p.getImagen().equals("default.jpg")){
				upload.deleteImage(p.getImagen());
			}
			
			String nombreImagen= upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}
		
		productoService.Update(producto);
		return "redirect:/productos";

	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {
		
		//Eliminar desde el servidor la imagen
		Producto p = new Producto();
		p=productoService.get(id).get();

		//Si la imagen no es la imagen por defecto se va a eliminar
		if(!p.getImagen().equals("default.jpg")){
			upload.deleteImage(p.getImagen());
		}
		
		
		productoService.Delete(id);
		return "redirect:/productos";
	}
	

}
