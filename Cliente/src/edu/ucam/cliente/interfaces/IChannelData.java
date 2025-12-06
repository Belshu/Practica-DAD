package edu.ucam.cliente.interfaces;

import java.io.IOException;

public interface IChannelData {
	public void enviarObjeto(String ip, String puerto, Object modelo) throws IOException;
	public Object recibirObjeto(String ip, String puerto) throws IOException;
}
