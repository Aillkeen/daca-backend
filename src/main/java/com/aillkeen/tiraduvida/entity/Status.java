package com.aillkeen.tiraduvida.entity;

public enum Status {

	ABERTO,
	ACEITO,
	FINALIZADO,
	APROVADO,
	MODERADO,
	RUIM;

	Status(){

	}

	public static Status getStatus(String status) {
		
		switch(status.trim().toLowerCase()) {
			case "aberto": return ABERTO;
			case "aceito": return ACEITO;
			case "finalizado": return FINALIZADO;
			case "aprovado": return APROVADO;
			case "moderado": return MODERADO;
			case "ruim": return RUIM;
			default: return ABERTO;
		}
	}




}
