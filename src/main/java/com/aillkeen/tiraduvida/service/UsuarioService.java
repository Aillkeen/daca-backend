package com.aillkeen.tiraduvida.service;

import org.springframework.data.domain.Page;

import com.aillkeen.tiraduvida.entity.Usuario;

public interface UsuarioService {
	
	Usuario findByEmail(String email);
	
	Usuario createOrUpdate(Usuario usuario);
	
	Usuario findById(String id);
	
	void delete(String id);
	
	Page<Usuario> findAll(int page, int count );

}
