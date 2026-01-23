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
	
	// ---------------------------------------------- CONECTAR CON EL SERVIDOR
	@Override
	public void connectar() {
		try {
			socket = new Socket(ClientConfig.ip, ClientConfig.puerto);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			
			// ---------------------------------------------- MENSAJE DE BIENVENIDA
			String respuesta = recibirRespuesta();
			ResponseParser parser = new ResponseParser(respuesta);
			if(parser.isOK()) System.out.println(parser.getMessage());
			else System.out.println("RESPUESTA: " + parser.getMessage());
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
				
				System.out.println("CONEXION CERRADA!");
			}
		} catch(IOException ex) {
			System.out.println("conectar (CommunicationSocket): " + ex.getMessage());
		}
	}
	
	
	// ---------------------------------------------- ENVIAR COMANDO AL SERVIDOR
	@Override
	public String enviarComando(String comando){
		if(pw == null) return "ERROR: PrintWriter!\n";
		
		if(comando == null || comando.trim().isEmpty()) return "ERROR: comando vacío\n";
		pw.println(idComunicacion + " " + comando);
		pw.flush();
		
		idComunicacion++;
		
		// ---------------------------------------------- RECIBIR LA RESPUESTA
		String respuestaServidor = recibirRespuesta() + "\n";
		return respuestaServidor;
	}
	
	// ---------------------------------------------- RECIBIR RESPUESTA DEL SRVIDOR
	@Override
	public String recibirRespuesta(){
		if(br == null) return "ERROR: BufferedReader!\n";
		
		try {
			return br.readLine();
		} catch(IOException ex) {
			System.out.println();
			return "recibirRespuesta (CommunicationSocket): " + ex.getMessage() + "\n";
		}
	}
	
	
	// ---------------------------------------------- GETTER
	
	@Override
	public int getIdComunicacion() {
		return idComunicacion;
	}
}
