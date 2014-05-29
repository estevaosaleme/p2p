package br.ufes.inf.lprm.p2p.protocol.message;

public interface IMessage {
	public byte[] messageToSend(boolean server);
	public void getReceiveFromServer(byte[] networkMessage);
	public void getReceiveFromClient(byte[] networkMessage);
}
