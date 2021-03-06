package controller.resourcesManagement.services;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import controller.PMF;
import controller.usersManagement.access.AccessControllerView;
import model.Service;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("serial")
public class ServicesControllerDelete extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");

        try{

            if (AccessControllerView.checkPermission(request.getSession().getAttribute("userID").toString(),request.getRequestURI())){

                PersistenceManager pm = PMF.get().getPersistenceManager();

                String serviceKey = request.getParameter("serviceKey");

                try{
                    Key k = KeyFactory.stringToKey(serviceKey);
                    Service service = pm.getObjectById(Service.class, k);

                    pm.deletePersistent(service);

                    request.getSession().setAttribute("serverResponse","{\"color\": \"#26a69a\",\"response\":\"Servicio eliminado con éxito.\"}");
                } catch (JDOObjectNotFoundException e){
                    System.err.println("Exception catched -> " + e.getMessage());
                }

                pm.close();

                response.sendRedirect("/e/services");

            } else {
                request.getSession().setAttribute("serverResponse","{\"color\": \"red\",\"response\":\"No tienes permiso para eliminar un Servicio.\"}");
                response.sendRedirect("/e/services");
            }


        } catch (NullPointerException e){
            response.sendRedirect("/");
        }

		
	}

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
	    doGet(request, response);
    }

}