package br.ufes.inf.lprm.p2p.protocol.message;

import br.ufes.inf.lprm.p2p.util.Constantes;

public class MessageLookup extends Message{
	
	public MessageLookup (){
		this.setEnvioCodigoMensagem(Constantes.CODIGO_ENVIO_LOOKUP);
	}
	
	private byte[] envioId;
	private byte[] envioEnderecoIp;
	private byte[] envioIdProcurado;
	
	private byte[] respostaIdProcurado;
	private byte[] respostaSucessorId;
	private byte[] respostaSucessorEnderecoIp;

	public byte[] getEnvioId() {
		return envioId;
	}

	public void setEnvioId(byte[] envioId) {
		this.envioId = envioId;
	}

	public byte[] getEnvioEnderecoIp() {
		return envioEnderecoIp;
	}

	public void setEnvioEnderecoIp(byte[] envioEnderecoIp) {
		this.envioEnderecoIp = envioEnderecoIp;
	}

	public byte[] getEnvioIdProcurado() {
		return envioIdProcurado;
	}

	public void setEnvioIdProcurado(byte[] envioIdProcurado) {
		this.envioIdProcurado = envioIdProcurado;
	}

	public byte[] getRespostaIdProcurado() {
		return respostaIdProcurado;
	}

	public void setRespostaIdProcurado(byte[] respostaIdProcurado) {
		this.respostaIdProcurado = respostaIdProcurado;
	}

	public byte[] getRespostaSucessorId() {
		return respostaSucessorId;
	}

	public void setRespostaSucessorId(byte[] respostaSucessorId) {
		this.respostaSucessorId = respostaSucessorId;
	}

	public byte[] getRespostaSucessorEnderecoIp() {
		return respostaSucessorEnderecoIp;
	}

	public void setRespostaSucessorEnderecoIp(byte[] respostaSucessorEnderecoIp) {
		this.respostaSucessorEnderecoIp = respostaSucessorEnderecoIp;
	}

	@Override
	public byte[] messageToSend(boolean server) {
		if (server)
			return new byte[]
					{
						getRespostaCodigoMensagem(),
						getRespostaIdProcurado()[0],getRespostaIdProcurado()[1],getRespostaIdProcurado()[2],getRespostaIdProcurado()[3],
						getRespostaSucessorId()[0],getRespostaSucessorId()[1],getRespostaSucessorId()[2],getRespostaSucessorId()[3],
						getRespostaSucessorEnderecoIp()[0],getRespostaSucessorEnderecoIp()[1],getRespostaSucessorEnderecoIp()[2],getRespostaSucessorEnderecoIp()[3]
					};
		else
			return new byte[]
					{
						getEnvioCodigoMensagem(),
						getEnvioId()[0],getEnvioId()[1],getEnvioId()[2],getEnvioId()[3],
						getEnvioEnderecoIp()[0],getEnvioEnderecoIp()[1],getEnvioEnderecoIp()[2],getEnvioEnderecoIp()[3],
						getEnvioIdProcurado()[0],getEnvioIdProcurado()[1],getEnvioIdProcurado()[2],getEnvioIdProcurado()[3]
					};
	}
	
	@Override
	public void getReceiveFromServer(byte[] networkMessage) {
		byte codigoMensagem = networkMessage[0];
		byte[] idProcurado = {networkMessage[1],networkMessage[2],networkMessage[3],networkMessage[4]};
		byte[] sucessorId = {networkMessage[5],networkMessage[6],networkMessage[7],networkMessage[8]};
		byte[] sucessorEnderecoIp = {networkMessage[9],networkMessage[10],networkMessage[11],networkMessage[12]};
		
		this.setRespostaCodigoMensagem(codigoMensagem);
		this.setRespostaIdProcurado(idProcurado);	
		this.setRespostaSucessorId(sucessorId);
		this.setRespostaSucessorEnderecoIp(sucessorEnderecoIp);	
	}

	@Override
	public void getReceiveFromClient(byte[] networkMessage) {
		byte codigoMensagem = networkMessage[0];
		byte[] id = {networkMessage[1],networkMessage[2],networkMessage[3],networkMessage[4]};
		byte[] enderecoIp = {networkMessage[5],networkMessage[6],networkMessage[7],networkMessage[8]};
		byte[] idProcurado = {networkMessage[9],networkMessage[10],networkMessage[11],networkMessage[12]};
		
		this.setEnvioCodigoMensagem(codigoMensagem);
		this.setEnvioId(id);
		this.setEnvioEnderecoIp(enderecoIp);
		this.setEnvioIdProcurado(idProcurado);
	}	
}
