package edu.ucam.cliente.service;

import java.io.IOException;
import java.util.List;

import edu.ucam.cliente.interfaces.*;

public class BaseRepository <T> implements IRepository<T> {
	protected final ICommunicationServer communication;
	protected final IChannelData channelData;
	protected final String addComando, removeComando, getComando, listComando, countComando, updateComando;

	public BaseRepository(ICommunicationServer communication, IChannelData channelData, String addComando,
			String removeComando, String getComando, String listComando, String countComando, String updateComando) {
		super();
		this.communication = communication;
		this.channelData = channelData;
		this.addComando = addComando;
		this.removeComando = removeComando;
		this.getComando = getComando;
		this.listComando = listComando;
		this.countComando = countComando;
		this.updateComando = updateComando;
	}

	@Override
	public void add(T modelo) throws IOException, ClassNotFoundException {
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
	public void update(String id, T modelo) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getModel(String id) throws IOException, ClassNotFoundException {
		String respuesta = communication.enviarComando(getComando);
		ResponseParser parser = new ResponseParser(respuesta);
		
		if(parser.isPREOK()) {
			T responseModel = (T) channelData.recibirObjeto(parser.getIp(), parser.getPort());
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
