package controller.usersManagement.resources;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import controller.usersManagement.access.AccessControllerView;
import model.Resource;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@SuppressWarnings("serial")
public class ResourcesControllerDelete extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try{

            if (AccessControllerView.checkPermission(request.getSession().getAttribute("userID").toString(),request.getRequestURI())){

                PersistenceManager pm = controller.PMF.get().getPersistenceManager();

                try {
                    Key key = KeyFactory.stringToKey(request.getParameter("key"));
                    try{
                        pm.deletePersistent(pm.getObjectById(Resource.class, key));
                        request.getSession().setAttribute("serverResponse","{\"color\": \"#26a69a\",\"response\":\"Recurso eliminado con éxito.\"}");
                    } catch (JDOObjectNotFoundException e){
                        System.err.println("Exception catched -> " + e.getMessage());
                    }


                } catch (NullPointerException e){
                    System.err.println("Exception captured -> " + e.getMessage());
                }

                response.sendRedirect("/e/resources");

            } else {
                request.getSession().setAttribute("serverResponse","{\"color\": \"red\",\"response\":\"No tienes permiso para eliminar un Recurso.\"}");
                response.sendRedirect("/e/resources");
            }

        } catch (NullPointerException e){
            response.sendRedirect("/");
        }
        
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
