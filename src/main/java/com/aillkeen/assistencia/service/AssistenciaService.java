package com.aillkeen.assistencia.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.aillkeen.assistencia.entity.Assistencia;
import com.aillkeen.assistencia.entity.Historico;

@Component
public interface AssistenciaService {

	Assistencia createOrUpdate(Assistencia assistencia);

	Assistencia findById(String id);

	void delete(String id);

	Page<Assistencia> listAssistencia(int page, int count);

	Historico createHistorico(Historico historico);

	Iterable<Historico> listHistorico(String assistenciaId);

	Page<Assistencia> findByCurrentUser(int page, int count, String usuarioId);
	
	Page<Assistencia> findByParameters(int page, int count, String titulo, String status);
	
	Page<Assistencia> findByParametersAndCurrentUser(int page, int count, String titulo, String status, String usuarioId);
	
	Page<Assistencia> findByCodigo(int page, int count, Integer codigo);
	
	Iterable<Assistencia> findAll();
	
	Page<Assistencia> findByParametersAndTutor(int page, int count, String titulo, String status, String tutorId);

}
