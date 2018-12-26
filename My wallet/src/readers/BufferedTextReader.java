package readers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class BufferedTextReader implements TextReader{

	private BufferedReader buf;

	public BufferedTextReader(String ruta) throws FileNotFoundException{
		buf = new BufferedReader(new FileReader(ruta));
	}

	@Override
	public boolean isReady() {
		boolean ok = false;
		try {
			ok = buf.ready();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return ok;
	}

	@Override
	public String readLine() {
		String line = null;
		if(isReady()) {
			try {
				line = buf.readLine();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		return line;
	}

	@Override
	public void close() {
		try {
			buf.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
