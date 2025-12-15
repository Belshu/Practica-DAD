package edu.ucam.servidor.channels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import edu.ucam.domain.Titulacion;
import edu.ucam.servidor.commandHandlers.AddHandler;
import edu.ucam.servidor.commandHandlers.CountHandler;
import edu.ucam.servidor.commandHandlers.GetHandler;
import edu.ucam.servidor.config.ServerConfig;
import edu.ucam.servidor.repositories.ERPDataManager;

public class ClientChannel extends Thread{
	private Socket socketCliente = null;
	private BufferedReader br;
	private PrintWriter pw;
	
	private static int sesiones = 0; // NUMERO DE SESIONES
	private boolean nombreCorrecto = false, contrasenaCorrecta = false, cerrado = true;
	private static final Object candado = new Object(); // SINCRONIZAR NUMERO DE SESIONES DE CADA HILO
	
	
	// ---------------------------------------------- MANEJO DE DATOS Y CANAL DE OBJETOS PARA ENVIAR/RECIBIR
	private final ERPDataManager data;
	private final DataChannel dataChannel;
	
	
	// ---------------------------------------------- CONSTRUCTOR
	public ClientChannel(Socket socketCliente, ERPDataManager data) {
		this.socketCliente = socketCliente;
		this.data = data;
		this.dataChannel = new DataChannel();
		
		try {
			br = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
			pw = new PrintWriter(new OutputStreamWriter(socketCliente.getOutputStream()));
			
			synchronized(candado) {
				sesiones++;
			}
			
			cerrado = false;
		} catch (IOException e) {
			System.out.println("CONSTRUCTOR (ClienteChannel): " + e.getMessage());
		}
	}
	
	
	// ---------------------------------------------- LANZAR HILO
	@Override
	public void run() {
		try {
			pw.println("OK 0 200 Bienvenido!"); // mensaje de bienvenida
			pw.flush();
			
			
			//  ----------------------------------------------------------- AUTENTICAR NOMBRE DE USUARIO
			String mensaje = null;
			mensaje = br.readLine();
			System.out.println("\nNombre del cliente: " + mensaje);
			gestionarComandos(mensaje);
			
			
			//  ----------------------------------------------------------- AUTENTICAR CONTRASEÑA DE USUARIO
			mensaje = br.readLine();
			System.out.println("\nContraseña del cliente: " + mensaje);
			gestionarComandos(mensaje);
			
			
			//  ----------------------------------------------------------- COMANDOS DEL CLIENTE
			while((mensaje = br.readLine()) != null) {
				System.out.println("\nMensaje del cliente: " + mensaje);
				gestionarComandos(mensaje);
			}
			
		} catch(IOException ex) {
			System.out.println("Conexion cerrada con el cliente " + socketCliente.getInetAddress().getHostAddress() 
					+ " : " + socketCliente.getPort() + " (" + ex.getMessage() + ")");
		} finally {
			if(!cerrado) cerrarConexion();
		}
	}
	
	
	// ----------------------------------------------------------- GESTIONAR COMANDOS
	private void gestionarComandos(String comandoCompleto) {
		String [] partes = comandoCompleto.trim().split(" ");
		
		
		// ---------------------------------------------- MINIMO [ ID_COMANDO ]  & [ COMANDO ] 
		if(partes.length < 2) {
			System.out.println("RESPUESTA: FAILED 0 400 comando_no_valido");
			pw.println("FAILED 0 400 comando_no_valido");
			pw.flush();
			return;
		}
		
		
		String idComando = partes[0], comando = partes[1].toUpperCase();
		
		if (comando.startsWith("ADD")) { // ---------------------------------------------- [ ADD ] 
			if(!autenticado(idComando)) return; 
			
			// ---------------------------------------------- ENVIAR PREOK
			int puertoDatos = dataChannel.puertoLocal();
			if(puertoDatos == -1) {
				System.out.println("RESPUESTA: FAILED " + idComando + " 405 FALLO_ESTABLECIENDO_PUERTO");
			       pw.println("FAILED " + idComando + " 405 FALLO_ESTABLECIENDO_PUERTO");
			       pw.flush();
			       return;
			}
			pw.println("PREOK " + idComando + " 200 " + socketCliente.getLocalAddress().getHostAddress() + " " + puertoDatos);
			pw.flush();
				    
			Socket ss = dataChannel.esperarConexion();
			if(ss == null) {
				System.out.println("RESPUESTA: FAILED " + idComando + " 405 FALLO_CONEXION_SOCKET");
				pw.println("FAILED " + idComando + " 405 FALLO_CONEXION_SOCKET");
				pw.flush();
				return;
			}
			
			
			Object obj = dataChannel.recibirObjeto(ss);
			if(obj == null) {
				System.out.println("RESPUESTA: FAILED " + idComando + " 404 OBJETO_NO_RECIBIDO");
		        pw.println("FAILED " + idComando + " 404 OBJETO_NO_RECIBIDO");
		        pw.flush();
		        return;
			}
			AddHandler addHandler = new AddHandler(data);
			addHandler.setModel(obj);
			String respuesta = (String) addHandler.handle(idComando, partes);
			pw.println(respuesta);
			pw.flush();			
		}
		else if(comando.startsWith("GET")) { // ---------------------------------------------- [ GET ]
			if(!autenticado(idComando)) return; 

			GetHandler getHandler = new GetHandler(data, dataChannel);
			// ---------------------------------------------- GET OBJETO CORRESPONDIENTE
			Object obj = getHandler.handle(idComando, partes);
			if(obj == null) {
				System.out.println("RESPUESTA: FAILED " + idComando + " 404 OBJETO_NO_ENCONTRADO");
		        pw.println("FAILED " + idComando + " 404 OBJETO_NO_ENCONTRADO");
		        pw.flush();
		        return;
			}
			
			// ---------------------------------------------- ENVIAR PREOK
			int puertoDatos = dataChannel.puertoLocal();
			if(puertoDatos == -1) {
				System.out.println("RESPUESTA: FAILED " + idComando + " 405 FALLO_ESTABLECIENDO_PUERTO");
		        pw.println("FAILED " + idComando + " 405 FALLO_ESTABLECIENDO_PUERTO");
		        pw.flush();
		        return;
			}
			pw.println("PREOK " + idComando + " 200 " + socketCliente.getLocalAddress().getHostAddress() + " " + puertoDatos);
		    pw.flush();
		    
			Socket ss = dataChannel.esperarConexion();
			if(ss == null) {
				System.out.println("RESPUESTA: FAILED " + idComando + " 405 FALLO_CONEXION_SOCKET");
		        pw.println("FAILED " + idComando + " 405 FALLO_CONEXION_SOCKET");
		        pw.flush();
		        return;
			}
			
			if(dataChannel.enviarObjeto(ss, obj)) {
				System.out.println("RESPUESTA: OK " + idComando + " 200  OBJETO_ENVIADO");
				pw.println("OK " + idComando + " 200  OBJETO_ENVIADO");
			} else {
				System.out.println("RESPUESTA: FAILED " + idComando + " 500 ERROR_ENVIO_OBJETO\"");
				pw.println("FAILED " + idComando + " 500 ERROR_ENVIO_OBJETO");
			}
			
			pw.flush();
		} else if(comando.startsWith("COUNT")) { // ---------------------------------------------- [ COUNT ]
			if(!autenticado(idComando)) return; 
			
			CountHandler countHandler = new CountHandler(data);
			// ---------------------------------------------- RESPUESTA DEL HANDLER
			String respuesta = (String) countHandler.handle(idComando, partes);
			pw.println(respuesta);
			pw.flush();
		}
		else if(comando.equals("USER") || comando.equals("PASS")) autenticar(idComando, comando, partes); // AUTENTICACION DE USUARIO
		else {
			
			// ----------------------------------------------------------- SESIONES & EXIT
			switch(comando) {
			
				// [idComando] SESIONES = OK [idComando] [cod_respuesta] [num_sesiones] SESIONES_ACTIVAS
				case "SESIONES":
					int total;
					
					synchronized(candado) {
						total = sesiones;		
					}
							
					System.out.println("RESPUESTA: OK " + idComando + " 200 " + total + " SESIONES_ACTIVAS");
					pw.println("OK " + idComando + " 200 " + total + " SESIONES_ACTIVAS");
				break;
				
				// ----------------------------------------------------------- SALIR DEL PROGRAMA
				case "EXIT":
					pw.println("OK " + idComando + " 200 CERRANDO_CONEXIÓN...");
					if(!cerrado) cerrarConexion();
				break;
				
				default:
					pw.println("FAILED " + idComando + " 400 COMANDO_NO_EXISTENTE");
			}
			
			pw.flush();
		}
	}
	
	
	// ----------------------------------------------------------- AUTENTICAR USUARIO
	private void autenticar(String idComando, String comando, String [] partes) {
		switch(comando) {
		
		// ----------------------------------------------------------- AUTENTICAR NOMBRE DE USUARIO
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
		
		// ----------------------------------------------------------- AUTENTICAR CONTRASEÑA DE USUARIO
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
						
						System.out.println("RESPUESTA: FAILED " + idComando + 
								" 401 CONTRASEÑA_INCORRECTA");
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
	
	
	// ---------------------------------------------- SI ESTÁ O NO AUTENTICADO
	private boolean autenticado(String idComando) {
		if(!nombreCorrecto || !contrasenaCorrecta) {
			System.out.println("RESPUESTA FAILED " + idComando + " 403 NO_AUTORIZADO");
			pw.println("FAILED " + idComando + " 403 NO_AUTORIZADO");
			pw.flush();
			
			return false;
		}
		
		return true;
	}
	
	
	// ----------------------------------------------------------- CERRAR SOCKET
	private void cerrarConexion() {
		try {
			if(socketCliente.isConnected()) socketCliente.close();
			
			synchronized(candado) {
				sesiones--;
			}
			
			cerrado = true;
		} catch(IOException ex) {
			System.out.println("cerrarConexion (ClientChannel): " + ex.getMessage());
		}
	}
}
