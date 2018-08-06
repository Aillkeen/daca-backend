package com.aillkeen.tiraduvida.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.aillkeen.tiraduvida.entity.Duvida;

public interface DuvidaRepository extends MongoRepository<Duvida, String>{
	
	Page<Duvida> findByUsuarioIdOrderByDataDesc(Pageable pages, String usuarioId);
	
	Page<Duvida> findByTituloIgnoreCaseContainingAndStatusOrderByDataDesc(
			String titulo, String status, Pageable pages);
	
	Page<Duvida> findByTituloIgnoreCaseContainingAndStatusAndUsuarioIdOrderByDataDesc(
			String titulo, String status, String usuarioId, Pageable pages);
	
	Page<Duvida> findByTituloIgnoreCaseContainingAndStatusAndTutorIdOrderByDataDesc(
			String titulo, String status, String tutorId, Pageable pages);

	Page<Duvida> findByCodigo(Integer codigo, Pageable pages);
}
