package br.ufes.inf.lprm.p2p.protocol;

import br.ufes.inf.lprm.p2p.protocol.message.MessageJoin;
import br.ufes.inf.lprm.p2p.protocol.message.MessageLeave;
import br.ufes.inf.lprm.p2p.protocol.message.MessageLookup;
import br.ufes.inf.lprm.p2p.protocol.message.MessageUpdate;
import br.ufes.inf.lprm.p2p.udp.UdpClient;
import br.ufes.inf.lprm.p2p.util.Constantes;
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
	
	public void join(String enderecoIp, int idNodo)
    {    
		MessageJoin messageJoin = new MessageJoin();
		messageJoin.setEnvioCodigoMensagem(Constantes.CODIGO_ENVIO_JOIN);
		messageJoin.setEnvioId(Helper.intToByteArray(idNodo));
		UdpClient cliente = new UdpClient(messageJoin, enderecoIp);
		cliente.send();
    }
    
    public void leave()
    {   
    	if (getIdNodo() != getSucessorId()){
	    	MessageLeave messageLeave = new MessageLeave();
			messageLeave.setEnvioCodigoMensagem(Constantes.CODIGO_ENVIO_LEAVE);
			messageLeave.setEnvioId(Helper.intToByteArray(getIdNodo()));
			messageLeave.setEnvioSucessorId(Helper.intToByteArray(getSucessorId()));
			messageLeave.setEnvioSucessorEnderecoIp(Helper.enderecoIpStringToByte(getSucessorEnderecoIp()));
			messageLeave.setEnvioAntecessorId(Helper.intToByteArray(getAntecessorId()));
			messageLeave.setEnvioAntecessorEnderecoIp(Helper.enderecoIpStringToByte(getAntecessorEnderecoIp()));
			UdpClient clienteAntecessor = new UdpClient(messageLeave, getAntecessorEnderecoIp());
			clienteAntecessor.send();
			UdpClient clienteSucessor = new UdpClient(messageLeave, getSucessorEnderecoIp());
			clienteSucessor.send();
			
			setAntecessorEnderecoIp(getEnderecoIpNodo());
			setAntecessorId(getIdNodo());
			setSucessorEnderecoIp(getEnderecoIpNodo());
			setSucessorId(getIdNodo());
		}  
    }        

    
    public void lookup(String enderecoIp,int idOrigemProcura, String ipOrigemProcura, int chave) 
    { 
    	MessageLookup messageLookup = new MessageLookup();
		messageLookup.setEnvioCodigoMensagem(Constantes.CODIGO_ENVIO_LOOKUP);
		messageLookup.setEnvioId(Helper.intToByteArray(idOrigemProcura));
		messageLookup.setEnvioEnderecoIp(Helper.enderecoIpStringToByte(ipOrigemProcura));
		messageLookup.setEnvioIdProcurado(Helper.intToByteArray(chave));	
		UdpClient cliente = new UdpClient(messageLookup, enderecoIp);
		cliente.send();
    }    
              
    public void update(String enderecoIp, int idNovoSucessor, String enderecoIpNovoSucessor)
    {
    	MessageUpdate messageUpdate = new MessageUpdate();
		messageUpdate.setEnvioCodigoMensagem(Constantes.CODIGO_ENVIO_UPDATE);
		messageUpdate.setEnvioId(Helper.intToByteArray(getIdNodo()));
		messageUpdate.setEnvioSucessorId(Helper.intToByteArray(idNovoSucessor));
		messageUpdate.setEnvioSucessorEnderecoIp(Helper.enderecoIpStringToByte(enderecoIpNovoSucessor));
		UdpClient cliente = new UdpClient(messageUpdate, enderecoIp);
		cliente.send();
    }        
    
    public void respostaJoin(String enderecoIp, int idSucessorNovo, String enderecoIpSucessorNovo, int idAntecessorNovo, String enderecoIpAntecessorNovo)
    {
    	MessageJoin messageJoin = new MessageJoin();
		messageJoin.setRespostaCodigoMensagem(Constantes.CODIGO_RESPOSTA_JOIN);
		messageJoin.setRespostaSucessorId(Helper.intToByteArray(idSucessorNovo));
		messageJoin.setRespostaSucessorEnderecoIp(Helper.enderecoIpStringToByte(enderecoIpSucessorNovo));
		messageJoin.setRespostaAntecessorId(Helper.intToByteArray(idAntecessorNovo));
		messageJoin.setRespostaAntecessorEnderecoIp(Helper.enderecoIpStringToByte(enderecoIpAntecessorNovo));
		UdpClient cliente = new UdpClient(messageJoin, enderecoIp);
		cliente.send(true);        
    }        
    
    public void respostaLeave(String enderecoIp)
    {
    	MessageLeave messageLeave = new MessageLeave();
		messageLeave.setRespostaCodigoMensagem(Constantes.CODIGO_RESPOSTA_LEAVE);
		messageLeave.setRespostaId(Helper.intToByteArray(getIdNodo()));
		UdpClient cliente = new UdpClient(messageLeave, enderecoIp);
		cliente.send(true);   
    }   
    
    public void respostaLookup(String enderecoIp, int idProcurado, int idSucessorProcurado, String enderecoIpSucessorProcurado)
    {
    	MessageLookup messageLookup = new MessageLookup();
		messageLookup.setRespostaCodigoMensagem(Constantes.CODIGO_RESPOSTA_LOOKUP);
		messageLookup.setRespostaIdProcurado(Helper.intToByteArray(idProcurado));
		messageLookup.setRespostaSucessorId(Helper.intToByteArray(idSucessorProcurado));
		messageLookup.setRespostaSucessorEnderecoIp(Helper.enderecoIpStringToByte(enderecoIpSucessorProcurado));
		UdpClient cliente = new UdpClient(messageLookup, enderecoIp);
		cliente.send(true);  
    }   
    
    public void respostaUpdate(String enderecoIp)
    {
    	MessageUpdate messageUpdate = new MessageUpdate();
 		messageUpdate.setRespostaCodigoMensagem(Constantes.CODIGO_RESPOSTA_UPDATE);
 		messageUpdate.setRespostaId(Helper.intToByteArray(getIdNodo()));
 		UdpClient cliente = new UdpClient(messageUpdate, enderecoIp);
		cliente.send(true);
    }   
	
}
