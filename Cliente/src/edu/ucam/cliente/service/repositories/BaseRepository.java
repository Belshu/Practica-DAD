package edu.ucam.cliente.service.repositories;

import java.io.IOException;
import java.util.List;

import edu.ucam.cliente.interfaces.*;
import edu.ucam.cliente.service.ResponseParser;

public abstract class BaseRepository <T> implements IRepository<T> {
	protected final ICommunicationServer comunicacion;
	protected final IChannelData channelData;
	protected final String addComando, removeComando, getComando, listComando, countComando, updateComando;

	public BaseRepository(ICommunicationServer communication, IChannelData channelData, String addComando,
			String removeComando, String getComando, String listComando, String countComando, String updateComando) {
		super();
		this.comunicacion = communication;
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
		String respuesta = comunicacion.enviarComando(getComando + " " + id);
		
		if(respuesta == null) {
			System.out.println("Sin respuesta por parte del servidor: " + getComando);
			return null;
		}
		
		ResponseParser parser = new ResponseParser(respuesta);
		
		if(parser.isPREOK()) {
			T responseModel = (T) channelData.recibirObjeto(parser.getIp(), parser.getPort());
			
			String respuesta2 = comunicacion.recibirRespuesta();
			
			if(respuesta2 != null) {
				ResponseParser parser2 = new ResponseParser(respuesta2);
				if(parser2.isOK()) return responseModel;
				
				System.out.println("Fallo final tras PREOK: " + parser2.getCodigo() + " " + parser2.getMessage());
				return null;
			}
			
			 System.out.println("No llegó el OK final tras PREOK");
			 return null;
		}
		else if(parser.isFAILED()) {
			if(parser.getMessage().equals("OBJETO_NO_ENCONTRADO")) System.out.println("NO SE HA ENCONTADO EL OBJETO PEDIDO\n");
			else System.out.println("ERROR INESPERADO\n");
		}
		
		return null;
	}

	@Override
	public int modelSize() {
		try {
			String respuestaServidor = comunicacion.enviarComando(countComando);
			
			if(respuestaServidor == null) {
				System.out.println("ERROR EN LA RESPUESTA DEL SERVIDOR\n");
				return -1;
			}
			
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
