package br.ufes.inf.lprm.p2p.protocol;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import br.ufes.inf.lprm.p2p.protocol.message.MessageJoin;
import br.ufes.inf.lprm.p2p.protocol.message.MessageLeave;
import br.ufes.inf.lprm.p2p.protocol.message.MessageLookup;
import br.ufes.inf.lprm.p2p.protocol.message.MessageUpdate;
import br.ufes.inf.lprm.p2p.udp.UdpClient;
import br.ufes.inf.lprm.p2p.udp.UdpServer;
import br.ufes.inf.lprm.p2p.util.Constantes;
import br.ufes.inf.lprm.p2p.util.Helper;

public class EngineP2p {
	
	public static InetAddress enderecoIpHost = null;
	public static Nodo nodoP2p = null;
	
	public static void createNetwork(){
		List<Inet4Address> interfacesIpv4 = new ArrayList<Inet4Address>();
		Enumeration listaInterfaces = null;
		try {
			listaInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException s) {
			System.err.println("*ERRO: Não foi possível obter as interfaces de rede. Por favor, contacte o desenvovedor.");
			s.printStackTrace();
			System.exit(0);
		}
		while(listaInterfaces.hasMoreElements()){
		    NetworkInterface ni = (NetworkInterface)listaInterfaces.nextElement();
		    Enumeration ee = ni.getInetAddresses();
		    while(ee.hasMoreElements()) {
		    	try {
			        Inet4Address ia = (Inet4Address)ee.nextElement();
			        if (!ia.isLoopbackAddress())
			        	interfacesIpv4.add(ia);
		    	}
		    	catch (Exception e){}
		    }
		}
				
		if (interfacesIpv4.size() > 1){
			System.out.println("*ATENÇÃO: Mais de uma interface de rede foi detectada:");
			int i = 1;
			for (Inet4Address inet4Address : interfacesIpv4) {
				System.out.println(i + " - " + inet4Address.getHostAddress());
				i++;
			}
			
			System.out.println("*INFORME O NÚMERO DA INTERFACE QUE SERÁ UTILIZADA:");
			BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
			int opcao = 99;
			while (opcao >= i || opcao < 1 ){
				try {
					opcao = Integer.parseInt(buffer.readLine());
				} catch (Exception e) {
					System.err.println("*ERRO: digite um número válido que indica a interface de rede.");
				}
				if (opcao >= 1 && opcao < i)
					enderecoIpHost = interfacesIpv4.get(opcao-1);
				else
					System.out.println(">>> Opção inválida.");
			}
		}
		
		if (enderecoIpHost == null)
			enderecoIpHost = interfacesIpv4.get(0);
		
		nodoP2p = new Nodo(enderecoIpHost.getHostAddress());
		nodoP2p.setAntecessorEnderecoIp(nodoP2p.getEnderecoIpNodo());
		nodoP2p.setAntecessorId(nodoP2p.getIdNodo());
		nodoP2p.setSucessorEnderecoIp(nodoP2p.getEnderecoIpNodo());
		nodoP2p.setSucessorId(nodoP2p.getIdNodo());
		
		System.out.println("*INICIALIZANDO NODO \""+ enderecoIpHost.getHostName() +"\", ID = " + nodoP2p.getIdNodo() + ", IP: " + nodoP2p.getEnderecoIpNodo());
		
		UdpServer servidor = new UdpServer();
		Thread tServidor = new Thread(servidor);
	    tServidor.start();

	    try {
	        Thread.sleep(2000);
	    } catch(InterruptedException ex) {
	        Thread.currentThread().interrupt();
	    }
	}
	
