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
	private DataChannel dataChannel;
	private Titulacion t;
	
	
	// ---------------------------------------------- CONSTRUCTOR
	public GetHandler(ERPDataManager data, DataChannel dataChannel) {
		this.data = data;
		this.dataChannel = dataChannel;
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
				return t = data.getTitulacionRepository().get(id);
				
			case "GETASIG":
				return data.getAsigRepository().get(id);
				
			default:
				return null;
		}
	}

	
	// ---------------------------------------------- ENVIAR OBJETO AL CLIENTE
	public String responderGet(String idComando, Object obj, String msgOk) {
		Socket socketDatos = dataChannel.esperarConexion();
		if(socketDatos == null) return "FAILED " + idComando + " 500 ERROR_CONEXION_DATOS";
		
		try {
			if(dataChannel.enviarObjeto(socketDatos, obj)) {
			    return "OK " + idComando + " 200  " + msgOk;
			}
			return "FAILED " + idComando + " 500 ERROR_ENVIO_OBJETO";
		} finally {
			try {
				socketDatos.close();
			} catch (IOException e) {
				System.out.println("responderGet: " + e.getMessage());
			}
		}
	}
	
}
