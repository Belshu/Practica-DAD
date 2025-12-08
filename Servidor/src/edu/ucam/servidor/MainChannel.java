package edu.ucam.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import edu.ucam.servidor.config.ServerConfig;

public class MainChannel {
	private ServerSocket socket = null;
	private Socket socketCliente = null;
	ClientHandler hilo = null;
	
	
	// CONSTRUCTOR
	public MainChannel() {
		try {
			socket = new ServerSocket(ServerConfig.puertoComandos);
			System.out.println("Servidor abierto");
		} catch (IOException ex) { 
			System.out.println(ex.getMessage());
		}
	}
	
	
	// METODO PRINCIPAL: abrir el serversocket, el socket y el hilo (y lanzarlo)
	public void abrirCanalComandos() {
		try {
			while((socketCliente = socket.accept()) != null) {
				System.out.println("Cliente conectado: " + socketCliente.getInetAddress().getHostAddress() + " : " + socketCliente.getPort());
				
				hilo = new ClientHandler(socketCliente);
				hilo.start();
			}
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
