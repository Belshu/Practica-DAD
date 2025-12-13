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
	
	private final DataChannel dataChannel;
	private final ERPDataManager dataManager = new ERPDataManager();
	
	
	// ---------------------------------------------- CONSTRUCTOR
	public MainChannel() throws IOException {
		socket = new ServerSocket(ServerConfig.puertoComandos);
		this.dataChannel = new DataChannel();
		
		System.out.println("Servidor abierto");
	}
	
	// METODO PRINCIPAL: abrir el serversocket, el socket y el hilo (y lanzarlo)
	public void abrirCanalComandos() {
		try {
			
			// ---------------------------------------------- CONEXION ESTABLECIDA CON EL CLIENTE
			while((socketCliente = socket.accept()) != null) {
				System.out.println("Cliente conectado: " + socketCliente.getInetAddress().getHostAddress() 
						+ " : " + socketCliente.getPort());
				
				
				// ---------------------------------------------- CANAL DE COMANDOS 
				hilo = new ClientChannel(socketCliente, dataManager, dataChannel);
				hilo.start();
			}
		} catch (IOException ex) {
			System.out.println("abrirCanalComandos (MainChannel): " + ex.getMessage());
		}
	}
}
