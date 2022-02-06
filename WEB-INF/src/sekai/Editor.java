package sekai;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.stringtemplate.v4.ST;


@SuppressWarnings("serial")
public class Editor extends HttpServlet {
	
	private SekaiDatabase db = new SekaiDatabase();
	private String entryId = null;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		PrintWriter out;
        ST html;
        HttpSession session;
        String entryIdentifier;
        
        session = req.getSession(false);
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        
        if (session != null)
        {
        	String usuario = (String) session.getAttribute("usuario_de_la_sesion");
        	Usuario u = db.findUsuarioById(usuario).get();
        	
        	out = resp.getWriter();
            html = new ST(HTMLTemplate.getEditor(),'$','$');
            
            entryIdentifier = req.getParameter("entry_id");
            
            if (entryIdentifier != null)
            {
            	Optional<Entrada> entrada = u.getEntradas().stream()
            		.filter(e -> e.getId() == Integer.parseInt(entryIdentifier))
            		.findFirst();
            	
            	if (entrada.isPresent())
            	{
            		entryId = entryIdentifier;
            		html.add("title", entrada.get().getTitulo());
            		html.add("content", entrada.get().getTexto());
            	}
            	
            }
            else
            {
            	html.add("title", "");
        		html.add("content", "");
            }
        	
        	out.println(html.render());
        }
        else
        	resp.sendRedirect(req.getContextPath() + "/login");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		HttpSession session;
		String title, content;
		int fecha;
		
		session = req.getSession(false);
		resp.setCharacterEncoding("UTF-8");
		String usuario = (String) session.getAttribute("usuario_de_la_sesion");
    	Usuario u = db.findUsuarioById(usuario).get();

		if (session != null)
		{
			title = req.getParameter("title");
			content = req.getParameter("content");
			fecha = LocalDate.now().getYear();
			
			
			
			if (entryId != null)
			{
				title = StringHTMLizer.convert(title);
				content = StringHTMLizer.convert(content);
				
				if (db.updateEntry(new Entrada(Integer.parseInt(entryId), title, content, fecha)))
					resp.sendRedirect(req.getContextPath() + "/panel");
				entryId = null;
			}
			else if (db.insertEntry(u, new Entrada(null, title, content, fecha)))
				resp.sendRedirect(req.getContextPath() + "/panel");
			
		}
	}
}