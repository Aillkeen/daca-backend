package com.aillkeen.tiraduvida.model;

import com.aillkeen.tiraduvida.entity.Usuario;

public class CurrentUser {
	
	private String token;
	private Usuario usuario;

	public CurrentUser(String token, Usuario user) {
		this.token = token;
		this.usuario = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Usuario getUser() {
		return usuario;
	}

	public void setUser(Usuario user) {
		this.usuario = user;
	}
}
