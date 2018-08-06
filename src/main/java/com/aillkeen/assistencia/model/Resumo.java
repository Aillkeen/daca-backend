package com.aillkeen.assistencia.model;

import java.io.Serializable;

public class Resumo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Integer qtdAberto;
	private Integer qtdAceito;
	private Integer qtdFinalizado;
	private Integer qtdAprovado;
	private Integer qtdModerado;
	private Integer qtdRuim;
	
	public Resumo() {
		
	}
	
	public Integer getQtdAberto() {
		return qtdAberto;
	}
	public void setQtdAberto(Integer qtdAberto) {
		this.qtdAberto = qtdAberto;
	}
	public Integer getQtdAceito() {
		return qtdAceito;
	}
	public void setQtdAceito(Integer qtdAceito) {
		this.qtdAceito = qtdAceito;
	}
	public Integer getQtdFinalizado() {
		return qtdFinalizado;
	}
	public void setQtdFinalizado(Integer qtdFinalizado) {
		this.qtdFinalizado = qtdFinalizado;
	}
	public Integer getQtdAprovado() {
		return qtdAprovado;
	}
	public void setQtdAprovado(Integer qtdAprovado) {
		this.qtdAprovado = qtdAprovado;
	}
	public Integer getQtdModerado() {
		return qtdModerado;
	}
	public void setQtdModerado(Integer qtdModerado) {
		this.qtdModerado = qtdModerado;
	}
	public Integer getQtdRuim() {
		return qtdRuim;
	}
	public void setQtdRuim(Integer qtdRuim) {
		this.qtdRuim = qtdRuim;
	}
	
	
	
	

}
