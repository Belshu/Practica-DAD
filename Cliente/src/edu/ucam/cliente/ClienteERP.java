package edu.ucam.cliente;

import java.io.IOException;
import java.util.Scanner;

import edu.ucam.cliente.interfaces.IAuthentication;
import edu.ucam.cliente.interfaces.IChannelData;
import edu.ucam.cliente.interfaces.ICommunicationServer;
import edu.ucam.cliente.interfaces.IRepository;

import edu.ucam.cliente.service.AuthenticationService;
import edu.ucam.cliente.service.ChannelData;
import edu.ucam.cliente.service.CommunicationSocket;
import edu.ucam.cliente.service.ResponseParser;
import edu.ucam.cliente.service.repositories.*;

import edu.ucam.domain.Alumno;
import edu.ucam.domain.Asignatura;
import edu.ucam.domain.Matricula;
import edu.ucam.domain.Titulacion;

public class ClienteERP {
	private final ICommunicationServer comunicacion;
	private final IAuthentication autenticacion;
	private final IRepository<Asignatura> repositorioAsignaturas;
	private final IRepository<Titulacion> repositorioTitulaciones;
	private final IRepository<Matricula> repositorioMatriculas;
	
	private String respuestaServidor = "";
	
	// ---------------------------------------------- CONEXIÓN SOCKET CON SERVIDOR Y AUTENTICAR
	public ClienteERP() throws IOException{
		this.comunicacion = new CommunicationSocket();
		this.comunicacion.connectar();
	
		IChannelData channelData = new ChannelData();
		this.autenticacion = new AuthenticationService(this.comunicacion);
		
		
		// ---------------------------------------------- REPOSITORIOS
		this.repositorioAsignaturas = new SubjectRepository(comunicacion, channelData);
		this.repositorioTitulaciones = new TituRepository(comunicacion, channelData);
		this.repositorioMatriculas = new MatRepository(comunicacion, channelData);
	}
	
	
	// ---------------------------------------------- MÉTODOS
	
	public boolean autenticar(String usuario, String password) throws IOException {
		return autenticacion.autenticar(usuario, password);
	}
	
	public void cerrarSesion() throws IOException {
		System.out.println("CERRANDO SESION...");
		respuestaServidor = "CERRANDO SESION...";
		autenticacion.cerrarSesion();
	}
	
	
	// ---------------------------------------------- GESTOR DE COMANDOS
	public void ejecutarComando(String mensaje) {
		
		if(mensaje.isEmpty()) return;
		
		String [] partes = mensaje.trim().split(" ");
		String comando = partes[0].toUpperCase(); // ADD, GET, COUNT...
		
		try {
			if(comando.startsWith("ADD")) gestionarAdd(comando, partes);
			else if(comando.startsWith("GET")) gestionarGet(comando, partes);
			else if(comando.startsWith("COUNT")) gestionarCount(comando); 
			else if(comando.equalsIgnoreCase("SESIONES")) imprimirSesiones(mensaje);
			else if(comando.equalsIgnoreCase("EXIT")) autenticacion.cerrarSesion();
			else if(comando.equalsIgnoreCase("save")) guardarInformacion(mensaje);
			else if(comando.equalsIgnoreCase("USER") || comando.equalsIgnoreCase("PASS")){
				String respuesta = comunicacion.enviarComando(mensaje);
				ResponseParser parser = new ResponseParser(respuesta);
				
				if(parser.isOK()) {
					System.out.println(parser.getMessage());
					respuestaServidor = parser.getMessage();
				}
				else if(parser.isFAILED()) {
					System.out.println("ERROR: " + parser.getMessage());
					respuestaServidor = parser.getMessage();
				}
				else {
					System.out.println("ERROR INESPERADO!");
					respuestaServidor = "ERROR INESPERADO!";
					return;
				}
			}
			else respuestaServidor = "";
		
		} catch(IOException ex) {
			System.out.println("ejecutarComando (ClienteERP): " + ex.getMessage());
		}
	}
	
