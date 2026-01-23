package edu.ucam.servidor.repositories;

import java.util.ArrayList;
import java.util.List;

import edu.ucam.domain.Matricula;

public class MatriculaRepository {
private final List <Matricula> matriculas = new ArrayList<>();
	
	public synchronized int count() {
		return matriculas.size();
	}
	
	public synchronized boolean add(Matricula m) {
		if(get(m.getId()) != null) return false;
		
		matriculas.add(m);
		return true;
	}
	
	public synchronized Matricula get(String id) {
		for(Matricula m : matriculas)
			if(m.getId().equals(id)) return m;
		
		return null;
	}
	
	public synchronized List <Matricula> list() {
		return matriculas;
	}
	
	public synchronized boolean remove(String id) {
		Matricula m = get(id);
		
		if(m == null) return false;
		else matriculas.remove(m);
		
		return true;
	}
	
	public synchronized void setAll(java.util.List<Matricula> nuevas) {
		if(nuevas != null) {
			matriculas.clear();
			matriculas.addAll(nuevas);
		}
	}
}
