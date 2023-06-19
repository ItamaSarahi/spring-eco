package com.curso.ecommerce;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//Clase de configuracion. Path relativo para apuntar desde cualquier lugar al recurso de imagenes
public class ResourceWebConfiguration implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	 registry.addResourceHandler("/images/**").addResourceLocations("file:images/");
	}

}
