package controller.usersManagement.roles;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import controller.usersManagement.access.AccessControllerView;
import model.Role;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("serial")
public class RolesControllerDelete extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");

        try {

            if (AccessControllerView.checkPermission(request.getSession().getAttribute("userID").toString(),request.getRequestURI())){

                PersistenceManager pm = controller.PMF.get().getPersistenceManager();

                try {
                    Key k = KeyFactory.stringToKey(request.getParameter("key"));
                    try{
                        pm.deletePersistent(pm.getObjectById(Role.class, k));
                        request.getSession().setAttribute("serverResponse","{\"color\": \"#26a69a\",\"response\":\"Rol eliminado con éxito.\"}");
                    } catch (JDOObjectNotFoundException e){
                        System.err.println("Exception catched -> " + e.getMessage());
                    }


                } catch (NullPointerException e){
                    System.err.println("Exception captured -> " + e.getMessage());
                }

                response.sendRedirect("/e/roles");

            } else {
                request.getSession().setAttribute("serverResponse","{\"color\": \"red\",\"response\":\"No tienes permiso para eliminar un Rol.\"}");
                response.sendRedirect("/e/roles");
            }

        } catch (NullPointerException e){
            response.sendRedirect("/");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
