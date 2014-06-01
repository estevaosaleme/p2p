package br.ufes.inf.lprm.p2p.protocol;

import br.ufes.inf.lprm.p2p.util.Helper;

public class Nodo {
	
	public Nodo(String enderecoIp){
		enderecoIpNodo = enderecoIp;
		idNodo = Helper.gerarMd5(enderecoIp);
	}
	
	private int idNodo;
	private String enderecoIpNodo;
	private int sucessorId;
	private String sucessorEnderecoIp;
	private int antecessorId;
	private String antecessorEnderecoIp;
	
	public int getIdNodo() {
		return idNodo;
	}
	
	public int getSucessorId() {
		return sucessorId;
	}
	public void setSucessorId(int sucessorId) {
		this.sucessorId = sucessorId;
	}
	public String getSucessorEnderecoIp() {
		return sucessorEnderecoIp;
	}
	public void setSucessorEnderecoIp(String sucessorEnderecoIp) {
		this.sucessorEnderecoIp = sucessorEnderecoIp;
	}
	
	public int getAntecessorId() {
		return antecessorId;
	}
	public void setAntecessorId(int antecessorId) {
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
