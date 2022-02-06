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
public class DeleteUser extends HttpServlet {
	
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
        resp.setHeader("Cache-Control","max-age=60");
        html = new ST(HTMLTemplate.getDeleteUserMessage(),'$','$');

        if (session != null)
        {
        	String usuario = (String) session.getAttribute("usuario_de_la_sesion");
        	
        	if (usuario.equals("admin"))
        	{
        		String idUserDelete;
        		Optional<Usuario> userDelete;
        		
        		idUserDelete = req.getParameter("user_id");
        		userDelete = db.findUsuarioById(idUserDelete);
        		
        		if (userDelete.isPresent() && db.deleteUser(userDelete.get()) > 0)
        			html.add("deleted_user", "El usuario " + idUserDelete + " fué eliminado con éxito");
        		else
        			html.add("deleted_user", "Se produjo un error al eliminar el usuario, es posible que este no exista");
        		
        		out.print(html.render());
        	}
        	else
        		resp.sendRedirect(req.getContextPath() + "/login");
        	
        }
        else
        	resp.sendRedirect(req.getContextPath() + "/login");
        
	}
}