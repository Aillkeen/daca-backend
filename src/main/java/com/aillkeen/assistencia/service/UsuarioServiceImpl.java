package com.aillkeen.assistencia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.aillkeen.assistencia.entity.Usuario;
import com.aillkeen.assistencia.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public Usuario findByEmail(String email) {
		return usuarioRepository.findByEmail(email);
	}

	@Override
	public Usuario createOrUpdate(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}

	@Override
	public Usuario findById(String id) {
		return usuarioRepository.findOne(id);
	}

	@Override
	public void delete(String id) {
		usuarioRepository.delete(id);
		
	}

	@Override
	public Page<Usuario> findAll(int page, int count) {
		Pageable pages = new PageRequest(page, count);
		return usuarioRepository.findAll(pages);
	}

}
