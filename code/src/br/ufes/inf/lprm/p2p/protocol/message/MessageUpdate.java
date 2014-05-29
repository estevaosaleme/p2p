package br.ufes.inf.lprm.p2p.protocol.message;

import br.ufes.inf.lprm.p2p.util.Constantes;

public class MessageUpdate extends Message{
	
	public MessageUpdate (){
		this.setEnvioCodigoMensagem(Constantes.CODIGO_ENVIO_UPDATE);
	}
	
	private byte[] envioId;
	private byte[] envioSucessorId;
	private byte[] envioSucessorEnderecoIp;
	
	private byte[] respostaId;
	

	public byte[] getEnvioId() {
		return envioId;
	}

	public void setEnvioId(byte[] envioId) {
		this.envioId = envioId;
	}

	public byte[] getEnvioSucessorId() {
		return envioSucessorId;
	}

	public void setEnvioSucessorId(byte[] envioSucessorId) {
		this.envioSucessorId = envioSucessorId;
	}

	public byte[] getEnvioSucessorEnderecoIp() {
		return envioSucessorEnderecoIp;
	}

	public void setEnvioSucessorEnderecoIp(byte[] envioSucessorEnderecoIp) {
		this.envioSucessorEnderecoIp = envioSucessorEnderecoIp;
	}

	public byte[] getRespostaId() {
		return respostaId;
	}

	public void setRespostaId(byte[] respostaId) {
		this.respostaId = respostaId;
	}

	@Override
	public byte[] messageToSend(boolean server) {
		if (server)
			return new byte[]
					{
						getRespostaCodigoMensagem(),
						getRespostaId()[0],getRespostaId()[1],getRespostaId()[2],getRespostaId()[3]
					};
		else
			return new byte[]
					{
						getEnvioCodigoMensagem(),
						getEnvioId()[0],getEnvioId()[1],getEnvioId()[2],getEnvioId()[3],
						getEnvioSucessorId()[0],getEnvioSucessorId()[1],getEnvioSucessorId()[2],getEnvioSucessorId()[3],
						getEnvioSucessorEnderecoIp()[0],getEnvioSucessorEnderecoIp()[1],getEnvioSucessorEnderecoIp()[2],getEnvioSucessorEnderecoIp()[3],
					};
	}
	
	@Override
	public void getReceiveFromServer(byte[] networkMessage) {
		byte codigoMensagem = networkMessage[0];
		byte[] idOrigem = {networkMessage[1],networkMessage[2],networkMessage[3],networkMessage[4]};

		this.setRespostaCodigoMensagem(codigoMensagem);
		this.setRespostaId(idOrigem);	
	}

	@Override
	public void getReceiveFromClient(byte[] networkMessage) {
		byte codigoMensagem = networkMessage[0];
		byte[] id = {networkMessage[1],networkMessage[2],networkMessage[3],networkMessage[4]};
		byte[] sucessorId = {networkMessage[5],networkMessage[6],networkMessage[7],networkMessage[8]};
		byte[] sucessorEnderecoIp = {networkMessage[9],networkMessage[10],networkMessage[11],networkMessage[12]};

		this.setEnvioCodigoMensagem(codigoMensagem);
		this.setEnvioId(id);
		this.setEnvioSucessorId(sucessorId);
		this.setEnvioSucessorEnderecoIp(sucessorEnderecoIp);
	}	
}
