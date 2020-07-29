package project2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

//cited the Oracle Digital Signature Documentation 
public class DigitalSignature {
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException, IOException, InvalidKeySpecException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the file path.");
		String filePath = sc.nextLine();
		FileInputStream fs = new FileInputStream(filePath);
		BufferedInputStream bs = new BufferedInputStream(fs);
		
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
		
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
		keyGen.initialize(1024, random);
		
		KeyPair pair = keyGen.generateKeyPair();
		PrivateKey priv = pair.getPrivate();
		PublicKey pub = pair.getPublic();
		
		Signature dsa = Signature.getInstance("SHA1withDSA", "SUN"); 
		
		dsa.initSign(priv);
		
		byte [] buff = new byte[1024];
		int len;
		
		while ((len = bs.read(buff)) >= 0) {
		    dsa.update(buff, 0, len);
		};
		bs.close();
		
		byte[] realSig = dsa.sign();
		byte[] pubKey = pub.getEncoded();
		
		String encodedSignature = Base64.getEncoder().encodeToString(realSig);
		String encodedPublicKey = Base64.getEncoder().encodeToString(pubKey);
		
		System.out.println("Here is the digital signature.");
		System.out.println(encodedSignature);
		System.out.println("Here is the public key.");
		System.out.println(encodedPublicKey);
		
		System.out.println(" ");
		
		System.out.println("Now let us verify the digital signature for a file.");
		System.out.println("Enter the file path.");
		String verifyFilePath = sc.nextLine();
		System.out.println("Enter the digital signature.");
		String verifyDigSig = sc.nextLine();
		System.out.println("Enter the verification key");
		String verifyKey = sc.nextLine();
		
		byte[] decodedDigSig = Base64.getDecoder().decode(verifyDigSig);
		byte[] decodedKey = Base64.getDecoder().decode(verifyKey);
		
		X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(decodedKey);
		KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
		PublicKey checkKey = keyFactory.generatePublic(pubKeySpec);
		
		Signature checkSig = Signature.getInstance("SHA1withDSA", "SUN");
		checkSig.initVerify(checkKey);
		
		FileInputStream checkFS = new FileInputStream(verifyFilePath);
		BufferedInputStream checkBS = new BufferedInputStream(checkFS);
		byte[] checkBuff = new byte[1024];
		
		int checkLen;
		while (checkBS.available() != 0) {
		    checkLen = checkBS.read(checkBuff);
		    checkSig.update(checkBuff, 0, checkLen);
		};

		checkBS.close();
		
		boolean validityCheck = checkSig.verify(decodedDigSig);
		
		if (validityCheck) {
			System.out.println("Valid signature.");
		}
		else {
			System.out.println("Invalid signature.");
		}
		
		sc.close();
	}
}
