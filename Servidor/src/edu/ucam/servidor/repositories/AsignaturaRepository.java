package edu.ucam.servidor.repositories;

import java.util.ArrayList;
import java.util.List;

import edu.ucam.domain.Titulacion;

public class AsignaturaRepository {
	private final List <Titulacion> asignaturas = new ArrayList<>();
	
	public synchronized int count() {
		return asignaturas.size();
	}
	
	public synchronized void add(Titulacion t) {
		asignaturas.add(t);
	}
	
	public synchronized Titulacion get(String id) {
		return null;
	}
	
	public synchronized List <Titulacion> list() {
		return asignaturas;
	}
	
	public synchronized boolean remove(String id) {
		return false;
	}
}
