package br.ufes.inf.lprm.p2p.util;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Helper {
	public static Integer gerarMd5(String text){
		try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(text.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
// Removido para considerar apenas inteiros para este trabalho espec�fico
//            String hashtext = number.toString(16);
//            while (hashtext.length() < 32) {
//                hashtext = "0" + hashtext;
//                
//            }
            return Integer.parseInt(number.toString().substring(0,4));
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
	}
	
	public static final byte[] intToByteArray(int value) {
	    return new byte[] {
	            (byte)(value >>> 24),
	            (byte)(value >>> 16),
	            (byte)(value >>> 8),
	            (byte)value};
	}
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	public static final int byteArrayToInt(byte[] bytes) {
	     return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
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
