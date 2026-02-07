package com.pharmacie;
import org.mindrot.jbcrypt.BCrypt;
public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 String plaintextPassword = "admin123";
	        
	        // 1. Generate a salt and hash the password (this would typically happen during user registration)
	        // The salt is embedded within the resulting 60-character hash string
	        String storedHash = BCrypt.hashpw(plaintextPassword, BCrypt.gensalt(12)); // A workload of 12 is a reasonable default
	        System.out.println("Stored Hash: " + storedHash);

	        // 2. Later, when a user attempts to log in, use checkpw() to verify their entered password
	        String candidatePassword = "mySecretPassword123"; 

	        if (BCrypt.checkpw(candidatePassword, storedHash)) {
	            System.out.println("Password matches. User authenticated successfully.");
	        } else {
	            System.out.println("Password does not match. Authentication failed.");
	        }

	}

}
