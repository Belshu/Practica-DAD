package edu.ucam.cliente;

import java.io.IOException;
import java.util.Scanner;

import edu.ucam.cliente.UI.AuthenticationForm;

public class ClientePrincipal {
	public static void main(String[] args){
		
		// ---------------------------------------------- INTERFAZ GRAFICA INICIAL
		new AuthenticationForm();
		
		/*
		if(usuario == null) {
			System.out.println("Nombre de usuario no introducido correctamente!");
			System.exit(0);
		}
		
		if(contrasena == null) {
			System.out.println("Contraseña no introducida correctamente!");
			System.exit(0);
		}

		// ---------------------------------------------- COMENZAR CONEXIÓN CON EL SERVIDOR
		try {
			ClienteERP cliente = new ClienteERP();
			
			// ---------------------------------------------- AUTENTICAR CLIENTE
			if(cliente.autenticar(usuario, contrasena)) { 				
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
		*/
	}
}