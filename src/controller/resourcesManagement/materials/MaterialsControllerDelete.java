package controller.resourcesManagement.materials;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import controller.PMF;
import controller.usersManagement.access.AccessControllerView;
import model.Material;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MaterialsControllerDelete extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try{

            if (AccessControllerView.checkPermission(request.getSession().getAttribute("userID").toString(),request.getRequestURI())){

                PersistenceManager pm = PMF.get().getPersistenceManager();

                String materialKey = request.getParameter("materialKey");

                try{
                    Key k = KeyFactory.stringToKey(materialKey);
                    Material material = pm.getObjectById(Material.class, k);

                    pm.deletePersistent(material);

                    request.getSession().setAttribute("serverResponse","{\"color\": \"#26a69a\",\"response\":\"Material eliminado con éxito.\"}");
                } catch (JDOObjectNotFoundException e){
                    System.err.println("Exception catched -> " + e.getMessage());
                }

                pm.close();

                response.sendRedirect("/e/materials");

            } else {

                request.getSession().setAttribute("serverResponse","{\"color\": \"red\",\"response\":\"No tienes permiso para eliminar un Material.\"}");
                response.sendRedirect("/e/materials");

            }

        } catch (NullPointerException e){
            response.sendRedirect("/");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
