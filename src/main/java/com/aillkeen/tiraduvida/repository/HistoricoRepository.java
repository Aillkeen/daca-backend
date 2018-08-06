package com.aillkeen.tiraduvida.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aillkeen.tiraduvida.entity.Historico;

public interface HistoricoRepository extends MongoRepository<Historico, String>{
	
	Iterable<Historico> findByDuvidaIdOrderByDataHistoricoDesc(String duvidaId);

}
