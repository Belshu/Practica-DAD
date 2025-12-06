package edu.ucam.cliente.interfaces;

import java.io.IOException;

public interface IAuthentication {
	public boolean autenticar(String usuario, String contrasena) throws IOException;
	void cerrarSesion() throws IOException;
}
