package com.aillkeen.assistencia.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aillkeen.assistencia.entity.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {
	
	Usuario findByEmail(String email);

}
