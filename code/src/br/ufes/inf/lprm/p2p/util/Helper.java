package br.ufes.inf.lprm.p2p.util;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Helper {
	public static String gerarMd5(String text){
		try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(text.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
// Removido para considerar apenas inteiros para este trabalho específico
//            String hashtext = number.toString(16);
//            while (hashtext.length() < 32) {
//                hashtext = "0" + hashtext;
//                
//            }
            return number.toString().substring(0,4);
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
	}
	
	public static String enderecoIpByteToString(byte[] enderecoIp){
		int i = 4;
        String ipAddress = new String();
        for (byte raw : enderecoIp)
        {
            ipAddress += (raw & 0xFF);
            if (--i > 0)
            {
                ipAddress += ".";
            }
        }
        return ipAddress;
	}
	
	public static byte[] enderecoIpStringToByte(String enderecoIp){
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getByName(enderecoIp);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return inetAddress.getAddress();
	}
	
	public static String byteArrayToString(byte[] bytes)
	{
	    StringBuilder ret = new StringBuilder();
	    for(int i=0; i<bytes.length; i++) {
	        ret.append((char)bytes[i]);
	    }
	    return ret.toString();
	}
}
