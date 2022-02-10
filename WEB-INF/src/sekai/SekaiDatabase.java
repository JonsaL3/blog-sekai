package sekai;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SekaiDatabase {

	final private static String BBDDNAME = "sekai_database.db";
	
	private static Connection conn;
	private static String sql;
	
	public SekaiDatabase()
	{
		conn = null;
		sql = "";
	}
	
	// En esta serie de funciones irán las CRUD
	
	public String findAuthorUsernameOfEntry(Entrada e)
	{
		boolean success = openDatabase();
		String username = "";
		
		if (success)
		{
			try
			{
				ResultSet resultado;
				sql = "SELECT usuario.usuario FROM usuario INNER JOIN entrada ON (entrada.usuario_escritor LIKE usuario.usuario) WHERE entrada.id = ?";
				PreparedStatement p = conn.prepareStatement(sql);
				
				p.setInt(1, e.getId());
				resultado = p.executeQuery();
				
				if (resultado.next())
				{
					username = resultado.getString("usuario");
				}
			}
			catch(SQLException e1)
			{
				success = false;
				System.err.println(e1);
			}
		}
		
		closeDatabase();
		return username;
	}
	
	public Set<Entrada> findAllEntrys()
	{
		boolean success = openDatabase();
		Set<Entrada> entrys = new HashSet<Entrada>();
		
		if (success)
		{
			try
			{
				Statement s = conn.createStatement();
				ResultSet resultado;
				sql = "SELECT * FROM entrada";
				resultado = s.executeQuery(sql);
				
				while (resultado.next())
				{
					entrys.add(new Entrada(resultado.getInt("id"), resultado.getString("titulo"), resultado.getString("texto"), resultado.getInt("fecha")));
				}
				
			}
			catch (SQLException e)
			{
				success = false;
				System.err.println(e);
			}
		}
		
		closeDatabase();
		return entrys;
	}
	
	public boolean updateEntry(Entrada e)
	{
		boolean success = openDatabase();
		
		if (success)
		{
			try
			{
				sql = "UPDATE entrada SET titulo = ?, texto = ? WHERE id = ?";
				PreparedStatement p = conn.prepareStatement(sql);
				
				p.setString(1, e.getTitulo());
				p.setString(2, e.getTexto());
				p.setInt(3, e.getId());
				p.executeUpdate();
			}
			catch (SQLException e1)
			{
				success = false;
				System.err.println(e1);
			}
		}
		
		closeDatabase();
		return success;
	}
	
	// Se que con la id sería suficiente para borrar una entrada, pero prefiero traerme el objeto entero
	// porsiacaso en un hipotetico futuro hiciese falta
	public boolean deleteEntry(Entrada e)
	{
		boolean success = openDatabase();
		
		if (success)
		{
			try
			{
				sql = "DELETE FROM entrada WHERE id = ?";
				PreparedStatement p = conn.prepareStatement(sql);
				
				p.setInt(1, e.getId());
				p.executeUpdate();
			}
			catch (SQLException e1)
			{
				success = false;
				System.err.println(e1);
			}
		}
		
		closeDatabase();
		return success;
	}
	
	public Optional<Entrada> findEntryById(int id)
	{
		boolean success = openDatabase();
		Optional<Entrada> entrada = Optional.empty();
		
		if (success)
		{
			try
			{
				sql = "SELECT * FROM entrada WHERE id LIKE ?";
				
				PreparedStatement p; 
				p = conn.prepareStatement(sql);
				p.setInt(1, id);
				ResultSet resultado = p.executeQuery();
				Entrada e;
				
				if (resultado.next())
				{
					e = new Entrada(resultado.getInt("id"), resultado.getString("titulo"), resultado.getString("texto"), resultado.getInt("fecha"));
					entrada = Optional.of(e);
				}
			}
			catch (SQLException e)
			{
				success = false;
				System.err.println(e);
			}
		}
		
		closeDatabase();
		return entrada;
	}
	
	public boolean insertEntry(Usuario u, Entrada e)
	{
		boolean success = openDatabase();
		
		if (success)
		{
			try
			{
				sql = "INSERT INTO entrada (titulo, texto, fecha, usuario_escritor) VALUES (?,?,?,?)";
				PreparedStatement p = conn.prepareStatement(sql);
				
				p.setString(1, e.getTitulo());
				p.setString(2, e.getTexto());
				p.setInt(3, e.getFecha());
				p.setString(4, u.getUsuario());
				p.executeUpdate();
			} 
			catch (SQLException e1) 
			{
				success = false;
				System.err.println(e1);
			}
			
		}
		
		closeDatabase();
		return success;
	}
	
	public boolean createSekaiDatabase()
	{
		boolean success = openDatabase();
		
		if (success)
		{
			try
			{
				Statement s = conn.createStatement();
				
				sql = "CREATE TABLE IF NOT EXISTS usuario (usuario TEXT PRIMARY KEY, password TEXT)";
				s.execute(sql);
				
				sql = 
						
				"""
				CREATE TABLE IF NOT EXISTS entrada (
						id INTEGER PRIMARY KEY AUTOINCREMENT,
						titulo TEXT,
						texto TEXT,
						fecha INTEGER,
						usuario_escritor TEXT,
						FOREIGN KEY (usuario_escritor) REFERENCES usuario(usuario)
						ON DELETE CASCADE ON UPDATE CASCADE
				)		
				""";
				
				s.execute(sql);
			}
			catch (SQLException e)
			{
				success = false;
				System.err.println(e);
			}
			
		}
		
		closeDatabase();
		return success;
	}
	
	public int updatePassword(Usuario u, String newPassword)
	{
		boolean success = openDatabase();
		int rowsAffected = 0;
		
		if (success)
		{
			sql = "UPDATE usuario SET password = ? WHERE usuario LIKE ?";
			
			try 
			{
				PreparedStatement p = conn.prepareStatement(sql);
				p.setString(1, newPassword);
				p.setString(2, u.getUsuario());
				rowsAffected = p.executeUpdate();
			} 
			catch (SQLException e) 
			{
				rowsAffected = -1;
				System.err.println(e);
			}
		}
		
		closeDatabase();
		return rowsAffected;
	}
	
	public int deleteUser(Usuario u)
	{
		boolean success = openDatabase();
		int rowsAffected = 0;
		
		if (success)
		{
			sql = "DELETE FROM usuario WHERE usuario LIKE ?";
			
			try 
			{
				PreparedStatement p = conn.prepareStatement(sql);
				p.setString(1, u.getUsuario());
				rowsAffected = p.executeUpdate();
			} 
			catch (SQLException e) 
			{
				rowsAffected = -1;
				System.err.println(e);
			}
		}
		
		closeDatabase();
		return rowsAffected;
	}
	
	public boolean insertUser(Usuario u)
	{
		boolean success = openDatabase();
		
		if (success)
		{
			sql = "INSERT INTO usuario (usuario, password) VALUES(?,?)";
			
			try 
			{
				PreparedStatement p = conn.prepareStatement(sql);
				p.setString(1, u.getUsuario());
				p.setString(2, u.getPassword());
				p.executeUpdate();
			} 
			catch (SQLException e) 
			{
				success = false;
				System.err.println(e);
			}
			
		}
		
		closeDatabase();
		return success;
	}
	
	public Set<Usuario> findAllUsuarios()
	{
		boolean success = openDatabase();
		Optional<Usuario> u = Optional.empty();
		Set<Usuario> usuarios = new HashSet<Usuario>();
		
		if (success)
		{
			try
			{
				Statement s; 
				ResultSet resultado, resultado2;
				Usuario us;
				
				sql = "SELECT * FROM usuario";
				s = conn.createStatement();
				resultado = s.executeQuery(sql);
				
				while (resultado.next())
				{
					us = new Usuario(resultado.getString("usuario"), resultado.getString("password"), new HashSet<Entrada>());
					
					sql = "SELECT entrada.* FROM entrada INNER JOIN usuario ON (entrada.usuario_escritor = usuario.usuario) WHERE entrada.usuario_escritor LIKE ?";
					PreparedStatement s2 = conn.prepareStatement(sql);
					resultado2 = s2.executeQuery();
					
					while (resultado2.next())
					{
						us.addEntrada(new Entrada(resultado2.getInt("id"), resultado2.getString("titulo"), resultado2.getString("texto"), resultado2.getInt("fecha")));
					}
					
					u = Optional.of(us);
					
					if (u.isPresent()) // mejor prevenir
						usuarios.add(u.get());
				}
			}
			catch (SQLException e)
			{
				success = false;
				System.err.println(e);
			}
		}
		
		closeDatabase();
		return usuarios;
		
	}
	
	public Optional<Usuario> findUsuarioById(String usuario)
	{
		boolean success = openDatabase();
		Optional<Usuario> u = Optional.empty();
		
		if (success)
		{
			sql = "SELECT * FROM usuario WHERE usuario LIKE ?";
			
			try
			{
				PreparedStatement p; 
				p = conn.prepareStatement(sql);
				p.setString(1, usuario);
				ResultSet resultado = p.executeQuery();
				Usuario us;
				
				if (resultado.next())
				{
					us = new Usuario(resultado.getString("usuario"), resultado.getString("password"), new HashSet<Entrada>());
					
					sql = "SELECT entrada.* FROM entrada INNER JOIN usuario ON (entrada.usuario_escritor = usuario.usuario) WHERE entrada.usuario_escritor LIKE ?";
					PreparedStatement s;
					s = conn.prepareStatement(sql);
					s.setString(1, usuario);
					resultado = s.executeQuery();
					
					while (resultado.next())
					{
						us.addEntrada(new Entrada(resultado.getInt("id"), resultado.getString("titulo"), resultado.getString("texto"), resultado.getInt("fecha")));
					}
					
					u = Optional.of(us);
				}
			}
			catch (SQLException e)
			{
				success = false;
				System.err.println(e);
			}
		}
		
		closeDatabase();
		return u;
		
	}
	
	public boolean databaseExists()
	{
		// si hubiese usado otra ubicacion para la base de datos en el constructor
		// pondría algo del estilo: C: \ users\pepe\superSecuredFolder\sekaiDatabase.db
		File baseDeDatos = new File("sekai_database.db");
		return baseDeDatos.exists();
	}
	
	// En estas otras operaciones para administrar la base de datos
	
	public boolean closeDatabase() 
	{
		boolean success = conn != null;
		
		if (success)
		{
			try 
			{
				conn.close();
				conn = null;
			} 
			catch (SQLException e) 
			{
				success = false;
				e.printStackTrace();
			}
		}
		
		return success;
	}
	
	public boolean openDatabase()
	{
		boolean sucess = conn == null;
		String url;
		
		if (sucess)
		{
			try
			{
				// Sé que lo óptimo es utilizar la ruta absoluta de algún lugar distinto al proyecto
				//en lugar de poner aqui directamente
				// la base de datos, puesto que así es muy vulnerable.
				// Pero como lo vas a corregir en tu ordenador y no en el mio, creo que lo mas 
				// como es dejar asi la bbdd
				url = "jdbc:sqlite:" + BBDDNAME;
				Class.forName("org.sqlite.JDBC").getDeclaredConstructor().newInstance();
		        conn = DriverManager.getConnection(url);
			}
			catch (Exception e)
			{
				sucess = false;
				System.err.println(e);
			}
		}
		
		return sucess;
	}
	
}
