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
	public void connectar() throws IOException {
		socket = new Socket(ClientConfig.ip, ClientConfig.puerto);
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		
		System.out.println(recibirComando());
		
		estado = true;
	}
	
	@Override
	public void desconectar() throws IOException {
		if(socket.isConnected()) {
			socket.close();
			estado = false;
		}
	}
	
	@Override
	public String enviarComando(String comando) throws IOException {
		pw.println(idComunicacion + " " + comando);
		pw.flush();
		idComunicacion++;
		return br.readLine();
	}
	
	@Override
	public boolean isVivo() {
		return estado;
	}
	
	public String recibirComando() throws IOException {
		return br.readLine();
	}
}
