package edu.ucam.servidor.repositories;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.ucam.domain.*;

public class ServerState implements Serializable{
	private static final long serialVersionUID = 10L;
	
	public List<Titulacion> titulaciones = new ArrayList<>();
    public List<Asignatura> asignaturas = new ArrayList<>();
    public List<Matricula> matriculas = new ArrayList<>();
}
