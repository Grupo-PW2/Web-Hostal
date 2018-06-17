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
public class AccessControllerEdit extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// create the persistence manager instance
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			System.out.print(request.getParameter("info"));
			Key k = KeyFactory.createKey(Access.class.getSimpleName(), new Long(request.getParameter("id")).longValue());
			Access a = pm.getObjectById(Access.class, k);
			request.setAttribute("access", a);
			String query = "select from " + Role.class.getName();
			String query2 = "select from " + Resource.class.getName();
			List<Role> roles = (List<Role>)pm.newQuery(query).execute();
			List<Resource> resources = (List<Resource>)pm.newQuery(query2).execute();
			request.setAttribute("roles", roles);
			request.setAttribute("resources", resources);
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/Views/Access/edit.jsp");
			dispatcher.forward(request, response);
			try{
				if(request.getParameter("info").equals("editar")){
					String idRole=request.getParameter("rolesl");
					String idResource=request.getParameter("resourcesl");
					if(idRole == null || idRole.equals("")|| idResource == null || idResource.equals("")){System.out.print("nombre vacio");}
					else{
						if(a.getIdRole().equals(idRole)==false){
							a.setIdRole(new Long(idRole).longValue());
						}
						if(a.getIdResource().equals(idResource)==false){
							a.setIdResource(new Long(idResource).longValue());
						}
					}
				}
			}catch (java.lang.NullPointerException np){}
		}catch(javax.jdo.JDOObjectNotFoundException nf) {
				response.sendRedirect("/index.html");
			}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}