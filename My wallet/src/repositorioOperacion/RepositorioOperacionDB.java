package repositorioOperacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import connection.ConnectionDB;
import enumerations.TipoOperacion;
import inputOutput.Input;
import inputOutput.Output;
import wallet.Gasto;
import wallet.GeneradorCategoriaOp;
import wallet.GeneradorOperacion;
import wallet.Ingreso;
import wallet.Operacion;

public class RepositorioOperacionDB implements RepositorioOperacion {

	private final static String K_GET_LISTA_GASTOS = "SELECT id, fecha, tipo, importe, categoria, descripcion FROM Operacion WHERE tipo = 'Gasto' ORDER BY fecha ASC";
	private final static String K_SEL_ALL_OP = "SELECT id, fecha, tipo, importe, categoria, descripcion FROM Operacion ORDER BY id ASC";
	private final static String K_DELETE_OP = "Delete from Operacion where id=?";
	private final static String K_UPDATE_IMP_OP = "UPDATE Operacion SET importe=? WHERE id=?";
	private final static String K_UPDATE_CAT_OP = "UPDATE Operacion SET categoria=? WHERE id=?";
	private final static String K_UPDATE_DESC_OP = "UPDATE Operacion SET descripcion=? where id=?";
	private final static String K_UPDATE_FEC_OP = "UPDATE Operacion SET fecha=? where id=?";
	private final static String K_INSERT_OP = "INSERT INTO Operacion (id, fecha, tipo, importe, categoria, descripcion) VALUES (?,?,?,?,?,?)";
	private final static String K_BUSCAR_OP = "SELECT id, fecha, tipo, importe, categoria, descripcion FROM Operacion WHERE id=?";
	private final static String K_CANTIDAD_OP = "SELECT MAX(id) AS CANTIDAD FROM  Operacion";
	private final static String K_DATOS_INVALIDOS = "Los datos ingresados son invalidos";
	private Connection connection;
	private GeneradorCategoriaOp generadorCategoriaOp;
	private GeneradorOperacion generadorOperacion;

	public RepositorioOperacionDB() throws SQLException {
		this.connection = ConnectionDB.getConection();
		this.generadorCategoriaOp = new GeneradorCategoriaOp();
		this.generadorOperacion = new GeneradorOperacion(generadorCategoriaOp);
		crearTablaOperaciones();
	}

	@Override
	public Operacion getOperacion(int id) {
		Operacion op = null;
		try {
			ResultSet rSet = buscarRegistro(id);
			if (rSet.next()) {
				if (rSet.getString("tipo").equals(String.valueOf(TipoOperacion.Gasto))) {
					op = new Gasto(rSet.getInt("id"), rSet.getDouble("importe"), rSet.getString("categoria"),
							rSet.getString("descripcion"), rSet.getDate("fecha"));
				} else {
					op = new Ingreso(rSet.getInt("id"), rSet.getDouble("importe"), rSet.getString("categoria"),
							rSet.getString("descripcion"), rSet.getDate("fecha"));
				}
			}

		} catch (SQLException e) {
			Output.mostrarMensaje("No se ha encontrado la operacion en la tabla");
		}
		return op;
	}

	@Override
	public ArrayList<Operacion> getListaGastos() {
		ArrayList<Operacion> listaGastos = new ArrayList<>();
		try {
			PreparedStatement statement = connection.prepareStatement(K_GET_LISTA_GASTOS);
			ResultSet rSet = statement.executeQuery();
			while (rSet.next()) {
				Operacion op = new Gasto(rSet.getInt("id"), rSet.getDouble("importe"), rSet.getString("categoria"),
						rSet.getString("descripcion"), rSet.getDate("fecha"));
				listaGastos.add(op);
			}
		} catch (SQLException e) {
			Output.mostrarMensaje("No se ha podido obtener la lista de gastos");
		}
		return listaGastos;
	}

	@Override
	public void informarOperaciones() {
		try {
			Operacion op;
			PreparedStatement statement = connection.prepareStatement(K_SEL_ALL_OP);
			ResultSet rSet = statement.executeQuery();
			while (rSet.next()) {
				if (rSet.getString("tipo").equals(String.valueOf(TipoOperacion.Gasto))) {
					op = new Gasto(rSet.getInt("id"), rSet.getDouble("importe"), rSet.getString("categoria"),
							rSet.getString("descripcion"), rSet.getDate("fecha"));
				} else {
					op = new Ingreso(rSet.getInt("id"), rSet.getDouble("importe"), rSet.getString("categoria"),
							rSet.getString("descripcion"), rSet.getDate("fecha"));
				}
				Output.mostrarMensaje(op.toString());
			}
		} catch (SQLException e) {
			Output.mostrarMensaje("No se ha encontrado la operacion en la tabla");
		}
	}

	@Override
	public Operacion crearOperacion(TipoOperacion tipo, double topeImporte) throws IllegalArgumentException {
		Operacion op = null;
		try {
			int proximoId = getNextId();
			op = generadorOperacion.crearOperacion(proximoId, tipo, topeImporte);
			guardarOperacion(op);
		} catch (SQLException e) {
			e.printStackTrace();
			Output.mostrarMensaje("No se ha pudido insertar la operacion en la tabla");
		}
		return op;
	}

	@Override
	public Operacion eliminarOperacion(int id) {
		Operacion operacion = null;
		try {
			operacion = getOperacion(id);
			PreparedStatement statement = connection.prepareStatement(K_DELETE_OP);
			statement.setString(1, String.valueOf(id));
			statement.executeUpdate();
		} catch (SQLException e) {
			Output.mostrarMensaje("No se pudo eliminar la operacion en la tabla");
		}
		return operacion;
	}

