package edu.ucam.cliente.service.repositories;

import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

import edu.ucam.cliente.UI.UISelection;
import edu.ucam.cliente.interfaces.IChannelData;
import edu.ucam.cliente.interfaces.ICommunicationServer;
import edu.ucam.cliente.interfaces.IRepository;
import edu.ucam.domain.Titulacion;
import edu.ucam.domain.Asignatura;
import edu.ucam.domain.Matricula;

public class TituRepository extends BaseRepository <Titulacion>{
	private IRepository <Asignatura> asigRepo;
	private IRepository <Matricula> matRepo;

	public TituRepository(ICommunicationServer communication, IChannelData channelData, 
			IRepository <Asignatura> asigRepo, IRepository <Matricula> matRepo) {
		super(communication, channelData, 
				"ADDTIT", "REMOVETIT", "GETTIT", "LISTTIT", "COUNTTIT", "UPDATETIT");
		this.asigRepo = asigRepo;
		this.matRepo = matRepo;
	}
	
	@Override
	public Titulacion crearObjeto(Scanner S, String idObjeto) {
		String nombre;
		
		do {
			System.out.println(">> Nombre: ");
			nombre = S.nextLine();
		} while(nombre.isEmpty());
		
		Titulacion t = new Titulacion();
		t.setId(idObjeto);
		t.setNombre(nombre);
		
		return t;
	}
	
	public Titulacion crearObjeto(String idObjeto) {
		String nombre = JOptionPane.showInputDialog(null, "Nombre de la titulación:");
		
		if(nombre == null) return null;
	    nombre = nombre.trim();
	    if(nombre.isEmpty()) return null;
		
		Titulacion t = new Titulacion();
		t.setId(idObjeto);
		t.setNombre(nombre);
		
		
		// ---------------- ASIGNATURAS DISPONIBLES ---------------- 
		List<Asignatura> asigs = null;
		List<Matricula> mats = null;
		try { 
			asigs = asigRepo.list();
			mats = matRepo.list();
		} catch (Exception ex) {
			System.out.println("Error obteniendo asignaturas: " + ex.getMessage());
			return null;
		}
		
		if (asigs == null || asigs.isEmpty()) {
			System.out.println("No hay asignaturas creadas. No se puede crear titulación."); 
			return null;
		} 
		
		if (mats == null || mats.isEmpty()) {
			System.out.println("No hay matrículas creadas. No se puede crear titulación."); 
			return null;
		} 
		
		List<Asignatura> asigSeleccionadas = UISelection.seleccionarVarios(null, "Seleccionar asignaturas", asigs, 
				a -> a.getId() + " - " + a.getNombre());
		if (asigSeleccionadas == null || asigSeleccionadas.isEmpty()) {
			System.out.println("No hay asignaturas seleccionadas. No se puede crear titulación."); 
			return null;
		}
		
		List<Matricula> matSeleccionadas = UISelection.seleccionarVarios(null, "Seleccionar matriculas", mats, 
				m -> m.getId() + " - " + m.getAlumno().getNombre() + " " + m.getAlumno().getApellidos() + " (" + m.getAlumno().getDni() + ")");
		if (matSeleccionadas == null || matSeleccionadas.isEmpty()) {
			System.out.println("No hay matrículas seleccionadas. No se puede crear titulación."); 
			return null;
		}
		
		for(Asignatura a : asigSeleccionadas) t.addAsignatura(a);
		for(Matricula m : matSeleccionadas) t.addMatricula(m);
		
		return t;
	}
}
