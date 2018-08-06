package com.aillkeen.assistencia.model;

import java.util.ArrayList;
import java.util.List;

public class Response<T> {


	private T objeto;
	private List<String> erros;
	
	public T getObjeto() {
		return objeto;
	}
	
	public void setObjeto(T objeto) {
		this.objeto = objeto;
	}
	
	public List<String> getErros() {
		if(this.erros == null) {
			this.erros = new ArrayList<String>();
		}
		return erros;
	}
	
	public void setErros(List<String> erros) {
		this.erros = erros;
	}

}
