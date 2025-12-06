package edu.ucam.servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import edu.ucam.servidor.config.ServerConfig;

public class ClientHandler extends Thread{
	private Socket socketCliente = null;
	private BufferedReader br;
	private PrintWriter pw;
	
	private boolean nombreCorrecto = false, contrasenaCorrecta = false;
	
	public ClientHandler(Socket socketCliente) {
		this.socketCliente = socketCliente;
	}
	
	@Override
	public void run() {
		try {
			br = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
			pw = new PrintWriter(new OutputStreamWriter(socketCliente.getOutputStream()));
			
			pw.println("OK 0 200 Bienvenido!");
			pw.flush();

			String mensaje = null;
			
			while((mensaje = br.readLine()) != null && (mensaje = br.readLine()).equalsIgnoreCase("EXIT")) {
				System.out.println("Mensaje del cliente: " + mensaje);
				// procesarCliente(mensaje);
			}
			
		} catch(IOException ex) {
			System.out.println(ex.getMessage());
		} finally {
			cerrarConexion();
		}
	}
	
	private void procesarCliente(String comandoCompleto) {
		String [] partes = comandoCompleto.split(" ");
		
		if(partes.length < 2) {
			pw.println("FAILED 0 400 comando_no_valido");
			return;
		}
		
		String idComando = partes[0], comando = partes[1];
		
		switch(comando.toUpperCase()) {
			case "USER":
				if(partes.length > 3) {
					pw.println("FAILED " + idComando + " 400 FALTA_NOMBRE");
				} else {
					String nombre = partes[2];
					
					if(ServerConfig.nombre.equals(nombre)) {
						nombreCorrecto = true;
						pw.println("OK " + idComando + " 200 NOMBRE_OK");
					} else {
						nombreCorrecto = false;
						pw.println("FAILED " + idComando + " 401 NOMBRE_INCORRECTO");
					}
				}
			break;
			
			case "PASS":
				if(partes.length > 3) {
					pw.println("FAILED " + idComando + " 402 FALTA_CONTRASEÑA");
				} else {
					String contrasena = partes[2];
					if(nombreCorrecto) {
						if(ServerConfig.contrasena.equals(contrasena)) {
							contrasenaCorrecta = true;
							pw.println("OK " + idComando + " 200 CONTRASEÑA_OK");
						} else {
							contrasenaCorrecta = false;
							pw.println("FAILED " + idComando + " 401 CONTRASEÑA_INCORRECTA");
						}
					}
				}
			break;
			
			default:
				pw.println("FAILED " + idComando + " 400 COMANDO_NO_EXISTENTE");
		}
	}
	
	private void cerrarConexion() {
		try {
			if(socketCliente.isConnected()) socketCliente.close();
		} catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
