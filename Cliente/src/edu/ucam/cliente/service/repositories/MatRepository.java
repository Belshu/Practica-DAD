package edu.ucam.cliente.service.repositories;

import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

import edu.ucam.cliente.interfaces.IChannelData;
import edu.ucam.cliente.interfaces.ICommunicationServer;
import edu.ucam.domain.Alumno;
import edu.ucam.domain.Asignatura;
import edu.ucam.domain.Matricula;

public class MatRepository extends BaseRepository <Matricula>{
	private SubjectRepository repositorioAsignaturas;

	public MatRepository(ICommunicationServer communication, IChannelData channelData) {
		super(communication, channelData, "ADDMATRICULA", "REMOVEMATRICULA", "GETMATRICULA", 
				"LISTMATRICULA", "COUNTMATRICULA", "UPDATEMATRICULA");
	}

	@Override
	public Matricula crearObjeto(Scanner S, String idObjeto) {
		Matricula m = new Matricula();
		m.setId(idObjeto);
		
		// ---------------- DATOS DEL ALUMNO ---------------- 
		System.out.print("DNI alumno: "); 
		String dni = S.nextLine(); 
		System.out.print("Nombre alumno: ");
		String nombre = S.nextLine();
		System.out.print("Apellidos alumno: "); 
		String apellidos = S.nextLine();
		Alumno alu = new Alumno(dni, nombre, apellidos);
		m.setAlumno(alu);
		
		// ---------------- ASIGNATURAS DISPONIBLES ---------------- 
		System.out.println("\nAsignaturas disponibles:"); 
		List<Asignatura> disponibles = null;
		try { 
			disponibles = repositorioAsignaturas.list();
			} catch (Exception ex) {
				System.out.println("Error obteniendo asignaturas: " + ex.getMessage());
				return null;
				} 
		if (disponibles == null || disponibles.isEmpty()) {
			System.out.println("No hay asignaturas creadas. No se puede crear matrícula."); 
			return null;
			} 
		disponibles.forEach(a -> System.out.println(a.getId() + " - " + a.getNombre()) );
		
		// ---------------- SELECCIÓN DE ASIGNATURAS ---------------- 
		System.out.print("\nIntroduce los IDs de las asignaturas (separados por coma): ");
		String linea = S.nextLine(); 
		
		Hashtable<String, Asignatura> tabla = new Hashtable<>();
		
		for (String idAsig : linea.split(",")) { 
			Asignatura a = repositorioAsignaturas.getModel(idAsig.trim());
		if (a != null) {
			tabla.put(a.getId(), a);
			} else {
				System.out.println("Asignatura no encontrada: " + idAsig.trim());
			} 
		}
		
		m.setAsignaturas(tabla);
		return m;
	}

	@Override
	public Matricula crearObjeto(String idObjeto) {
		Matricula m = new Matricula();
		m.setId(idObjeto);
		
		String datosAlumno = JOptionPane.showInputDialog(null, "[DNI] [nombre] [apellido1_apellido2]");
		String [] partes = datosAlumno.split(datosAlumno.trim());
		m.setAlumno(new Alumno(partes[0], partes[1], partes[2]));
		
		return m;
	}

}
