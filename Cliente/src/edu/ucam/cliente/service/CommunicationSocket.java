package edu.ucam.cliente.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import edu.ucam.cliente.interfaces.ICommunicationServer;
import edu.ucam.cliente.config.*;

public class CommunicationSocket implements ICommunicationServer{
	private Socket socket;
	private int idComunicacion = 1;
	private BufferedReader br;
	private PrintWriter pw;
	private boolean estado;

	
	// ---------------------------------------------- CONECTAR CON EL SERVIDOR
	@Override
	public void connectar() {
		try {
			socket = new Socket(ClientConfig.ip, ClientConfig.puerto);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			
			// ---------------------------------------------- MENSAJE DE BIENVENIDA
			System.out.println(recibirRespuesta());
			
			estado = true;
		} catch(IOException ex) {
			System.out.println("conectar (CommunicationSocket): " + ex.getMessage());
		}
	}
	
	
	// ---------------------------------------------- CERRAR SOCKET 
	@Override
	public void desconectar(){
		if(socket == null) return;
		
		try {
			if(socket.isConnected()) {
				socket.close();
				estado = false;
				
				System.out.println("CONEXION CERRADA!");
			}
		} catch(IOException ex) {
			System.out.println("conectar (CommunicationSocket): " + ex.getMessage());
		}
	}
	
	
	// ---------------------------------------------- ENVIAR COMANDO AL SERVIDOR
	@Override
	public String enviarComando(String comando){
		if(pw == null) return null;
		
		if(comando == null || comando.trim().isEmpty()) return null;
		
		pw.println(idComunicacion + " " + comando);
		pw.flush();
		
		idComunicacion++;
		
		// ---------------------------------------------- RECIBIR LA RESPUESTA
		String respuesta = recibirRespuesta();
		return respuesta;
	}
	
	
	// ---------------------------------------------- RECIBIR RESPUESTA DEL SERVIDOR
	@Override
	public String recibirRespuesta(){
		if(br == null) return null;
		
		try {
			return br.readLine();
		} catch(IOException ex) {
			System.out.println("recibirRespuesta (CommunicationSocket): " + ex.getMessage());
		}
		
		return null;
	}
}
