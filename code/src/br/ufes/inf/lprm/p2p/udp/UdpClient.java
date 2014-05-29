package br.ufes.inf.lprm.p2p.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import br.ufes.inf.lprm.p2p.protocol.message.Message;
import br.ufes.inf.lprm.p2p.util.Constantes;
import br.ufes.inf.lprm.p2p.util.Helper;

public class UdpClient extends Thread {
	
	private Message mensagem;
	private String enderecoIp;
	
	public UdpClient(Message mensagem, String enderecoIp){
		this.mensagem = mensagem;
		this.enderecoIp = enderecoIp;
	}

	public void setEnderecoIp(String enderecoIp) {
		this.enderecoIp = enderecoIp;
	}

	public void send() {
    	  DatagramSocket socketCliente = null;
		  try {
			  socketCliente = new DatagramSocket();
			  System.out.println("*CLIENTE: Socket inicializado.");
		  } catch (SocketException e) {
			  System.err.println("*ERRO: o cliente n�o conseguiu inicializar o socket.");
			  e.printStackTrace();
			  return;
		  }
    	  InetAddress ip = null;
		  try {
			  ip = InetAddress.getByName(enderecoIp);
			  System.out.println("*CLIENTE: O endere�o ip " + enderecoIp + " � v�lido.");
	  	  } catch (UnknownHostException e) {
	  		  System.err.println("*ERRO: o cliente n�o conseguiu resolver o endere�o IP "+ enderecoIp);
	  		  e.printStackTrace();
	  		  socketCliente.close();
	  		  return;
		  } 
		  
	      byte[] dadosEnvio = new byte[1024]; 
	      dadosEnvio = mensagem.messageToSend(false);
	      System.out.println("*CLIENTE: Preparando conex�o com o servidor "+ enderecoIp + " para envio de pacote.");
	      DatagramPacket pacoteEnviado = new DatagramPacket(dadosEnvio, dadosEnvio.length, ip, Constantes.PORTA); 
	      try {
	    	  socketCliente.send(pacoteEnviado);
	    	  System.out.println("*CLIENTE: Mensagem "+ Helper.byteArrayToString(dadosEnvio) + " enviada ao servidor " + enderecoIp);
	      } catch (IOException e) {
	    	  System.err.println("*ERRO: o cliente n�o conseguiu enviar a mensagem " + Helper.byteArrayToString(dadosEnvio) + " ao servidor " + enderecoIp);
	    	  e.printStackTrace();
	    	  socketCliente.close();
	    	  return;
	      } 
	  
	      byte[] dadosRecebidos = new byte[1024];
	      DatagramPacket pacoteRecebido = new DatagramPacket(dadosRecebidos, dadosRecebidos.length); 
	      try {
	    	  socketCliente.receive(pacoteRecebido);
	    	  mensagem.getReceiveFromServer(pacoteRecebido.getData());
	    	  System.out.println("*CLIENTE: Resposta " + Helper.byteArrayToString(pacoteRecebido.getData()) + " recebida do servidor "+ enderecoIp);
	      } catch (IOException e) {
	    	  System.err.println("*ERRO: o cliente n�o conseguiu receber resposta do servidor "+ enderecoIp);
	    	  e.printStackTrace();
	    	  socketCliente.close();
	    	  return;
	      } 
	      socketCliente.close(); 
	      System.out.println("*CLIENTE: conex�o fechada com o servidor "+ enderecoIp);
	}
	
	@Override
	public void run() {
		send();
	}
}
