package edu.ucam.cliente.service;

import java.io.IOException;
import java.util.List;

import edu.ucam.cliente.interfaces.*;

public class BaseRepository <T> implements IRepository<T> {
	protected final ICommunicationServer communication;
	protected final IChannelData channelData;
	protected final String insertCommand, deleteCommand, getCommand, listCommand, countCommand, updateCommand;

	public BaseRepository(ICommunicationServer communication, IChannelData channelData, String insertCommand,
			String deleteCommand, String getCommand, String listCommand, String countCommand, String updateCommand) {
		super();
		this.communication = communication;
		this.channelData = channelData;
		this.insertCommand = insertCommand;
		this.deleteCommand = deleteCommand;
		this.getCommand = getCommand;
		this.listCommand = listCommand;
		this.countCommand = countCommand;
		this.updateCommand = updateCommand;
	}

	@Override
	public void add(T model) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<T> list() throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(String id, T model) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getModel(String id) throws IOException, ClassNotFoundException {
		String response = communication.sendCommand(getCommand);
		ResponseParser parser = new ResponseParser(response);
		
		if(parser.isPREOK()) {
			T responseModel = (T) channelData.receiveObject(parser.getIp(), parser.getPort());
			return responseModel;
		} 
		return null;
	}

	@Override
	public int modelSize() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
