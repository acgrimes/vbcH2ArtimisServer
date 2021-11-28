package com.ll.vbc.utils;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

/**
 * Utility class for chapter 4 examples
 */
public class Utils
{
    private static class FixedRand extends SecureRandom
    {
        MessageDigest sha;
        byte[]		  state;
        
        FixedRand()
        {
            try
            {
                this.sha = MessageDigest.getInstance("SHA-1");
                this.state = sha.digest();
            }
            catch (NoSuchAlgorithmException e)
            {
                throw new RuntimeException("can't find SHA-1!");
            }
        }
	
	    public void nextBytes(
	       byte[] bytes)
	    {
	        int	off = 0;
	        
	        sha.update(state);
	        
	        while (off < bytes.length)
	        {	            
	            state = sha.digest();
	            
	            if (bytes.length - off > state.length)
	            {
	                System.arraycopy(state, 0, bytes, off, state.length);
	            }
	            else
	            {
	                System.arraycopy(state, 0, bytes, off, bytes.length - off);
	            }
	            
	            off += state.length;
	            
	            sha.update(state);
	        }
	    }
    }
    
    /**
     * Return a SecureRandom which produces the same value.
     * <b>This is for testing only!</b>
     * @return a fixed random
     */
    public static SecureRandom createFixedRandom()
    {
        return new FixedRand();
    }

    public final static KeyPair generateECDSAKeyPair() {

        KeyPair keyPair = null;
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            keyGen.initialize(ecSpec, new SecureRandom());
            keyPair = keyGen.generateKeyPair();
        } catch(NoSuchProviderException | NoSuchAlgorithmException | InvalidAlgorithmParameterException ex) {
            ex.printStackTrace();
        }
        return keyPair;
    }

    public final static byte[] digitalSignature(PrivateKey privKey, byte[] message) {

        Signature signature = null;
        byte[] result = null;
        try {
            signature = Signature.getInstance("ECDSA", "BC");
            signature.initSign(privKey, Utils.createFixedRandom());
            signature.update(message);
            result = signature.sign();
        } catch(NoSuchAlgorithmException | NoSuchProviderException |
                InvalidKeyException | SignatureException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public final static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
