package writers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class BufferedTextWriter implements TextWriter {

	private BufferedWriter buf;

	public BufferedTextWriter(String ruta) throws IOException{
		buf = new BufferedWriter(new FileWriter(ruta));
	}


	@Override
	public String writeLine(String line) {
		try {
			buf.write(line + "\n");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	public void close() {
		if(buf != null) {
			try {
				buf.close();
			}
			catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
