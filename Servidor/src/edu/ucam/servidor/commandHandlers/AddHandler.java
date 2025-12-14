package edu.ucam.servidor.commandHandlers;

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
					data.getTitulacionRepository().add(t);
				} else {
					System.out.println("RESPUSETA: FAILED " + idComando + " 400 OBJETO_INVALIDO");
					return "FAILED " + idComando + " 400 OBJETO_INVALIDO";
				}
			break;
			
			case "ADDMATRICULA":
				
			break;
			
			case "ADDASIG":
				
			break;
			
			case "ADDALU":
				
			break;
		
			default:
				 System.out.println("RESPUESTA: FAILED " + idComando + " 400 COMANDO_ADD_NO_VALIDO");
		            return "FAILED " + idComando + " 400 COMANDO_ADD_NO_VALIDO";
		}
		
		return "OK " + idComando + " 200 OBJETO_RECIBIDO";
	}

	public void setModel(Object model) {
		this.model = model;
	}
}
