package br.ufes.inf.lprm.p2p.protocol;

import br.ufes.inf.lprm.p2p.util.Helper;

public class Nodo {
	
	public Nodo(String enderecoIp){
		enderecoIpNodo = enderecoIp;
		idNodo = Helper.gerarMd5(enderecoIp);
	}
	
	private String idNodo;
	private String enderecoIpNodo;
	private String sucessorId;
	private String sucessorEnderecoIp;
	private String antecessorId;
	private String antecessorEnderecoIp;
	
	public String getIdNodo() {
		return idNodo;
	}
	
	public String getSucessorId() {
		return sucessorId;
	}
	public void setSucessorId(String sucessorId) {
		this.sucessorId = sucessorId;
	}
	public String getSucessorEnderecoIp() {
		return sucessorEnderecoIp;
	}
	public void setSucessorEnderecoIp(String sucessorEnderecoIp) {
		this.sucessorEnderecoIp = sucessorEnderecoIp;
	}
	
	public String getAntecessorId() {
		return antecessorId;
	}
	public void setAntecessorId(String antecessorId) {
		this.antecessorId = antecessorId;
	}
	public String getAntecessorEnderecoIp() {
		return antecessorEnderecoIp;
	}
	public void setAntecessorEnderecoIp(String antecessorEnderecoIp) {
		this.antecessorEnderecoIp = antecessorEnderecoIp;
	}
	
	public String getEnderecoIpNodo() {
		return enderecoIpNodo;
	}	
	
}
