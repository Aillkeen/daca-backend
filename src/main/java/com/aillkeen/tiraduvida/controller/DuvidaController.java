package com.aillkeen.tiraduvida.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.aillkeen.tiraduvida.entity.Duvida;
import com.aillkeen.tiraduvida.entity.Historico;
import com.aillkeen.tiraduvida.entity.Papel;
import com.aillkeen.tiraduvida.entity.Status;
import com.aillkeen.tiraduvida.entity.Usuario;
import com.aillkeen.tiraduvida.model.Response;
import com.aillkeen.tiraduvida.model.Resumo;
import com.aillkeen.tiraduvida.service.DuvidaService;
import com.aillkeen.tiraduvida.service.UsuarioService;
import com.aillkeen.tiraduvida.util.JwtTokenUtil;

@RestController
@RequestMapping("/rest/duvidas")
@CrossOrigin(origins = "*")
public class DuvidaController {


	@Autowired
	private DuvidaService duvidaService;
	
    @Autowired
    protected JwtTokenUtil jwtTokenUtil;
    
	@Autowired
	private UsuarioService usuarioServico;
	
	@PostMapping()
	@PreAuthorize("hasAnyRole('ALUNO')")
	public ResponseEntity<Response<Duvida>> create(HttpServletRequest request, @RequestBody Duvida duvida,
			BindingResult result) {
		Response<Duvida> response = new Response<Duvida>();
		
		try {
			validateCreateDuvida(duvida, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			duvida.setStatus(Status.getStatus("Aberto"));
			duvida.setUsuario(userFromRequest(request));
			duvida.setData(new Date());
			duvida.setCodigo(generateNumber());
			Duvida duvidaPersistida = (Duvida) duvidaService.createOrUpdate(duvida);
			response.setObjeto(duvidaPersistida);
		} catch (Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	private void validateCreateDuvida(Duvida duvida, BindingResult result) {
		if (duvida.getTitulo() == null) {
			result.addError(new ObjectError("Duvida", "Titulo da duvida nao informado."));
			return;
		}
	}
	
	public Usuario userFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String email = jwtTokenUtil.getUsernameFromToken(token);
        return usuarioServico.findByEmail(email);
    }
	
	private Integer generateNumber() {
		Random random = new Random();
		return random.nextInt(9999);
	}
	
	@PutMapping()
	@PreAuthorize("hasAnyRole('ALUNO')")
	public ResponseEntity<Response<Duvida>> update(HttpServletRequest request, @RequestBody Duvida duvida,
			BindingResult result) {
		Response<Duvida> response = new Response<Duvida>();
		try {
			validateUpdateDuvida(duvida, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			Duvida duvidaAtual = duvidaService.findById(duvida.getId());
			duvida.setStatus(duvidaAtual.getStatus());
			duvida.setUsuario(duvidaAtual.getUsuario());
			duvida.setData(duvidaAtual.getData());
			duvida.setCodigo(duvidaAtual.getCodigo());
			if(duvidaAtual.getTutor() != null) {
				duvida.setTutor(duvidaAtual.getTutor());
			}
			Duvida duvidaPersistida = (Duvida) duvidaService.createOrUpdate(duvida);
			response.setObjeto(duvidaPersistida);
		} catch (Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	private void validateUpdateDuvida(Duvida duvida, BindingResult result) {
		if (duvida.getId() == null) {
			result.addError(new ObjectError("Duvida", "Id da duvida nao encontrado."));
			return;
		}
		if (duvida.getTitulo() == null) {
			result.addError(new ObjectError("Duvida", "Titulo da duvida nao informado"));
			return;
		}
	}
	
	
	@GetMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('ALUNO','TUTOR')")
	public ResponseEntity<Response<Duvida>> findById(@PathVariable("id") String id) {
		Response<Duvida> response = new Response<Duvida>();
		Duvida duvida = duvidaService.findById(id);
		if (duvida == null) {
			response.getErros().add("Duvida nao encontrada com id: " + id);
			return ResponseEntity.badRequest().body(response);
		}
		List<Historico> historicos = new ArrayList<Historico>();
		Iterable<Historico> historicoAtual =  duvidaService.listHistorico(duvida.getId());
		for (Iterator<Historico> iterator = historicoAtual.iterator(); iterator.hasNext();) {
			Historico historico = iterator.next();
			historico.setDuvida(null);
			historicos.add(historico);
		}	
		duvida.setHistoricos(historicos);
		response.setObjeto(duvida);
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ALUNO')")
	public ResponseEntity<Response<String>> delete(@PathVariable("id") String id) {
		Response<String> response = new Response<String>();
		Duvida duvida = duvidaService.findById(id);
		if (duvida == null) {
			response.getErros().add("Registro nao encontrado com id: " + id);
			return ResponseEntity.badRequest().body(response);
		}
		duvidaService.delete(id);
		return ResponseEntity.ok(new Response<String>());
	}
	
	
	@GetMapping(value = "{page}/{count}")
	@PreAuthorize("hasAnyRole('ALUNO','TUTOR')")
    public  ResponseEntity<Response<Page<Duvida>>> findAll(HttpServletRequest request, @PathVariable int page, @PathVariable int count) {
		
		Response<Page<Duvida>> response = new Response<Page<Duvida>>();
		Page<Duvida> duvidas = null;
		Usuario usuarioRequest = userFromRequest(request);
		if(usuarioRequest.getPapel().equals(Papel.ROLE_TUTOR)) {
			duvidas = duvidaService.listDuvida(page, count);
		} else if(usuarioRequest.getPapel().equals(Papel.ROLE_ALUNO)) {
			duvidas = duvidaService.findByCurrentUser(page, count, usuarioRequest.getId());
		}
		response.setObjeto(duvidas);
		return ResponseEntity.ok(response);
    }
	
	@GetMapping(value = "{page}/{count}/{codigo}/{titulo}/{status}/{aceito}")
	@PreAuthorize("hasAnyRole('ALUNO','TUTOR')")
    public  ResponseEntity<Response<Page<Duvida>>> findByParams(HttpServletRequest request, 
    		 							@PathVariable("page") int page, 
    		 							@PathVariable("count") int count,
    		 							@PathVariable("codigo") Integer codigo,
    		 							@PathVariable("titulo") String titulo,
    		 							@PathVariable("status") String status,
										@PathVariable("aceito") boolean aceito) {
		
		titulo = titulo.equals("uninformed") ? "" : titulo;
		status = status.equals("uninformed") ? "" : status;
		
		Response<Page<Duvida>> response = new Response<Page<Duvida>>();
		Page<Duvida> duvidas = null;
		if(codigo > 0) {
			duvidas = duvidaService.findByCodigo(page, count, codigo);
		} else {
			Usuario userRequest = userFromRequest(request);
			if(userRequest.getPapel().equals(Papel.ROLE_TUTOR)) {
				if(aceito) {
					duvidas = duvidaService.findByParametersAndTutor(page, count, titulo, status.toUpperCase(), userRequest.getId());
				} else {
					duvidas = duvidaService.findByParameters(page, count, titulo, status.toUpperCase());
				}
			} else if(userRequest.getPapel().equals(Papel.ROLE_ALUNO)) {
				duvidas = duvidaService.findByParametersAndCurrentUser(page, count, titulo, status.toUpperCase(), userRequest.getId());
			}
		}
		response.setObjeto(duvidas);
		return ResponseEntity.ok(response);
    }
	
	@PutMapping(value = "/{id}/{status}")
	@PreAuthorize("hasAnyRole('ALUNO','TUTOR')")
	public ResponseEntity<Response<Duvida>> changeStatus(
													@PathVariable("id") String id, 
													@PathVariable("status") String status, 
													HttpServletRequest request,  
													@RequestBody Duvida duvida,
													BindingResult result) {
		
		Response<Duvida> response = new Response<Duvida>();
		try {
			validateHistorico(id, status, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			Duvida duvidaCurrent = duvidaService.findById(id);
			duvidaCurrent.setStatus(Status.getStatus(status));
			if(status.equals("Aceito")) {
				duvidaCurrent.setTutor(userFromRequest(request));
			}
			Duvida duvidaPersistida = (Duvida) duvidaService.createOrUpdate(duvidaCurrent);
			Historico historico = new Historico();
			historico.setUsuario(userFromRequest(request));
			historico.setData(new Date());
			historico.setStatus(Status.getStatus(status));
			historico.setDuvida(duvidaPersistida);
			duvidaService.createHistorico(historico);
			response.setObjeto(duvidaPersistida);
		} catch (Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	private void validateHistorico(String id,String status, BindingResult result) {
		if (id == null || id.equals("")) {
			result.addError(new ObjectError("Duvida", "Id nao encontrado"));
			return;
		}
		if (status == null || status.equals("")) {
			result.addError(new ObjectError("Duvida", "Status nao informado"));
			return;
		}
	}
	
	@GetMapping(value = "/resumo")
	public ResponseEntity<Response<Resumo>> findChart() {
		
		Response<Resumo> response = new Response<Resumo>();
		Resumo resumo = new Resumo();
		Integer qtdAberto = 0;
		Integer qtdAceito = 0;
		Integer qtdFinalizado = 0;
		Integer qtdAprovado = 0;
		Integer qtdModerado = 0;
		Integer qtdRuim = 0;
		
		Iterable<Duvida> duvidas = duvidaService.findAll();
		if (duvidas != null) {
			for (Iterator<Duvida> iterator = duvidas.iterator(); iterator.hasNext();) {
				Duvida duvida = iterator.next();
				if(duvida.getStatus().equals(Status.ABERTO)){
					qtdAberto ++;
				}
				if(duvida.getStatus().equals(Status.ACEITO)){
					qtdAceito ++;
				}
				if(duvida.getStatus().equals(Status.APROVADO)){
					qtdAprovado ++;
				}
				if(duvida.getStatus().equals(Status.RUIM)){
					qtdRuim ++;
				}
				if(duvida.getStatus().equals(Status.MODERADO)){
					qtdModerado ++;
				}
				if(duvida.getStatus().equals(Status.FINALIZADO)){
					qtdFinalizado ++;
				}
			}	
		}
		resumo.setQtdAberto(qtdAberto);
		resumo.setQtdAceito(qtdAceito);
		resumo.setQtdFinalizado(qtdFinalizado);
		resumo.setQtdAprovado(qtdAprovado);
		resumo.setQtdModerado(qtdModerado);
		resumo.setQtdRuim(qtdRuim);
		
		response.setObjeto(resumo);
		return ResponseEntity.ok(response);
	}
}
