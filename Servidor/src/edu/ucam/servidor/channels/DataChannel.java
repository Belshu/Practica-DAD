package edu.ucam.servidor.channels;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import edu.ucam.servidor.config.ServerConfig;

public class DataChannel {
	private final ServerSocket serverSocket;
	
	public DataChannel() throws IOException {
		this.serverSocket = new ServerSocket(ServerConfig.puertoObjetos);
		// System.out.println("Canal de datos escuchando en puerto: " + ServerConfig.puertoObjetos);
	}
	
	public Socket esperarConexion() {
		try {
			return serverSocket.accept();
		} catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
		
		return null;
	}
	
	public boolean enviarObjeto(Socket socket, Object modelo) {
		ObjectOutputStream oos = null;
		
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(modelo);
			oos.flush();
			
			return true;
		} catch(IOException ex) {
			System.out.println(ex.getMessage());
			
			return false;
		} finally {
			
			// CERRAR OBJETO
			if(oos != null) {
				try {
					oos.close();
				} catch(IOException ex) {
					System.out.println(ex.getMessage());
				}
			}
			
			// CERRAR SOCKET
			if(socket != null && !socket.isClosed()) {
				try {
					socket.close();
				} catch(IOException ex) {
					System.out.println(ex.getMessage());
				}
			}
		}
	}
	
	public Object recibirObjeto(Socket socket) {
		ObjectInputStream ois = null;
		
		try {
			ois = new ObjectInputStream(socket.getInputStream());

			return ois.readObject();
			
		} catch(IOException ex) {
			System.out.println(ex.getMessage());
		} catch(ClassNotFoundException ex) {
			System.out.println("Error en la clase");
		} finally {
			
			// CERRAR OBJETO
			if(ois != null) {
				try {
					ois.close();
				} catch(IOException ex) {
					System.out.println(ex.getMessage());
				}
			}
			
			// CERRAR SOCKET
			if(socket != null && !socket.isClosed()) {
				try {
					socket.close();
				} catch(IOException ex) {
					System.out.println(ex.getMessage());
				}
			}			
		}
		
		return null;
	}
}
