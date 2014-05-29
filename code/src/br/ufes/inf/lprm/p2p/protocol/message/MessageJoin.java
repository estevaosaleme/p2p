package br.ufes.inf.lprm.p2p.protocol.message;

import br.ufes.inf.lprm.p2p.util.Constantes;

public class MessageJoin extends Message{
	
	public MessageJoin (){
		this.setEnvioCodigoMensagem(Constantes.CODIGO_ENVIO_JOIN);
	}
	
	private byte[] envioId;
	
	private byte[] respostaSucessorId;
	private byte[] respostaSucessorEnderecoIp;
	private byte[] respostaAntecessorId;
	private byte[] respostaAntecessorEnderecoIp;
	
	public byte[] getEnvioId() {
		return envioId;
	}

	public void setEnvioId(byte[] envioId) {
		this.envioId = envioId;
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

	public byte[] getRespostaAntecessorId() {
		return respostaAntecessorId;
	}

	public void setRespostaAntecessorId(byte[] respostaAntecessorId) {
		this.respostaAntecessorId = respostaAntecessorId;
	}

	public byte[] getRespostaAntecessorEnderecoIp() {
		return respostaAntecessorEnderecoIp;
	}

	public void setRespostaAntecessorEnderecoIp(byte[] respostaAntecessorEnderecoIp) {
		this.respostaAntecessorEnderecoIp = respostaAntecessorEnderecoIp;
	}

	@Override
	public byte[] messageToSend(boolean server) {
		if (server)
			return new byte[]
					{
						getRespostaCodigoMensagem(),
						getRespostaSucessorId()[0],getRespostaSucessorId()[1],getRespostaSucessorId()[2],getRespostaSucessorId()[3],
						getRespostaSucessorEnderecoIp()[0],getRespostaSucessorEnderecoIp()[1],getRespostaSucessorEnderecoIp()[2],getRespostaSucessorEnderecoIp()[3],
						getRespostaAntecessorId()[0],getRespostaAntecessorId()[1],getRespostaAntecessorId()[2],getRespostaAntecessorId()[3],
						getRespostaAntecessorEnderecoIp()[0],getRespostaAntecessorEnderecoIp()[1],getRespostaAntecessorEnderecoIp()[2],getRespostaAntecessorEnderecoIp()[3]
					};
		else
			return new byte[]
					{
						getEnvioCodigoMensagem(),
						getEnvioId()[0],getEnvioId()[1],getEnvioId()[2],getEnvioId()[3]
					};
	}
	
	@Override
	public void getReceiveFromServer(byte[] networkMessage) {
		byte codigoMensagem = networkMessage[0];
		byte[] sucessorId = {networkMessage[1],networkMessage[2],networkMessage[3],networkMessage[4]};
		byte[] sucessorEnderecoIp = {networkMessage[5],networkMessage[6],networkMessage[7],networkMessage[8]};
		byte[] antecessorId = {networkMessage[9],networkMessage[10],networkMessage[11],networkMessage[12]};
		byte[] antecessorEnderecoIp = {networkMessage[13],networkMessage[14],networkMessage[15],networkMessage[16]};
		
		this.setRespostaCodigoMensagem(codigoMensagem);
		this.setRespostaSucessorId(sucessorId);
		this.setRespostaSucessorEnderecoIp(sucessorEnderecoIp);
		this.setRespostaAntecessorId(antecessorId);
		this.setRespostaAntecessorEnderecoIp(antecessorEnderecoIp);	
	}

	@Override
	public void getReceiveFromClient(byte[] networkMessage) {
		byte codigoMensagem = networkMessage[0];
		byte[] id = {networkMessage[1],networkMessage[2],networkMessage[3],networkMessage[4]};
		
		this.setEnvioCodigoMensagem(codigoMensagem);
		this.setEnvioId(id);
	}	
}
