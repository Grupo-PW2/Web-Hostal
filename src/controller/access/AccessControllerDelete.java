package controller.access;

import java.io.IOException;
import javax.servlet.http.*;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import javax.servlet.*;
import javax.jdo.PersistenceManager;
import model.*;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import controller.PMF;
@SuppressWarnings("serial")
public class AccessControllerDelete extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try{

		    if (AccessControllerView.checkPermission(request.getSession().getAttribute("userID").toString(),request.getRequestURI())){

                // create the persistence manager instance
                PersistenceManager pm = PMF.get().getPersistenceManager();
                // create the new account
                try{
                    Key k = KeyFactory.createKey(Access.class.getSimpleName(), new Long(request.getParameter("accessId")));
                    Access r = pm.getObjectById(Access.class, k);

                    pm.deletePersistent(r);

                    request.getSession().setAttribute("serverResponse","{\"color\": \"#26a69a\",\"response\":\"Access deleted successfully.\"}");
                    response.sendRedirect("/access");
                } catch(javax.jdo.JDOObjectNotFoundException nf) {
                    response.sendRedirect("/access");
                }

            } else {
                request.getSession().setAttribute("serverResponse","{\"color\": \"red\",\"response\":\"You don\\'t have permission to delete an access.\"}");
                response.sendRedirect("/access");
            }

        } catch (NullPointerException e){
		    e.printStackTrace();
		    response.sendRedirect("/");
        }

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}