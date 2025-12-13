	package edu.ucam.servidor.repositories;

import java.util.ArrayList;
import java.util.List;

import edu.ucam.domain.Alumno;

public class AlumnoRepository {
private final List <Alumno> alumnos = new ArrayList<>();
	
	public synchronized int count() {
		return alumnos.size();
	}
	
	public synchronized void add(Alumno a) {
		alumnos.add(a);
	}
	
	public synchronized Alumno get(String id) {
		for(Alumno a : alumnos) 
			if(a.getDni().equals(id)) return a;
		
		return null;
	}
	
	public synchronized List <Alumno> list() {
		return alumnos;
	}
	
	public synchronized boolean remove(String id) {
		Alumno a = get(id);
		
		if(a == null) return false;
		else alumnos.remove(a);
		
		return true;
	}
}
