package project2;

import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Blockchain {
	public static void main(String[] args) throws ClassNotFoundException, IOException, NoSuchAlgorithmException {
		if (new File("ledgerData").exists()) {
		    FileInputStream fis = new FileInputStream("ledgerData");
            ObjectInputStream ois = new ObjectInputStream(fis);
            
            ArrayList<Block> currAppendLedger = new ArrayList<Block>();
            currAppendLedger = (ArrayList) ois.readObject();
            Ledger l = new Ledger();
            l.appendLedger = currAppendLedger;
            
            if (l.checkTampering(l.appendLedger) == false) {
            		System.out.println("The ledger has been tampered with.");
            		System.exit(0);
            }
            else {
            		System.out.println("The ledger is safe from tampering.");
            }
            
            boolean quit = false;
			while (!quit) {
				Scanner sc = new Scanner(System.in);
				System.out.println("Enter the string you would like to append.");
				String stringToAdd = sc.nextLine();
				l.addBlock(stringToAdd);
				System.out.println("Enter 'c' to continue or enter 'q' to quit the program.");
				String decision = sc.nextLine();
				if (decision.equals("q")) {
					quit = true;
				}
				else if (decision.equals("c")) {
					continue;
				}
			}
			
            ois.close();
		}
		else {
			Ledger l = new Ledger();
			Block initializationBlock = new Block("0","0");
			l.appendLedger.add(initializationBlock);
			boolean quit = false;
			while (!quit) {
				Scanner sc = new Scanner(System.in);
				System.out.println("Enter the string you would like to append.");
				String stringToAdd = sc.nextLine();
				l.addBlock(stringToAdd);
				System.out.println("Enter 'c' to continue or enter 'q' to quit the program.");
				String decision = sc.nextLine();
				if (decision.equals("q")) {
					quit = true;
				}
				else if (decision.equals("c")) {
					continue;
				}
			}
		}
		
	}
	
	public static class Ledger implements Serializable {
		public ArrayList<Block> appendLedger = new ArrayList<Block>();
	
		public void addBlock(String input) throws NoSuchAlgorithmException, IOException {
			Block b = new Block("0",input);
			appendLedger.add(b);
			for (int i = appendLedger.size() - 2; i-- > 0;) {
				String prevHash = appendLedger.get(i+1).hashValue;
				String currentHash = applySHA256(prevHash, appendLedger.get(i+1).nextData);
				appendLedger.get(i).setHash(currentHash);
			}
			
		    FileOutputStream fos = new FileOutputStream("ledgerData", false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(appendLedger);
            oos.close();
            fos.close();
            
            FileOutputStream fos2 = new FileOutputStream("firstBlock", false);
            ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
            oos2.writeObject(appendLedger.get(0));
            oos2.close();
            fos2.close();
		}
		
		public boolean checkTampering(ArrayList<Block> ledger) throws NoSuchAlgorithmException, IOException, ClassNotFoundException {
			for (int i = appendLedger.size() - 2; i-- > 0;) {
				String nextHash = applySHA256(ledger.get(i+1).hashValue, ledger.get(i+1).nextData);
				if (!nextHash.equals(ledger.get(i).hashValue)) {
					return false;
				}
			}
			
			
			FileInputStream fis = new FileInputStream("firstBlock");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Block b = (Block) ois.readObject();
            String hValue = b.hashValue;
            String hValue2 = ledger.get(0).hashValue;
         
            if (!hValue.equals(hValue2)) {
            		return false;
            }
            
			return true;
		}
	}

	public static class Block implements Serializable {
		public String hashValue;
		private String nextData;
		private static final long serialVersionUID = 4L;
		
		public Block(String hashValue, String nextData) {
			this.hashValue = hashValue;
			this.nextData = nextData;
		}
		
		public void setHash(String hash) {
			this.hashValue = hash;
		}
	}
	
	public static String applySHA256(String hashValue, String data) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");	        
		byte[] hash = digest.digest((hashValue + data).getBytes("UTF-8"));	        
		StringBuffer hexString = new StringBuffer(); 
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if(hex.length() == 1) hexString.append('0');
				hexString.append(hex);
		}
		return hexString.toString();
		
	}
}
