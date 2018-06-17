package controller.access;

import java.io.IOException;
import javax.servlet.http.*;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import javax.servlet.*;
import javax.jdo.PersistenceManager;
import model.entity.*;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import controller.PMF;

@SuppressWarnings("serial")
public class AccessControllerView extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// create the persistence manager instance
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			System.out.print(request.getParameter("info"));
			Key k = KeyFactory.createKey(Access.class.getSimpleName(), new Long(request.getParameter("id")).longValue());
			Access a = pm.getObjectById(Access.class, k);
			request.setAttribute("access", a);
			Key krol = KeyFactory.createKey(Role.class.getSimpleName(), a.getIdRole());
			Role rol = pm.getObjectById(Role.class, krol);
			String nrol = rol.getName();
			Key kres = KeyFactory.createKey(Resource.class.getSimpleName(), a.getIdResource());
			Resource res = pm.getObjectById(Resource.class, kres);
			String nres = res.getName();
			System.out.print(nres);
			request.setAttribute("role", nrol);
			request.setAttribute("resource", nres);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/Views/Access/view.jsp");
			dispatcher.forward(request, response);
		}catch(javax.jdo.JDOObjectNotFoundException nf) {
				response.sendRedirect("/access");
			}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}