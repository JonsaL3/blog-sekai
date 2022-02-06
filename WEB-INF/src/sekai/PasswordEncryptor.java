package sekai;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncryptor {

	public static String encrypt(String string)
	{
        MessageDigest md;
        String md5 = "";
        
		try 
		{
			md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(string.getBytes());
	        BigInteger number = new BigInteger(1, messageDigest);
	        md5 = number.toString(16);
		}
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		}
		
		return md5;
        
	}
	
}
