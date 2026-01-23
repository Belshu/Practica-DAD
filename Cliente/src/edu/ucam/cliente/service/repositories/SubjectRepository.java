package edu.ucam.cliente.service.repositories;

import java.util.Scanner;

import javax.swing.JOptionPane;

import edu.ucam.cliente.interfaces.IChannelData;
import edu.ucam.cliente.interfaces.ICommunicationServer;
import edu.ucam.domain.Asignatura;

public class SubjectRepository extends BaseRepository <Asignatura>{
	
	public SubjectRepository(ICommunicationServer communication, IChannelData channelData) {
		super(communication, channelData, "ADDASIG", "REMOVEASIG", "GETASIG", 
				"LISTASIG", "COUNTASIG", "UPDATEASIG");
	}

	@Override
	public Asignatura crearObjeto(Scanner S, String idObjeto) {
		String nombre = ""; 
		int creditos = -1;
		
		// Pedir nombre
		do {
			System.out.print(">> Nombre asignatura: ");
			nombre = S.nextLine().trim();
			} while (nombre.isEmpty()); 
		
		// Pedir créditos
		boolean valido = false;
		while (!valido) { 
			try { 
				System.out.print(">> Créditos: "); 
				creditos = Integer.parseInt(S.nextLine().trim());
				
				if(creditos > 0 && creditos < 7) valido = true; 
				else System.out.println("Créditos fuera de rango");
			} catch (NumberFormatException ex) { 
				System.out.println("Valor inválido. Introduce un número entero."); 
				}
			}
		
		Asignatura a = null;
		if(valido) {
			a = new Asignatura();
			a.setId(idObjeto); 
			a.setNombre(nombre);
			a.setCreditos(creditos);
		}
		
		return a;
	}

	@Override
	public Asignatura crearObjeto(String idObjeto) {
		Asignatura a = new Asignatura();
		a.setId(idObjeto);
		
		// ---------------- DATOS DE LA ASIGNATURA ---------------- 
		String datosAsignatura = JOptionPane.showInputDialog(null, "[nombre] [Nº de créditos]");
		if(datosAsignatura == null) return null;
		datosAsignatura = datosAsignatura.trim();
		if(datosAsignatura.isEmpty()) return null;
		
		String [] partes = datosAsignatura.split("\\s+");
		
		if(partes.length != 2) {
			JOptionPane.showMessageDialog(null, "FORMATO INVÁLIDO", "ERROR", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		try {
			a.setNombre(partes[0]);
			a.setCreditos(Integer.parseInt(partes[1]));
			
			return a;
		} catch(Exception ex) {
			JOptionPane.showMessageDialog(null, "Error crearObjeto (SubjectRepository): " + ex.getMessage(),
					"ERROR", JOptionPane.ERROR_MESSAGE);
		}
		
		return null;
	}
	
}
