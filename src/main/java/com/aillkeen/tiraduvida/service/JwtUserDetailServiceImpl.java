package com.aillkeen.tiraduvida.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.aillkeen.tiraduvida.entity.Usuario;
import com.aillkeen.tiraduvida.security.jwt.JwtUserFactory;

@Service
public class JwtUserDetailServiceImpl implements UserDetailsService{
	
	@Autowired
	private UsuarioService usuarioService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario = usuarioService.findByEmail(email);
		
		if(usuario == null) {
			throw new UsernameNotFoundException(String.format("Usuario com email %s nao encontrado",email));
		}else {
			return JwtUserFactory.create(usuario);
		}
	}

}
