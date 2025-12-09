package edu.ucam.servidor.repositories;

import java.util.ArrayList;
import java.util.List;

import edu.ucam.domain.Titulacion;

public class AlumnoRepository {
private final List <Titulacion> alumnos = new ArrayList<>();
	
	public synchronized int count() {
		return alumnos.size();
	}
	
	public synchronized void add(Titulacion t) {
		alumnos.add(t);
	}
	
	public synchronized Titulacion get(String id) {
		return null;
	}
	
	public synchronized List <Titulacion> list() {
		return alumnos;
	}
	
	public synchronized boolean remove(String id) {
		return false;
	}
}
