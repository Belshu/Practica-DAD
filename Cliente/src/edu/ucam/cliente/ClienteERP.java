package edu.ucam.cliente;

import java.io.IOException;

import edu.ucam.cliente.interfaces.IAuthentication;
import edu.ucam.cliente.interfaces.IChannelData;
import edu.ucam.cliente.interfaces.ICommunicationServer;
import edu.ucam.cliente.interfaces.IRepository;

import edu.ucam.cliente.service.AuthenticationService;
import edu.ucam.cliente.service.ChannelData;
import edu.ucam.cliente.service.CommunicationSocket;
import edu.ucam.cliente.service.SubjectRepository;
import edu.ucam.cliente.service.TituRepository;

import edu.ucam.domain.Asignatura;
import edu.ucam.domain.Matricula;
import edu.ucam.domain.Titulacion;

public class ClienteERP {
	private final ICommunicationServer comunicacion;
	private final IAuthentication autenticacion;
	private final IRepository<Asignatura> repositorioAsignaturas;
	private final IRepository<Titulacion> repositorioTitulaciones;
	// private final IRepository<Matricula> repositorioMatriculas;
	// private final IRepository<Alumno> repositorioAlumnos;
	
	public ClienteERP() throws IOException{
		this.comunicacion = new CommunicationSocket();
		this.comunicacion.connectar();
	
		IChannelData channelData = new ChannelData();
		this.autenticacion = new AuthenticationService(this.comunicacion);
		this.repositorioAsignaturas = new SubjectRepository(comunicacion, channelData);
		this.repositorioTitulaciones = new TituRepository(comunicacion, channelData);
		// this.repositorioMatriculas = new MatRepository(communication, channelData);
		// this.repositorioMatriculas = new AluRepository(communication, channelData);
	}
	
	public boolean autenticar(String usuario, String password) throws IOException {
		return autenticacion.autenticar(usuario, password);
	}
	
	public void cerrarSesion() throws IOException {
		autenticacion.cerrarSesion();
	}
	
	public boolean insertarAsignatura(Asignatura asig) throws ClassNotFoundException, IOException {
		repositorioAsignaturas.add(asig);
		return false;
	}
}
