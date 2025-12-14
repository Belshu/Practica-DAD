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
	private final IRepository<Alumno> repositorioAlumnos;
	
	
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
		this.repositorioAlumnos = new AluRepository(comunicacion, channelData);
	}
	
	
	// ---------------------------------------------- MÉTODOS
	
	public boolean autenticar(String usuario, String password) throws IOException {
		return autenticacion.autenticar(usuario, password);
	}
	
	public void cerrarSesion() throws IOException {
		System.out.println("CERRANDO SESION...");
		autenticacion.cerrarSesion();
	}
	
	
	// ---------------------------------------------- GESTOR DE COMANDOS
	public void ejecutarComando(String mensaje) {
		
		String [] partes = mensaje.trim().split(" ");
		String comando = partes[0].toUpperCase(); // ADD, GET, COUNT...
		
		try {
			if(comando.startsWith("ADD")) gestionarAdd(comando, partes);
			else if(comando.startsWith("GET")) gestionarGet(comando, partes);
			else if(comando.startsWith("COUNT")) gestionarCount(comando);
			else if(comando.equalsIgnoreCase("SESIONES")) imprimirSesiones(mensaje);
			else if(comando.equalsIgnoreCase("EXIT")) autenticacion.cerrarSesion();
			else comunicacion.enviarComando(mensaje);
		
		} catch(IOException ex) {
			System.out.println("ejecutarComando (ClienteERP): " + ex.getMessage());
		}
	}
	
	// ---------------------------------------------- GESTOR DEL COMANDO ADD
	private void gestionarAdd(String comando, String [] partes) {
		Scanner S = new Scanner(System.in);
		
		if(partes.length < 2) {
			System.out.println("COMANDO INCOMPLETO!\n");
			return;
		}
		
		String idObjeto = partes[1];
		try {
			switch(comando) {
				case "ADDTIT":
					Titulacion t = repositorioTitulaciones.crearObjeto(S, idObjeto);
					System.out.println(repositorioTitulaciones.add(idObjeto, t) + "\n");
				break;
				
				default:
					System.out.println("COMANDO NO RECONOCIDO\n");
			}
		} catch(Exception ex) {
			System.out.println("gestionarAdd (ClienteERP): " + ex.getMessage());
		}
	}
	
	// ---------------------------------------------- GESTOR DEL COMANDO GET
	private void gestionarGet(String comando, String [] partes) {
		
		if(partes.length < 2) {
			System.out.println("COMANDO INCOMPLETO!\n");
			return;
		}
		
		try {
			switch(comando) {
				case "GETTIT":
					Titulacion t = repositorioTitulaciones.getModel(partes[1]);
					if(t != null) System.out.println(">> ID: " + t.getId() + "\t>> NOMBRE: " + t.getNombre() + "\n");
				break;
				
				default:
					System.out.println("COMANDO NO RECONOCIDO\n");
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
				if(total != -1) System.out.println("CANTIDAD DE TITULACIONES -> " + total + "\n");
			break;
		}
	}
	
	// ---------------------------------------------- GESTOR DEL COMANDO SESIONES
	private void imprimirSesiones(String mensaje) {
		try {
			String respuesta = comunicacion.enviarComando(mensaje);
			
			if(respuesta == null) {
				System.out.println("Sin respuesta por parte del servidor.\n");
				return;
			}
			
			ResponseParser parser = new ResponseParser(respuesta);
			if(parser.isOK()) System.out.println("SESIONES ACTIVAS -> " + parser.getMessage() + "\n");
			
		} catch (IOException e) {
			System.out.println("imprimirSesiones (ClienteERP): " + e.getMessage());
		}
		
	}
}
