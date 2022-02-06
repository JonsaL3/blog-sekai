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
public class Delete extends HttpServlet {
	
	private SekaiDatabase db = new SekaiDatabase();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		doPost(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		PrintWriter out;
        ST html;
        HttpSession session;
        
        out = resp.getWriter();
        session = req.getSession(false);
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("refresh","2;/sekai-project-gonzalo-racero-galan/panel");
        resp.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        html = new ST(HTMLTemplate.getDelete(),'$','$');
        
        if (session != null)
        {
        	String usuario = (String) session.getAttribute("usuario_de_la_sesion");
        	Usuario u = db.findUsuarioById(usuario).get();
        	String idEntry = req.getParameter("entry_id");
        	
        	Optional<Entrada> entrada = db.findEntryById(Integer.parseInt(idEntry));
        	
        	if (entrada.isPresent())
        	{
        		if (db.deleteEntry(entrada.get()))
        			html.add("deleted_entry", "La entrada " + entrada.get().getTitulo() + " del usuario " + u.getUsuario() + " fué eliminada exitosamente.");
        		else
        			html.add("deleted_entry", "Error al borrar la entrada.");	
        	}
        	else
        		html.add("deleted_entry", "La entrada introducida no existe.");
        	
        	out.println(html.render());
        }
        else
        	resp.sendRedirect(req.getContextPath() + "/login");
        
	}
}