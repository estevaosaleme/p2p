package br.ufes.inf.lprm.p2p;

import br.ufes.inf.lprm.p2p.protocol.EngineP2p;

public class ProgramP2p {

	public static void main(String[] args)  {
		System.out.println("*Desenvolvido por Estêvão Bissoli Saleme - UFES/PPGI/LPRM");
		EngineP2p.createNetwork();   
	    ConsoleP2p.getConsole();
	}
}