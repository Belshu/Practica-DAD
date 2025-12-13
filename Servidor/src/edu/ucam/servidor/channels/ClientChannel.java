package edu.ucam.servidor.channels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import edu.ucam.domain.Titulacion;
import edu.ucam.servidor.config.ServerConfig;
import edu.ucam.servidor.repositories.ERPDataManager;

public class ClientChannel extends Thread{
	private Socket socketCliente = null;
	private BufferedReader br;
	private PrintWriter pw;
	
	private static int sesiones = 0;
	private boolean nombreCorrecto = false, contrasenaCorrecta = false;
	private static final Object candado = new Object();
	private final ERPDataManager data;
	private final DataChannel dataChannel;
	
	
	// CONSTRUCTOR: tener referencia del socket creado
	public ClientChannel(Socket socketCliente, ERPDataManager data, DataChannel dataChannel) {
		this.socketCliente = socketCliente;
		this.data = data;
		this.dataChannel = dataChannel;
		
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
			
			
			//  ----------------------------------------------------------- AUTENTICAR NOMBRE DE USUARIO
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
			cerrarConexion();
		}
	}
	
	
//  ----------------------------------------------------------- GESTIONAR COMANDOS
	private void gestionarComandos(String comandoCompleto) {
		String [] partes = comandoCompleto.split(" ");
		
		if(partes.length < 2) {
			System.out.println("RESPUESTA: FAILED 0 400 comando_no_valido");
			pw.println("FAILED 0 400 comando_no_valido");
			pw.flush();
			return;
		}
		
		String idComando = partes[0], comando = partes[1].toUpperCase();
		
		// if (comando.startsWith("ADD")) gestionarAdd(idComando, partes);
		// else 
		if(comando.startsWith("GET")) gestionarGet(idComando, partes);
		else if(comando.startsWith("COUNT")) gestionarCount(idComando, comando);
		else {
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
				
				// ----------------------------------------------------------- AUTENTICAR USUARIO
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
				
				// ----------------------------------------------------------- AUTENTICAR CONTRASEÑA
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
				
				
				// SALIR DEL PROGRAMA
				case "EXIT":
					pw.println("OK " + idComando + " 200 CERRANDO_CONEXIÓN...");
					cerrarConexion();
				break;
				
				default:
					pw.println("FAILED " + idComando + " 400 COMANDO_NO_EXISTENTE");
			}
			
			pw.flush();
		}
	}
	
	
	//  ----------------------------------------------------------- GESTIONAR COMANDO GET
	private void gestionarGet(String idComando, String [] partes) {
		// partes [0] = idComando;
		// partes [1] = GETTIT
		// partes [2] = id modelo
		
		if(partes.length < 3) {
			System.out.println("RESPUESTA: FAILED " + idComando + " 400 FALTAN_PARAMETROS_COMANDO:GET");
			pw.println("FAILED " + idComando + " 400 FALTAN_PARAMETROS_COMANDO:GET");
			pw.flush();
			return;
		}
		
		String comando = partes[1].toUpperCase(), id = partes[2];
		
		switch(comando) {
			case "GETTIT":
				Titulacion t = data.getTitulacionRepository().get(id);
				
				if(t == null) {
					System.out.println("RESPUESTA: FAILED " + idComando + " 404 TITULACION_NO_ENCONTRADA");
			        pw.println("FAILED " + idComando + " 404 TITULACION_NO_ENCONTRADA");
			        pw.flush();
			        return;
				}
				
				pw.println("PREOK " + idComando + " 500 " + socketCliente.getInetAddress().getHostAddress()
						+ " " + ServerConfig.puertoObjetos);
				pw.flush();
				
				Socket socketDatos = null;
				
				if(abrirDataChannel(idComando, socketDatos)) {
					if(dataChannel.enviarObjeto(socketDatos, t)) {
						System.out.println("RESPUESTA: OK " + idComando + " 200 TITULACION_ENVIADA");
					    pw.println("OK " + idComando + " 200 TITULACION_ENVIADA");
					    pw.flush();
					} else {
						System.out.println("RESPUESTA: FAILED " + idComando + " 500 ERROR_ENVIO_OBJETO");
				        pw.println("FAILED " + idComando + " 500 ERROR_ENVIO_OBJETO");
				        pw.flush();
					}
				}
				
			break;
		}
	}
	
	
	//  ----------------------------------------------------------- GESTIONAR COMANDO COUNT
	private void gestionarCount(String idComando, String comando) {
	    int total = -1;

	    switch (comando) {
	        case "COUNTTIT":
	            total = data.getTitulacionRepository().count();
	            break;

	        case "COUNTASIG":
	            total = data.getAsigRepository().count();
	            break;

	        case "COUNTMATRICULA":
	            total = data.getMatRepository().count();
	            break;

	        case "COUNTALU":
	            total = data.getAluRepository().count();
	            break;

	        default:
	            System.out.println("RESPUESTA: FAILED " + idComando + " 400 COMANDO_COUNT_NO_VALIDO");
	            pw.println("FAILED " + idComando + " 400 COMANDO_COUNT_NO_VALIDO");
	            pw.flush();
	            return;
	    }

	    System.out.println("RESPUESTA: OK " + idComando + " 200 " + total);
	    pw.println("OK " + idComando + " 200 " + total);
	    pw.flush();
	}
	
	private boolean abrirDataChannel (String idComando, Socket socketDatos) {
		try {
			socketDatos = dataChannel.esperarConexion();
			if(socketDatos != null) return true;
			
		} catch(Exception ex) {
			System.out.println("Error de conexion de datos: " + ex.getMessage());
	        pw.println("FAILED " + idComando + " 500 ERROR_ENVIO_OBJETO");
	        pw.flush();
		}
		
		return false;
	}
	
	
	//  ----------------------------------------------------------- CERRAR SOCKET
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
}
