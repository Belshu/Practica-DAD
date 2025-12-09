package edu.ucam.servidor.repositories;

public class ERPDataManager {
	private final TitulacionRepository tituRepository = new TitulacionRepository();
	private final AsignaturaRepository asigRepository = new AsignaturaRepository();
	private final MatriculaRepository matRepository = new MatriculaRepository();
	private final AlumnoRepository aluRepository = new AlumnoRepository();
	
	
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
	
	public AlumnoRepository getAluRepository() {
		return aluRepository;
	}
}
