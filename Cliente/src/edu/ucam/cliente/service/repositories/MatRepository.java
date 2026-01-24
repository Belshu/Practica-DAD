package edu.ucam.cliente.service.repositories;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

import edu.ucam.cliente.UI.UISelection;
import edu.ucam.cliente.interfaces.IChannelData;
import edu.ucam.cliente.interfaces.ICommunicationServer;
import edu.ucam.cliente.interfaces.IRepository;
import edu.ucam.domain.Alumno;
import edu.ucam.domain.Asignatura;
import edu.ucam.domain.Matricula;

public class MatRepository extends BaseRepository <Matricula>{
	private IRepository<Asignatura> asigRepo;

	public MatRepository(ICommunicationServer communication, IChannelData channelData, IRepository<Asignatura> asigRepo) {
		super(communication, channelData, "ADDMATRICULA", "REMOVEMATRICULA", "GETMATRICULA", 
				"LISTMATRICULA", "COUNTMATRICULA", "UPDATEMATRICULA");
		this.asigRepo = asigRepo;
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
			disponibles = asigRepo.list();
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
			Asignatura a = null;
			
			try {
				a = asigRepo.getModel(idAsig.trim());
			} catch (ClassNotFoundException e) {
				System.out.println("Error crearObjeto (MatRepository): " + e.getMessage());
				return null;
			} catch (IOException e) {
				System.out.println("Error crearObjeto (MatRepository): " + e.getMessage());
				return null;
			}
			
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
		
		
		// ---------------- DATOS DEL ALUMNO ---------------- 
		String datosAlumno = JOptionPane.showInputDialog(null, "[DNI] [nombre] [apellido1 apellido2]");
		
		if(datosAlumno == null) return null;
		datosAlumno = datosAlumno.trim();
		if(datosAlumno.isEmpty()) return null;
		
		String [] partes = datosAlumno.split("\\s+");
		
		if(partes.length < 3 || partes.length > 5) {
			JOptionPane.showMessageDialog(null, "FORMATO INVÁLIDO", "ERROR", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		try {
			if(partes.length == 4) {
				m.setAlumno(new Alumno(partes[0], partes[1], partes[2] + " " + partes[3]));
			} else {
				m.setAlumno(new Alumno(partes[0], partes[1], partes[2]));
			}
		} catch(Exception ex) {
			JOptionPane.showMessageDialog(null, "Error crearObjeto (MatRepository): " + ex.getMessage(),
					"ERROR", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		
		// ---------------- DATOS DE LAS ASIGNATURAS ---------------- 
		List<Asignatura> asigs = null;
		try {
			asigs = asigRepo.list();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Error obteniendo asignaturas: " + ex.getMessage(),
					"ERROR", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		if (asigs == null || asigs.isEmpty()) {
			System.out.println(); 
			JOptionPane.showMessageDialog(null, "No hay asignaturas creadas. No se puede crear matrícula.",
					"ERROR", JOptionPane.ERROR_MESSAGE);
			return null;
		} 
		
		List<Asignatura> seleccionadas = UISelection.seleccionarVarios(null, "Seleccionar asignaturas", asigs, 
				a -> a.getId() + " - " + a.getNombre());
		
		if (seleccionadas == null || seleccionadas.isEmpty()) {
			JOptionPane.showMessageDialog(null, "No hay asignaturas seleccionadas. No se puede crear matrícula.",
					"ERROR", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		Hashtable<String, Asignatura> tabla = new Hashtable<>();
		for (Asignatura a : seleccionadas) tabla.put(a.getId(), a);
		
		m.setAsignaturas(tabla);
		
		return m;
	}

}
