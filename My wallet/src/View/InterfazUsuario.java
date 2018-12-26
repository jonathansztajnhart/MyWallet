package View;

import java.util.Date;

import inputOutput.Input;
import inputOutput.Output;
import wallet.Wallet;

public class InterfazUsuario {
	private Wallet wallet;
	private final static String K_DATOS_INVALIDOS =  "Los datos ingresados son invalidos";

	public InterfazUsuario(Wallet wallet) {
		this.wallet = wallet;
	}	

	public void start() {
		try {
			int seleccion;
			informarTareasDisponibles();
			seleccion = Input.pedirNroTarea();
			while(seleccion != 0) {
				if (seleccion >= 1 && seleccion <= 11) {
					realizarTarea(seleccion);
				}
				else {
					Output.mostrarMensaje(K_DATOS_INVALIDOS);
				}
				informarTareasDisponibles();
				seleccion = Input.pedirNroTarea();
			}
			wallet.guardaDatosWallet();
			wallet.cerrarRepositorio();
		} catch (IllegalArgumentException e) {
			start();
		}
	}
	
	private void informarTareasDisponibles() {
		Output.mostrarMensaje("1) Asignar Saldo");
		Output.mostrarMensaje("2) actualizar limite mensual de gastos");
		Output.mostrarMensaje("3) crear un gasto");
		Output.mostrarMensaje("4) crear un ingreso");
		Output.mostrarMensaje("5) modificar un gasto o ingreso");
		Output.mostrarMensaje("6) eliminar un gasto o ingreso");
		Output.mostrarMensaje("7) generar un reporte de gastos");
		Output.mostrarMensaje("8) mostrar lista de operaciones");
		Output.mostrarMensaje("9) informar saldo restante");
		Output.mostrarMensaje("10) informar total de gastos de este mes");
		Output.mostrarMensaje("11) informar limite mensual de gastos");
	}

	private void informarModicacionesDisponibles() {
		Output.mostrarMensaje("1) Modificar importe");
		Output.mostrarMensaje("2) Modificar categoria");
		Output.mostrarMensaje("3) Modificar descripción");
		Output.mostrarMensaje("4) Modificar fecha");
	}


	private void realizarTarea(int seleccion) {
		switch (seleccion) {
		case 1:
			agregarSaldo();
			break;
		case 2:
			agregarLimiteGastos();
			break;
		case 3:
			wallet.generarGasto();
			break;
		case 4:
			wallet.generarIngreso();
			break;	
		case 5:
			modificarOperacion();
			break;		
		case 6:
			eliminarOperacion();
			break;	
		case 7:
			generarReporteGastos();
			break;	
		case 8:
			mostrarOperaciones();
			break;
		case 9:
			mostrarSaldo();
			break;
		case 10:
			mostrarTotMesGastos();
			break;
		case 11:
			mostrarLimiteGastos();
			break;
		default:
			break;
		}
	}

	private void agregarSaldo() {
		try {
			double saldo = Input.pedirImporte();
			wallet.setSaldo(saldo);
		} catch (IllegalArgumentException e) {
			Output.mostrarMensaje(K_DATOS_INVALIDOS);
			agregarSaldo();
		}
	}

	private void agregarLimiteGastos() {
		try {
			double limiteGastos = Input.pedirImporte();
			wallet.setLimiteGastos(limiteGastos);
		} catch (IllegalArgumentException e) {
			Output.mostrarMensaje(K_DATOS_INVALIDOS);
			agregarLimiteGastos();
		}
	}

	private void modificarOperacion() {
		try {
			mostrarOperaciones();
			int id;
			int modificacion;
			do {
				id = Input.pedirIdOperacion();
				informarModicacionesDisponibles();
				modificacion = Input.pedirNroModificacion();
			}while (modificacion < 1 || modificacion > 4);
			realizarMoficacion(id, modificacion);
		} catch (IllegalArgumentException e) {
			modificarOperacion();
		}

	}

	private void realizarMoficacion(int id, int modificacion) {
		switch (modificacion) {
		case 1:
			wallet.modificarImporteOperacion(id);
			break;
		case 2:
			wallet.modificarCategoriaOperacion(id);
			break;	
		case 3:
			wallet.modificarDescOperacion(id);
			break;	
		case 4:
			wallet.modificarFechaOperacion(id);
			break;		
		default:
			break;
		}
	}

	private void eliminarOperacion() {
		try {
			mostrarOperaciones();
			int id = Input.pedirIdOperacion();
			wallet.eliminarOperacion(id);
		} catch (IllegalArgumentException e) {
			eliminarOperacion();
		}

	}

	private void generarReporteGastos() {
		try {
			int mesDesde;
			int mesHasta;
			int anioDesde;
			int anioHasta; 
			mesDesde = Input.pedirMesDesdeReporte();
			while(!validarNroMes(mesDesde)){
				Output.mostrarMensaje("mes invalido");
				mesDesde = Input.pedirMesDesdeReporte();
			}
			anioDesde = Input.pedirAnioDesdeReporte();
			while(!validarMesAnioHasta(mesDesde, anioDesde)){
				Output.mostrarMensaje("El mes y año seleccionado es posterior al mes y año actual");
				anioDesde = Input.pedirAnioDesdeReporte();
			}
			mesHasta = Input.pedirMesHastaReporte();
			while(!validarNroMes(mesHasta)){
				Output.mostrarMensaje("mes invalido");
				mesHasta = Input.pedirMesDesdeReporte();
			}
			anioHasta = Input.pedirAnioHastaReporte();
			while(!validarMesAnioHasta(mesHasta, anioHasta)){
				Output.mostrarMensaje("El mes y año seleccionado es posterior al mes y año actual");
				anioHasta = Input.pedirAnioDesdeReporte();
			}
			if(!validarDatosReporte(mesDesde, mesHasta, anioDesde, anioHasta)){
				Output.mostrarMensaje("la primer combinación mes y año es posterior a la segunda");
				generarReporteGastos();
			}
			wallet.generarReporteGastos(mesDesde, mesHasta, anioDesde, anioHasta);
		} catch (IllegalArgumentException e) {
			generarReporteGastos();
		}
	}

	private boolean validarDatosReporte(int mesDesde, int mesHasta, int anioDesde, int anioHasta) {
		boolean esValido = false;
		if(anioDesde < anioHasta) {
			esValido = true;
		}
		else if(anioDesde == anioHasta) {
			if (mesDesde <= mesHasta) {
				esValido = true;
			}
		}
		return esValido;
	}

	private boolean validarNroMes(int mes) {
		return mes >= 1 && mes <= 12;
	}

	private boolean validarMesAnioHasta(int mes, int anio) {
		boolean esValido = false;
		Date fechaActual = new Date();
		int anioActual = fechaActual.getYear() + 1900;
		if(anio != 0){
			if(anio < anioActual) {
				esValido = true;
			}
			else if(anio == anioActual) {
				if (mes <= fechaActual.getMonth() + 1) {
					esValido = true;
				}
			}
		}
		return esValido;
	}

	private void mostrarOperaciones() {
		wallet.informarOperaciones();
	}

	private void mostrarSaldo() {
		Output.mostrarMensaje("Saldo restante: $" + wallet.getSaldo());
	}

	private void mostrarTotMesGastos() {
		Output.mostrarMensaje("Gastos totales de este mes: $" + wallet.getTotMesGastos());

	}
	private void mostrarLimiteGastos() {
		Output.mostrarMensaje("Limite mesnual de gastos: $" + wallet.getLimiteGastos());
	}
}
