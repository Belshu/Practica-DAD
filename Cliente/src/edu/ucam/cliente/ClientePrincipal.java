package edu.ucam.cliente;

import java.io.IOException;
import java.util.Scanner;

public class ClientePrincipal {
	public static void main(String[] args) throws IOException {
		// PREGUNTAR AL USUARIO EL NOMBRE Y LA CONTRASEÑA
		String usuario = null, contrasena = null;
		Scanner S = new Scanner(System.in);
	
		System.out.print("Nombre: "); usuario = S.nextLine();
		System.out.print("Contraseña: "); contrasena = S.nextLine();
		
		// System.out.println("Nombre: " + usuario + " | contraseña: " + contrasena);
		
		ClienteERP cliente = new ClienteERP();
		
		if(cliente.autenticar(usuario, contrasena)) {
			System.out.println("HOLAA");
		} else {
			System.out.println("Autenticación incorrecta");
		}
		
		System.out.println("FIN DE LA APLICACIÓN");
		S.close();
	}
}
