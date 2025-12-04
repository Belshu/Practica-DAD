package edu.ucam.cliente.service;

import java.io.IOException;

import edu.ucam.cliente.interfaces.IAutentication;
import edu.ucam.cliente.interfaces.ICommunicationServer;

public class AutenticationService implements IAutentication{
	private final ICommunicationServer communication;

	public AutenticationService(ICommunicationServer communication) {
		super();
		this.communication = communication;
	}

	@Override
	public boolean autenticar(String usuario, String password) throws IOException {
		String response = communication.sendCommand("USER " + usuario);
		
		if(response.startsWith("OK")) {
			response = communication.sendCommand("PASS " + password);
			return response.startsWith("OK");
		}
		return false;
	}

	@Override
	public void closeSession() throws IOException {
		communication.sendCommand("EXIT");
		communication.disconnect();
	}
}
