package edu.ucam.cliente.UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import edu.ucam.cliente.ClienteERP;

public class MainMenu extends JFrame implements ActionListener{
	private static final long serialVersionUID = 2L;
	
	private ClienteERP clienteERP;
	
	
	// ---------------------------------------------- AUTENTICACION EN EL CONSTRUCTOR
	public MainMenu(String nombre, String pass) {
		try {
			clienteERP = new ClienteERP();
			
			if(clienteERP.autenticar(nombre, pass)) initialize();
			else {
				JOptionPane.showMessageDialog(this, "Credenciales de autenticación incorrectas! Cerrando sesión...", 
						"Autenticación incorrecta", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		} catch(IOException ex) {
			System.out.println("MainMenu (constructor): " + ex.getMessage());
		}
	}

	
	// ---------------------------------------------- ACTIONLISTENER DE BOTONES
	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
	
	
	// ---------------------------------------------- INICIALIZAR INTERFAZ GRAFICA
	private void initialize() {
		
	}
}
