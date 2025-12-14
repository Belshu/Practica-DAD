package edu.ucam.servidor.channels;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import edu.ucam.servidor.config.ServerConfig;
import edu.ucam.servidor.repositories.ERPDataManager;

public class MainChannel {
	private ServerSocket socket = null;
	private Socket socketCliente = null;
	private ClientChannel hilo = null;

	private final ERPDataManager dataManager = new ERPDataManager();
	
	
	// ---------------------------------------------- CONSTRUCTOR
	public MainChannel() throws IOException {
		socket = new ServerSocket(ServerConfig.puertoComandos);
		
		System.out.println("Servidor abierto");
	}
	
	// ---------------------------------------------- ABRIR EL SERVERSOCKET, EL SOCKET Y EL HILO (Y LANZARLO)
	public void abrirCanalComandos() {
		try {
			
			// ---------------------------------------------- CONEXION ESTABLECIDA CON EL CLIENTE
			while((socketCliente = socket.accept()) != null) {
				System.out.println("Cliente conectado: " + socketCliente.getInetAddress().getHostAddress() 
						+ " : " + socketCliente.getPort());
				
				
				// ---------------------------------------------- CANAL DE COMANDOS 
				hilo = new ClientChannel(socketCliente, dataManager);
				hilo.start();
			}
		} catch (IOException ex) {
			System.out.println("abrirCanalComandos (MainChannel): " + ex.getMessage());
		}
	}
}
