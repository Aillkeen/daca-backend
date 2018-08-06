package com.aillkeen.assistencia.controller;

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

import com.aillkeen.assistencia.entity.Assistencia;
import com.aillkeen.assistencia.entity.Papel;
import com.aillkeen.assistencia.entity.Historico;
import com.aillkeen.assistencia.entity.Status;
import com.aillkeen.assistencia.entity.Usuario;
import com.aillkeen.assistencia.model.Response;
import com.aillkeen.assistencia.model.Resumo;
import com.aillkeen.assistencia.service.AssistenciaService;
import com.aillkeen.assistencia.service.UsuarioService;
import com.aillkeen.assistencia.util.JwtTokenUtil;

@RestController
@RequestMapping("/rest/assistencias")
@CrossOrigin(origins = "*")
public class AssistenciaController {


	@Autowired
	private AssistenciaService assistenciaService;
	
    @Autowired
    protected JwtTokenUtil jwtTokenUtil;
    
	@Autowired
	private UsuarioService usuarioServico;
	
	@PostMapping()
	@PreAuthorize("hasAnyRole('ALUNO')")
	public ResponseEntity<Response<Assistencia>> create(HttpServletRequest request, @RequestBody Assistencia assistencia,
			BindingResult result) {
		Response<Assistencia> response = new Response<Assistencia>();
		
		try {
			validateCreateAssistencia(assistencia, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			assistencia.setStatus(Status.getStatus("Aberto"));
			assistencia.setUsuario(userFromRequest(request));
			assistencia.setData(new Date());
			assistencia.setCodigo(generateNumber());
			Assistencia assistenciaPersistida = (Assistencia) assistenciaService.createOrUpdate(assistencia);
			response.setObjeto(assistenciaPersistida);
		} catch (Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	private void validateCreateAssistencia(Assistencia assistencia, BindingResult result) {
		if (assistencia.getTitulo() == null) {
			result.addError(new ObjectError("Assistencia", "Titulo da assistencia nao informado."));
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
	public ResponseEntity<Response<Assistencia>> update(HttpServletRequest request, @RequestBody Assistencia assistencia,
			BindingResult result) {
		Response<Assistencia> response = new Response<Assistencia>();
		try {
			validateUpdateAssistencia(assistencia, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			Assistencia assistenciaAtual = assistenciaService.findById(assistencia.getId());
			assistencia.setStatus(assistenciaAtual.getStatus());
			assistencia.setUsuario(assistenciaAtual.getUsuario());
			assistencia.setData(assistenciaAtual.getData());
			assistencia.setCodigo(assistenciaAtual.getCodigo());
			if(assistenciaAtual.getTutor() != null) {
				assistencia.setTutor(assistenciaAtual.getTutor());
			}
			Assistencia assistenciaPersistida = (Assistencia) assistenciaService.createOrUpdate(assistencia);
			response.setObjeto(assistenciaPersistida);
		} catch (Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}

	private void validateUpdateAssistencia(Assistencia assistencia, BindingResult result) {
		if (assistencia.getId() == null) {
			result.addError(new ObjectError("Assistencia", "Id da assistencia nao encontrado."));
			return;
		}
		if (assistencia.getTitulo() == null) {
			result.addError(new ObjectError("Assistencia", "Titulo da assistencia nao informado"));
			return;
		}
	}
	
	
	@GetMapping(value = "{id}")
	@PreAuthorize("hasAnyRole('ALUNO','TUTOR')")
	public ResponseEntity<Response<Assistencia>> findById(@PathVariable("id") String id) {
		Response<Assistencia> response = new Response<Assistencia>();
		Assistencia assistencia = assistenciaService.findById(id);
		if (assistencia == null) {
			response.getErros().add("Assistencia nao encontrada com id: " + id);
			return ResponseEntity.badRequest().body(response);
		}
		List<Historico> historicos = new ArrayList<Historico>();
		Iterable<Historico> historicoAtual =  assistenciaService.listHistorico(assistencia.getId());
		for (Iterator<Historico> iterator = historicoAtual.iterator(); iterator.hasNext();) {
			Historico historico = iterator.next();
			historico.setAssistencia(null);
			historicos.add(historico);
		}	
		assistencia.setHistorico(historicos);
		response.setObjeto(assistencia);
		return ResponseEntity.ok(response);
	}
	
	@DeleteMapping(value = "/{id}")
	@PreAuthorize("hasAnyRole('ALUNO')")
	public ResponseEntity<Response<String>> delete(@PathVariable("id") String id) {
		Response<String> response = new Response<String>();
		Assistencia assistencia = assistenciaService.findById(id);
		if (assistencia == null) {
			response.getErros().add("Registro nao encontrado com id: " + id);
			return ResponseEntity.badRequest().body(response);
		}
		assistenciaService.delete(id);
		return ResponseEntity.ok(new Response<String>());
	}
	
	
	@GetMapping(value = "{page}/{count}")
	@PreAuthorize("hasAnyRole('ALUNO','TUTOR')")
    public  ResponseEntity<Response<Page<Assistencia>>> findAll(HttpServletRequest request, @PathVariable int page, @PathVariable int count) {
		
		Response<Page<Assistencia>> response = new Response<Page<Assistencia>>();
		Page<Assistencia> assistencias = null;
		Usuario usuarioRequest = userFromRequest(request);
		if(usuarioRequest.getPapel().equals(Papel.ROLE_TUTOR)) {
			assistencias = assistenciaService.listAssistencia(page, count);
		} else if(usuarioRequest.getPapel().equals(Papel.ROLE_ALUNO)) {
			assistencias = assistenciaService.findByCurrentUser(page, count, usuarioRequest.getId());
		}
		response.setObjeto(assistencias);
		return ResponseEntity.ok(response);
    }
	
	@GetMapping(value = "{page}/{count}/{codigo}/{titulo}/{status}/{aceito}")
	@PreAuthorize("hasAnyRole('ALUNO','TUTOR')")
    public  ResponseEntity<Response<Page<Assistencia>>> findByParams(HttpServletRequest request, 
    		 							@PathVariable("page") int page, 
    		 							@PathVariable("count") int count,
    		 							@PathVariable("codigo") Integer codigo,
    		 							@PathVariable("titulo") String titulo,
    		 							@PathVariable("status") String status,
										@PathVariable("aceito") boolean aceito) {
		
		titulo = titulo.equals("uninformed") ? "" : titulo;
		status = status.equals("uninformed") ? "" : status;
		
		Response<Page<Assistencia>> response = new Response<Page<Assistencia>>();
		Page<Assistencia> assistencias = null;
		if(codigo > 0) {
			assistencias = assistenciaService.findByCodigo(page, count, codigo);
		} else {
			Usuario userRequest = userFromRequest(request);
			if(userRequest.getPapel().equals(Papel.ROLE_TUTOR)) {
				if(aceito) {
					assistencias = assistenciaService.findByParametersAndTutor(page, count, titulo, status.toUpperCase(), userRequest.getId());
				} else {
					assistencias = assistenciaService.findByParameters(page, count, titulo, status.toUpperCase());
				}
			} else if(userRequest.getPapel().equals(Papel.ROLE_ALUNO)) {
				assistencias = assistenciaService.findByParametersAndCurrentUser(page, count, titulo, status.toUpperCase(), userRequest.getId());
			}
		}
		response.setObjeto(assistencias);
		return ResponseEntity.ok(response);
    }
	
	@PutMapping(value = "/{id}/{status}")
	@PreAuthorize("hasAnyRole('ALUNO','TUTOR')")
	public ResponseEntity<Response<Assistencia>> changeStatus(
													@PathVariable("id") String id, 
													@PathVariable("status") String status, 
													HttpServletRequest request,  
													@RequestBody Assistencia assistencia,
													BindingResult result) {
		
		Response<Assistencia> response = new Response<Assistencia>();
		try {
			validateHistorico(id, status, result);
			if (result.hasErrors()) {
				result.getAllErrors().forEach(error -> response.getErros().add(error.getDefaultMessage()));
				return ResponseEntity.badRequest().body(response);
			}
			Assistencia assistenciaCurrent = assistenciaService.findById(id);
			assistenciaCurrent.setStatus(Status.getStatus(status));
			if(status.equals("Aceito")) {
				assistenciaCurrent.setTutor(userFromRequest(request));
			}
			Assistencia assistenciaPersistida = (Assistencia) assistenciaService.createOrUpdate(assistenciaCurrent);
			Historico changeStatus = new Historico();
			changeStatus.setUsuario(userFromRequest(request));
			changeStatus.setData(new Date());
			changeStatus.setStatus(Status.getStatus(status));
			changeStatus.setAssistencia(assistenciaPersistida);
			assistenciaService.createHistorico(changeStatus);
			response.setObjeto(assistenciaPersistida);
		} catch (Exception e) {
			response.getErros().add(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
		return ResponseEntity.ok(response);
	}
	
	private void validateHistorico(String id,String status, BindingResult result) {
		if (id == null || id.equals("")) {
			result.addError(new ObjectError("Assistencia", "Id nao encontrado"));
			return;
		}
		if (status == null || status.equals("")) {
			result.addError(new ObjectError("Assistencia", "Status nao informado"));
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
		
		Iterable<Assistencia> assistencias = assistenciaService.findAll();
		if (assistencias != null) {
			for (Iterator<Assistencia> iterator = assistencias.iterator(); iterator.hasNext();) {
				Assistencia assistencia = iterator.next();
				if(assistencia.getStatus().equals(Status.ABERTO)){
					qtdAberto ++;
				}
				if(assistencia.getStatus().equals(Status.ACEITO)){
					qtdAceito ++;
				}
				if(assistencia.getStatus().equals(Status.APROVADO)){
					qtdAprovado ++;
				}
				if(assistencia.getStatus().equals(Status.RUIM)){
					qtdRuim ++;
				}
				if(assistencia.getStatus().equals(Status.MODERADO)){
					qtdModerado ++;
				}
				if(assistencia.getStatus().equals(Status.FINALIZADO)){
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
