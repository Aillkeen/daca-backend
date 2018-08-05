package com.aillkeen.assistencia.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Assistencia {

	@Id
	private String id;
	
	@DBRef(lazy = true)
	private Usuario usuario;
	
	@DBRef(lazy = true)
	private Usuario tutor;
	
	private Date data;
	
	private Integer codigo;
	
	private Status status;
	
	private String titulo;
	
	private String descricao;
	
	private String imagem;
	
	@Transient
	private List<Historico> historico;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getTutor() {
		return tutor;
	}

	public void setTutor(Usuario tutor) {
		this.tutor = tutor;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public List<Historico> getHistorico() {
		return historico;
	}

	public void setHistorico(List<Historico> historico) {
		this.historico = historico;
	}
	
	
	
	
	
}
