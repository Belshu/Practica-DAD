package edu.ucam.servidor.commandHandlers;

import edu.ucam.servidor.interfaces.ICommandHandler;
import edu.ucam.servidor.repositories.ERPDataManager;

public class CountHandler implements ICommandHandler{
	private ERPDataManager data;
	
	
	// ---------------------------------------------- CONSTRUCTOR
	public CountHandler(ERPDataManager data) {
		this.data = data;
	}

	// partes [0] = idComando;
	// partes [1] = COUNTTIT
	
	@Override
	public Object handle(String idComando, String[] partes) {
		int total = -1;
		String comando = partes[1].toUpperCase();

	    switch (comando) {
	        case "COUNTTIT":
	            total = data.getTitulacionRepository().count();
	            break;

	        case "COUNTASIG":
	            total = data.getAsigRepository().count();
	            break;

	        case "COUNTMATRICULA":
	            total = data.getMatRepository().count();
	            break;

	        case "COUNTALU":
	            total = data.getAluRepository().count();
	            break;

	        default:
	            System.out.println("RESPUESTA: FAILED " + idComando + " 400 COMANDO_COUNT_NO_VALIDO");
	            return "FAILED " + idComando + " 400 COMANDO_COUNT_NO_VALIDO";
	    }

	    System.out.println("RESPUESTA: OK " + idComando + " 200 " + total);
		return "OK " + idComando + " 200 " + total;
	}

}
