package edu.ucam.cliente;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import edu.ucam.cliente.interfaces.IAuthentication;
import edu.ucam.cliente.interfaces.IChannelData;
import edu.ucam.cliente.interfaces.ICommunicationServer;
import edu.ucam.cliente.interfaces.IRepository;

import edu.ucam.cliente.service.AuthenticationService;
import edu.ucam.cliente.service.ChannelData;
import edu.ucam.cliente.service.CommunicationSocket;
import edu.ucam.cliente.service.ResponseParser;
import edu.ucam.cliente.service.repositories.*;

import edu.ucam.domain.Alumno;
import edu.ucam.domain.Asignatura;
import edu.ucam.domain.Matricula;
import edu.ucam.domain.Titulacion;

public class ClienteERP {
	private final ICommunicationServer comunicacion;
	private final IAuthentication autenticacion;
	private final IRepository<Asignatura> asigRepo;
	private final IRepository<Titulacion> tituRepo;
	private final IRepository<Matricula> matRepo;
	
	private String respuestaServidor = "";
	
	// ---------------------------------------------- CONEXIÓN SOCKET CON SERVIDOR Y AUTENTICAR
	public ClienteERP() throws IOException{
		this.comunicacion = new CommunicationSocket();
		this.comunicacion.connectar();
	
		IChannelData channelData = new ChannelData();
		this.autenticacion = new AuthenticationService(this.comunicacion);
		
		
		// ---------------------------------------------- REPOSITORIOS
		this.asigRepo = new SubjectRepository(comunicacion, channelData);
		this.matRepo = new MatRepository(comunicacion, channelData, asigRepo);
		this.tituRepo = new TituRepository(comunicacion, channelData, asigRepo, matRepo);
	}
	
	
	// ---------------------------------------------- MÉTODOS
	
	public boolean autenticar(String usuario, String password) throws IOException {
		return autenticacion.autenticar(usuario, password);
	}
	
	public void cerrarSesion() throws IOException {
		System.out.println("CERRANDO SESION...");
		respuestaServidor = "CERRANDO SESION...";
		autenticacion.cerrarSesion();
	}
	
	
	// ---------------------------------------------- GESTOR DE COMANDOS
	public void ejecutarComando(String mensaje) {
		
		if(mensaje.isEmpty()) return;
		
		String [] partes = mensaje.trim().split(" ");
		String comando = partes[0].toUpperCase(); // ADD, GET, COUNT...
		
		try {
			if(comando.startsWith("ADD")) gestionarAdd(comando, partes);
			else if(comando.startsWith("GET")) gestionarGet(comando, partes);
			else if (comando.startsWith("LIST")) gestionarList(comando);
			else if (comando.startsWith("REMOVE")) gestionarRemove(comando, partes);
			else if (comando.startsWith("UPDATE")) gestionarUpdate(comando, partes);
			else if(comando.startsWith("COUNT")) gestionarCount(comando); 			
			else if(comando.equalsIgnoreCase("SESIONES")) imprimirSesiones(mensaje);
			else if(comando.equalsIgnoreCase("EXIT")) autenticacion.cerrarSesion();
			else if(comando.equalsIgnoreCase("SAVE")) guardarInformacion(mensaje);
			else if(comando.equalsIgnoreCase("USER") || comando.equalsIgnoreCase("PASS")){
				String respuesta = comunicacion.enviarComando(mensaje);
				ResponseParser parser = new ResponseParser(respuesta);
				
				if(parser.isOK()) {
					System.out.println(parser.getMessage());
					respuestaServidor = parser.getMessage();
				}
				else if(parser.isFAILED()) {
					System.out.println("ERROR: " + parser.getMessage());
					respuestaServidor = parser.getMessage();
				}
				else {
					System.out.println("ERROR INESPERADO!");
					respuestaServidor = "ERROR INESPERADO!";
					return;
				}
			}
			else respuestaServidor = "";
		
		} catch(IOException ex) {
			System.out.println("ejecutarComando (ClienteERP): " + ex.getMessage());
		}
	}
	
	// ---------------------------------------------- GESTOR DEL COMANDO ADD
	private void gestionarAdd(String comando, String [] partes) {
		Scanner S = new Scanner(System.in);
		
		if(partes.length < 2) {
			respuestaServidor = "COMANDO INCOMPLETO!";
			return;
		}
		
		String idObjeto = partes[1];
		try {
			switch(comando) {
				case "ADDTIT":
					Titulacion t = tituRepo.crearObjeto(idObjeto);
					respuestaServidor = tituRepo.add(idObjeto, t);
				break;
				
				case "ADDASIG": 
					Asignatura a = asigRepo.crearObjeto(idObjeto); 
					respuestaServidor = asigRepo.add(idObjeto, a);
				break;
				
				case "ADDMATRICULA":
					Matricula m = matRepo.crearObjeto(idObjeto);
					respuestaServidor = matRepo.add(idObjeto, m);
				break;	
				default:
					respuestaServidor = "COMANDO NO RECONOCIDO";
			}
		} catch(Exception ex) {
			System.out.println("gestionarAdd (ClienteERP): " + ex.getMessage());
		}
	}
	
