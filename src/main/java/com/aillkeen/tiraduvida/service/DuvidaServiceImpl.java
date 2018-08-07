package com.aillkeen.tiraduvida.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.aillkeen.tiraduvida.entity.Duvida;
import com.aillkeen.tiraduvida.entity.Historico;
import com.aillkeen.tiraduvida.repository.DuvidaRepository;
import com.aillkeen.tiraduvida.repository.HistoricoRepository;

@Service
public class DuvidaServiceImpl implements DuvidaService {

	@Autowired
	private DuvidaRepository duvidaRepository;

	@Autowired
	private HistoricoRepository historicoRepository;

	@Override
	public Duvida createOrUpdate(Duvida duvida) {
		return duvidaRepository.save(duvida);
	}

	@Override
	public Duvida findById(String id) {
		return duvidaRepository.findOne(id);
	}

	@Override
	public void delete(String id) {
		duvidaRepository.delete(id);		
	}

	@Override
	public Page<Duvida> listDuvida(int page, int count) {
		Pageable pages = new PageRequest(page, count);
		return duvidaRepository.findAll(pages);
	}

	@Override
	public Historico createHistorico(Historico historico) {
		return historicoRepository.save(historico);
	}

	@Override
	public Iterable<Historico> listHistorico(String duvidaId) {
		return historicoRepository.findByDuvidaIdOrderByDataHistoricoDesc(duvidaId);
	}

	@Override
	public Page<Duvida> findByCurrentUser(int page, int count, String usuarioId) {
		Pageable pages = new PageRequest(page, count);
		return duvidaRepository.findByUsuarioIdOrderByDataDesc(pages, usuarioId)
				;
	}

	@Override
	public Page<Duvida> findByParameters(int page, int count, String titulo, String status) {

		Pageable pages = new PageRequest(page, count);
		return duvidaRepository.findByTituloIgnoreCaseContainingAndStatusOrderByDataDesc(titulo, status, pages);
	}

	@Override
	public Page<Duvida> findByParametersAndCurrentUser(int page, int count, String titulo, String status,
			String usuarioId) {
		Pageable pages = new PageRequest(page, count);

		return duvidaRepository.findByTituloIgnoreCaseContainingAndStatusAndUsuarioIdOrderByDataDesc(titulo, status, usuarioId, pages);
	}

	@Override
	public Page<Duvida> findByCodigo(int page, int count, Integer codigo) {
		Pageable pages = new PageRequest(page, count);

		return duvidaRepository.findByCodigo(codigo, pages);
	}

	@Override
	public Iterable<Duvida> findAll() {

		return duvidaRepository.findAll();
	}

	@Override
	public Page<Duvida> findByParametersAndTutor(int page, int count, String titulo, String status,
			String tutorId) {
		Pageable pages = new PageRequest(page, count);
		return duvidaRepository.findByTituloIgnoreCaseContainingAndStatusAndTutorIdOrderByDataDesc(titulo, status, tutorId, pages);
	}

}
