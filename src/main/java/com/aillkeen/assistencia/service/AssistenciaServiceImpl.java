package com.aillkeen.assistencia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.aillkeen.assistencia.entity.Assistencia;
import com.aillkeen.assistencia.entity.Historico;
import com.aillkeen.assistencia.repository.AssistenciaRepository;
import com.aillkeen.assistencia.repository.HistoricoRepository;

@Service
public class AssistenciaServiceImpl implements AssistenciaService {

	@Autowired
	private AssistenciaRepository assistenciaRepository;

	@Autowired
	private HistoricoRepository historicoRepository;

	@Override
	public Assistencia createOrUpdate(Assistencia assistencia) {
		return assistenciaRepository.save(assistencia);
	}

	@Override
	public Assistencia findById(String id) {
		return assistenciaRepository.findOne(id);
	}

	@Override
	public void delete(String id) {
		assistenciaRepository.delete(id);		
	}

	@Override
	public Page<Assistencia> listAssistencia(int page, int count) {
		Pageable pages = new PageRequest(page, count);
		return assistenciaRepository.findAll(pages);
	}

	@Override
	public Historico createHistorico(Historico historico) {
		return historicoRepository.save(historico);
	}

	@Override
	public Iterable<Historico> listHistorico(String assistenciaId) {
		return historicoRepository.findByAssistenciaIdOrderByDataHistoricoDesc(assistenciaId);
	}

	@Override
	public Page<Assistencia> findByCurrentUser(int page, int count, String usuarioId) {
		Pageable pages = new PageRequest(page, count);
		return assistenciaRepository.findByUsuarioIdOrderByDataDesc(pages, usuarioId)
				;
	}

	@Override
	public Page<Assistencia> findByParameters(int page, int count, String titulo, String status) {

		Pageable pages = new PageRequest(page, count);
		return assistenciaRepository.findByTituloIgnoreCaseContainingAndStatusOrderByDataDesc(titulo, status, pages);
	}

	@Override
	public Page<Assistencia> findByParametersAndCurrentUser(int page, int count, String titulo, String status,
			String usuarioId) {
		Pageable pages = new PageRequest(page, count);

		return assistenciaRepository.findByTituloIgnoreCaseContainingAndStatusAndUsuarioIdOrderByDataDesc(titulo, status, usuarioId, pages);
	}

	@Override
	public Page<Assistencia> findByCodigo(int page, int count, Integer codigo) {
		Pageable pages = new PageRequest(page, count);

		return assistenciaRepository.findByCodigo(codigo, pages);
	}

	@Override
	public Iterable<Assistencia> findAll() {

		return assistenciaRepository.findAll();
	}

	@Override
	public Page<Assistencia> findByParametersAndTutor(int page, int count, String titulo, String status,
			String tutorId) {
		Pageable pages = new PageRequest(page, count);
		return assistenciaRepository.findByTituloIgnoreCaseContainingAndStatusAndTutorIdOrderByDataDesc(titulo, status, tutorId, pages);
	}

}
