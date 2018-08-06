package com.aillkeen.assistencia.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aillkeen.assistencia.entity.Usuario;
import com.aillkeen.assistencia.model.Response;
import com.aillkeen.assistencia.service.UsuarioService;

@RestController
@RequestMapping("/rest/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping
	@PreAuthorize("hasAnyRole('PROFESSOR')")
	public ResponseEntity<Response<Usuario>> create(HttpServletRequest request, @RequestBody Usuario usuario,
			BindingResult result){
		Response<Usuario> response = new Response<>();
		validateUsuario(usuario,result);
		try {
			if(result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
			Usuario usuarioPersistido = (Usuario) usuarioService.createOrUpdate(usuario);
			response.setObjeto(usuarioPersistido);
		}catch(DuplicateKeyException e) {
			response.getErros().add("Esse email já está cadastrado no sistema");
			return ResponseEntity.badRequest().body(response);
		}catch(Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	private void validateUsuario(Usuario usuario, BindingResult result) {
		
		if(usuario.getEmail() == null) {
			result.addError(new ObjectError("Usuario", "Email nao informado"));
		}
	}
	
	@PutMapping()
	@PreAuthorize("hasAnyRole('PROFESSOR')")
	public ResponseEntity<Response<Usuario>> update(HttpServletRequest request, @RequestBody Usuario user,
			BindingResult result) {
		Response<Usuario> response = new Response<>();
		try {
			validateUpdate(user, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			user.setSenha(passwordEncoder.encode(user.getSenha()));
			Usuario userPersisted = (Usuario) usuarioService.createOrUpdate(user);
			response.setObjeto(userPersisted);
		} catch (Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	private void validateUpdate(Usuario user, BindingResult result) {
		if (user.getId() == null) {
			result.addError(new ObjectError("Usuario", "Usuario com id vazio"));
			return;
		}
		if (user.getEmail() == null) {
			result.addError(new ObjectError("Usuario", "Email nao informado"));
			return;
		}
	}
	
	@GetMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('PROFESSOR')")
	public ResponseEntity<Response<Usuario>> findById(@PathVariable("id") String id) {
		Response<Usuario> response = new Response<Usuario>();
		Usuario user = usuarioService.findById(id);
		if (user == null) {
			response.getErros().add("Usuario nao encontrado com id: " + id);
			return ResponseEntity.badRequest().body(response);
		}
		response.setObjeto(user);
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('PROFESSOR')")
	public ResponseEntity<Response<String>> delete(@PathVariable("id") String id) {
		Response<String> response = new Response<String>();
		Usuario user = usuarioService.findById(id);
		if (user == null) {
			response.getErros().add("Usuario nao encontrado com id: " + id);
			return ResponseEntity.badRequest().body(response);
		}
		usuarioService.delete(id);
		return ResponseEntity.ok(new Response<String>());
	}
	
	
	@GetMapping(value = "{page}/{count}")
	@PreAuthorize("hasAnyRole('PROFESSOR')")
    public  ResponseEntity<Response<Page<Usuario>>> findAll(@PathVariable int page, @PathVariable int count) {
		Response<Page<Usuario>> response = new Response<Page<Usuario>>();
		Page<Usuario> users = usuarioService.findAll(page, count);
		response.setObjeto(users);
		return ResponseEntity.ok(response);
    }
}
