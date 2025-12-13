package edu.ucam.servidor.interfaces;

import java.io.IOException;

public interface ICommandHandler {
	Object handle(String idComando, String [] partes);
}
