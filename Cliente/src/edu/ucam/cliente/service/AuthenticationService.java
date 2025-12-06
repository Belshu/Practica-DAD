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
	public boolean autenticar(String usuario, String contrasena) throws IOException {
		String respuestaServidor = communication.enviarComando("USER " + usuario);
		
		System.out.println(respuestaServidor);
		if(respuestaServidor.startsWith("OK")) {
			respuestaServidor = communication.enviarComando("PASS " + contrasena);
			return respuestaServidor.startsWith("OK");
		}
		return false;
	}

	@Override
	public void cerrarSesion() throws IOException {
		communication.enviarComando("EXIT");
		communication.desconectar();
	}
}
