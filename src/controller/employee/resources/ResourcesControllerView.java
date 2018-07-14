package controller.employee.resources;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import controller.employee.users.UsersControllerView;
import model.Resource;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static controller.employee.roles.RolesControllerView.getRole;

@SuppressWarnings("serial")
public class ResourcesControllerView extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String action = request.getParameter("action");

        //Para evitar errores, si no hay ninguna accion, se establece a vacio.
        if (action == null)
            action = "";

        String key = request.getParameter("key");

        //Redirige al formulario para editar un Resource (resource/view)
        if (action.equals("editRedirect") && key != null){
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/Resources/view.jsp");
            request.setAttribute("Resource",getResource(key));
            request.setAttribute("UserLogged",UsersControllerView.getUser(request.getSession().getAttribute("userID").toString()));

            //Ya que se quiere editar, el atributo permitirEdicion es verdadero. Este atributo se comprueba en el JSP.
            request.setAttribute("editAllowed",true);
            request.setAttribute("action","Edit");
            try{
                dispatcher.forward(request,response);
            } catch (javax.servlet.ServletException e){
                System.err.println("Exception captured -> " + e.getMessage());
            }
        }
        //Redirige al formulario para ver un usuario (user/view)
        else if (action.equals("viewRedirect") && key != null){
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/Resources/view.jsp");
            request.setAttribute("Resource",getResource(key));
            request.setAttribute("UserLogged",UsersControllerView.getUser(request.getSession().getAttribute("userID").toString()));

            //Ya que no quiere editar, el atributo permitirEdicion es falso. Este atributo se comprueba en el JSP.
            request.setAttribute("editAllowed",false);
            request.setAttribute("action","View");
            try{
                dispatcher.forward(request,response);
            } catch (javax.servlet.ServletException e){
                System.err.println("Exception captured -> " + e.getMessage());
            }

        }
        //Si no se encontró acción, regresa al inicio
        else {
            response.getWriter().println("<html><head><script>window.location.replace(\"../\");</script><body></body></html>");
        }

        
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
    
    /**
     * Metodo Estatico getAllResources
     *
     * Devuelve un list con todos los Recursos que existen desde cualquier parte del codigo.
     *
     * @return          Un List<Resource> con todos los Recursos
     * */
    @SuppressWarnings("unchecked")
    public static List<Resource> getAllResources(){
        PersistenceManager pm = controller.PMF.get().getPersistenceManager();
        List<Resource> resources = (List<Resource>) pm.newQuery("select from " + Resource.class.getName()).execute();
        pm.close();
        return resources;
    }

    public static Resource getResource(String key){
        PersistenceManager pm = controller.PMF.get().getPersistenceManager();
        Key k = KeyFactory.stringToKey(key);
        Resource resource = pm.getObjectById(Resource.class, k);
        pm.close();
        return resource;
    }

}
