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
	public String add(String idObjeto,T modelo) {
		try {
			String respuesta = comunicacion.enviarComando(addComando + " " + idObjeto);
			
			if(respuesta == null) {
				return "Sin respuesta por parte del servidor: " + addComando;
			}
						
			ResponseParser parser = new ResponseParser(respuesta);
			if(parser.isPREOK()) {
				Object obj = modelo;
				channelData.enviarObjeto(parser.getIp(), parser.getPort(), obj);
				
				String respuesta2 = comunicacion.recibirRespuesta();
				if(respuesta2 != null) {
					ResponseParser parser2 = new ResponseParser(respuesta2);
					
					if(parser2.isOK()) return "OBJETO ENVIADO CON EXITO!";
				}
			} else if(parser.isFAILED()) return "ERROR: " + parser.getMessage();
		} catch (IOException e) {
			System.out.println("add (BaseRepository): " + e.getMessage());
		}
		
		return "NO SE PUDO ENVIAR EL OBJETO";
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
	public T getModel(String idObjeto) {
		try {
			
			// ---------------------------------------------- RECIBIR RESPUESTA DEL SERVIDOR
			String respuesta = comunicacion.enviarComando(getComando + " " + idObjeto);
			if(respuesta == null) {
				System.out.println("Sin respuesta por parte del servidor: " + getComando);
				return null;
			}
			
			
			// ---------------------------------------------- PARSEAR PREOK PARA EXTRAER IP Y PUERTO
			ResponseParser parser = new ResponseParser(respuesta);
			if(parser.isPREOK()) {
				T responseModel = (T) channelData.recibirObjeto(parser.getIp(), parser.getPort());
			
				
				// ---------------------------------------------- OBTENER OBJETO RECIBIDO POR EL CANAL CON OK
				String respuesta2 = comunicacion.recibirRespuesta();
				if(respuesta2 != null) {
					ResponseParser parser2 = new ResponseParser(respuesta2);
					if(parser2.isOK()) return responseModel; 
					
					System.out.println("Fallo final tras PREOK: " + parser2.getCodigo() + " " + parser2.getMessage());
				}
				
				 System.out.println("No llegó el OK final tras PREOK");
				 return null;
			}
			else if(parser.isFAILED()) {
				System.out.println("ERROR: " + parser.getMessage());
			}
		} catch(IOException ex) {
			System.out.println("getModel (BaseRepository): " + ex.getMessage());
		}
		
		return null;
	}

	@Override
	public int modelSize() {
		try {
			
			// ---------------------------------------------- RECIBIR RESPUESTA DEL SERVIDOR
			String respuesta = comunicacion.enviarComando(countComando);
			if(respuesta == null) {
				System.out.println("ERROR EN LA RESPUESTA DEL SERVIDOR\n");
				return -1;
			}
			
			
			// ---------------------------------------------- PARSEAR RESPUESTA PARA EXTRAER TAMAÑO
			ResponseParser parser = new ResponseParser(respuesta);
			if(parser.isOK()) {
				String msg = parser.getMessage();
				
				
				// ---------------------------------------------- PARSEAR A UN ENTERO
				try {
					return Integer.parseInt(msg);
				} catch(NumberFormatException ex) {
					System.out.println("ERROR: parseo de COUNT (" + countComando + "): " + msg);
					return -1;
				}
			} else {
				System.out.println("ERROR: " + parser.getMessage());
		            return -1;
			}
			
		} catch (IOException e) {
			System.out.println("modelSize (BaseRepository): " + e.getMessage());
			return -1;
		}
	}
}
