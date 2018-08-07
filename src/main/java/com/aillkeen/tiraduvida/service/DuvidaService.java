package com.aillkeen.tiraduvida.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.aillkeen.tiraduvida.entity.Duvida;
import com.aillkeen.tiraduvida.entity.Historico;

@Component
public interface DuvidaService {

	Duvida createOrUpdate(Duvida duvida);

	Duvida findById(String id);

	void delete(String id);

	Page<Duvida> listDuvida(int page, int count);

	Historico createHistorico(Historico historico);

	Iterable<Historico> listHistorico(String duvidaId);

	Page<Duvida> findByCurrentUser(int page, int count, String usuarioId);
	
	Page<Duvida> findByParameters(int page, int count, String titulo, String status);
	
	Page<Duvida> findByParametersAndCurrentUser(int page, int count, String titulo, String status, String usuarioId);
	
	Page<Duvida> findByCodigo(int page, int count, Integer codigo);
	
	Iterable<Duvida> findAll();
	
	Page<Duvida> findByParametersAndTutor(int page, int count, String titulo, String status, String tutorId);

}
