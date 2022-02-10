package sekai;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.stringtemplate.v4.ST;


@SuppressWarnings("serial")
public class Destroy extends HttpServlet {
	
	private SekaiDatabase db = new SekaiDatabase();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		if (!db.databaseExists())
			resp.sendRedirect(req.getContextPath() + "/installer");
		else
		{
			PrintWriter out;
			ST html;
			HttpSession session;
        
			out = resp.getWriter();
			session = req.getSession(false);
			resp.setCharacterEncoding("UTF-8");
			resp.setHeader("refresh","2;/sekai-project-gonzalo-racero-galan/blog");
			resp.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
			resp.setHeader("Pragma", "no-cache");
			html = new ST(HTMLTemplate.getDestroy(),'$','$');

			if (session != null)
			{
				html.add("destroyed_user", "Se cerró la sessión de " + session.getAttribute("usuario_de_la_sesion"));
				session.invalidate();
			}
			else
				html.add("destroyed_user", "No había ninguna sesión abierta");
        
			out.println(html.render());
		}
	}
}