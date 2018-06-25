package controller.services;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import controller.PMF;
import controller.access.AccessControllerView;
import model.Service;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("serial")
public class ServicesControllerDelete extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try{

            if (AccessControllerView.checkPermission(request.getSession().getAttribute("userID").toString(),request.getRequestURI())){

                PersistenceManager pm = PMF.get().getPersistenceManager();

                String serviceKey = request.getParameter("serviceKey");

                try{
                    Key k = KeyFactory.stringToKey(serviceKey);
                    Service service = pm.getObjectById(Service.class, k);

                    pm.deletePersistent(service);

                    request.getSession().setAttribute("serverResponse","Service deleted successfully.");
                } catch (JDOObjectNotFoundException e){
                    System.err.println("Exception catched -> " + e.getMessage());
                }

                pm.close();

                response.sendRedirect("/services");

            } else {
                request.getSession().setAttribute("serverResponse","You do not have the rights to access.");
                response.sendRedirect("/users");
            }


        } catch (NullPointerException e){
            response.sendRedirect("/users");
        }

		
	}

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
	    doGet(request, response);
    }

}