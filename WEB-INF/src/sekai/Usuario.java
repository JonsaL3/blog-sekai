package sekai;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Usuario {

	private String usuario;
	private String password;
	private Set<Entrada> entradas;
	
	public Usuario(String usuario, String password, Set<Entrada> entradas) 
	{
		this.usuario = usuario;
		this.password = password;
		this.entradas = entradas;
	}
	
	public Usuario() 
	{
		this ("","", new HashSet<Entrada>());
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Entrada> getEntradas() {
		return entradas;
	}

	public void setEntradas(Set<Entrada> entradas) {
		this.entradas = entradas;
	}

	@Override
	public String toString() {
		return "Usuario [usuario=" + usuario + ", password=" + password + ", entradas=" + entradas + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(usuario);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(usuario, other.usuario);
	}

	public boolean addEntrada(Entrada e)
	{
		return entradas.add(e);
	}
	
	public boolean removeEntrada(Entrada e)
	{
		return entradas.removeIf(esta -> esta.equals(e));
	}
	
}
