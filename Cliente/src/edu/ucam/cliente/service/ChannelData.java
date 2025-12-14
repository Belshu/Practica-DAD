package edu.ucam.cliente.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import edu.ucam.cliente.interfaces.IChannelData;

public class ChannelData implements IChannelData{
	
	// ---------------------------------------------- ENVIAR OBJETO
	@Override
	public void enviarObjeto(String ip, String puerto, Object modelo) {
		Socket socket = null;
		ObjectOutputStream oos = null;
		
		try {
			int p = Integer.parseInt(puerto);
			socket = new Socket(ip, p);
			
			
			// ---------------------------------------------- ENVIAR MEDIANTE EL OUTPUT
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(modelo); 
			oos.flush();
			
		} catch(IOException ex) {
			System.out.println("enviarObjeto (ChannelData): " + ex.getMessage());
		} catch(Exception ex) {
			System.out.println("enviarObjeto (ChannelData): " + ex.getMessage());
		} finally {
			
			// ---------------------------------------------- CERRAR OBJETO
			if(oos != null) {
				try {
					oos.close();
				} catch(IOException ex) {
					System.out.println("enviarObjeto (ChannelData): " + ex.getMessage());
				}
			}
			
			// ---------------------------------------------- CERRAR SOCKET
			if(socket != null && !socket.isClosed()) {
				try {
					socket.close();
				} catch(IOException ex) {
					System.out.println("enviarObjeto (ChannelData): " + ex.getMessage());
				}
			}
		}
	}

	@Override
	public Object recibirObjeto(String ip, String puerto) {
		Socket socket = null;
		ObjectInputStream ois = null;
		
		try {
			int p = Integer.parseInt(puerto);
			socket = new Socket(ip, p);
			
			// ---------------------------------------------- RECIBIR MEDIANTE EL INPUT
			ois = new ObjectInputStream(socket.getInputStream());
			return ois.readObject();
			
		} catch(IOException ex) {
			System.out.println("recibirObjeto (ChannelData): " + ex.getMessage());
		} catch(ClassNotFoundException ex) {
			System.out.println("recibirObjeto (ChannelData): " + ex.getMessage());
		} finally {
			
			// ---------------------------------------------- CERRAR OBJETO
			if(ois != null) {
				try {
					ois.close();
				} catch(IOException ex) {
					System.out.println("recibirObjeto (ChannelData): " +ex.getMessage());
				}
			}
			
			// ---------------------------------------------- CERRAR SOCKET
			if(socket != null && !socket.isClosed()) {
				try {
					socket.close();
				} catch(IOException ex) {
					System.out.println("recibirObjeto (ChannelData): " +ex.getMessage());
				}
			}			
		}
		
		return null;
	}

}
