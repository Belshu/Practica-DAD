package edu.ucam.servidor.repositories;

import java.util.ArrayList;
import java.util.List;

import edu.ucam.domain.Asignatura;

public class AsignaturaRepository {
	private final List <Asignatura> asignaturas = new ArrayList<>();
	
	public synchronized int count() {
		return asignaturas.size();
	}
	
	public synchronized boolean add(Asignatura a) {
		if(get(a.getId()) != null) return false;
		
		asignaturas.add(a);
		return true;
	}
	
	public synchronized Asignatura get(String id) {
		for(Asignatura a : asignaturas)
			if(a.getId().equals(id)) return a;
		
		return null;
	}
	
	public synchronized List <Asignatura> list() {
		return asignaturas;
	}
	
	public synchronized boolean remove(String id) {
		Asignatura a = get(id);
		
		if(a == null) return false;
		else asignaturas.remove(a);
		
		return true;
	}
}
