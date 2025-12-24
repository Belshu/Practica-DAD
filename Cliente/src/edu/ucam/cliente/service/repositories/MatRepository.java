package edu.ucam.cliente.service.repositories;

import java.util.Scanner;

import edu.ucam.cliente.interfaces.IChannelData;
import edu.ucam.cliente.interfaces.ICommunicationServer;
import edu.ucam.domain.Matricula;

public class MatRepository extends BaseRepository <Matricula>{

	public MatRepository(ICommunicationServer communication, IChannelData channelData) {
		super(communication, channelData, "ADDMATRICULA", "REMOVEMATRICULA", "GETMATRICULA", 
				"LISTMATRICULA", "COUNTMATRICULA", "UPDATEMATRICULA");
	}

	@Override
	public Matricula crearObjeto(Scanner S, String idObjeto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matricula crearObjeto(String idObjeto) {
		// TODO Auto-generated method stub
		return null;
	}

}
