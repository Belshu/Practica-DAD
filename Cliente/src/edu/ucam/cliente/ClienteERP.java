package edu.ucam.cliente;

import java.io.IOException;

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
	
	
	// CONEXIÓN DEL SOCKET CON EL SERVIDOR, CONECTAR Y AUTENTICACIÓN
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
			
			if(comando.startsWith("GET")) gestionarGet(comando, partes);
			else if(comando.startsWith("COUNT")) gestionarCount(comando);
			else if(comando.equalsIgnoreCase("SESIONES")) imprimirSesiones(mensaje);
			else if(comando.equalsIgnoreCase("EXIT")) autenticacion.cerrarSesion();
			else comunicacion.enviarComando(mensaje);
		
		} catch(IOException ex) {
			System.out.println("ejecutarComando (ClienteERP): " + ex.getMessage());
		}
	}
	
	private void gestionarGet(String comando, String [] partes) {
		try {
			switch(comando) {
				case "GETTIT":
					if(partes.length >= 2) {
						Titulacion t = repositorioTitulaciones.getModel(partes[1]);
						if(t != null) System.out.println(t.toString());
					} else {
						System.out.println("COMANDO INCOMPLETO!\n");
					}
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
	
	private void gestionarCount(String comando) {
		switch(comando) {
			case "COUNTTIT":
				int total =  repositorioTitulaciones.modelSize();
				if(total != -1) System.out.println("CANTIDAD DE TITULACIONES > " + total + "\n");
			break;
		}
	}
	
	private void imprimirSesiones(String mensaje) {
		try {
			String respuesta = comunicacion.enviarComando(mensaje);
			
			if(respuesta == null) {
				System.out.println("Sin respuesta por parte del servidor.\n");
				return;
			}
			
			ResponseParser parser = new ResponseParser(respuesta);
			if(parser.isOK()) System.out.println("SESIONES ACTIVAS > " + parser.getMessage());
			
		} catch (IOException e) {
			System.out.println("imprimirSesiones (ClienteERP): " + e.getMessage());
		}
		
	}
	
	// ---------------------------------------------- GETTERS
	public ICommunicationServer getComunicacion() {
		return comunicacion;
	}
}
