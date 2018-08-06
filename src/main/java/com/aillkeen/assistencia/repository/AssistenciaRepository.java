package com.aillkeen.assistencia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.aillkeen.assistencia.entity.Assistencia;

public interface AssistenciaRepository extends MongoRepository<Assistencia, String>{
	
	Page<Assistencia> findByUsuarioIdOrderByDataDesc(Pageable pages, String usuarioId);
	
	Page<Assistencia> findByTituloIgnoreCaseContainingAndStatusOrderByDataDesc(
			String titulo, String status, Pageable pages);
	
	Page<Assistencia> findByTituloIgnoreCaseContainingAndStatusAndUsuarioIdOrderByDataDesc(
			String titulo, String status, String usuarioId, Pageable pages);
	
	Page<Assistencia> findByTituloIgnoreCaseContainingAndStatusAndTutorIdOrderByDataDesc(
			String titulo, String status, String tutorId, Pageable pages);

	Page<Assistencia> findByCodigo(Integer codigo, Pageable pages);
}
