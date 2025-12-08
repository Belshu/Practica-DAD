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
			System.out.println(recibirComando());
			
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
		try {
			pw.println(idComunicacion + " " + comando);
			pw.flush();
			
			System.out.println("\nComando enviado al servidor: " + idComunicacion + " " + comando);
			idComunicacion++;
			String respuesta = br.readLine();
			System.out.println("Respuesta del servidor: " + respuesta);
			return respuesta;
			
		} catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
		
		return null;
	}
	
	@Override
	public boolean isVivo() {
		return estado;
	}
	
	public String recibirComando(){
		try {
			return br.readLine();
		} catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
		
		return "";
	}
}
