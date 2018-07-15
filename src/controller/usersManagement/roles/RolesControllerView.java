package controller.usersManagement.roles;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import controller.usersManagement.access.AccessControllerView;
import controller.usersManagement.users.UsersControllerView;
import model.Role;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


/**
 * RolesControllerView
 *
 * Permite ver los Roles
 *
 * Metodos importantes:
 *
 * public static getAllRoles()
 * Devuelve un List<Role> con todos los roles que existen.
 *
 * public static getRoleKey(String key)
 * Devuelve un Rol dada una key.
 * La key se obtiene usando el metodo getKey() de un objeto Role
 *
 * */

@SuppressWarnings("serial")
public class RolesControllerView extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            if (AccessControllerView.checkPermission(request.getSession().getAttribute("userID").toString(),request.getRequestURI())){
                String action = request.getParameter("action");

                //Para evitar errores, si no hay ninguna accion, se establece a vacio.
                if (action == null)
                    action = "";

                String key = request.getParameter("key");

                //Redirige al formulario para editar un Role (role/view)
                if (action.equals("editRedirect") && key != null){
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/Roles/view.jsp");
                    request.setAttribute("Role",getRole(key));
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
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/Roles/view.jsp");
                    request.setAttribute("Role",getRole(key));
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
            } else {
                request.getSession().setAttribute("serverResponse","{\"color\": \"red\",\"response\":\"No tienes permiso para ver un Rol.\"}");
                response.sendRedirect("/e/roles");
            }
        } catch (NullPointerException e){
            response.sendRedirect("/");
        }



    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }


    /**
     * Metodo Estatico getAllRoles
     *
     * Devuelve un list con todos los Roles que existen desde cualquier parte del codigo.
     *
     * @return          Un List<Role> con todos los Roles
     * */
    @SuppressWarnings("unchecked")
    public static List<Role> getAllRoles(){
        PersistenceManager pm = controller.PMF.get().getPersistenceManager();
        List<Role> users = (List<Role>) pm.newQuery("select from " + Role.class.getName()).execute();
        pm.close();
        return users;
    }

    public static Role getRole(String key){
        PersistenceManager pm = controller.PMF.get().getPersistenceManager();
        Key k = KeyFactory.stringToKey(key);
        Role role = pm.getObjectById(Role.class, k);
        pm.close();
        return role;
    }

    @SuppressWarnings("unchecked")
    public static List<Role> searchRole(String name){
        PersistenceManager pm = controller.PMF.get().getPersistenceManager();
        List<Role> users = (List<Role>) pm.newQuery("select from " + Role.class.getName() + " where name == '" + name + "'").execute();
        pm.close();
        return users;
    }
}
