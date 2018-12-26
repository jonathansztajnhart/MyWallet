package inputOutput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import readers.BufferedTextReader;
import readers.TextReader;
import writers.BufferedTextWriter;
import writers.TextWriter;


public class IniManager{

	private String filename;
	private HashMap<String, Section> sections;

	public IniManager() {
		this("inimanager.ini");
	}

	public IniManager(String filename) {
		this.filename = filename;
		sections = new HashMap<>();
		load();
	}
	public String getPath() {
		return filename;
	}
	public int getSectionsCount() {
		return 0;

	}
	public void list() {
		Collection<Section> coleccion = sections.values();
		for (Section section : coleccion) {
			section.list();
		}
	}
	public void setItem(String sectionName, String clave, String valor) {
		Section section = getSection(sectionName);
		if (section == null) {
			section = new Section(sectionName);
			sections.put(sectionName, section);
		}
		section.setItem(clave, valor);

	}
	public String getValueOf(String SectionName, String clave) {
		Section section = getSection(SectionName);
		String valor = null; 
		if (section != null) {
			valor = section.getValue(clave);
		}
		return valor;
	}
	public Section removeSection(String name) {
		return sections.remove(name);
	}
	public boolean removeItem(String nameSection, String key) {
		boolean removed = false;
		Section section = sections.get(nameSection);
		if(section != null) {
			section.remove(key);
			removed = true;
		}
		return removed;
	}
	public void save() {
		TextWriter textWriter = null;
		try {
			textWriter = new BufferedTextWriter(filename);
			Collection<Section> coleccion = sections.values();
			for (Section section : coleccion) {
				section.save(textWriter);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		finally {
			textWriter.close();
		}
	}
	
	public ArrayList<String> getSectionNames() {
		Collection<Section> collection;
		collection = sections.values();
		ArrayList<String> sectionNames = new ArrayList<>() ;
		for (Section section : collection) {
			sectionNames.add(section.getName());
		}
		return sectionNames;
	}
	
	public boolean hasSection(String nameSection) {
		return getSection(nameSection) != null;
	}
	
	public int getSizeSection(String nameSection) {
		int size = 0;
		Section section = getSection(nameSection);
		if (section != null) {
			size = section.getSize();
		}
		return size;
	}
	
	private void load() {
		TextReader textReader = null;
		try {
			textReader = new BufferedTextReader(filename);
			String nameSection = null;
			String line = textReader.readLine();
			while(line != null) {
				if(isSection(line)) {
					nameSection = line.substring(1, line.length()-1);
					Section section = new Section(nameSection);
					sections.put(nameSection, section);
				}
				else if(isItem(line)) {
					Section section = getSection(nameSection);
					if (section != null) {
						section.setItem(line);
					}
				}
				line = textReader.readLine();
			} 
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		finally {
			if(textReader != null) {
				textReader.close();
			}
		}
	}
	private Section getSection(String name) {
		return sections.get(name);
	}
	
	private boolean isSection(String a) {
		Pattern patron = Pattern.compile("\\[\\w[\\w ]*\\]");
		Matcher matcher = patron.matcher(a);
		return matcher.matches();
	}
	private boolean isItem(String a) {
		Pattern patron = Pattern.compile("[a-zA-Z0-9]\\w* *=.*");
		Matcher matcher = patron.matcher(a);
		return matcher.matches();

	}



}
