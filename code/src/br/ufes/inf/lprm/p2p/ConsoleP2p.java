package br.ufes.inf.lprm.p2p;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import br.ufes.inf.lprm.p2p.protocol.EngineP2p;

public class ConsoleP2p {

	public static void getConsole(){
		System.out.println("*COMANDOS: leave, lookup, info, quit.");
		System.out.println("*DIGITE O COMANDO DESEJADO:");
					
	    Scanner scanner = new Scanner(System.in);
	    while (scanner.hasNextLine()) {
	        String line = scanner.nextLine();
	        switch (line) {
		        case "info":
		        	System.out.println(">>> ID NODO.........: " + EngineP2p.nodoP2p.getIdNodo());
		        	System.out.println(">>> IP NODO.........: " + EngineP2p.nodoP2p.getEnderecoIpNodo());
		        	System.out.println(">>> ID ANTECESSOR...: " + EngineP2p.nodoP2p.getAntecessorId());
		        	System.out.println(">>> IP ANTECESSOR...: " + EngineP2p.nodoP2p.getAntecessorEnderecoIp());
		        	System.out.println(">>> ID SUCESSOR.....: " + EngineP2p.nodoP2p.getSucessorId());
		        	System.out.println(">>> IP SUCESSOR.....: " + EngineP2p.nodoP2p.getSucessorEnderecoIp());
					System.out.println(">>> Comando \"info\" foi executado com sucesso.");
					break;
					
				case "quit":
					EngineP2p.nodoP2p.leave();
					System.out.println(">>> Comando \"quit\" foi executado.");
					System.exit(0);
					break;
					
				case "lookup":
					System.out.println("*INFORME O IP DE UM SERVIDOR P2P:");
					BufferedReader bufferReadLookup = new BufferedReader(new InputStreamReader(System.in));
					String enderecoIpLookup = new String();
					try {
						enderecoIpLookup = bufferReadLookup.readLine();
					} catch (IOException e) {
						System.err.println("*ERRO: erro ao tentar ler o endereco IP fornecido no comando lookup.");
						e.printStackTrace();
					}
					
					System.out.println(">>> Conectando na rede...");
					EngineP2p.nodoP2p.lookup(enderecoIpLookup, EngineP2p.nodoP2p.getIdNodo(), EngineP2p.nodoP2p.getEnderecoIpNodo(), EngineP2p.nodoP2p.getIdNodo());
					System.out.println(">>> Comando \"lookup\" foi executado.");
					
					
					break;	
					
				case "leave":
					EngineP2p.nodoP2p.leave();
					System.out.println(">>> Comando \"leave\" foi executado.");
					break;
					
				default:
					System.err.println(">>> Comando \""+line+"\" n�o foi encontrado.");
					break;
			}
	    }
	    scanner.close();
	}
}
