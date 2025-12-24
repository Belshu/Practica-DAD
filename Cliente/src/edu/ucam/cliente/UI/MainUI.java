package edu.ucam.cliente.UI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.ucam.cliente.ClienteERP;

public class MainUI extends JFrame implements ActionListener{
	private static final long serialVersionUID = 2L;
	
	private ClienteERP clienteERP;
	private JButton saveBtn, closeBtn;
	
	private JTextArea textArea;
	private JTextField textField;
	
	// ---------------------------------------------- AUTENTICACION EN EL CONSTRUCTOR
	public MainUI(String nombre, String pass) {
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
		if(e.getSource() == textField) {
			final String cmd = textField.getText().trim();
			
			if(cmd == null) return;
			
			if(cmd.equalsIgnoreCase("clean")) {
				textArea.setText("");
				textField.setText("");
				return;
			}
		
			if(cmd.equalsIgnoreCase("exit")) {
				clienteERP.ejecutarComando(cmd);		
				dispose();
				setVisible(false);
				System.exit(0);
			}
			
			clienteERP.ejecutarComando(cmd);
			String respuesta = clienteERP.getRespuestaServidor();
			if(respuesta.isEmpty()) respuesta = cmd;
			
			String text = clienteERP.getIdComando() + " " + cmd.toUpperCase() + " >> " + respuesta;
			
			if(textArea.getText().isEmpty()) {
				textArea.setText(text);
			}
			else textArea.append("\n" + text);
			
			textField.setText("");
		}
	}
	
	
	// ---------------------------------------------- INICIALIZAR INTERFAZ GRAFICA
	private void initialize() {
		setTitle("Menu principal");
		setContentPane(getMainPanel());
		setSize(900, 600);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		textField.requestFocusInWindow();
	}
	
	
	// ---------------------------------------------- GETTERS
	
	
	// ---------------------------------------------- MAIN PANEL
	private JPanel getMainPanel() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		
		JLabel marginTop = new JLabel(" ");
		marginTop.setFont(new Font("Arial", Font.PLAIN, 20));
		
		mainPanel.add(marginTop, BorderLayout.NORTH);
		mainPanel.add(getTextAreaPanel(), BorderLayout.CENTER);
		mainPanel.add(new JLabel("      "), BorderLayout.WEST);
		mainPanel.add(getMarginButtonsPanel(), BorderLayout.EAST);
		mainPanel.add(getTextFieldPanel(), BorderLayout.SOUTH);
		
		return mainPanel;
	}
	
	
	// ---------------------------------------------- TEXTAREA
	private JPanel getTextAreaPanel() {
		JPanel textAreaPanel = new JPanel(new BorderLayout());
		
		textArea = new JTextArea(25, 25);
		textArea.setEditable(false);
		textArea.setText("");
		JScrollPane scroll = new JScrollPane(textArea);

		textAreaPanel.add(scroll, BorderLayout.CENTER);
		
		return textAreaPanel;
	}
	
	
	// ---------------------------------------------- BUTTONS 
	private JPanel getMarginButtonsPanel() {
		JPanel marginButtonsPanel = new JPanel(new BorderLayout());
		
		JLabel marginTop = new JLabel(" ");
		marginTop.setFont(new Font("Arial", Font.PLAIN, 10));
		
		marginButtonsPanel.add(marginTop, BorderLayout.NORTH);
		marginButtonsPanel.add(new JLabel("      "), BorderLayout.WEST);
		marginButtonsPanel.add(getButtonsPanel(), BorderLayout.CENTER);
		marginButtonsPanel.add(new JLabel("      "), BorderLayout.EAST);
		
		return marginButtonsPanel;
	}
	
	private JPanel getButtonsPanel() {
		JPanel buttonsPanel = new JPanel(new GridLayout(0, 1, 0, 10));
		
		saveBtn = new JButton("Guardar");
		closeBtn = new JButton("Salir");
		
		saveBtn.addActionListener(this);
		closeBtn.addActionListener(e -> {
			try {
				clienteERP.cerrarSesion();
			} catch (IOException ex) {
				System.out.println("getButtonsPanel (MainUI): " + ex.getMessage());
			}
			
			System.exit(0);
		});
		
		buttonsPanel.add(saveBtn);
		buttonsPanel.add(closeBtn);
		
		return buttonsPanel;
	}
	
	
	// ---------------------------------------------- TEXTFIELD
	private JPanel getTextFieldPanel() {
		JPanel textFieldPanel = new JPanel(new BorderLayout());
		
		JLabel marginBottom = new JLabel(" ");
		JLabel marginTop = new JLabel(" ");
		marginBottom.setFont(new Font("Arial", Font.PLAIN, 20));
		marginTop.setFont(new Font("Arial", Font.PLAIN, 10));
		
		textField = new JTextField(20);
		textField.addActionListener(this);
		
		
		textFieldPanel.add(marginTop, BorderLayout.NORTH);
		textFieldPanel.add(new JLabel("  >>  "), BorderLayout.WEST);
		textFieldPanel.add(new JLabel("      "), BorderLayout.EAST);
		textFieldPanel.add(textField, BorderLayout.CENTER);
		textFieldPanel.add(marginBottom, BorderLayout.SOUTH);
		
		return textFieldPanel;
	}
}
