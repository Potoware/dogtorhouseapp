package com.dogtorhouse.app.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Constantes {

	// MODULO CITAS
	public final static String ELIMINAR_BEAN_ERROR = "404";
	public final static String REAGENDAR_CITA = "REAGENDAR";
	public final static String CANCELAR_CITA = "CANCELAR";
	public final static String ATENDER_CITA = "ATENDER";
	public final static String CODIGO_ESTADO_CITA_ACTIVA = "001";
	public final static String CODIGO_ESTADO_CITA_PROGRESO = "002";
	public final static String CODIGO_ESTADO_CITA_FINALIZADA = "003";
	public final static String CODIGO_ESTADO_CITA_CANCELADA = "010";
	public final static String CODIGO_ESTADO_CITA_BAJA = "404";

	// Listas de la aplicacion
	private static List<LabelValue> initMotivosCancelacion = Arrays.asList(new LabelValue("001", "No acude"),
			new LabelValue("002", "Por cliente"), new LabelValue("003", "Por veterinario"),
			new LabelValue("004", "Otros motivos"));

	public final static List<LabelValue> motivosCancelacion = Collections.unmodifiableList(initMotivosCancelacion);

	// Listas de la aplicacion
	private static Map<String, String> initEstadosCita() {
		Map<String, String> map = new HashMap<>();
		map.put(CODIGO_ESTADO_CITA_ACTIVA, "Activa");
		map.put(CODIGO_ESTADO_CITA_PROGRESO, "En progreso");
		map.put(CODIGO_ESTADO_CITA_FINALIZADA, "Finalizada");
		map.put(CODIGO_ESTADO_CITA_CANCELADA, "Cancelada");
		map.put(CODIGO_ESTADO_CITA_BAJA, "Baja");
		return map;
	}

	public final static Map<String, String> listaEstadosCita = Collections.unmodifiableMap(initEstadosCita());

	private static Map<String, List<String>> initEspeciesRazas() {
		Map<String, List<String>> map = new LinkedHashMap<>();

		// Especies y razas de perros
		map.put("Perro", Arrays.asList("Boxer", "Bulldog Inglés", "Chihuahua", "Criollo", "Dálmata", "Golden Retriever",
				"Labrador Retriever", "Pastor Alemán", "Pitbull", "Rottweiler", "Schnauzer"));

		// Especies y razas de gatos
		map.put("Gato", Arrays.asList("Angora", "Bengalí", "British Shorthair", "Criollo","Egipcio", "Maine Coon", "Persa",
				"Ragdoll", "Scottish Fold", "Siamés", "Sphinx"));

		// Especies y razas de conejos
		map.put("Conejo", Arrays.asList("Angora", "Cabeza de León", "Criollo", "Enano", "Flemish Giant", "Holandés",
				"Mini Rex"));

		// Especies y razas de aves
		map.put("Ave", Arrays.asList("Águila", "Búho", "Canario", "Colibrí", "Cotorra", "Criollo", "Guacamaya", "Loros",
				"Pavo Real", "Periquito Australiano"));

		// Especies y razas de reptiles
		map.put("Reptil", Arrays.asList("Camaleón", "Cocodrilo", "Criollo", "Gecko", "Iguana", "Lagarto", "Serpiente",
				"Tortuga"));

		// Especies y razas de peces
		map.put("Pez", Arrays.asList("Betta", "Carpín", "Criollo", "Goldfish", "Guppy", "Pez Ángel", "Pez Disco",
				"Tetra Neón"));

		// Especies y razas de roedores
		map.put("Roedor", Arrays.asList("Ardilla", "Cobaya", "Chinchilla", "Conejillo de Indias", "Criollo", "Hamster",
				"Jerbo", "Rata", "Ratón"));

		return map;
	}

	public final static Map<String, List<String>> listaEspeciesRazas = Collections.unmodifiableMap(initEspeciesRazas());
}
