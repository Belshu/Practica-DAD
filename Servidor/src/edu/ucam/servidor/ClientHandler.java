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
	
	private static int sesiones = 0;
	private boolean nombreCorrecto = false, contrasenaCorrecta = false;
	private static final Object candado = new Object();
	
	
	// CONSTRUCTOR: tener referencia del socket creado
	public ClientHandler(Socket socketCliente) {
		this.socketCliente = socketCliente;
		
		try {
			br = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
			pw = new PrintWriter(new OutputStreamWriter(socketCliente.getOutputStream()));
			
			synchronized(candado) {
				sesiones++;
			}
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	
	// Inicializar el BufferedReader/PrintWriter, lanzar el mensaje de bienvenida 
	// y leer todos los mensajes recibidos por el cliente
	@Override
	public void run() {
		try {
			pw.println("OK 0 200 Bienvenido!");
			pw.flush();
			
			String mensaje = null; 
			
			
			// AUTENTICAR NOMBRE DE USUARIO
			mensaje = br.readLine();
			System.out.println("\nNombre del cliente: " + mensaje);
			autenticarCliente(mensaje);
			
			
			// AUTENTICAR CONTRASEÑA DE USUARIO
			mensaje = br.readLine();
			System.out.println("\nContraseña del cliente: " + mensaje);
			autenticarCliente(mensaje);
			
			
			// COMANDOS DEL CLIENTE
			while((mensaje = br.readLine()) != null) {
				System.out.println("\nMensaje del cliente: " + mensaje);
				gestionarComandos(mensaje);
			}
			
		} catch(IOException ex) {
			System.out.println("Conexion cerrada con el cliente " + socketCliente.getInetAddress().getHostAddress() 
					+ " : " + socketCliente.getPort() + " (" + ex.getMessage() + ")");
		} finally {
			cerrarConexion();
		}
	}
	
	
	// GESTIONAR COMANDOS ADD, GET, LIST....
	private void gestionarComandos(String comandoCompleto) {
		String [] partes = comandoCompleto.split(" ");
		
		if(partes.length < 2) {
			System.out.println("RESPUESTA: FAILED 0 400 comando_no_valido");
			pw.println("FAILED 0 400 comando_no_valido");
			return;
		}
		
		String idComando = partes[0], comando = partes[1];
		
		switch(comando.toUpperCase())  {
		
		
			// X SESIONES = OK X 200 1000 SESIONES_ACTIVAS
			case "SESIONES":
				int total;
				
				synchronized(candado) {
					total = sesiones;
				}
				
				System.out.println("RESPUESTA: OK " + idComando + " 200 " + total + " SESIONES_ACTIVAS");
				pw.println("OK " + idComando + " 200 " + total + " SESIONES_ACTIVAS");
			break;
		
			case "EXIT":
				pw.println("OK " + idComando + " 200 CERRANDO CONEXIÓN...");
				cerrarConexion();
				break;
		
			default:
				pw.println("FAILED " + idComando + " 400 COMANDO_NO_EXISTENTE");
		}
		
		pw.flush();
	}
	
	
	// AUTENTICACION INICIAL
	private void autenticarCliente(String comandoCompleto) {
		String [] partes = comandoCompleto.split(" ");
		
		if(partes.length < 2) {
			System.out.println("RESPUESTA: FAILED 0 400 comando_no_valido");
			pw.println("FAILED 0 400 comando_no_valido");
			return;
		}
		
		String idComando = partes[0], comando = partes[1];
		
		switch(comando.toUpperCase()) {
		
			// AUTENTICAR NOMBRE DE USUARIO
			case "USER":
				if(partes.length != 3) {
					System.out.println("RESPUESTA: FAILED " + idComando + " 400 FALTA_NOMBRE");
					pw.println("FAILED " + idComando + " 400 FALTA_NOMBRE");
				} else {
					String nombre = partes[2];
					
					if(ServerConfig.nombre.equals(nombre)) {
						nombreCorrecto = true;
						System.out.println("RESPUESTA: OK " + idComando + " 200 NOMBRE_OK");
						pw.println("OK " + idComando + " 200 NOMBRE_OK");
					} else {
						nombreCorrecto = false;
						System.out.println("RESPUESTA: FAILED " + idComando + " 401 NOMBRE_INCORRECTO");
						pw.println("FAILED " + idComando + " 401 NOMBRE_INCORRECTO");
					}
				}
			break;
			
			// AUTENTICAR CONTRASEÑA
			case "PASS":
				if(partes.length != 3) {
					System.out.println("RESPUESTA: FAILED " + idComando + " 402 FALTA_CONTRASEÑA");
					pw.println("FAILED " + idComando + " 402 FALTA_CONTRASEÑA");
				} else {
					String contrasena = partes[2];
					if(nombreCorrecto) {
						if(ServerConfig.contrasena.equals(contrasena)) {
							contrasenaCorrecta = true;
							
							System.out.println("RESPUESTA: OK " + idComando + " 200 CONTRASEÑA_OK");
							pw.println("OK " + idComando + " 200 CONTRASEÑA_OK");
						} else {
							contrasenaCorrecta = false;
							
							System.out.println("RESPUESTA: FAILED " + idComando + " 401 CONTRASEÑA_INCORRECTA");
							pw.println("FAILED " + idComando + " 401 CONTRASEÑA_INCORRECTA");
						}
					} else {
						System.out.println("RESPUESTA: FAILED " + idComando + " 403 NOMBRE_NO_VALIDO");
						pw.println("FAILED " + idComando + " 403 NOMBRE_NO_VALIDO");
					}
				}
			break;
			
			default:
				pw.println("FAILED " + idComando + " 400 COMANDO_NO_EXISTENTE");
		}
		
		pw.flush();
	}
	
	
	// CERRAR SOCKET
	private void cerrarConexion() {
		try {
			if(socketCliente.isConnected()) socketCliente.close();
			
			synchronized(candado) {
				sesiones--;
			}
			
		} catch(IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	
	// GETTERS & SETTERS
	public static int getSesiones() {
		synchronized(candado) {
			return sesiones;
		}
	}
}
