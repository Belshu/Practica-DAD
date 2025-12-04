package edu.ucam.cliente;

import java.io.IOException;

import edu.ucam.cliente.interfaces.IAutentication;
import edu.ucam.cliente.interfaces.IChannelData;
import edu.ucam.cliente.interfaces.ICommunicationServer;
import edu.ucam.cliente.interfaces.IRepository;

import edu.ucam.cliente.service.AutenticationService;
import edu.ucam.cliente.service.ChannelData;
import edu.ucam.cliente.service.CommunicationSocket;
import edu.ucam.cliente.service.SubjectRepository;
import edu.ucam.cliente.service.TituRepository;

import edu.ucam.domain.Asignatura;
import edu.ucam.domain.Titulacion;

public class ClienteERP {
	private final ICommunicationServer communication;
	private final IAutentication autentication;
	private final IRepository<Asignatura> subjectRepository;
	private final IRepository<Titulacion> tituRepository;
	
	public ClienteERP() throws IOException{
		this.communication = new CommunicationSocket();
		this.communication.connect();
		
		IChannelData channelData = new ChannelData();
		this.autentication = new AutenticationService(this.communication);
		this.subjectRepository = new SubjectRepository(communication, channelData);
		this.tituRepository = new TituRepository(communication, channelData);
	}
	
	public boolean autenticar(String usuario, String password) throws IOException {
		return autentication.autenticar(usuario, password);
	}
	
	public void cerrarSesion() throws IOException {
		autentication.closeSession();
	}
	
	public boolean insertarAsignatura(Asignatura asig) throws ClassNotFoundException, IOException {
		subjectRepository.add(asig);
		return false;
	}
}