	// ---------------------------------------------- GESTOR DEL COMANDO GET
	private void gestionarGet(String comando, String [] partes) {
		
		if(partes.length < 2) {
			respuestaServidor = "COMANDO INCOMPLETO!";
			return;
		}
		
		try {
			switch(comando) {
				case "GETTIT":
					Titulacion t = tituRepo.getModel(partes[1]);
					if(t != null) {
						StringBuilder matriculas = new StringBuilder();
						t.getMatriculas().forEach(ma -> matriculas.append(" > " + ma.getId() + " | " + ma.getAlumno().getDni() + "\n\t\t"));
						
						respuestaServidor = "\n\t>> ID: " + t.getId() + "\t>> NOMBRE: " + t.getNombre() + 
								"\n\t>> MATRICULAS: \n\t\t" + matriculas.toString();
					} else {
						respuestaServidor = "TITULACION NO ENCONTRADA -> " + partes[1] + "\n";
					}
				break;
				
				case "GETASIG": 
					Asignatura a = asigRepo.getModel(partes[1]); 
					if(a != null) {
						respuestaServidor = "\n\t>> ID: " + a.getId() + "\t>> NOMBRE: " + a.getNombre() + "\t>> CREDITOS: " + a.getCreditos();
					} else {
						respuestaServidor = "ASIGNATURA NO ENCONTRADA -> " + partes[1] + "\n";
					}
				break;
				
				case "GETMATRICULA":
					Matricula m = matRepo.getModel(partes[1]); 
					if (m != null) { System.out.println("\n>> ID: " + m.getId());
					System.out.println();
					
					StringBuilder asignaturas = new StringBuilder();
					m.getAsignaturas().forEach(as -> asignaturas.append(" > " + as.getNombre() + "\n\t"));
					
					respuestaServidor = "\n\t>> ALUMNO: " + m.getAlumno().getNombre() + " " + m.getAlumno().getApellidos() + " | "
							+ ">> ASIGNATURAS MATRICULADAS: \n\t" + asignaturas.toString();
					} else {
						System.out.println("MATRICULA NO ENCONTRADA -> " + partes[1] + "\n");
					}
				break;
				
				default:
					respuestaServidor = "COMANDO NO RECONOCIDO";
			}
		} catch(IOException ex) {
			System.out.println("gestionarGet (ClienteERP): " + ex.getMessage());
		} catch (ClassNotFoundException ex) {
			System.out.println("gestionarGet (ClienteERP): " + ex.getMessage());
		}
	}
	
	// ---------------------------------------------- GESTOR DEL COMANDO COUNT
	private void gestionarCount(String comando) {
		int total = -1;
		String cantidadMsg = null;
		switch(comando) {
			case "COUNTTIT":
				total =  tituRepo.modelSize();
				cantidadMsg = "CANTIDAD DE TITULACIONES -> ";
				
			break;
			case "COUNTASIG":
				total = asigRepo.modelSize(); 
				cantidadMsg = "CANTIDAD DE ASIGNATURAS -> ";
			break;
				
			case "COUNTMATRICULA": 
				total = matRepo.modelSize();
				cantidadMsg = "CANTIDAD DE MATRICULAS -> ";
			break;
			
			default:
				respuestaServidor = "COMANDO NO RECONOCIDO\n";
		}
		
		if(total != -1 && cantidadMsg != null) {
			respuestaServidor = cantidadMsg + " " + total + "\n";
		}
	}
	
	// ---------------------------------------------- GESTOR DEL COMANDO LIST
	private void gestionarList(String comando) {
		StringBuilder sb = new StringBuilder();
			
	    try {
	    	switch (comando) {
	    		case "LISTTIT":
	    			List<Titulacion> lt = tituRepo.list();
	    			if(lt == null) {
	    				respuestaServidor = "ERROR al inicializar las titulaciones";
	    				return;
	    			}
	    			lt.forEach(t -> sb.append(">> " + t.getId() + " - " + t.getNombre() + "\n\t"));
		            
	    		break;
	    		case "LISTASIG":
	    			List<Asignatura> la = asigRepo.list();
	    			if(la == null) {
	    				respuestaServidor = "ERROR al inicializar las asignaturas";	
	    				return;
	    			}
	    			la.forEach(a -> sb.append(">> " + a.getId() + " - " + a.getNombre() + "\n\t"));
	    		break;
	    		
	    		case "LISTMATRICULA":
	    			List<Matricula> lm = matRepo.list();
	    			if(lm == null) {    	
	    				respuestaServidor = "ERROR al inicializar las matriculas";
	    				return;
	    			}
	    			lm.forEach(m -> sb.append(">> " + m.getId() + " - " + m.getAlumno().getNombre() + 
	    					" (" + m.getAlumno().getDni() + ")" + "\n\t"));
	    		break;
	    		default:
	    			System.out.println("COMANDO NO RECONOCIDO");
	    	}
	    	
	    	if(!sb.isEmpty()) respuestaServidor = "\n\t" + sb.toString();
	    } catch (Exception ex) {
	    	System.out.println("gestionarList (ClienteERP): " + ex.getMessage());
	    }
	}
	
