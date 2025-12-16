package edu.ucam.cliente.service;

import java.io.IOException;

import edu.ucam.cliente.interfaces.IAuthentication;
import edu.ucam.cliente.interfaces.ICommunicationServer;

public class AuthenticationService implements IAuthentication{
	private final ICommunicationServer communication;

	public AuthenticationService(ICommunicationServer communication) {
		this.communication = communication;
	} 

	
	// ---------------------------------------------- BASADO EN LO RESPONDIDO AL INICIO
	@Override
	public boolean autenticar(String usuario, String contrasena){
		try {
			String respuestaServidor = communication.enviarComando("USER " + usuario); 
			
			if(respuestaServidor != null) {
				ResponseParser parser = new ResponseParser(respuestaServidor);
				if(parser.isOK()) {
					respuestaServidor = communication.enviarComando("PASS " + contrasena);
					System.out.println(parser.getMessage());
					
					if(respuestaServidor != null) {
						ResponseParser parser2 = new ResponseParser(respuestaServidor);
						System.out.println(parser2.getMessage());
						return respuestaServidor.startsWith("OK");
					}
				} else if(parser.isFAILED()) {
					System.out.println("ERROR: " + parser.getMessage());
				}
			}
		} catch(IOException ex) {
			System.out.println("autenticar (AuthenticationService): " + ex.getMessage());
		}
		
		return false;
	}

	
	// ---------------------------------------------- CERRAR CONEXION CON EL SERVIDOR
	@Override
	public void cerrarSesion(){
		try {
			communication.enviarComando("EXIT");
			communication.desconectar();
		} catch(IOException ex) {
			System.out.println("cerrarSesion (AuthenticationService): " + ex.getMessage());
		}
	}
}
