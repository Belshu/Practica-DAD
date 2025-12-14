package edu.ucam.cliente.service.repositories;

import java.util.Scanner;

import edu.ucam.cliente.interfaces.IChannelData;
import edu.ucam.cliente.interfaces.ICommunicationServer;
import edu.ucam.domain.Asignatura;

public class SubjectRepository extends BaseRepository <Asignatura>{
	
	public SubjectRepository(ICommunicationServer communication, IChannelData channelData) {
		super(communication, channelData, "ADDASIG", "REMOVEASIG", "GETASIG", 
				"LISTASIG", "COUNTASIG", "UPDATEASIG");
	}

	@Override
	public Asignatura crearObjeto(Scanner S, String idObjeto) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
