package edu.ucam.servidor;

import java.io.IOException;

public class ServidorMain {
	public static void main(String[] args) throws IOException{
		MainChannel channel = new MainChannel();
		channel.abrirCanalComandos();
	}
}