	// ---------------------------------------------- GESTOR DEL COMANDO REMOVE
	private void gestionarRemove(String comando, String[] partes) {
		if (partes.length < 2) {
			System.out.println("COMANDO INCOMPLETO");
			return;
		}
		
		try {
			switch (comando) {
				case "REMOVETIT":
					tituRepo.delete(partes[1]);
					respuestaServidor = "TITULACION ELIMINADA \n";
				break;
				
				case "REMOVEASIG":
					asigRepo.delete(partes[1]);
					respuestaServidor = "ASIGNATURA ELIMINADA \n";
				break;
				
				case "REMOVEMATRICULA":
					matRepo.delete(partes[1]);
					respuestaServidor = "MATRICULA ELIMINADA \n";
				break;
				
				default:
					respuestaServidor = "COMANDO NO RECONOCIDO\n";
			}
		} catch (Exception ex) {
			System.out.println("gestionarRemove (ClienteERP): " + ex.getMessage());
		}
	}

		// ---------------------------------------------- GESTOR DEL COMANDO UPDATE
		private void gestionarUpdate(String comando, String[] partes) {
		    Scanner S = new Scanner(System.in);

		    if (partes.length < 2) {
		        System.out.println("COMANDO INCOMPLETO\n");
		        return;
		    }

		    String id = partes[1];

		    try {
		        switch (comando) {
		            case "UPDATETIT":
		                Titulacion t = tituRepo.crearObjeto(S, id);
		                tituRepo.update(id, t);
		            break;

		            case "UPDATEASIG":
		                Asignatura a = asigRepo.crearObjeto(S, id);
		                asigRepo.update(id, a);
		            break;

		            case "UPDATEMATRICULA":
		                Matricula m = matRepo.crearObjeto(S, id);
		                matRepo.update(id, m);
		            break;

		            default:
		                System.out.println("COMANDO NO RECONOCIDO\n");
		        }
		    } catch (Exception ex) {
		        System.out.println("gestionarUpdate: " + ex.getMessage());
		    }
		}
	
	// ---------------------------------------------- GESTOR DEL COMANDO SESIONES
	private void imprimirSesiones(String mensaje) {
		try {
			respuestaServidor = comunicacion.enviarComando(mensaje);
			
			if(respuestaServidor == null) {
				System.out.println("Sin respuesta por parte del servidor.\n");
				
				return;
			}
			
			ResponseParser parser = new ResponseParser(respuestaServidor);
			if(parser.isOK()) {
				respuestaServidor = "SESIONES ACTIVAS -> " + parser.getMessage() + "\n";
			} else if(parser.isFAILED()){
				respuestaServidor = "ERROR SESIONES -> " + parser.getMessage() + "\n";
			} else {
				respuestaServidor = "RESPUESTA DESCONOCIDA" + "\n";
			}
			
		} catch (IOException e) {
			System.out.println("imprimirSesiones (ClienteERP): " + e.getMessage());
		}
	}
	
	
	// ---------------------------------------------- GUARDAR INFORMACION DEL SERVIDOR
	private void guardarInformacion(String mensaje) {
		try {
			respuestaServidor = comunicacion.enviarComando(mensaje);
			
			if(respuestaServidor == null) {
				System.out.println("Sin respuesta por parte del servidor.\n");
				
				return;
			}
			
			ResponseParser parser = new ResponseParser(respuestaServidor);
			
			System.out.println(respuestaServidor);
			
			if(parser.isOK()) {
				respuestaServidor = parser.getMessage();
			} else if(parser.isFAILED()){
				respuestaServidor = "ERROR -> " + parser.getMessage() + "\n";
			} else {
				respuestaServidor = "RESPUESTA DESCONOCIDA" + "\n";
			}
		} catch(IOException ex) {
			System.out.println("imprimirSesiones (ClienteERP): " + ex.getMessage());
		}
	}
	
	public int getIdComando() {
		return comunicacion.getIdComunicacion();
	}
	
	public String getRespuestaServidor() {
		return respuestaServidor;
	}
}
