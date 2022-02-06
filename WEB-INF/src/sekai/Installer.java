package sekai;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.stringtemplate.v4.ST;

@SuppressWarnings("serial")
public class Installer extends HttpServlet {

	private SekaiDatabase db = new SekaiDatabase();
	
	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
        PrintWriter out;
        ST html;
        
        out = resp.getWriter();
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        
        if (db.createSekaiDatabase() && db.insertUser(new Usuario("admin",PasswordEncryptor.encrypt("admin"),new HashSet<Entrada>()))) 	
        {
        	html = new ST(HTMLTemplate.getInstaller(),'$','$');
        	html.add("success_installation", "Instalación exitosa, será redirigido a la pantalla de inicio de sesión");
        	resp.setHeader("refresh","5;/sekai-project-gonzalo-racero-galan/panel");
        	out.println(html.render()); 
        }
        else
        	resp.sendRedirect(req.getContextPath() + "/login");
    }
}