	// ---------------------------------------------- GESTOR DEL COMANDO ADD
	private void gestionarAdd(String comando, String [] partes) {
		Scanner S = new Scanner(System.in);
		
		if(partes.length < 2) {
			respuestaServidor = "COMANDO INCOMPLETO!";
			return;
		}
		
		String idObjeto = partes[1];
		try {
			switch(comando) {
				case "ADDTIT":
					Titulacion t = repositorioTitulaciones.crearObjeto(idObjeto);
					respuestaServidor = repositorioTitulaciones.add(idObjeto, t);
				break;
				
				default:
					respuestaServidor = "COMANDO NO RECONOCIDO";
			}
		} catch(Exception ex) {
			System.out.println("gestionarAdd (ClienteERP): " + ex.getMessage());
		}
	}
	
	// ---------------------------------------------- GESTOR DEL COMANDO GET
	private void gestionarGet(String comando, String [] partes) {
		
		if(partes.length < 2) {
			respuestaServidor = "COMANDO INCOMPLETO!";
			return;
		}
		
		try {
			switch(comando) {
				case "GETTIT":
					Titulacion t = repositorioTitulaciones.getModel(partes[1]);
					if(t != null) {
						respuestaServidor = "\t>> ID: " + t.getId() + "\t>> NOMBRE: " + t.getNombre();
					} else {
						respuestaServidor = "TITULACION NO ENCONTRADA -> " + partes[1];
					}
				break;
				
				default:
					respuestaServidor = "COMANDO NO RECONOCIDO";
			}
		} catch(IOException ex) {
			System.out.println("gestionarGet (ClienteERP): " + ex.getMessage());
		} catch (ClassNotFoundException ex) {
			System.out.println("gestionarGet (ClienteERP): " + ex.getMessage());
		}
	}
	
	// ---------------------------------------------- GESTOR DEL COMANDO COUNT
	private void gestionarCount(String comando) {
		int total = -1;
		
		switch(comando) {
			case "COUNTTIT":
				total =  repositorioTitulaciones.modelSize();
				if(total != -1) {
					respuestaServidor = "CANTIDAD DE TITULACIONES -> " + total;
				}
			break;
			
			default:
				respuestaServidor = "COMANDO NO RECONOCIDO\n";
		}
	}
	
	// ---------------------------------------------- GESTOR DEL COMANDO SESIONES
	private void imprimirSesiones(String mensaje) {
		try {
			respuestaServidor = comunicacion.enviarComando(mensaje);
			
			if(respuestaServidor == null) {
				System.out.println("Sin respuesta por parte del servidor.\n");
				
				return;
			}
			
			ResponseParser parser = new ResponseParser(respuestaServidor);
			if(parser.isOK()) {
				respuestaServidor = "SESIONES ACTIVAS -> " + parser.getMessage();
			} else if(parser.isFAILED()){
				respuestaServidor = "ERROR SESIONES -> " + parser.getMessage();
			} else {
				respuestaServidor = "RESPUESTA DESCONOCIDA";
			}
			
		} catch (IOException e) {
			System.out.println("imprimirSesiones (ClienteERP): " + e.getMessage());
		}
	}
	
	
	// ---------------------------------------------- GUARDAR INFORMACION DEL SERVIDOR
	private void guardarInformacion(String mensaje) {
		try {
			respuestaServidor = comunicacion.enviarComando(mensaje);
			
			if(respuestaServidor == null) {
				System.out.println("Sin respuesta por parte del servidor.\n");
				
				return;
			}
			
			ResponseParser parser = new ResponseParser(respuestaServidor);
			
			System.out.println(respuestaServidor);
			
			if(parser.isOK()) {
				respuestaServidor = parser.getMessage();
			} else if(parser.isFAILED()){
				respuestaServidor = "ERROR -> " + parser.getMessage();
			} else {
				respuestaServidor = "RESPUESTA DESCONOCIDA";
			}
		} catch(IOException ex) {
			System.out.println("imprimirSesiones (ClienteERP): " + ex.getMessage());
		}
	}
	
	public int getIdComando() {
		return comunicacion.getIdComunicacion();
	}
	
	public String getRespuestaServidor() {
		return respuestaServidor;
	}
}
