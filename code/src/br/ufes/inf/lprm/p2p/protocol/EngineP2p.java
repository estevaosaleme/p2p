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
			System.err.println("*ERRO: N�o foi poss�vel obter as interfaces de rede. Por favor, contacte o desenvovedor.");
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
			System.out.println("*ATEN��O: Mais de uma interface de rede foi detectada:");
			int i = 1;
			for (Inet4Address inet4Address : interfacesIpv4) {
				System.out.println(i + " - " + inet4Address.getHostAddress());
				i++;
			}
			
			System.out.println("*INFORME O N�MERO DA INTERFACE QUE SER� UTILIZADA:");
			BufferedReader buffer = new BufferedReader(new InputStreamReader(System.in));
			int opcao = 99;
			while (opcao >= i || opcao < 1 ){
				try {
					opcao = Integer.parseInt(buffer.readLine());
				} catch (Exception e) {
					System.err.println("*ERRO: digite um n�mero v�lido que indica a interface de rede.");
				}
				if (opcao >= 1 && opcao < i)
					enderecoIpHost = interfacesIpv4.get(opcao-1);
				else
					System.out.println(">>> Op��o inv�lida.");
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
	
	public static void response(byte[] enderecoIpOrigem, byte[] pacoteRecebido){
		byte codigoDados = pacoteRecebido[0];
	    
		switch (codigoDados) {
			case Constantes.CODIGO_ENVIO_JOIN:{
				System.out.println(">>> Join recebido...");
				MessageJoin messageJoin = new MessageJoin();
				messageJoin.getReceiveFromClient(pacoteRecebido);
				nodoP2p.respostaJoin(Helper.enderecoIpByteToString(enderecoIpOrigem),
						nodoP2p.getIdNodo(), 
						nodoP2p.getEnderecoIpNodo(),
						nodoP2p.getAntecessorId(),
						nodoP2p.getAntecessorEnderecoIp());             
				nodoP2p.setAntecessorId(Helper.byteArrayToInt(messageJoin.getEnvioId()));
				nodoP2p.setAntecessorEnderecoIp(Helper.enderecoIpByteToString(enderecoIpOrigem));
			}
				break;
			case Constantes.CODIGO_ENVIO_LEAVE:{
				System.out.println(">>> Leave recebido...");
				MessageLeave messageLeave = new MessageLeave();
				messageLeave.getReceiveFromClient(pacoteRecebido);
                if (messageLeave.getEnvioId() == messageLeave.getEnvioAntecessorId()) {
                	nodoP2p.setAntecessorId(Helper.byteArrayToInt(messageLeave.getEnvioAntecessorId()));
                	nodoP2p.setAntecessorEnderecoIp(Helper.enderecoIpByteToString(messageLeave.getEnvioAntecessorEnderecoIp()));
                	nodoP2p.respostaLeave(Helper.enderecoIpByteToString(enderecoIpOrigem));
                }   
                if (messageLeave.getEnvioId() == messageLeave.getEnvioSucessorId()) {
                	nodoP2p.setSucessorId(Helper.byteArrayToInt(messageLeave.getEnvioSucessorId()));
                	nodoP2p.setSucessorEnderecoIp(Helper.enderecoIpByteToString(messageLeave.getEnvioSucessorEnderecoIp()));
                	nodoP2p.respostaLeave(Helper.enderecoIpByteToString(enderecoIpOrigem));
                } 
			}
				break;
			case Constantes.CODIGO_ENVIO_LOOKUP:{
				System.out.println(">>> Lookup recebido...");
				MessageLookup messageLookup = new MessageLookup();
				messageLookup.getReceiveFromClient(pacoteRecebido);             
                int idProcurado = Helper.byteArrayToInt(messageLookup.getEnvioIdProcurado());    
                if ( (idProcurado == nodoP2p.getIdNodo()) ||
                     (((nodoP2p.getIdNodo() > idProcurado)&&(nodoP2p.getIdNodo() > nodoP2p.getSucessorId()) && (idProcurado < nodoP2p.getSucessorId())) )||
                     ((nodoP2p.getIdNodo() <  idProcurado)&&(nodoP2p.getSucessorId() > idProcurado)) || 
                     ((nodoP2p.getIdNodo() <  idProcurado)&&(nodoP2p.getSucessorId() < idProcurado)&&(nodoP2p.getIdNodo() > nodoP2p.getSucessorId())) ||
                      (nodoP2p.getIdNodo() == nodoP2p.getSucessorId()))          
                	nodoP2p.respostaLookup(Helper.enderecoIpByteToString(messageLookup.getEnvioEnderecoIp()),
                			idProcurado,
                			nodoP2p.getSucessorId(), 
                			nodoP2p.getSucessorEnderecoIp());   
                else
                	nodoP2p.lookup(nodoP2p.getSucessorEnderecoIp(),
                			Helper.byteArrayToInt(messageLookup.getEnvioId()),
                			Helper.enderecoIpByteToString(messageLookup.getEnvioEnderecoIp()),
                			idProcurado);
			}
				break;
			case Constantes.CODIGO_ENVIO_UPDATE: {
				System.out.println(">>> Update recebido...");
				MessageUpdate messageUpdate = new MessageUpdate();
				messageUpdate.getReceiveFromClient(pacoteRecebido);      
                nodoP2p.setSucessorId(Helper.byteArrayToInt(messageUpdate.getEnvioSucessorId()));
                nodoP2p.setSucessorEnderecoIp(Helper.enderecoIpByteToString(messageUpdate.getEnvioSucessorEnderecoIp()));
                nodoP2p.respostaUpdate(Helper.enderecoIpByteToString(enderecoIpOrigem));
			}
				break;
			case Constantes.CODIGO_RESPOSTA_LEAVE:
				//
				break;
			case Constantes.CODIGO_RESPOSTA_UPDATE:
				//
				break;
			case Constantes.CODIGO_RESPOSTA_JOIN:{
				System.out.println(">>> Join Resposta recebido...");
				MessageJoin messageJoin = new MessageJoin();
				messageJoin.getReceiveFromServer(pacoteRecebido);
                nodoP2p.setSucessorId(Helper.byteArrayToInt(messageJoin.getRespostaSucessorId()));
                nodoP2p.setSucessorEnderecoIp(Helper.enderecoIpByteToString(messageJoin.getRespostaSucessorEnderecoIp()));
                nodoP2p.setAntecessorId(Helper.byteArrayToInt(messageJoin.getRespostaAntecessorId()));
                nodoP2p.setAntecessorEnderecoIp(Helper.enderecoIpByteToString(messageJoin.getRespostaAntecessorEnderecoIp()));
                nodoP2p.update(nodoP2p.getAntecessorEnderecoIp(), 
                		nodoP2p.getIdNodo(), 
                		nodoP2p.getEnderecoIpNodo());
			}
				break;
			case Constantes.CODIGO_RESPOSTA_LOOKUP:{
				System.out.println(">>> Lookup Resposta recebido...");
				MessageLookup messageLookup = new MessageLookup();
				messageLookup.getReceiveFromServer(pacoteRecebido);
                nodoP2p.join(Helper.enderecoIpByteToString(messageLookup.getRespostaSucessorEnderecoIp()), 
                		nodoP2p.getIdNodo());
			}
				break;
		}
	}
}
