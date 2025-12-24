package edu.ucam.servidor.repositories;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class ERPDataManager {
	private final TitulacionRepository tituRepository = new TitulacionRepository();
	private final AsignaturaRepository asigRepository = new AsignaturaRepository();
	private final MatriculaRepository matRepository = new MatriculaRepository();
	
	// ---------------------------------------------- GUARDAR EN: ...\Servidor\datas
	private final String dir = System.getProperty("user.dir") + File.separator + "datas";
	private final String FILE = dir + File.separator + "server_state.dat";
	private final String TMP = dir + File.separator + "server_state.tmp";
	
	
	// ---------------------------------------------- GUARDAR DATOS
	public synchronized boolean save() {
		ServerState state = new ServerState();
		state.titulaciones = new ArrayList<>(tituRepository.list());
		state.asignaturas = new ArrayList<>(asigRepository.list());
		state.matriculas = new ArrayList<>(matRepository.list());
		
		try {
			try (ObjectOutputStream out = new ObjectOutputStream(
	                new BufferedOutputStream(new FileOutputStream(TMP)))) {
	            out.writeObject(state);
	        }
			
			// ---------------------------------------------- REEMPLAZAR ARCHIVO .TMP POR .DAT
			Files.move(Paths.get(TMP), Paths.get(FILE), 
					StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
			return true;
		} catch(Exception ex) {
			System.out.println("save (ERPDataManager): " + ex.getMessage());
		}
		
		return false;
	}
	
	
	// ---------------------------------------------- CARGAR DATOS
	public synchronized void load() {
		ServerState state = null;
		try {
			Files.createDirectories(Paths.get(dir));
			File file = new File(FILE);
			
			if(!file.exists()) {
				state = new ServerState();
			} else {
				try (ObjectInputStream ois = new ObjectInputStream(
	                    new BufferedInputStream(new FileInputStream(FILE)))) {
	                state = (ServerState) ois.readObject();
	            }
			}
		} catch(Exception ex) {
			System.out.println("load (ERPDataManager): " + ex.getMessage());
			state = new ServerState();
		} finally {
			if(state != null) {
				tituRepository.setAll(state.titulaciones);
				
				// ---------------------------------------------- AÑADIR EL RESTO
				// ---------------------------------------------- 
				// ----------------------------------------------
				// ----------------------------------------------
				// ----------------------------------------------
				// ----------------------------------------------
			}
		}
	}
	
	// ---------------------------------------------- GETTERS
	
	
	public TitulacionRepository getTitulacionRepository() {
		return tituRepository;
	}
	
	public AsignaturaRepository getAsigRepository() {
		return asigRepository;
	}
	
	public MatriculaRepository getMatRepository() {
		return matRepository;
	}
}
