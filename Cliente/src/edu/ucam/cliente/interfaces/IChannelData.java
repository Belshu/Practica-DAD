package edu.ucam.cliente.interfaces;

import java.io.IOException;

public interface IChannelData {
	public void sendObject(String ip, String port, Object model) throws IOException;
	public Object receiveObject(String ip, String port) throws IOException;
}
