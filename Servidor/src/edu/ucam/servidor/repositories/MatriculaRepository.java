package edu.ucam.servidor.repositories;

import java.util.ArrayList;
import java.util.List;

import edu.ucam.domain.Titulacion;

public class MatriculaRepository {
private final List <Titulacion> matriculas = new ArrayList<>();
	
	public synchronized int count() {
		return matriculas.size();
	}
	
	public synchronized void add(Titulacion t) {
		matriculas.add(t);
	}
	
	public synchronized Titulacion get(String id) {
		return null;
	}
	
	public synchronized List <Titulacion> list() {
		return matriculas;
	}
	
	public synchronized boolean remove(String id) {
		return false;
	}
}
