package inputOutput;

import java.util.Date;
import java.util.Scanner;

public class Input {

	private final static Scanner input = new Scanner(System.in); 
	private final static String K_DATOS_INVALIDOS = "Los datos ingresados son invalidos";
	private final static String K_SIN_DESCRIPCION = "Sin Descripción";

	public static String pedirCategoria(){
		String categoria = pedirString("ingrese la categoria");
		return categoria;
	}

	/**
	 * Emite un mensaje y pide la carga de un string.
	 * @return el String cargado.
	 */
	private static String pedirString(String mensaje) {
		Output.mostrarMensaje(mensaje);
		String string = input.nextLine();
		return string;
	}

	public static double pedirImporte() throws IllegalArgumentException{
		double importe = 0;
		try {
			Output.mostrarMensaje("ingrese el importe");
			importe = Double.parseDouble(input.nextLine());
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(K_DATOS_INVALIDOS);
		}
		catch (NullPointerException e) {
			throw new IllegalArgumentException(K_DATOS_INVALIDOS);
		}
		return importe;
	}

	public static String pedirDescripcion(){
		String descripcion = pedirString("ingrese la descripcion");
		if (descripcion.equals("")) {
			descripcion = K_SIN_DESCRIPCION;
		}
		return descripcion;
	}

	public static Date pedirFecha() throws IllegalArgumentException {
		int dia = pedirDia();
		int mes = pedirMes();
		int anio = pedirAnio();
		return convertir(dia, mes, anio);
	}

	private static int pedirDia() {
		int dia = 0;
		try {
			dia = pedirEntero("ingrese la fecha (nro)");
			while(dia <= 0 || dia > 31){
				Output.mostrarMensaje("ingreso invalido, reingrese el dia");
				dia = pedirEntero("ingrese la fecha (nro)");
			}
		} catch (IllegalArgumentException e) {
			Output.mostrarMensaje(e.getMessage());
			dia = pedirDia();
		}
		return dia;
	}

	private static int pedirMes() {
		int mes = 0;
		try {
			mes = pedirEntero("ingrese el mes (en nro)") - 1;
			while(mes == -1 || mes > 11){
				Output.mostrarMensaje("ingreso invalido, reingrese el mes");
				mes = pedirEntero("ingrese el mes (en nro)") - 1;
			}
		} catch (IllegalArgumentException e) {
			Output.mostrarMensaje(e.getMessage());
			mes = pedirMes();
		}
		return mes;
	}

	private static int pedirAnio() {
		int anio = 0;
		try {
			anio = pedirEntero("ingrese el año") - 1900;
			while(anio == 0){
				Output.mostrarMensaje("ingreso invalido, reingrese el año");
				anio = pedirEntero("ingrese el año");
			}
		} catch (IllegalArgumentException e) {
			Output.mostrarMensaje(e.getMessage());
			anio = pedirAnio();
		}
		return anio;
	}

	public static int pedirNroTarea() throws IllegalArgumentException{
		return pedirEntero("Seleccione la opcion deseada para realizar una tarea o 0 para finalizar");
	}

	public static int pedirIdOperacion() throws IllegalArgumentException {
		return pedirEntero("ingrese id Operacion");
	}

	public static int pedirNroModificacion() throws IllegalArgumentException {
		return pedirEntero("Seleccione opcion deseada");
	}
	public static int pedirNroCatergoria() {
		return pedirEntero("Seleccione la categoria deseada o 0 para crear una nueva categoria");
	}

	public static int pedirMesDesdeReporte()  {
		int mes = 0;
		try{
			mes = pedirEntero("seleccione el mes desde");
		}catch (IllegalArgumentException e){
			Output.mostrarMensaje("ingreso invalido, reingrese el mes");
			mes = pedirMesDesdeReporte();
		}
		return mes;
	}

	public static int pedirMesHastaReporte() {
		int mes = 0;
		try{
			mes = pedirEntero("seleccione el mes hasta");
		}catch (IllegalArgumentException e){
			Output.mostrarMensaje("ingreso invalido, reingrese el mes");
			mes = pedirMesHastaReporte();
		}
		return mes;
	}

	public static int pedirAnioDesdeReporte(){
		int anio = 0;
		try{
			anio = pedirEntero("seleccione el anio desde");
		}catch (IllegalArgumentException e){
			Output.mostrarMensaje("ingreso invalido, reingrese el anio");
			anio = pedirAnioDesdeReporte();
		}
		return anio;
	}

	public static int pedirAnioHastaReporte() throws IllegalArgumentException {
		int anio = 0;
		try{
			anio = pedirEntero("seleccione el anio hasta");
		}catch (IllegalArgumentException e){
			Output.mostrarMensaje("ingreso invalido, reingrese el anio");
			anio = pedirAnioHastaReporte();
		}
		return anio;
	}

	private static int pedirEntero(String mensaje) throws IllegalArgumentException{
		String valorString = null;
		int valor = 0;
		try {
			Output.mostrarMensaje(mensaje);
			valorString = input.nextLine();
			if (!valorString.equals("")) {
				valor = Integer.parseInt(valorString);
			}
		} catch(NumberFormatException e) {
			throw new IllegalArgumentException(K_DATOS_INVALIDOS);
		}
		return valor;
	}
	private static Date convertir(int dia, int mes, int anio) throws IllegalArgumentException {
		Date fecha = new Date(anio, mes, dia);
		if(dia != fecha.getDate() || mes != fecha.getMonth() || anio != fecha.getYear()){
			throw new IllegalArgumentException(K_DATOS_INVALIDOS);
		}
		return fecha;
	}
}
