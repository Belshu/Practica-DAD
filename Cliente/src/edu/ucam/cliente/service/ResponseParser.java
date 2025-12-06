package edu.ucam.cliente.service;

public class ResponseParser {
	private String type, numero, codigo, mensaje, ip, puerto;
	
	public ResponseParser(String respuesta) {
		String [] chuncks = respuesta.split(" ");
		
		if(chuncks.length >= 4) {
			this.type = chuncks[0];
			this.numero = chuncks[1];
			this.codigo = chuncks[2];
			this.mensaje = chuncks[3];
			
			if("PREOK".equals(type) && chuncks.length >= 5) {
				this.ip = chuncks[3];
				this.puerto = chuncks[4];
			}
		}
	}
	
	public boolean isSuccess() {
		return "OK".equals(type);
	}
	
	public boolean isPREOK() {
		return "PREOK".equals(type);
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
