package com.curso.ecommerce.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

//clase de servicio para subir y eliminar una imagen una imsgen 
@Service
public class UploadFileService {
	//ubicacion del proyecto en donde se van a cargar las imagenes
	//carpeta image dentro del proyecto
	private String folder="images//";
	
	
	//metodo para guardar la imagen
	public String saveImage(MultipartFile file) throws IOException{
		//si la imagen no esta vacia
		if(!file.isEmpty()){
			//convertir a bytes para que pueda enviarse de un lugar a otro
			byte[ ]bytes=file.getBytes();
			//ubicacion donde queremos que se fguarde la imagen
		     Path path=Paths.get(folder+file.getOriginalFilename());
		     //ruta y byte para poder enviarla hacia el servidor
		     Files.write(path, bytes);
		     //se retorna el nombre de la imagen
		     return file.getOriginalFilename();
		}
		
		//Si la imagen viene vacia, se va a guardar una imagen por defecto
		return "default.jpg";
	}
	
	
	//metodo que eliminara la imagen cuando ya este en el proyecto
	public void deleteImage(String nombre) {
		String ruta="images//";
		File file=new File(ruta+nombre);
		file.delete();
		
	}
}
