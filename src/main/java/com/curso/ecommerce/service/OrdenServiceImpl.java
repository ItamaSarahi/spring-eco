package com.curso.ecommerce.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.ecommerce.model.Orden;
import com.curso.ecommerce.repository.IOrdenRepository;

@Service
//clase de servicio
public class OrdenServiceImpl implements IOrdenService {

	@Autowired
	private IOrdenRepository ordenRepository;

	@Override
	public Orden save(Orden orden) {
		// aqui se guarda la orden que viene
		return ordenRepository.save(orden);
	}

	// con esto obtenemos todas las ordenes. Numero secuencial de las ordenes

	@Override
	public List<Orden> findAll() {
		// TODO Auto-generated method stub
		return ordenRepository.findAll();
	}

	// metodo para devolver el numero de la orden
	public String generarNumeroOrden() {
		int numero = 0;
		// este numero devolvera el string con el secuencial
		String numeroConcatenado = "";

		// ultimo numero ingresado de dicho
		List<Orden> ordenes = findAll();

		// incremento del string
		List<Integer> numeros = new ArrayList<Integer>();

		// funciones de java 8
		ordenes.stream().forEach(o -> numeros.add(Integer.parseInt(o.getNumero())));

		// si la orden viene vacia
		if (ordenes.isEmpty()) {
			numero = 1;
		} else {
			// mayor valor de la lista de numeros, ya que han habido ordenes ingresadas
			numero = numeros.stream().max(Integer::compare).get();
			// y al ultimo numero le aumentamos 1
			numero++;
		}

		// como es en entero lo pasaremos a cadena
		if (numero < 10) {
			numeroConcatenado = "000000000" + String.valueOf(numero);
		} else if (numero < 100) {
			numeroConcatenado = "00000000" + String.valueOf(numero);
		} else if (numero < 1000) {
			numeroConcatenado = "0000000" + String.valueOf(numero);
		} else if (numero < 10000) {
			numeroConcatenado = "000000" + String.valueOf(numero);
		} else if (numero < 100000) {
			numeroConcatenado = "00000" + String.valueOf(numero);
		} else if (numero < 1000000) {
			numeroConcatenado = "0000" + String.valueOf(numero);
		}

		return numeroConcatenado;
	}

}
