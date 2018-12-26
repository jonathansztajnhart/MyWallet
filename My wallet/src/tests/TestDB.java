package tests;


import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import View.InterfazUsuario;
import repositorioOperacion.RepositorioOperacionDB;
import wallet.Wallet;

public class TestDB {

	public static void main(String[] args) throws IllegalArgumentException, ParseException, IOException, SQLException {
			Wallet wallet = new Wallet(new RepositorioOperacionDB());
			InterfazUsuario interfaz = new InterfazUsuario(wallet);
			interfaz.start();
	}
}