	public byte[] response(byte[] enderecoIpOrigem, byte[] pacoteRecebido){
		byte[] dadosResposta = new byte[1024]; 
		byte codigoDados = pacoteRecebido[0];
	    switch (codigoDados) {
			case Constantes.CODIGO_ENVIO_JOIN:
				MessageJoin mensagemJoinCliente = new MessageJoin();
				mensagemJoinCliente.setEnderecoIpOrigemMensagem(enderecoIpOrigem);
				mensagemJoinCliente.getReceiveFromClient(pacoteRecebido);
				dadosResposta = serverResponseJoin(mensagemJoinCliente);
				break;
			case Constantes.CODIGO_ENVIO_LEAVE:
				MessageLeave mensagemLeaveCliente = new MessageLeave();
				mensagemLeaveCliente.setEnderecoIpOrigemMensagem(enderecoIpOrigem);
				mensagemLeaveCliente.getReceiveFromClient(pacoteRecebido);
				dadosResposta = serverResponseLeave(mensagemLeaveCliente);
				break;
			case Constantes.CODIGO_ENVIO_LOOKUP:
				MessageLookup mensagemLookupCliente = new MessageLookup();
				mensagemLookupCliente.setEnderecoIpOrigemMensagem(enderecoIpOrigem);
				mensagemLookupCliente.getReceiveFromClient(pacoteRecebido);
				dadosResposta = serverResponseLookup(mensagemLookupCliente);
				break;
			case Constantes.CODIGO_ENVIO_UPDATE:
				MessageUpdate mensagemUpdateCliente = new MessageUpdate();
				mensagemUpdateCliente.setEnderecoIpOrigemMensagem(enderecoIpOrigem);
				mensagemUpdateCliente.getReceiveFromClient(pacoteRecebido);
				dadosResposta = serverResponseUpdate(mensagemUpdateCliente);
				break;
			default:
				break;
		}
		return dadosResposta;
	}
	
