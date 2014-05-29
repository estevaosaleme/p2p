package br.ufes.inf.lprm.p2p.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import br.ufes.inf.lprm.p2p.ProgramP2p;
import br.ufes.inf.lprm.p2p.protocol.EngineP2p;
import br.ufes.inf.lprm.p2p.protocol.message.MessageJoin;
import br.ufes.inf.lprm.p2p.util.Constantes;
import br.ufes.inf.lprm.p2p.util.Helper;

public class UdpServer extends Thread {
	
	@Override
	public void run() {
		System.out.println("*INICIALIZANDO SERVIDOR P2P...");
		DatagramSocket socketServidor = null;
		try {
			socketServidor = new DatagramSocket(Constantes.PORTA, EngineP2p.enderecoIpHost);
			System.out.println("*SERVIDOR: Inicializado na porta "+ Constantes.PORTA);
		} catch (SocketException e) {
			System.err.println("*ERRO: o servidor não conseguiu inicializar o socket na porta "+ Constantes.PORTA);
			e.printStackTrace();
			return;
		} 
		  
	    byte[] dadosRecebidos = new byte[1024]; 
	    byte[] dadosResposta = new byte[1024]; 
	
		while(true && socketServidor != null) { 
		    DatagramPacket pacoteRecebido = new DatagramPacket(dadosRecebidos, dadosRecebidos.length); 
		    try {
				socketServidor.receive(pacoteRecebido);
				System.out.println("*SERVIDOR: Pacote recebido: " + Helper.byteArrayToString(pacoteRecebido.getData()));
			} catch (IOException e) {
				System.err.println("*ERRO: o servidor não conseguiu receber um pacote.");
				e.printStackTrace();
			} 
		    
		    System.out.println("*SERVIDOR: Preparando resposta...");   
		    InetAddress enderecoIp = pacoteRecebido.getAddress(); 
		    int porta = pacoteRecebido.getPort();
		    dadosResposta = new EngineP2p().response(Helper.enderecoIpStringToByte(enderecoIp.getHostAddress()), pacoteRecebido.getData());    
		    DatagramPacket pacoteEnviado = new DatagramPacket(dadosResposta, dadosResposta.length, enderecoIp, porta); 
		    try {
				socketServidor.send(pacoteEnviado);
				System.out.println("*SERVIDOR: Resposta enviada ao cliente " + enderecoIp.toString() + ": " + Helper.byteArrayToString(dadosResposta));
			} catch (IOException e) {
				System.err.println("*ERRO: o servidor não conseguiu enviar um pacote.");
				e.printStackTrace();
			} 
		}
	}

	

}
