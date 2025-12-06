package edu.ucam.cliente.interfaces;

import java.io.IOException;

public interface ICommunicationServer {
	public void connectar() throws IOException;
	public void desconectar() throws IOException;
	public boolean isVivo();
	public String enviarComando(String comando) throws IOException;
	
}