	private byte[] serverResponseLookup(MessageLookup mensagemLookupCliente){
		// IMPLEMENTAÇÃO ORIGINAL PROCURANDO CONTEÚDO NA REDE
		
//		if (nodoP2p.getIdNodo() == Helper.byteArrayToInt(mensagemLookupCliente.getEnvioIdProcurado())){
//			MessageLookup messageLookup = new MessageLookup();
//			messageLookup.setRespostaCodigoMensagem(Constantes.CODIGO_RESPOSTA_LOOKUP);
//			messageLookup.setRespostaIdProcurado(mensagemLookupCliente.getEnvioId());
//			messageLookup.setRespostaSucessorId(Helper.intToByteArray(nodoP2p.getSucessorId()));
//			messageLookup.setRespostaSucessorEnderecoIp(Helper.enderecoIpStringToByte(nodoP2p.getSucessorEnderecoIp()));
//			System.out.println("*SERVIDOR: serverResponseLookup PROCESSADO.");
//			return messageLookup.messageToSend(true);
//		} 
//		else {
//			System.out.println("*SERVIDOR: serverResponseLookup LOOKUP FOWARD DISPARADO.");
//			MessageLookup messageLookupEnvio = new MessageLookup();
//			messageLookupEnvio.setEnvioCodigoMensagem(Constantes.CODIGO_ENVIO_LOOKUP);
//			messageLookupEnvio.setEnvioId(mensagemLookupCliente.getEnvioId());
//			messageLookupEnvio.setEnvioEnderecoIp(mensagemLookupCliente.getEnvioEnderecoIp());
//			messageLookupEnvio.setEnvioIdProcurado(mensagemLookupCliente.getEnvioIdProcurado());
//			
//			UdpClient cliente = new UdpClient(messageLookupEnvio, nodoP2p.getSucessorEnderecoIp());
//			cliente.send();
//					
//			MessageLookup messageLookupResposta = new MessageLookup();
//			messageLookupResposta.setRespostaCodigoMensagem(Constantes.CODIGO_RESPOSTA_LOOKUP);
//			messageLookupResposta.setRespostaIdProcurado(mensagemLookupCliente.getEnvioIdProcurado());
//			messageLookupResposta.setRespostaSucessorId(messageLookupEnvio.getRespostaIdProcurado());
//			messageLookupResposta.setRespostaSucessorEnderecoIp(messageLookupEnvio.getRespostaSucessorEnderecoIp());
//			System.out.println("*SERVIDOR: serverResponseLookup PROCESSADO.");
//			return messageLookupResposta.messageToSend(true);			
//			
//			// Procurar técnica para não ficar em lookup infinito.
//		}	
		
		// IMPLEMENTAÇÃO PROCURANDO PELO PRÓPRIO ID
		
		MessageLookup messageLookup = new MessageLookup();
		messageLookup.setRespostaCodigoMensagem(Constantes.CODIGO_RESPOSTA_LOOKUP);
		
		//# 1 - se o nid existe
		if (nodoP2p.getIdNodo() == Helper.byteArrayToInt(mensagemLookupCliente.getEnvioIdProcurado())){
			messageLookup.setRespostaSucessorId(Helper.intToByteArray(nodoP2p.getSucessorId()));
			messageLookup.setRespostaSucessorEnderecoIp(Helper.enderecoIpStringToByte(nodoP2p.getSucessorEnderecoIp())); 
		}
        //# 2 - se o nid não existe
		else 
			if (Helper.byteArrayToInt(mensagemLookupCliente.getEnvioIdProcurado()) < nodoP2p.getIdNodo()){
				
				//# 2.1 - procura nid proximo caminhando para o inicio
				if (nodoP2p.getIdNodo() <= nodoP2p.getAntecessorId()){
					//# se esta no inicio do circulo ou só tem 1 no na rede 
	                //# entao o novo sucessor é no atual, ou seja, o primeiro
					messageLookup.setRespostaSucessorId(Helper.intToByteArray(nodoP2p.getIdNodo()));
					messageLookup.setRespostaSucessorEnderecoIp(Helper.enderecoIpStringToByte(nodoP2p.getEnderecoIpNodo())); 
				}
	            else
	            	if (Helper.byteArrayToInt(mensagemLookupCliente.getEnvioIdProcurado()) > nodoP2p.getAntecessorId()){
	                    //# se antecessor nao pode ser sucessor o novo sucessor eh o nó atual
	            		messageLookup.setRespostaSucessorId(Helper.intToByteArray(nodoP2p.getIdNodo()));
						messageLookup.setRespostaSucessorEnderecoIp(Helper.enderecoIpStringToByte(nodoP2p.getEnderecoIpNodo()));
	            	}
	                else {
	                    //# se nao pergunte para ele quem é o novo sucessor
	                	MessageLookup messageLookupEnvio = new MessageLookup();
	        			messageLookupEnvio.setEnvioCodigoMensagem(Constantes.CODIGO_ENVIO_LOOKUP);
	        			messageLookupEnvio.setEnvioId(mensagemLookupCliente.getEnvioId());
	        			messageLookupEnvio.setEnvioEnderecoIp(Helper.enderecoIpStringToByte(nodoP2p.getAntecessorEnderecoIp()));
	        			messageLookupEnvio.setEnvioIdProcurado(mensagemLookupCliente.getEnvioIdProcurado());
	                	UdpClient cliente = new UdpClient(messageLookupEnvio, nodoP2p.getAntecessorEnderecoIp());
	        			cliente.send();
	        			System.out.println("*SERVIDOR: serverResponseLookup LOOKUP BACKWARD DISPARADO.");
	        			return messageLookupEnvio.messageToSend(true);
	                } 		
			}
			else
				if (Helper.byteArrayToInt(mensagemLookupCliente.getEnvioIdProcurado()) > nodoP2p.getIdNodo()){
		            //# 2.2 - procura nid proximo caminhando para o fim
					if (nodoP2p.getIdNodo() >= nodoP2p.getSucessorId()){
		                //# se esta no fim do circulo ou so tem 1 no na rede  
		                //# entao o sucessor é o sucessor do no atual
						messageLookup.setRespostaSucessorId(Helper.intToByteArray(nodoP2p.getSucessorId()));
						messageLookup.setRespostaSucessorEnderecoIp(Helper.enderecoIpStringToByte(nodoP2p.getSucessorEnderecoIp())); 
					}
		            else
		            	if (Helper.byteArrayToInt(mensagemLookupCliente.getEnvioIdProcurado()) > nodoP2p.getSucessorId()){
		                    //# se o sucessor nao pode ser sucessor o novo sucessor eh o no atual
		            		messageLookup.setRespostaSucessorId(Helper.intToByteArray(nodoP2p.getIdNodo()));
							messageLookup.setRespostaSucessorEnderecoIp(Helper.enderecoIpStringToByte(nodoP2p.getEnderecoIpNodo()));
		            	}
		            	else {
		                    //# se nao pergunte para ele quem é o novo sucessor
		            		MessageLookup messageLookupEnvio = new MessageLookup();
		        			messageLookupEnvio.setEnvioCodigoMensagem(Constantes.CODIGO_ENVIO_LOOKUP);
		        			messageLookupEnvio.setEnvioId(mensagemLookupCliente.getEnvioId());
		        			messageLookupEnvio.setEnvioEnderecoIp(Helper.enderecoIpStringToByte(nodoP2p.getSucessorEnderecoIp()));
		        			messageLookupEnvio.setEnvioIdProcurado(mensagemLookupCliente.getEnvioIdProcurado());
		                	UdpClient cliente = new UdpClient(messageLookupEnvio, nodoP2p.getSucessorEnderecoIp());
		        			cliente.send();
		        			System.out.println("*SERVIDOR: serverResponseLookup LOOKUP FOWARD DISPARADO.");
		        			return messageLookupEnvio.messageToSend(true);
		            	}
				}

		messageLookup.setRespostaIdProcurado(mensagemLookupCliente.getEnvioIdProcurado());
		messageLookup.setRespostaSucessorId(Helper.intToByteArray(nodoP2p.getSucessorId()));
		messageLookup.setRespostaSucessorEnderecoIp(Helper.enderecoIpStringToByte(nodoP2p.getSucessorEnderecoIp()));
		System.out.println("*SERVIDOR: serverResponseLookup PROCESSADO.");
		return messageLookup.messageToSend(true);
	}
	
