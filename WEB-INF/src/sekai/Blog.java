package sekai;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

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
		PrintWriter out;
        ST html, blogEntry;
        HttpSession session;
        String allEntrys = "";
        Set<Entrada> entrys;
        
        out = resp.getWriter();
        session = req.getSession(false);
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        html = new ST(HTMLTemplate.getBlog(),'$','$');
        
        entrys = db.findAllEntrys();
        
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
