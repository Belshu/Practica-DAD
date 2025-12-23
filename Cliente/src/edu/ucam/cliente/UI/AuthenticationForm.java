package edu.ucam.cliente.UI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AuthenticationForm extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private final JPanel mainPanel;
	
	private JButton acceptBtn, cancelBtn;
	private JTextField nameTextField, passTextField;
	
	// ---------------------------------------------- CONSTRUCTOR
	public AuthenticationForm() {
		mainPanel = new JPanel();
		
		initialize();
	}
	
	
	// ---------------------------------------------- INICIALIZAR INTERFAZ GRAFICA
	private void initialize() {
		setTitle("Autenticacion");
		setContentPane(getMainPanel());
		setSize(400, 170);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setVisible(true);
	}

	
	// ---------------------------------------------- ACTIONLISTENER DE BOTONES
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == acceptBtn) { // ---------------------------------------------- EXTRAER TEXTO DE LOS TEXTFIELDS
			if(nameTextField.getText() == null || passTextField.getText() == null) return;
			
			if(!nameTextField.getText().isEmpty() && !passTextField.getText().isEmpty()) {
				String nombre = nameTextField.getText();
				String pass = passTextField.getText();
				
				MainMenu mainMenu = new MainMenu(nombre, pass);
				
				setVisible(false);
			} else { // ---------------------------------------------- SI LOS TEXTFIELDS NO ESTAN LLENOS
				JOptionPane.showMessageDialog(this, "Por favor, rellena todo el formulario!", "Advertencia", JOptionPane.WARNING_MESSAGE);
			}
		} else if(e.getSource() == cancelBtn){ // ---------------------------------------------- CANCEL BUTTON
			System.exit(0);
			setVisible(false);
		}
	}
	
	
	// ---------------------------------------------- PRIVATE GETTERS
	
	
	// ---------------------------------------------- MAINPANEL
	private JPanel getMainPanel() {
		mainPanel.setLayout(new BorderLayout());
		
		// ---------------------------------------------- MARGEN DE ARRIBA
		JLabel topMargin = new JLabel(" ");
		topMargin.setFont(new Font("Arial", Font.PLAIN, 20));
		
		mainPanel.add(topMargin, BorderLayout.NORTH);
		mainPanel.add(getFormPanel(), BorderLayout.CENTER);
		mainPanel.add(getButtonsPanel(), BorderLayout.SOUTH);
		
		return mainPanel;
	}
	
	// ---------------------------------------------- LABELSPANEL + TEXTFIELDSPANEL
	private JPanel getFormPanel() {
		JPanel formPanel = new JPanel(new FlowLayout());
		
		formPanel.add(getLabelsPanel());
		formPanel.add(getTextFieldsPanel());
		
		return formPanel;
	}
	
	// ---------------------------------------------- TEXTFIELDSPANEL
	private JPanel getTextFieldsPanel() {
		JPanel textFieldsPanel = new JPanel(new GridLayout(0, 1));
		
		nameTextField = new JTextField(20);
		passTextField = new JTextField(20);
		
		textFieldsPanel.add(nameTextField);
		textFieldsPanel.add(passTextField);
		
		return textFieldsPanel;
	}
	
	// ---------------------------------------------- LABELSPANEL
	private JPanel getLabelsPanel() {
		JPanel labelsPanel = new JPanel(new GridLayout(0, 1));
		
		labelsPanel.add(new JLabel("Nombre:"));
		labelsPanel.add(new JLabel("Contraseña:"));
		
		return labelsPanel;
	}
	
	// -------------------------------------- BUTTONSPANEL
		private JPanel getButtonsPanel() {
			JPanel buttonsPanel = new JPanel(new FlowLayout());
			
			acceptBtn = new JButton("Aceptar");
			cancelBtn = new JButton("Cancelar");
			
			acceptBtn.addActionListener(this);
			cancelBtn.addActionListener(this);
			
			buttonsPanel.add(acceptBtn);
			buttonsPanel.add(cancelBtn);
			
			return buttonsPanel;
		}
}
