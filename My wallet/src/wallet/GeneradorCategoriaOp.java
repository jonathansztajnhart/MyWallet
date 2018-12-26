package wallet;

import java.util.ArrayList;

import enumerations.TipoOperacion;
import inputOutput.IniManager;
import inputOutput.Input;
import inputOutput.Output;

public class GeneradorCategoriaOp {
	private final static String K_PATH = "src/categorias_gastos.ini";
	private final static String K_CATEGORIA = "categoria";
	private final static String K_COMIDA = "comida";
	private final static String K_ROPA = "ropa";
	private final static String K_ALQUILER = "alquiler";
	private static ArrayList<String> categoriasGastos;
	private IniManager iniManager;

	public GeneradorCategoriaOp() {
		iniManager = new IniManager(K_PATH);
		cargarCategoriasGastos();
	}

	public void cargarCategoriasGastos() {
		categoriasGastos = new ArrayList<>();
		guardarCategoria(K_COMIDA);
		guardarCategoria(K_ROPA);
		guardarCategoria(K_ALQUILER);
		int size = iniManager.getSizeSection(K_CATEGORIA);
		if (size != 0) {
			for (int i = 0; i < size; i++) {
				guardarCategoria(iniManager.getValueOf(K_CATEGORIA, K_CATEGORIA + (i+4)));
			}
		}	
	}

	public void guardarListaCategoriasGastos() {
		for (int i = 3; i < categoriasGastos.size(); i++) {
			iniManager.setItem(K_CATEGORIA, K_CATEGORIA + (i+1), categoriasGastos.get(i));
			iniManager.save();
		}
	}

	
	public String pedirCategoria(String tipo) {
		String categoria = null;
		if(tipo.equals(TipoOperacion.Gasto.toString())) {	
			categoria = pedirCategoriaGasto();
		}	
		else {
			categoria = pedirCategoriaIngreso();
		}
		return categoria;
	}
	
	private String pedirCategoriaGasto() {
		String categoria = seleccionarCategoria();
		if(categoria == null) {
			categoria = Input.pedirCategoria();
			if(!categoria.equals("")) {
				guardarCategoria(categoria);
			}
		}
		return categoria;
	}
	
	private String pedirCategoriaIngreso() {
		return Input.pedirCategoria();
	}
	
	/**
	 * muestra todas la categorias de gastos disponibles y selecciona la
	 * solicicitada
	 * @return el string de la categoria o null si la seleccion es 0
	 **/
	private String seleccionarCategoria() {
		informarCategoriasGastos();
		int nroCategoria = Input.pedirNroCatergoria();
		String categoria = null;
		if(nroCategoria > 0) {
			categoria = categoriasGastos.get(nroCategoria - 1); 
		}
		else if(nroCategoria < 0) {
			seleccionarCategoria();
		}
		return categoria;
	}

	private void informarCategoriasGastos() {
		Output.mostrarMensaje("Seleccione la opcion deseada");
		for (int i = 0; i < categoriasGastos.size(); i++) {
			Output.mostrarMensaje((i+1) + ") " + categoriasGastos.get(i));
		}
		Output.mostrarMensaje("0) Crear nueva categoria"); 
	}

	private void guardarCategoria(String categoria) {
		categoriasGastos.add(categoria);
	}
}