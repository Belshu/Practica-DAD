package edu.ucam.cliente.service;

public class ResponseParser {
	private String type;
	private String numero;
	private String code;
	private String message;
	private String ip;
	private String port;
	
	public ResponseParser(String respuesta) {
		String [] chuncks = respuesta.split(" ");
		
		if(chuncks.length >= 4) {
			this.type = chuncks[0];
			this.numero = chuncks[1];
			this.code = chuncks[2];
			this.message = chuncks[3];
			
			if("PREOK".equals(type) && chuncks.length >= 5) {
				this.ip = chuncks[3];
				this.port = chuncks[4];
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
		return message;
	}

	public String getType() {
		return type;
	}

	public String getNumero() {
		return numero;
	}


	public String getCode() {
		return code;
	}


	public String getIp() {
		return ip;
	}

	public String getPort() {
		return port;
	}
}
