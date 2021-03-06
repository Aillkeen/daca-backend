package com.aillkeen.tiraduvida.security.jwt;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JwtUser implements UserDetails {

	private static final long serialVersionUID = 1L;

	private final String id;
	private final String email;
	private final String senha;
	private final Collection<? extends GrantedAuthority> autorizacoes;

	public JwtUser(String id, String email, String senha, Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.email = email;
		this.senha = senha;
		this.autorizacoes = authorities;
	}

	@JsonIgnore
	public String getId() {
		return id;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return autorizacoes;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
