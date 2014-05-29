package br.ufes.inf.lprm.p2p.protocol.message;

import br.ufes.inf.lprm.p2p.util.Constantes;

public class MessageLeave extends Message{
	
	public MessageLeave (){
		this.setEnvioCodigoMensagem(Constantes.CODIGO_ENVIO_LEAVE);
	}
	
	private byte[] envioId;
	private byte[] envioSucessorId;
	private byte[] envioSucessorEnderecoIp;
	private byte[] envioAntecessorId;
	private byte[] envioAntecessorEnderecoIp;
	
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

	public byte[] getEnvioAntecessorId() {
		return envioAntecessorId;
	}

	public void setEnvioAntecessorId(byte[] envioAntecessorId) {
		this.envioAntecessorId = envioAntecessorId;
	}

	public byte[] getEnvioAntecessorEnderecoIp() {
		return envioAntecessorEnderecoIp;
	}

	public void setEnvioAntecessorEnderecoIp(byte[] envioAntecessorEnderecoIp) {
		this.envioAntecessorEnderecoIp = envioAntecessorEnderecoIp;
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
						getEnvioAntecessorId()[0],getEnvioAntecessorId()[1],getEnvioAntecessorId()[2],getEnvioAntecessorId()[3],
						getEnvioAntecessorEnderecoIp()[0],getEnvioAntecessorEnderecoIp()[1],getEnvioAntecessorEnderecoIp()[2],getEnvioAntecessorEnderecoIp()[3]
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
		byte[] antecessorId = {networkMessage[13],networkMessage[14],networkMessage[15],networkMessage[16]};
		byte[] antecessorEnderecoIp = {networkMessage[17],networkMessage[18],networkMessage[19],networkMessage[20]};

		this.setEnvioCodigoMensagem(codigoMensagem);
		this.setEnvioId(id);
		this.setEnvioSucessorId(sucessorId);
		this.setEnvioSucessorEnderecoIp(sucessorEnderecoIp);
		this.setEnvioAntecessorId(antecessorId);
		this.setEnvioAntecessorEnderecoIp(antecessorEnderecoIp);
	}	
}
