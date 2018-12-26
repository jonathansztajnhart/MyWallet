package inputOutput;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import writers.TextWriter;


public class Section{
	private String name;
	private HashMap<String,String> items;

	public Section(String name) {
		this.name = name;
		items = new HashMap<>();
	}
	public String getValue(String clave) {
		return items.get(clave);
	}
	public String getName() {
		return this.name;
	}
	public void setItem(String claveValor) {
		String[] item = claveValor.split("=");
		if(item.length == 2) {
			items.put(item[0], item[1]);
		}
		else {
			System.out.println("item invalido");
		}
	}
	public void setItem(String clave, String valor) {
		items.put(clave, valor);
	}
	public void list() {
		Collection<Entry<String, String>> coleccion = items.entrySet();
		System.out.println("[" + name + "]");
		for (Entry<String,String> item : coleccion) {
			System.out.println(item.getKey() + "=" + item.getValue());
		}
	}
	public void save(TextWriter textWriter) {
		Collection<Entry<String, String>> coleccion = items.entrySet();
		textWriter.writeLine("[" + name + "]");
		for (Entry<String,String> item : coleccion) {
			textWriter.writeLine(item.getKey() + "=" + item.getValue());
		}
	}
	public boolean remove(String clave) {
		boolean existe = false;
		String item = items.remove(clave);
		if(item != null) {
			existe = true;
		}
		return existe;
	}
	public int getSize() {
		return items.size();
	}

}
