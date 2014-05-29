package br.ufes.inf.lprm.p2p.protocol.message;

import br.ufes.inf.lprm.p2p.util.Constantes;

public abstract class Message implements IMessage {

	private byte envioCodigoMensagem = Constantes.CODIGO_NAO_ATRIBUIDO;
	private byte respostaCodigoMensagem = Constantes.CODIGO_NAO_ATRIBUIDO;
	
	private byte[] enderecoIpOrigemMensagem = new byte[] {0,0,0,0};
	
	public byte getRespostaCodigoMensagem() {
		return respostaCodigoMensagem;
	}

	public void setRespostaCodigoMensagem(byte respostaCodigoMensagem) {
		this.respostaCodigoMensagem = respostaCodigoMensagem;
	}

	public byte getEnvioCodigoMensagem() {
		return envioCodigoMensagem;
	}

	public void setEnvioCodigoMensagem(byte envioCodigoMensagem) {
		this.envioCodigoMensagem = envioCodigoMensagem;
	}

	public byte[] getEnderecoIpOrigemMensagem() {
		return enderecoIpOrigemMensagem;
	}

	public void setEnderecoIpOrigemMensagem(byte[] ipOrigemMensagem) {
		this.enderecoIpOrigemMensagem = ipOrigemMensagem;
	}
}
