package repositorioOperacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import enumerations.ControlLevelError;
import enumerations.TipoOperacion;
import inputOutput.Input;
import inputOutput.Output;
import wallet.GeneradorCategoriaOp;
import wallet.GeneradorOperacion;
import wallet.Operacion;
import wallet.OperacionReader;
import writers.CSVWriter;

public class RepositorioOperacionCSV implements RepositorioOperacion{
	private final static String K_HEADER = "ID;" + "Fecha;" + "tipo;" + "importe;" + "categoria;" + "descripcion";
	private final static String K_PATH_CSV = "src/Operaciones";
	private final static String K_OPERACION_NO_ENCONTRADA = "La operacion con el id solicitado es inexistente";
	private final static String K_DATOS_INVALIDOS = "Los datos ingresados son invalidos";
	private ArrayList<Operacion> listaOperacion;
	private CSVWriter<Operacion> csvWriter;
	private GeneradorCategoriaOp generadorCategoriaOp;
	private GeneradorOperacion generadorOperacion;


	public RepositorioOperacionCSV() {
		this.csvWriter = new CSVWriter<>(K_HEADER);
		listaOperacion = cargarOperaciones();
		generadorCategoriaOp = new GeneradorCategoriaOp();
		generadorOperacion = new GeneradorOperacion(generadorCategoriaOp);
	}

	private void guardarOperacion(Operacion operacion) {
		listaOperacion.add(operacion);
	}

	public Operacion getOperacion(int id) {
		int i = 0;
		boolean encontre = false;
		Operacion operacion = null;
		while(i < listaOperacion.size() && encontre == false) {
			if(listaOperacion.get(i).getId() == id) {
				operacion = listaOperacion.get(i);
				encontre = true;
			}
			else {
				i++;
			}
		}
		return operacion;
	}
	
	/**
	 * extrae de la lista de operaciones los gastos y los pasa a
	 * otra lista|
	 * @return la lista de gastos
	 */
	public ArrayList<Operacion> getListaGastos(){
		ArrayList<Operacion> listaGasto = new ArrayList<>();
		for (Operacion operacion : this.listaOperacion) {
			if (operacion.getTipo().equals(TipoOperacion.Gasto)) {
				listaGasto.add(operacion);
			}
		}
		ordenarListaPorFecha(listaGasto);
		return listaGasto;
	}
	
	public void informarOperaciones() {
		ordenarListaPorId();
		for (Operacion operacion : listaOperacion) {
			Output.mostrarMensaje(operacion.toString());
		}
	}
	
	@Override
	public Operacion crearOperacion(TipoOperacion tipo, double topeImporte) throws IllegalArgumentException{
		Operacion op = null;
		int proximoIndice = getSize();
		op = generadorOperacion.crearOperacion(proximoIndice, tipo, topeImporte);
		guardarOperacion(op);
		return op;
	}

	@Override
	public Operacion eliminarOperacion(int id) {
		Operacion operacion = getOperacion(id);
		if (operacion != null) {
			listaOperacion.remove(operacion);
		}
		return operacion;
	}

	@Override
	public double modificarImporteOp(int id, double topeImporte) throws IllegalArgumentException{
		Operacion op = getOperacion(id);
		double nuevoImporte;
		if(op != null) {
			nuevoImporte = generadorOperacion.pedirImporte(op.getTipo(), topeImporte);
			op.setImporte(nuevoImporte);
		}
		else {
			throw new IllegalArgumentException(K_OPERACION_NO_ENCONTRADA);
		}
		return nuevoImporte;
	}

	@Override
	public void modificarCategoriaOp(int id) throws IllegalArgumentException{
		Operacion op = getOperacion(id);
		if(op != null) {
			String categoria = generadorOperacion.pedirCategoria(op.getTipo());
			op.setCategoria(categoria);
		}
		else {
			throw new IllegalArgumentException(K_OPERACION_NO_ENCONTRADA);
		}
	}

	@Override
	public void modificarDescripcionOp(int id) throws IllegalArgumentException{
		Operacion op = getOperacion(id);
		if (op != null) {
			String descripcion = Input.pedirDescripcion();
			op.setDescripcion(descripcion);
		}
		else {
			throw new IllegalArgumentException(K_OPERACION_NO_ENCONTRADA);
		}
	}

	@Override
	public Date modificarFechaOp(int id) throws IllegalArgumentException{
		Operacion op = getOperacion(id);
		Date fecha = null;
		if (op != null) {
			fecha = generadorOperacion.pedirFecha(op.getTipo());
			op.setFecha(fecha);
		}
		else {
			throw new IllegalArgumentException(K_OPERACION_NO_ENCONTRADA);
		}	
		return fecha;
	}

	@Override
	public void cerrar() {
		csvWriter.writeAll(K_PATH_CSV, listaOperacion);	
		generadorCategoriaOp.guardarListaCategoriasGastos();
	}

	public int getSize() {
		return listaOperacion.size();
	}

	public void ordenarListaPorId() {
		Comparator<Operacion> comparador = new Comparator<Operacion>() {

			@Override
			public int compare(Operacion op1, Operacion op2) {
				return Integer.compare(op1.getId(), op2.getId());
			}
		};
		ordenarLista(this.listaOperacion, comparador);
	}
	
	public void ordenarListaPorFecha(ArrayList<Operacion> listaOperacion) {
		Comparator<Operacion> comparador = new Comparator<Operacion>() {

			@Override
			public int compare(Operacion op1, Operacion op2) {
				return op1.getFecha().compareTo(op2.getFecha()); 
			}
		};
		ordenarLista(listaOperacion, comparador);
	}

	private ArrayList<Operacion> cargarOperaciones() {
		OperacionReader operacionReader = new OperacionReader(K_HEADER);
		ArrayList<Operacion> listaOperacion = new ArrayList<>();
		operacionReader.readAll(K_PATH_CSV, listaOperacion, ControlLevelError.LOAD_UNTIL_ERROR);
		return listaOperacion;
	}
	
	
	private void ordenarLista(ArrayList<Operacion> listaOperacion, Comparator<Operacion> comparador) {
		Collections.sort(listaOperacion, comparador);
	}
}
