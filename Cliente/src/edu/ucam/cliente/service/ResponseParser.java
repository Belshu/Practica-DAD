package edu.ucam.cliente.service;

public class ResponseParser {
	private String type, numero, codigo, mensaje, ip, puerto;
	
	/*
	 * type = OK | PREOK | FAILED
	 * numero = idComando
	 * codigo = 200: OK, 400: FAILED, 500: PREOK
	 * mensaje/ip = MENSAJE_DEL_SERVIDOR | IP
	 * puerto = PUERTO 
	 * */
	public ResponseParser(String respuesta) {
		String [] chuncks = respuesta.split(" ");
		
		if(chuncks.length >= 4) {
			this.type = chuncks[0];
			this.numero = chuncks[1];
			this.codigo = chuncks[2];
			
			if("PREOK".equals(type) && chuncks.length >= 5) {
				this.ip = chuncks[3];
				this.puerto = chuncks[4];
			} else {
				this.mensaje = chuncks[3];
			}
		}
	}
	
	
	// GETTERS
	public boolean isOK() {
		return "OK".equals(type);
	}
	
	public boolean isPREOK() {
		return "PREOK".equals(type);
	}
	
	public boolean isFAILED() {
		return "FAILED".equals(type);
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public String getMessage() {
		return mensaje;
	}
	
	public String getIp() {
		return ip;
	}

	public String getPort() {
		return puerto;
	}
}
