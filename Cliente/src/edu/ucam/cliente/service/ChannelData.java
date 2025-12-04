package edu.ucam.cliente.service;

import java.io.IOException;

import edu.ucam.cliente.interfaces.IChannelData;

public class ChannelData implements IChannelData{

	@Override
	public void sendObject(String ip, String port, Object model) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object receiveObject(String ip, String port) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
