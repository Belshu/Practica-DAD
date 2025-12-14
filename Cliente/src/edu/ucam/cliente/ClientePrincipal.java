package edu.ucam.cliente;

import java.io.IOException;
import java.util.Scanner;

public class ClientePrincipal {
	public static void main(String[] args){
		
		// ---------------------------------------------- PREGUNTAR AL USUARIO EL NOMBRE Y LA CONTRASEÑA
		String usuario = null, contrasena = null;
		Scanner S = new Scanner(System.in);
		
		while(usuario == null || usuario.isEmpty() || contrasena == null || contrasena.isEmpty()) {
			System.out.print("Nombre: "); usuario = S.nextLine();
			System.out.print("Contraseña: "); contrasena = S.nextLine();
		}
		
		// ---------------------------------------------- COMENZAR CONEXIÓN CON EL SERVIDOR
		try {
			ClienteERP cliente = new ClienteERP();
			
			
			// ---------------------------------------------- AUTENTICAR CLIENTE
			if(cliente.autenticar(usuario, contrasena)) { 
				System.out.println("ADMINISTRADOR AUTENTICADO\n");
				
				System.out.print("(Escribe los comandos a continuacion)> ");
				String mensaje = S.nextLine();
				
				
				// ---------------------------------------------- ESCRIBIR COMANDOS
				while(mensaje != null && !mensaje.equalsIgnoreCase("EXIT")) {
					
					// ---------------------------------------------- SI NO ESCIBRE NADA
					if (mensaje.trim().isEmpty()) {    
				        System.out.print("> ");
				        mensaje = S.nextLine();
				        continue;
				    }
					
					cliente.ejecutarComando(mensaje); 
					System.out.print("> ");
					mensaje = S.nextLine();
				}
				
				
				cliente.cerrarSesion();
			} else { // ---------------------------------------------- CLIENTE NO AUTENTICADO
				System.out.println("Autenticación incorrecta");
			}
		} catch(IOException ex) {
			System.out.println("Cliente Principal: " + ex.getMessage());
		}
		
		
		System.out.println("\nFIN DE LA APLICACIÓN");
		S.close();
	}
}