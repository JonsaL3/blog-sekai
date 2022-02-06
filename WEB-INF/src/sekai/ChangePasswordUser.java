package sekai;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.stringtemplate.v4.ST;


@SuppressWarnings("serial")
public class ChangePasswordUser extends HttpServlet {
	
	private SekaiDatabase db = new SekaiDatabase();
	private String userToChange = "";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		PrintWriter out;
        ST html;
        HttpSession session;
        
        out = resp.getWriter();
        session = req.getSession(false);
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        html = new ST(HTMLTemplate.getUpdatePasswordUser(),'$','$');

        if (session != null)
        {
        	String usuario = (String) session.getAttribute("usuario_de_la_sesion");
        	
        	if (usuario.equals("admin"))
        	{
        		String idUserUpdate;
        		Optional<Usuario> userUpdate;
        		
        		idUserUpdate = req.getParameter("user_id");
        		userUpdate = db.findUsuarioById(idUserUpdate);
        		
        		if (userUpdate.isPresent())
        		{
        			html.add("user_to_change", idUserUpdate);
        			html.add("updated_password", "Cambio de contraseñas");
        			userToChange = idUserUpdate;
        		}
        		else
        			html.add("updated_password", "El usuario al que quieres cambiarle la contraseña no existe");
        		
        		out.print(html.render());
        	}
        	else
        		resp.sendRedirect(req.getContextPath() + "/login");
        	
        }
        else
        	resp.sendRedirect(req.getContextPath() + "/login");
	}
	
	// Entiendo que como el usuario que cambia las contraseñas es el administrador, no hace falta que introduzca la contraseña antigua antes
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		PrintWriter out;
        ST html;
        HttpSession session;
        
        out = resp.getWriter();
        session = req.getSession(false);
        resp.setCharacterEncoding("UTF-8");
        html = new ST(HTMLTemplate.getUpdatePasswordUser(),'$','$');

        if (session != null)
        {
        	String usuario = (String) session.getAttribute("usuario_de_la_sesion");
        	
        	if (usuario.equals("admin"))
        	{
        		Optional<Usuario> userUpdate;
        		
        		if (!userToChange.isEmpty())
        		{
        			userUpdate = db.findUsuarioById(userToChange);
        			Usuario user = userUpdate.get();
        			String pass1, pass2;
        			
        			html.add("user_to_change", userToChange);
        			
        			pass1 = req.getParameter("pass1");
        			pass2 = req.getParameter("pass2");

        			if (pass1 != null && pass2 != null)
        			{
        				pass1 = StringHTMLizer.convert(pass1);
            			pass2 = StringHTMLizer.convert(pass2);
            			
            			pass1 = PasswordEncryptor.encrypt(pass1);
            			pass2 = PasswordEncryptor.encrypt(pass2);
        			}
        			
        			if (!pass1.isEmpty() && !pass2.isEmpty() && pass1.equals(pass2) && db.updatePassword(user, pass2) > 0)
        			{
        				html.add("updated_password", "Contraseña cambiada con éxito, será redirigido");
        				resp.setHeader("refresh","2;/sekai-project-gonzalo-racero-galan/panel");
        			}
        			else
        				html.add("updated_password", "Se produjo un error...");
        		}
        		else
        			html.add("updated_password", "El usuario no existe...");
        		
        		out.print(html.render());
        	}
        	else
        		resp.sendRedirect(req.getContextPath() + "/login");
        	
        }
        else
        	resp.sendRedirect(req.getContextPath() + "/login");
        
	}
}