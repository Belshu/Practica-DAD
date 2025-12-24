package edu.ucam.servidor;

import java.io.IOException;

import edu.ucam.servidor.channels.MainChannel;

public class ServidorMain {
	public static void main(String[] args) throws IOException{
		MainChannel channel = new MainChannel();
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				channel.saveData();
			} catch(Exception ex) {
				System.out.println("main (ServidorMain): " + ex.getMessage());
			}
		}));
		
		channel.abrirCanalComandos();
	}
}
