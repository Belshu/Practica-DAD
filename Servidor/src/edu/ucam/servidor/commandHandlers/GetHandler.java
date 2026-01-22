package edu.ucam.servidor.commandHandlers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import edu.ucam.domain.Titulacion;
import edu.ucam.servidor.channels.DataChannel;
import edu.ucam.servidor.interfaces.ICommandHandler;
import edu.ucam.servidor.repositories.ERPDataManager;

public class GetHandler implements ICommandHandler{
	private ERPDataManager data;
	
	// ---------------------------------------------- CONSTRUCTOR
	public GetHandler(ERPDataManager data) {
		this.data = data;
	}
	
	// partes [0] = idComando;
	// partes [1] = GETTIT
	// partes [2] = id modelo
	
	@Override
	public Object handle(String idComando, String[] partes) {
		if(partes.length < 3) return null;
		
		String comando = partes[1].toUpperCase(), id = partes[2];
		
		switch(comando) {
			case "GETTIT":
				return data.getTitulacionRepository().get(id);
				
			case "GETASIG":
				return data.getAsigRepository().get(id);
				
			case "GETMATRICULA":
				return data.getMatRepository().get(id);
				
			default:
				return null;
		}
	}
	
}
