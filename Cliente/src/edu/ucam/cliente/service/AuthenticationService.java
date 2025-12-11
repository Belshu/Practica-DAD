package edu.ucam.cliente.service;

import java.io.IOException;

import edu.ucam.cliente.interfaces.IAuthentication;
import edu.ucam.cliente.interfaces.ICommunicationServer;

public class AuthenticationService implements IAuthentication{
	private final ICommunicationServer communication;

	public AuthenticationService(ICommunicationServer communication) {
		this.communication = communication;
	} 

	@Override
	public boolean autenticar(String usuario, String contrasena){
		try {
			String respuestaServidor = communication.enviarComando("USER " + usuario); 
			
			if(respuestaServidor != null) {
				if(respuestaServidor.startsWith("OK")) {
					respuestaServidor = communication.enviarComando("PASS " + contrasena);
					
					if(respuestaServidor != null) return respuestaServidor.startsWith("OK");
					else return false;
				}
			}
		} catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
		
		return false;
	}

	@Override
	public void cerrarSesion(){
		try {
			communication.enviarComando("EXIT");
			communication.desconectar();
		} catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
