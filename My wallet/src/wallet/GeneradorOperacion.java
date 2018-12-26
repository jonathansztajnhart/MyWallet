package wallet;

import java.util.Date;

import enumerations.TipoOperacion;
import inputOutput.Input;
import inputOutput.Output;

public class GeneradorOperacion {

	private final static String K_CAT_INVALIDA = "La categoria ingresada es invalida";
	private static final String K_FECHA_INVALIDA = "La fecha ingresada es invalida";
	private static final String K_IMPORTE_INVALIDO = "el saldo restante es insuficiente";
	private GeneradorCategoriaOp generadorCategoriaOperacion;

	public GeneradorOperacion(GeneradorCategoriaOp generadorCategoriaGastos) {
		this.generadorCategoriaOperacion = generadorCategoriaGastos;
	}

	public Operacion crearOperacion(int proximoIndice,TipoOperacion tipo, double topeImporte) throws IllegalArgumentException{
		Operacion op = pedirOperacion(tipo, proximoIndice, topeImporte);
		return op;
	}

	public boolean validarImporte(double importe, double topeImporte, TipoOperacion tipo) {
		boolean esValido = false;
		if (importe > 0) {
			if(tipo.equals(TipoOperacion.Gasto)){
				if(importe <= topeImporte){
					esValido = true;
				}
			}
			else{
				esValido = true;
			}
		}
		return esValido;
	}

	public boolean validarCategoria(String categoria) {
		boolean esValido = false;
		if (!categoria.equals("")) {
			esValido = true;
		}
		return esValido;
	}

	public boolean validarFecha(Date fecha) {
		boolean esValido = false;
		if(fecha.compareTo(new Date()) <= 0) {
			esValido = true;
		}
		return esValido;
	}

	private Operacion pedirOperacion(TipoOperacion tipo, int id, double topeImporte) throws IllegalArgumentException{
		Operacion op = null;
		double importe = pedirImporte(tipo, topeImporte);
		String descripcion = Input.pedirDescripcion();
		String categoria = pedirCategoria(tipo);
		Date fecha = pedirFecha(tipo);
		if(tipo.equals(TipoOperacion.Gasto)) {
			op = new Gasto(id, importe, categoria, descripcion, fecha);
		}
		else {
			op = new Ingreso(id, importe, categoria, descripcion, fecha);
		}
		return op;
	}
	public String pedirCategoria(TipoOperacion tipo){
		String categoria = null;
		try{
			categoria = generadorCategoriaOperacion.pedirCategoria(tipo.toString());
			while(!validarCategoria(categoria)){
				Output.mostrarMensaje(K_CAT_INVALIDA);
				categoria = generadorCategoriaOperacion.pedirCategoria(tipo.toString());
			}
		}catch (IllegalArgumentException e){
			Output.mostrarMensaje(e.getMessage());
			pedirCategoria(tipo);
		}
		return categoria;
	}

	public Date pedirFecha(TipoOperacion tipo){
		Date fecha = null;
		try{
			fecha = Input.pedirFecha();
			while(!validarFecha(fecha)){
				Output.mostrarMensaje(K_FECHA_INVALIDA);
				fecha = Input.pedirFecha();
			}
		}catch (IllegalArgumentException e){
			Output.mostrarMensaje(e.getMessage());
			fecha = pedirFecha(tipo);
		}
		return fecha;
	}

	public double pedirImporte(TipoOperacion tipo, double topeImporte){
		double importe = 0;
		try{
			importe = Input.pedirImporte();
			while(!validarImporte(importe, topeImporte, tipo)){
				Output.mostrarMensaje(K_IMPORTE_INVALIDO);
				importe = Input.pedirImporte();
			}
		}catch (IllegalArgumentException e){
			Output.mostrarMensaje(e.getMessage());
			importe = pedirImporte(tipo, topeImporte);
		}
		return importe;
	}
}