	private byte[] serverResponseJoin(MessageJoin mensagemJoinCliente){
		MessageJoin messageJoin = new MessageJoin();
		messageJoin.setRespostaCodigoMensagem(Constantes.CODIGO_RESPOSTA_JOIN);
		messageJoin.setRespostaSucessorId(Helper.intToByteArray(nodoP2p.getSucessorId()));
		messageJoin.setRespostaSucessorEnderecoIp(Helper.enderecoIpStringToByte(nodoP2p.getSucessorEnderecoIp()));
		messageJoin.setRespostaAntecessorId(Helper.intToByteArray(nodoP2p.getAntecessorId()));
		messageJoin.setRespostaAntecessorEnderecoIp(Helper.enderecoIpStringToByte(nodoP2p.getAntecessorEnderecoIp()));
		
		nodoP2p.setAntecessorId(Helper.byteArrayToInt(mensagemJoinCliente.getEnvioId()));
		System.out.println("*NODO: serverResponseJoin - id do antecessor atualizado para " + nodoP2p.getAntecessorId() + " em " + nodoP2p.getIdNodo());
		nodoP2p.setAntecessorEnderecoIp(Helper.enderecoIpByteToString(mensagemJoinCliente.getEnderecoIpOrigemMensagem()));
		System.out.println("*NODO: serverResponseJoin - endereço IP do antecessor atualizado para " + nodoP2p.getAntecessorEnderecoIp() + " em " + nodoP2p.getIdNodo());

		System.out.println("*SERVIDOR: serverResponseJoin PROCESSADO.");
		return messageJoin.messageToSend(true);
	}
	
