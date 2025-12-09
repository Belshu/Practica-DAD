package edu.ucam.cliente.service.repositories;

import edu.ucam.cliente.interfaces.IChannelData;
import edu.ucam.cliente.interfaces.ICommunicationServer;
import edu.ucam.domain.Matricula;

public class MatRepository extends BaseRepository <Matricula>{

	public MatRepository(ICommunicationServer communication, IChannelData channelData) {
		super(communication, channelData, "ADDMATRICULA", "REMOVEMATRICULA", "GETMATRICULA", 
				"LISTMATRICULA", "COUNTMATRICULA", "UPDATEMATRICULA");
	}

}
