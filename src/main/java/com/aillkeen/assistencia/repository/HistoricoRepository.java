package com.aillkeen.assistencia.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aillkeen.assistencia.entity.Historico;

public interface HistoricoRepository extends MongoRepository<Historico, String>{
	
	Iterable<Historico> findByAssistenciaIdOrderByDataHistoricoDesc(String assistenciaId);

}
