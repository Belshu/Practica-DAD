package edu.ucam.servidor.repositories;

import java.util.ArrayList;
import java.util.List;

import edu.ucam.domain.Titulacion;

public class TitulacionRepository {
	private final List <Titulacion> titulaciones = new ArrayList<>();
	
	public synchronized int count() {
		return titulaciones.size();
	}
	
	public synchronized boolean add(Titulacion t) {
		if(get(t.getId()) != null) return false;
		
		titulaciones.add(t);
		return true;
	}
	
	public synchronized Titulacion get(String id) {		
		for(Titulacion t : titulaciones) {
			if(t.getId().equals(id)) {
				return t;
			}
		}
		
		return null;
	}
	
	public synchronized List <Titulacion> list() {
		return titulaciones;
	}
	
	public synchronized boolean remove(String id) {
		Titulacion t = get(id);
		
		if(t == null) return false;
		else titulaciones.remove(t);
		
		return true;
	}
	
	public synchronized void setAll(java.util.List<Titulacion> nuevos) {
		if(nuevos != null) {
			titulaciones.clear();
			titulaciones.addAll(nuevos);
		}
	}
}
