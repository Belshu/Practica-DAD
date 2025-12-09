package edu.ucam.cliente.service.repositories;

import edu.ucam.cliente.interfaces.IChannelData;
import edu.ucam.cliente.interfaces.ICommunicationServer;
import edu.ucam.domain.Titulacion;

public class TituRepository extends BaseRepository <Titulacion>{
	public TituRepository(ICommunicationServer communication, IChannelData channelData) {
		super(communication, channelData, 
				"ADDTIT", "REMOVETIT", "GETTIT", "LISTTIT", "COUNTTIT", "UPDATETIT");
	}
}
