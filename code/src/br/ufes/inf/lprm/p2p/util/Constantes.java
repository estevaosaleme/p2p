package br.ufes.inf.lprm.p2p.util;

public class Constantes {
	public final static Integer PORTA = 12345;
	
	public final static byte CODIGO_NAO_ATRIBUIDO = -1;
	
	public final static byte CODIGO_ENVIO_JOIN = 0;
	public final static byte CODIGO_ENVIO_LEAVE = 1;
	public final static byte CODIGO_ENVIO_LOOKUP = 2;
	public final static byte CODIGO_ENVIO_UPDATE = 3;
	
	public final static byte CODIGO_RESPOSTA_JOIN = 64;
	public final static byte CODIGO_RESPOSTA_LEAVE = 65;
	public final static byte CODIGO_RESPOSTA_LOOKUP = 66;
	public final static byte CODIGO_RESPOSTA_UPDATE = 67;
	
}
