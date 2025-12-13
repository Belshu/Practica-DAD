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

	@Override
	public void connectar() {
		try {
			socket = new Socket(ClientConfig.ip, ClientConfig.puerto);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			
			
			// Mensaje de bienvenida del servidor
			System.out.println(recibirRespuesta());
			
			estado = true;
		} catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@Override
	public void desconectar(){
		try {
			if(socket.isConnected()) {
				socket.close();
				estado = false;
			}
		} catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	@Override
	public String enviarComando(String comando){
		if(comando == null || comando.trim().isEmpty()) return null;
		
		pw.println(idComunicacion + " " + comando);
		pw.flush();
		
		idComunicacion++;
		String respuesta = recibirRespuesta();
		
		if(respuesta != null) System.out.println(respuesta + "\n");
		else System.out.println("Ninguna respuesta por parte del servidor.");
		
		return respuesta;
	}
	
	@Override
	public boolean isVivo() {
		return estado;
	}
	
	@Override
	public String recibirRespuesta(){
		try {
			return br.readLine();
		} catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
		
		return null;
	}
}
