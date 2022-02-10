package sekai;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.stringtemplate.v4.ST;


@SuppressWarnings("serial")
public class Blog extends HttpServlet {
	
	private SekaiDatabase db = new SekaiDatabase();
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		if (!db.databaseExists())
			resp.sendRedirect(req.getContextPath() + "/installer");
		else
		{
			PrintWriter out;
			ST html, blogEntry;
			HttpSession session;
			String allEntrys = "";
			List<Entrada> entrys;
			String busqueda;
        
			out = resp.getWriter();
			session = req.getSession(false);
			resp.setCharacterEncoding("UTF-8");
        	resp.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
        	resp.setHeader("Pragma", "no-cache");
        	html = new ST(HTMLTemplate.getBlog(),'$','$');
        	busqueda = req.getParameter("search");
        
        	// La funcionalidad extra de mi blog será un sistema de búsqueda de entradas
        	if (busqueda != null)
        	{
        		entrys = db.findAllEntrys() // lo se, no es lo mas eficiente, lo ideal seria preguntarle a la bbdd usando un where titulo LIKE %busqueda%
            		.stream()
            		.filter(e -> e.getTitulo().toUpperCase().contains(busqueda.toUpperCase()) || e.getTexto().toUpperCase().contains(busqueda.toUpperCase()))
            		.sorted((e1, e2) -> e2.getId().compareTo(e1.getId()))
            		.collect(Collectors.toList());
        	}
        	else
        	{
        		// como en mi caso no guardo la fecha exacta, solo guardo el año
        		// la forma de ordenar las entradas de la mas reciente a la mas
        		// antigua es por el identificador de las mismas.
        		entrys = db.findAllEntrys()
            		.stream()
            		.sorted((e1, e2) -> e2.getId().compareTo(e1.getId()))
            		.collect(Collectors.toList());
            
        	}
        
        	if (!entrys.isEmpty())
        	{
        		for (Entrada entry : entrys)
        		{
        			blogEntry = new ST(HTMLTemplate.getBlogEntry(),'$','$');
        			blogEntry.add("title", entry.getTitulo());
        			blogEntry.add("content", entry.getTexto());
        			blogEntry.add("year", entry.getFecha());
        		
        			String username = db.findAuthorUsernameOfEntry(entry);
        		
        			if (username != "")
        			{
        				blogEntry.add("author", username);
        			
        				String ownerOptions = HTMLTemplate.getBlogEntryOwnerOptions();
        				ST options = new ST(ownerOptions,'$','$');
                	
        				if (session != null)
        				{
        					String usuario = (String) session.getAttribute("usuario_de_la_sesion");
                    	
        					if (username.equals(usuario))
                    		{
        						options.add("entry_id", entry.getId());
        						blogEntry.add("owner_options", options.render());
                    		}
        					else
        					{
        						blogEntry.add("owner_options", "");
        					}
        				}	
        			}
        			else
        			{
        				// esto no debería de ocurrir
        				blogEntry.add("author", "Anónimo");
        			}
        		
        			allEntrys += blogEntry.render();
        		
        		}
        	
        		html.add("blog_entrys", allEntrys);
        	
        	}
        
        	out.println(html.render());
        
		}
	}
}
