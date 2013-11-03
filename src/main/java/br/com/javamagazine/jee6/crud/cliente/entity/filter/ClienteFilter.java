package br.com.javamagazine.jee6.crud.cliente.entity.filter;

import java.io.Serializable;

public final class ClienteFilter implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String nome;
	
	public ClienteFilter(String nome) {
		this.nome = nome;
	}
	
	public String getNome() {
		return nome;
	}

}
