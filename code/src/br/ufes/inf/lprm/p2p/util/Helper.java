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
            String hashtext = number.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext.substring(0,4);
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
//      return enderecoIp[0] + "." + enderecoIp[1] + "." + enderecoIp[2] + "."+ enderecoIp[3];
	}
	
	public static byte[] enderecoIpStringToByte(String enderecoIp){
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getByName(enderecoIp);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return inetAddress.getAddress();
		
//		byte[] ret = new byte[4];
//		String[] ip = enderecoIp.split("\\.");
//		if (ip.length == 4){
//			ret[0] = Byte.parseByte(ip[0]);
//			ret[1] = Byte.parseByte(ip[1]);	
//			ret[2] = Byte.parseByte(ip[2]);
//			ret[3] = Byte.parseByte(ip[3]);
//		}
//		return ret;
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
