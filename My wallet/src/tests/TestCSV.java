package tests;

import java.io.IOException;
import java.text.ParseException;

import View.InterfazUsuario;
import repositorioOperacion.RepositorioOperacionCSV;
import wallet.Wallet;

public class TestCSV {
	public static void main(String[] args) throws IllegalArgumentException, ParseException, IOException {
		Wallet wallet = new Wallet(new RepositorioOperacionCSV());
		InterfazUsuario interfaz = new InterfazUsuario(wallet);
		interfaz.start();
	}
}
