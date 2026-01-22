package edu.ucam.servidor.commandHandlers;

import edu.ucam.domain.Asignatura;
import edu.ucam.domain.Matricula;
import edu.ucam.domain.Titulacion;
import edu.ucam.servidor.interfaces.ICommandHandler;
import edu.ucam.servidor.repositories.ERPDataManager;

public class AddHandler implements ICommandHandler {
	private final ERPDataManager data;
	private Object model;
	
	public AddHandler(ERPDataManager data) {
		this.data = data;
	}

	// partes[0] = idComando
	// partes[1] = ADD...
	// partes[2] = idObjeto
	
	@Override
	public Object handle(String idComando, String[] partes) {
		if(partes.length < 3) return "FAILED " + idComando + " 400 COMANDO_INCOMPLETO";
		
		String comando = partes[1].toUpperCase(), idObjeto = partes[2];
		switch(comando) {
			case "ADDTIT":
				if(model instanceof Titulacion t) {
					t = (Titulacion) model;
					boolean added = data.getTitulacionRepository().add(t);
					
					if(!added) {
						System.out.println("RESPUSETA: FAILED " + idComando + " 409 ID_REPETIDO");
						return "FAILED " + idComando + " 409 ID_REPETIDO";
					}
				} else {
					System.out.println("RESPUSETA: FAILED " + idComando + " 400 OBJETO_INVALIDO");
					return "FAILED " + idComando + " 400 OBJETO_INVALIDO";
				}
			break;
			
			case "ADDMATRICULA":
				if(model instanceof Matricula m) {
					m = (Matricula) model;
					boolean added = data.getMatRepository().add(m);
					
					if(!added) {
						System.out.println("RESPUSETA: FAILED " + idComando + " 409 ID_REPETIDO");
						return "FAILED " + idComando + " 409 ID_REPETIDO";
					}
				} else {
					System.out.println("RESPUSETA: FAILED " + idComando + " 400 OBJETO_INVALIDO");
					return "FAILED " + idComando + " 400 OBJETO_INVALIDO";
				}
			break;
			
			case "ADDASIG":
				if(model instanceof Asignatura a) {
					a = (Asignatura) model;
					boolean added = data.getAsigRepository().add(a);
					
					if(!added) {
						System.out.println("RESPUSETA: FAILED " + idComando + " 409 ID_REPETIDO");
						return "FAILED " + idComando + " 409 ID_REPETIDO";
					}
				} else {
					System.out.println("RESPUSETA: FAILED " + idComando + " 400 OBJETO_INVALIDO");
					return "FAILED " + idComando + " 400 OBJETO_INVALIDO";
				}
			break;
		
			default:
				 System.out.println("RESPUESTA: FAILED " + idComando + " 400 COMANDO_ADD_NO_VALIDO");
		            return "FAILED " + idComando + " 400 COMANDO_ADD_NO_VALIDO";
		}
		
		return "OK " + idComando + " 201 OBJETO_RECIBIDO";
	}

	public void setModel(Object model) {
		this.model = model;
	}
}
