package sekai;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.stringtemplate.v4.ST;


@SuppressWarnings("serial")
public class Panel extends HttpServlet {
	
	private SekaiDatabase db = new SekaiDatabase();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		if (!db.databaseExists())
			resp.sendRedirect(req.getContextPath() + "/installer");
		else
			doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		if (!db.databaseExists())
			resp.sendRedirect(req.getContextPath() + "/installer");
		else
		{
			PrintWriter out;
			ST html, rowEditor, adminPanelUsers, usersAdminMenu;
			HttpSession session;
        
			out = resp.getWriter();
			session = req.getSession(false);
			resp.setCharacterEncoding("UTF-8");
			resp.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
			resp.setHeader("Pragma", "no-cache");
			html = new ST(HTMLTemplate.getPanel(),'$','$');
			usersAdminMenu = new ST(HTMLTemplate.getAdminTableUsers(),'$','$');

			if (session != null)
			{
				String usuario = (String) session.getAttribute("usuario_de_la_sesion");
				String entrys = "";
				Usuario u = db.findUsuarioById(usuario).get(); // se que va a estar seguro porque hay sesion
				String current, new1, new2, stringUsers;
        	
				if (usuario.equals("admin"))
				{
					Set<Usuario> usuarios = db.findAllUsuarios();
            	
					String newUser, newPass1, newPass2;
        		
					ST createUsers = new ST(HTMLTemplate.getAdminCreateUser(),'$','$');
					createUsers.add("user_added", "");

					newUser = req.getParameter("newUser");
					newPass1 = req.getParameter("newPass1");
					newPass2 = req.getParameter("newPass2");
        		
					if 
					(
							newUser != null &&
							newPass1 != null &&
							newPass2 != null &&
							newPass1.equals(newPass2)
					)
					{
						// convierto los caracteres que me pueden dar problemas
						newUser = StringHTMLizer.convert(newUser);
						newPass1 = StringHTMLizer.convert(newPass1);
						newPass2 = StringHTMLizer.convert(newPass2);
            		
						// antes de nada encripto las contraseñas
						newPass1 = PasswordEncryptor.encrypt(newPass1);
						newPass2 = PasswordEncryptor.encrypt(newPass2);
            		
						if (db.insertUser(new Usuario(newUser, newPass1, new HashSet<Entrada>())))
						{
							createUsers.add("user_added", "Usuario " + newUser + " agregado exitosamente");
							resp.setHeader("refresh","0;/sekai-project-gonzalo-racero-galan/panel");
						}
						else // esto solo se muestra un milisegundo antes de que se actualice la pagina pero bueno...
							createUsers.add("user_added", "Hubo un error al intentar introducir el usuario, es posible que este ya exista");
					}
					else
						createUsers.add("user_added", "");
        		
					html.add("create_users", createUsers.render());
					stringUsers = "";
        		
					for (Usuario user : usuarios)
					{	
						adminPanelUsers = new ST(HTMLTemplate.getAdminPanelUsers(),'$','$');
						adminPanelUsers.add("user_id", user.getUsuario());
						stringUsers += adminPanelUsers.render();
					}
            	
					usersAdminMenu.add("users_only_see_admin", stringUsers);
					html.add("users_admin_menu", usersAdminMenu.render());
				}
				else
				{
					html.add("create_users", "");
					html.add("users_only_see_admin", "");
				}
        	
				for (Entrada e : u.getEntradas())
				{
					rowEditor = new ST(HTMLTemplate.getRowEditorPanel(),'$','$');
					rowEditor.add("entry_id", e.getId());
					rowEditor.add("title_entry", e.getTitulo());
					entrys += rowEditor.render();
				}
				
				html.add("username", usuario);
				html.add("entrys_user", entrys);
        	
				current = (String) req.getParameter("current");
				new1 = (String) req.getParameter("new1");
				new2 = (String) req.getParameter("new2");
        	
				if (current != null && new1 != null && new2 != null)
				{
					current = StringHTMLizer.convert(current);
					new1 = StringHTMLizer.convert(new1);
					new2 = StringHTMLizer.convert(new2);
            	
					current = PasswordEncryptor.encrypt(current);
					new1 = PasswordEncryptor.encrypt(new1);
					new2 = PasswordEncryptor.encrypt(new2);
            	
					if (new1.equals(new2) && u.getPassword().equals(current))
					{
						if (db.updatePassword(u, new2) > 0)
							html.add("password_changed", "Contraseña cambiada exitosamente");
						else
							html.add("password_changed", "Se produjo un error en el cambio de contraseña");
					}
					else
					{
						html.add("password_changed", "Se produjo un error en el cambio de contraseña");
					}
				}
				else
					html.add("password_changed", "");
        	
				out.println(html.render());
			}
			else
				resp.sendRedirect(req.getContextPath() + "/login");
			} 
	}
}