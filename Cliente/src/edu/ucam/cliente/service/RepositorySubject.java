package edu.ucam.cliente.service;

import edu.ucam.cliente.interfaces.IChannelData;
import edu.ucam.cliente.interfaces.ICommunicationServer;
import edu.ucam.domain.Asignatura;

public class RepositorySubject extends BaseRepository <Asignatura>{
	
	public RepositorySubject(ICommunicationServer communication, IChannelData channelData, String insertCommand,
			String deleteCommand, String getCommand, String listCommand, String countCommand, String updateCommand) {
		// public repositorySubject(I, "ADDASIG");
		super(communication, channelData, insertCommand, deleteCommand, getCommand, 
				listCommand, countCommand, updateCommand);
	}
}
