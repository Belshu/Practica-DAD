package edu.ucam.servidor.repositories;

import java.util.ArrayList;
import java.util.List;

import edu.ucam.domain.Titulacion;

public class TitulacionRepository {
	private final List <Titulacion> titulaciones = new ArrayList<>();
	
	public synchronized int count() {
		return titulaciones.size();
	}
	
	public synchronized void add(Titulacion t) {
		titulaciones.add(t);
	}
	
	public synchronized Titulacion get(String id) {
		return null;
	}
	
	public synchronized List <Titulacion> list() {
		return titulaciones;
	}
	
	public synchronized boolean remove(String id) {
		return false;
	}
}
