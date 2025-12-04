package edu.ucam.cliente;

import java.io.IOException;

public class ClientePrincipal {
	public static void main(String[] args) throws IOException {
		// PREGUNTAR AL USUARIO EL NOMBRE Y LA CONTRASEÑA
		String usuario = null, password = null;
		
		ClienteERP cliente = new ClienteERP();
		if(cliente.autenticar(usuario, password)) {
			
		} else {
			System.out.println("Autenticación incorrecta");
		}
		
		System.out.println("FIN DE LA APLICACIÓN");
	}
}
