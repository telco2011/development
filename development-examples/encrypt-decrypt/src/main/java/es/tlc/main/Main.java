package es.tlc.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import es.tlc.jasig.EncryptPassword;


public class Main {

	public static void main(String[] args) {

		String uri = new StringBuilder("http://localhost:8080/mesvida-pt/modifyData?page=1").toString().split("\\?")[0];
		System.out.println(uri);
		StringBuilder result = new StringBuilder(uri.substring(0, uri.lastIndexOf("/")));
		result.append("/").append("home");
		System.out.println(result.toString());

	}

	@SuppressWarnings("unused")
	private static void simpleDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		String date = "10/05/2014 19:43";
		try {
			System.out.println(sdf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		jasigBasicExample();
	}

	private static void jasigBasicExample() {
		EncryptPassword jasigBasicExample = new EncryptPassword();

		String inputPassword = "usuarioJunitOK";
		String encryptedPassword = jasigBasicExample.showEncryptedPassword(inputPassword);
				
		System.out.println(encryptedPassword);
		
		System.out.println(jasigBasicExample.compartePassword(inputPassword, encryptedPassword));
	}
}
