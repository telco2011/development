package es.tlc.jasig;

import org.jasypt.util.password.StrongPasswordEncryptor;

public class EncryptPassword {

	public Boolean compartePassword(String inputPassword, String encryptedPassword) {
		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
		return passwordEncryptor.checkPassword(inputPassword, encryptedPassword);
	}
	
	public String showEncryptedPassword (String inputPassword) {
		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
		return passwordEncryptor.encryptPassword(inputPassword);
	}
}
