package readers;

public interface TextReader {
	boolean isReady();
	String readLine();
	void close();
}
