package edu.ucam.cliente;

import java.io.IOException;

import edu.ucam.cliente.interfaces.IAuthentication;
import edu.ucam.cliente.interfaces.IChannelData;
import edu.ucam.cliente.interfaces.ICommunicationServer;
import edu.ucam.cliente.interfaces.IRepository;

import edu.ucam.cliente.service.AuthenticationService;
import edu.ucam.cliente.service.ChannelData;
import edu.ucam.cliente.service.CommunicationSocket;
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
		autenticacion.cerrarSesion();
	}
	
	public void ejecutarComando(String mensaje) {
		if(mensaje == null) return;
		
		String [] partes = mensaje.trim().split(" ");
		String comando = partes[0].toUpperCase(); // ADD, GET, COUNT...
		
		try {
			if(comando.startsWith("GET")) gestionarGet(comando, partes);
			else comunicacion.enviarComando(mensaje);
		} catch(IOException ex) {
			System.out.println("ejecutarComando: " + ex.getMessage());
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
						System.out.println("COMANDO INCOMPLETO");
					}
				break;
			}
		} catch(IOException ex) {
			System.out.println("ejecutarComando: " + ex.getMessage());
		} catch (ClassNotFoundException ex) {
			System.out.println("ejecutarComando: " + ex.getMessage());
		}
	}
	
	
	// ---------------------------------------------- GETTERS
	public ICommunicationServer getComunicacion() {
		return comunicacion;
	}
}
