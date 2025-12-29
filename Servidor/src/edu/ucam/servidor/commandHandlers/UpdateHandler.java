package edu.ucam.servidor.commandHandlers;

import java.util.Hashtable;

import edu.ucam.domain.Asignatura;
import edu.ucam.domain.Matricula;
import edu.ucam.domain.Titulacion;
import edu.ucam.servidor.interfaces.ICommandHandler;
import edu.ucam.servidor.repositories.ERPDataManager;

public class UpdateHandler implements ICommandHandler {

    private final ERPDataManager data;
    private Object model; // el objeto recibido por canal de datos

    public UpdateHandler(ERPDataManager data) {
        this.data = data;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    @Override
    public Object handle(String idComando, String[] partes) {

        if (partes.length < 3) {
            return "FAILED " + idComando + " 400 COMANDO_INCOMPLETO";
        }

        String comando = partes[1].toUpperCase();
        String id = partes[2];

        switch (comando) {

            case "UPDATETIT":
                if (!(model instanceof Titulacion nuevaTit)) {
                    return "FAILED " + idComando + " 400 OBJETO_INVALIDO";
                }

                Titulacion tit = data.getTitulacionRepository().get(id);
                if (tit == null) {
                    return "FAILED " + idComando + " 404 NO_ENCONTRADO";
                }

                tit.setNombre(nuevaTit.getNombre());
                return "OK " + idComando + " 200 ACTUALIZADO";

            case "UPDATEASIG":
                if (!(model instanceof Asignatura nuevaAsig)) {
                    return "FAILED " + idComando + " 400 OBJETO_INVALIDO";
                }

                Asignatura asig = data.getAsigRepository().get(id);
                if (asig == null) {
                    return "FAILED " + idComando + " 404 NO_ENCONTRADO";
                }

                asig.setNombre(nuevaAsig.getNombre());
                asig.setCreditos(nuevaAsig.getCreditos());
                return "OK " + idComando + " 200 ACTUALIZADO";

            case "UPDATEMATRICULA":
                if (!(model instanceof Matricula nuevaMat)) {
                    return "FAILED " + idComando + " 400 OBJETO_INVALIDO";
                }

                Matricula mat = data.getMatRepository().get(id);
                if (mat == null) {
                    return "FAILED " + idComando + " 404 NO_ENCONTRADO";
                }
                
                // Convertir Collection -> Hashtable 
                Hashtable<String, Asignatura> tabla = new Hashtable<>(); 
                for (Asignatura a : nuevaMat.getAsignaturas()) { 
                	tabla.put(a.getId(), a);
                	}
                mat.setAlumno(nuevaMat.getAlumno());
                mat.setAsignaturas(tabla);
                return "OK " + idComando + " 200 ACTUALIZADO";

            default:
                return "FAILED " + idComando + " 400 COMANDO_UPDATE_NO_VALIDO";
        }
    }
}
