package com.aillkeen.tiraduvida.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Historico {

	@Id
	private String id;
	
	@DBRef(lazy = true)
	private Duvida duvida;
	
	@DBRef(lazy = true)
	private Usuario usuario;
	
	private Date dataHistorico;
	
	private Status status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Duvida getDuvida() {
		return duvida;
	}

	public void setDuvida(Duvida duvida) {
		this.duvida = duvida;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getData() {
		return dataHistorico;
	}

	public void setData(Date data) {
		this.dataHistorico = data;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
}
