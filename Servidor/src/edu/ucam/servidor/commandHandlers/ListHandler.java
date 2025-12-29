package edu.ucam.servidor.commandHandlers;

import java.util.List;

import edu.ucam.domain.Alumno;
import edu.ucam.domain.Asignatura;
import edu.ucam.domain.Matricula;
import edu.ucam.domain.Titulacion;
import edu.ucam.servidor.interfaces.ICommandHandler;
import edu.ucam.servidor.repositories.ERPDataManager;

public class ListHandler implements ICommandHandler {

    private final ERPDataManager data;

    public ListHandler(ERPDataManager data) {
        this.data = data;
    }

    @Override
    public Object handle(String idComando, String[] partes) {
        if (partes.length < 2) {
            return "FAILED " + idComando + " 400 COMANDO_INCOMPLETO";
        }

        String comando = partes[1].toUpperCase();

        switch (comando) {

        case "LISTTIT":
        	return data.getTitulacionRepository().list(); 
        	
        case "LISTASIG":
        	return data.getAsigRepository().list();
        	
        case "LISTMATRICULA":
        	return data.getMatRepository().list();

            default:
                return "FAILED " + idComando + " 400 COMANDO_LIST_NO_VALIDO";
        }
    }
}
