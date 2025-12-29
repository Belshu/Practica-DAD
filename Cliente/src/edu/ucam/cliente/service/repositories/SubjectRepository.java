package edu.ucam.cliente.service.repositories;

import java.util.Scanner;

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
			try { System.out.print(">> Créditos: "); 
			creditos = Integer.parseInt(S.nextLine().trim());
			valido = true; 
			} catch (NumberFormatException ex) { 
				System.out.println("Valor inválido. Introduce un número entero."); 
				}
			}
		
		Asignatura a = new Asignatura();
		a.setId(idObjeto); 
		a.setNombre(nombre);
		a.setCreditos(creditos); 
		return a;
	}

	@Override
	public Asignatura crearObjeto(String idObjeto) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
