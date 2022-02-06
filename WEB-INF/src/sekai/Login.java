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
public class Login extends HttpServlet {

	private SekaiDatabase db = new SekaiDatabase();
	
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		PrintWriter out;
        ST html;
        String usuario, password;
        HttpSession session;
        Optional<Usuario> user;
        
        out = resp.getWriter();
        session = req.getSession(false);
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        
        html = new ST(HTMLTemplate.getLogin(),'$','$');
        
        if (session == null)
        {
        	usuario = req.getParameter("usuario");
            password = req.getParameter("password");
            user = db.findUsuarioById(usuario);
           
            if (usuario != null && password != null)
            {
            	// lo convierto porque en la base de datos está convertido, y para comparar necesito que sean iguales
            	usuario = StringHTMLizer.convert(usuario);
            	password = StringHTMLizer.convert(password);
            	
            	password = PasswordEncryptor.encrypt(password);
            }
            
            if (user.isPresent())
            {
            	if (user.get().getPassword().equals(password))
            	{
            		html.add("login_user", user.get().toString());
                	session = req.getSession(true);
                	session.setAttribute("usuario_de_la_sesion", user.get().getUsuario());
                	session.setMaxInactiveInterval(100);
                	resp.sendRedirect(req.getContextPath() + "/panel");
            	}
            	else
            	{
            		html.add("login_user", "Datos erroneos");
            	}
            }
            else
            {
            	html.add("login_user", "Introduzca los datos");
            }
        }
        else
        {
        	html.add("login_user", "La sesión ya está iniciada por " + session.getAttribute("usuario_de_la_sesion"));
        	resp.sendRedirect(req.getContextPath() + "/panel");
        }
        
        out.println(html.render());
    }
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		doPost(req, resp);
	}
}