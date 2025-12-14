package edu.ucam.cliente.service.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.ucam.cliente.interfaces.IChannelData;
import edu.ucam.cliente.interfaces.ICommunicationServer;
import edu.ucam.domain.Titulacion;

public class TituRepository extends BaseRepository <Titulacion>{

	public TituRepository(ICommunicationServer communication, IChannelData channelData) {
		super(communication, channelData, 
				"ADDTIT", "REMOVETIT", "GETTIT", "LISTTIT", "COUNTTIT", "UPDATETIT");
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
}
