package edu.ucam.servidor.commandHandlers;

import edu.ucam.servidor.interfaces.ICommandHandler;
import edu.ucam.servidor.repositories.ERPDataManager;

public class RemoveHandler implements ICommandHandler {
    private final ERPDataManager data;

    public RemoveHandler(ERPDataManager data) {
        this.data = data;
    }

    @Override
    public Object handle(String idComando, String[] partes) {
        if (partes.length < 3) {
            return "FAILED " + idComando + " 400 COMANDO_INCOMPLETO";
        }

        String comando = partes[1].toUpperCase();
        String id = partes[2];
        boolean resultado;

        switch (comando) {
            case "REMOVETIT":
                resultado = data.getTitulacionRepository().remove(id);
                break;

            case "REMOVEASIG":
                resultado = data.getAsigRepository().remove(id);
                break;

            case "REMOVEMATRICULA":
                resultado = data.getMatRepository().remove(id);
                break;

            default:
                return "FAILED " + idComando + " 400 COMANDO_REMOVE_NO_VALIDO";
        }

        if (resultado) {
            return "OK " + idComando + " 200 ELIMINADO";
        } else {
            return "FAILED " + idComando + " 404 NO_ENCONTRADO";
        }
    }
}