	@Override
	public double modificarImporteOp(int id, double topeImporte) throws IllegalArgumentException {
		double nuevoImporte = 0;
		try {
			Operacion op = getOperacion(id);
			nuevoImporte = generadorOperacion.pedirImporte(op.getTipo(), topeImporte);
			PreparedStatement statement = connection.prepareStatement(K_UPDATE_IMP_OP);
			statement.setString(1, String.valueOf(nuevoImporte));
			statement.setString(2, String.valueOf(id));
			statement.execute();
		} catch (SQLException e) {
			Output.mostrarMensaje("no se pudo modificar la operacion en la tabla");
		}
		return nuevoImporte;
	}

	@Override
	public void modificarCategoriaOp(int id) throws IllegalArgumentException {
		try {
			Operacion op = getOperacion(id);
			String categoria = generadorOperacion.pedirCategoria(op.getTipo());
			PreparedStatement statement = connection.prepareStatement(K_UPDATE_CAT_OP);
			statement.setString(1, categoria);
			statement.setString(2, String.valueOf(id));
			statement.execute();

		} catch (SQLException e) {
			Output.mostrarMensaje("no se pudo modificar la operacion en la tabla");
		}
	}

	@Override
	public void modificarDescripcionOp(int id) {
		try {
			ResultSet rSet = buscarRegistro(id);
			if (rSet.next()) {
				String descripcion = Input.pedirDescripcion();
				PreparedStatement statement = connection.prepareStatement(K_UPDATE_DESC_OP);
				statement.setString(1, descripcion);
				statement.setString(2, String.valueOf(id));
				statement.execute();
			}
		} catch (SQLException e) {
			Output.mostrarMensaje("no se pudo modificar la operacion en la tabla");
		}
	}

	@Override
	public Date modificarFechaOp(int id) throws IllegalArgumentException {
		Date fecha = null;
		try {
			Operacion op = getOperacion(id);
			fecha = generadorOperacion.pedirFecha(op.getTipo());
			PreparedStatement statement = connection.prepareStatement(K_UPDATE_FEC_OP);
			statement.setString(1, (fecha.getYear() + 1900) + "-" + (fecha.getMonth() + 1) + "-" + fecha.getDate());
			statement.setString(2, String.valueOf(id));
			statement.execute();

		} catch (SQLException e) {
			Output.mostrarMensaje("no se pudo modificar la operacion en la tabla");
		}
		return fecha;
	}

	private void crearTablaOperaciones() throws SQLException {
		PreparedStatement statement = connection
				.prepareStatement("IF OBJECT_ID('OPERACION') IS NULL " + "BEGIN " + "CREATE TABLE [dbo].[Operacion] ( "
						+ "[id][int] NOT NULL, " + "[fecha][Date] NOT NULL, " + "[tipo][nvarchar](10) NOT NULL, "
						+ "[importe][decimal](10,2) NOT NULL, " + "[categoria][nvarchar](50) NOT NULL, "
						+ "[descripcion][nvarchar](50) NULL, " + "CONSTRAINT[PK_Operacion] PRIMARY KEY CLUSTERED "
						+ "( " + "[id] " + ")  ON [PRIMARY] " + ") ON [PRIMARY] " + " END ");
		statement.execute();
	}

	@Override
	public void cerrar() {
		try {
			connection.close();
			generadorCategoriaOp.guardarListaCategoriasGastos();
		} catch (SQLException e) {
			Output.mostrarMensaje("no se puede cerrar la base de datos");
		}
	}

	private void guardarOperacion(Operacion operacion) throws SQLException {
		 PreparedStatement statement = connection.prepareStatement(K_INSERT_OP);
		 statement.setString(1, String.valueOf(operacion.getId()));
		 statement.setString(2, operacion.getFechaString());
		 statement.setString(3, operacion.getTipo().toString());
		 statement.setString(4, String.valueOf(operacion.getImporte()));
		 statement.setString(5, operacion.getCategoria());
		 statement.setString(6, operacion.getDescripcion());

//		String id = String.valueOf(operacion.getId());
//		String fecha = operacion.getFechaString();
//		String tipo = operacion.getTipo().toString();
//		String importe = String.valueOf(operacion.getImporte());
//		String categoria = operacion.getCategoria();
//		String desc = operacion.getDescripcion();
//
//		String query = "INSERT INTO Operacion (id, fecha, tipo, importe, categoria, descripcion) VALUES (%s,%s,%s,%s,%s,%s)";
//		query = String.format(query, id, fecha, tipo, importe, categoria, desc);
//		
//		System.out.println(query);
//		
//		PreparedStatement stmt = connection.prepareStatement(query);
		statement.executeUpdate();
	}

	private ResultSet buscarRegistro(int id) throws SQLException {
		ResultSet rSet;
		PreparedStatement statement = connection.prepareStatement(K_BUSCAR_OP);
		statement.setString(1, String.valueOf(id));
		rSet = statement.executeQuery();
		return rSet;
	}

	private int getNextId() throws SQLException {
		PreparedStatement statement = connection.prepareStatement(K_CANTIDAD_OP);
		ResultSet rSet = statement.executeQuery();
		int cantidad = 0;
		if (rSet.next()) {
			cantidad = rSet.getInt("CANTIDAD") + 1;
		}
		return cantidad;
	}
}
