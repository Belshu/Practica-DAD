package edu.ucam.servidor.repositories;

public class ERPDataManager {
	private final TitulacionRepository tituRepository = new TitulacionRepository();
	private final AsignaturaRepository asigRepository = new AsignaturaRepository();
	private final MatriculaRepository matRepository = new MatriculaRepository();
	
	
	// GETTERS
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
