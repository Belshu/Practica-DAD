package edu.ucam.cliente.service.repositories;

import java.io.IOException;
import java.util.List;

import edu.ucam.cliente.interfaces.*;
import edu.ucam.cliente.service.ResponseParser;

public abstract class BaseRepository <T> implements IRepository<T> {
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
			if(responseModel != null) return responseModel;
		} 
		
		return null;
	}

	@Override
	public int modelSize() {
		try {
			String respuestaServidor = communication.enviarComando(countComando);
			ResponseParser parser = new ResponseParser(respuestaServidor);
			
			if(parser.isOK()) {
				String msg = parser.getMessage();
				try {
					return Integer.parseInt(msg);
				} catch(NumberFormatException ex) {
					System.out.println("ERROR: parseo de COUNT (" + countComando + "): " + msg);
					return -1;
				}
			} else {
				System.out.println("Error en " + countComando 
		                + " | código: " + parser.getCodigo()
		                + " | mensaje: " + parser.getMessage());
		            return -1;
			}
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return -1;
		}
	}
	
}
