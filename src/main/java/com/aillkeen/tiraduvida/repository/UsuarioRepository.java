package com.aillkeen.tiraduvida.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aillkeen.tiraduvida.entity.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
	
	Usuario findByEmail(String email);

}
