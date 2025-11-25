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
	private Socket mySocket;
	private int communicationId = 1;
	private BufferedReader br;
	private PrintWriter pw;
	private boolean status;

	@Override
	public void connect() throws IOException {
		mySocket = new Socket(ClientConfig.ip, ClientConfig.communicationPort);
		br = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
		pw = new PrintWriter(new OutputStreamWriter(mySocket.getOutputStream()));
		status = true;
	}

	@Override
	public void disconnect() throws IOException {
		if(mySocket.isConnected()) {
			mySocket.close();
			status = false;
		}
	}

	@Override
	public boolean isAlive() {
		return status;
	}

	@Override
	public String sendCommand(String command) throws IOException {
		pw.println(command);
		pw.flush();
		communicationId++;
		return null;
	}
	
	public String receiveCommand() {
		return null;
	}

}