	private byte[] serverResponseLeave(MessageLeave mensagemLeaveCliente){
		MessageLeave messageLeave = new MessageLeave();
		messageLeave.setRespostaCodigoMensagem(Constantes.CODIGO_RESPOSTA_LEAVE);
		messageLeave.setRespostaId(Helper.intToByteArray(nodoP2p.getIdNodo()));
		
		if (nodoP2p.getSucessorId()== Helper.byteArrayToInt(mensagemLeaveCliente.getEnvioId())){
			nodoP2p.setSucessorId(Helper.byteArrayToInt(mensagemLeaveCliente.getEnvioSucessorId()));
			System.out.println("*NODO: serverResponseUpdate - id do sucessor atualizado para " + nodoP2p.getSucessorId() + " em " + nodoP2p.getIdNodo());
			nodoP2p.setSucessorEnderecoIp(Helper.enderecoIpByteToString(mensagemLeaveCliente.getEnvioSucessorEnderecoIp()));
			System.out.println("*NODO: serverResponseUpdate - endereço IP do sucessor atualizado para " + nodoP2p.getSucessorEnderecoIp() + " em " + nodoP2p.getIdNodo());
			
		} else
			if (nodoP2p.getAntecessorId() == Helper.byteArrayToInt(mensagemLeaveCliente.getEnvioId())){
				nodoP2p.setAntecessorId(Helper.byteArrayToInt(mensagemLeaveCliente.getEnvioAntecessorId()));
				System.out.println("*NODO: serverResponseLeave - id do antecessor atualizado para " + nodoP2p.getAntecessorId() + " em " + nodoP2p.getIdNodo());
				nodoP2p.setAntecessorEnderecoIp(Helper.enderecoIpByteToString(mensagemLeaveCliente.getEnvioAntecessorEnderecoIp()));
				System.out.println("*NODO: serverResponseLeave - endereço IP do antecessor atualizado para " + nodoP2p.getAntecessorEnderecoIp() + " em " + nodoP2p.getIdNodo());
			}

		System.out.println("*SERVIDOR: serverResponseLeave PROCESSADO.");
		return messageLeave.messageToSend(true);
	}
	
	private byte[] serverResponseUpdate(MessageUpdate mensagemUpdateCliente){
		MessageUpdate messageUpdate = new MessageUpdate();
		messageUpdate.setRespostaCodigoMensagem(Constantes.CODIGO_RESPOSTA_UPDATE);
		messageUpdate.setRespostaId(mensagemUpdateCliente.getEnvioId());
		
		nodoP2p.setSucessorId(Helper.byteArrayToInt(mensagemUpdateCliente.getEnvioSucessorId()));
		System.out.println("*NODO: serverResponseUpdate - id do sucessor atualizado para " + nodoP2p.getSucessorId() + " em " + nodoP2p.getIdNodo());
		nodoP2p.setSucessorEnderecoIp(Helper.enderecoIpByteToString(mensagemUpdateCliente.getEnvioSucessorEnderecoIp()));
		System.out.println("*NODO: serverResponseUpdate - endereço IP do sucessor atualizado para " + nodoP2p.getSucessorEnderecoIp() + " em " + nodoP2p.getIdNodo());
		
		System.out.println("*SERVIDOR: serverResponseUpdate PROCESSADO.");
		return messageUpdate.messageToSend(true);
	}
	
	
	public void clientRequestLookup(String enderecoIp, String chave){
		MessageLookup messageLookup = new MessageLookup();
		messageLookup.setEnvioCodigoMensagem(Constantes.CODIGO_ENVIO_LOOKUP);
		messageLookup.setEnvioId(Helper.intToByteArray(nodoP2p.getIdNodo()));
		messageLookup.setEnvioEnderecoIp(Helper.enderecoIpStringToByte(nodoP2p.getEnderecoIpNodo()));
		messageLookup.setEnvioIdProcurado(Helper.intToByteArray(Integer.parseInt(chave)));	
		
		UdpClient cliente = new UdpClient(messageLookup, enderecoIp);
		cliente.send();
				
		if (messageLookup.getRespostaIdProcurado() != null && messageLookup.getRespostaIdProcurado().length > 0){
			nodoP2p.setSucessorId(Helper.byteArrayToInt(messageLookup.getRespostaSucessorId()));
			System.out.println("*NODO: clientRequestLookup - id do sucessor atualizado para " + nodoP2p.getSucessorId() + " em " + nodoP2p.getIdNodo());
			nodoP2p.setSucessorEnderecoIp(Helper.enderecoIpByteToString(messageLookup.getRespostaSucessorEnderecoIp()));
			System.out.println("*NODO: clientRequestLookup - endereço IP do sucessor atualizado para " + nodoP2p.getSucessorEnderecoIp() + " em " + nodoP2p.getIdNodo());
		}
	}
	
