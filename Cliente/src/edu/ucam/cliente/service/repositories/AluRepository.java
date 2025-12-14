package edu.ucam.cliente.service.repositories;

import java.util.Scanner;

import edu.ucam.cliente.interfaces.IChannelData;
import edu.ucam.cliente.interfaces.ICommunicationServer;
import edu.ucam.domain.Alumno;

public class AluRepository extends BaseRepository <Alumno>{

	public AluRepository(ICommunicationServer communication, IChannelData channelData) {
		super(communication, channelData, "ADDALU", "REMOVEALU", "GETALU", "LISTALU", 
				"COUNTALU", "UPDATEALU");
		
	}

	@Override
	public Alumno crearObjeto(Scanner S, String idObjeto) {
		// TODO Auto-generated method stub
		return null;
	}
}