	public void clientRequestJoin(String enderecoIpSucessor){
		MessageJoin messageJoin = new MessageJoin();
		messageJoin.setEnvioCodigoMensagem(Constantes.CODIGO_ENVIO_JOIN);
		messageJoin.setEnvioId(Helper.intToByteArray(nodoP2p.getIdNodo()));
		UdpClient cliente = new UdpClient(messageJoin, nodoP2p.getSucessorEnderecoIp());
		cliente.send();
		
		nodoP2p.setAntecessorId(Helper.byteArrayToInt(messageJoin.getRespostaAntecessorId()));
		System.out.println("*NODO: clientRequestJoin - id do antecessor atualizado para " + nodoP2p.getAntecessorId() + " em " + nodoP2p.getIdNodo());
		nodoP2p.setAntecessorEnderecoIp(Helper.enderecoIpByteToString(messageJoin.getRespostaAntecessorEnderecoIp()));
		System.out.println("*NODO: clientRequestJoin - endereço IP do antecessor atualizado para " + nodoP2p.getAntecessorEnderecoIp() + " em " + nodoP2p.getIdNodo());
	}
	
	public void clientRequestUpdate(String enderecoIpAntecessor){
		MessageUpdate messageUpdate = new MessageUpdate();
		messageUpdate.setEnvioCodigoMensagem(Constantes.CODIGO_ENVIO_UPDATE);
		messageUpdate.setEnvioId(Helper.intToByteArray(nodoP2p.getIdNodo()));
		messageUpdate.setEnvioSucessorId(Helper.intToByteArray(nodoP2p.getIdNodo()));
		messageUpdate.setEnvioSucessorEnderecoIp(Helper.enderecoIpStringToByte(nodoP2p.getEnderecoIpNodo()));
		UdpClient cliente = new UdpClient(messageUpdate, nodoP2p.getAntecessorEnderecoIp());
		cliente.send();
	}
	
	public void clientRequestLeave(){
		MessageLeave messageLeave = new MessageLeave();
		messageLeave.setEnvioCodigoMensagem(Constantes.CODIGO_ENVIO_LEAVE);
		messageLeave.setEnvioId(Helper.intToByteArray(nodoP2p.getIdNodo()));
		messageLeave.setEnvioSucessorId(Helper.intToByteArray(nodoP2p.getSucessorId()));
		messageLeave.setEnvioSucessorEnderecoIp(Helper.enderecoIpStringToByte(nodoP2p.getSucessorEnderecoIp()));
		messageLeave.setEnvioAntecessorId(Helper.intToByteArray(nodoP2p.getAntecessorId()));
		messageLeave.setEnvioAntecessorEnderecoIp(Helper.enderecoIpStringToByte(nodoP2p.getAntecessorEnderecoIp()));
		UdpClient clienteAntecessor = new UdpClient(messageLeave, nodoP2p.getAntecessorEnderecoIp());
		clienteAntecessor.send();
		UdpClient clienteSucessor = new UdpClient(messageLeave, nodoP2p.getSucessorEnderecoIp());
		clienteSucessor.send();
		
		nodoP2p.setAntecessorEnderecoIp(nodoP2p.getEnderecoIpNodo());
		nodoP2p.setAntecessorId(nodoP2p.getIdNodo());
		nodoP2p.setSucessorEnderecoIp(nodoP2p.getEnderecoIpNodo());
		nodoP2p.setSucessorId(nodoP2p.getIdNodo());
	}
}
