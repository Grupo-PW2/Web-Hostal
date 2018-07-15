package controller.usersManagement.roles;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import controller.usersManagement.users.UsersControllerView;
import model.Role;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * RolesControllerAdd
 *
 * Permite lo siguiente:
 *
 * Crear un Rol -> con el parametro action = create
 * Redireccionar al form para crear un Rol -> parametro action = redirect
 * Actualizar un Rol -> parametro action = update
 *
 *
 *
 * */

@SuppressWarnings("serial")
public class RolesControllerAdd extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PersistenceManager pm = controller.PMF.get().getPersistenceManager();

        //Accion a realizar
        String action = request.getParameter("action");

        if (action == null)
            action = "";

        switch (action){
            //Crea
            case "create":

                String name = request.getParameter("roleName");
                Boolean status = Boolean.parseBoolean(request.getParameter("roleStatus"));

                createRole(name,status,pm);
                request.getSession().setAttribute("serverResponse","{\"color\": \"#26a69a\",\"response\":\"Role created successfully.\"}");

                break;

            case "redirect":
                HttpSession sesion= request.getSession();
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/Roles/add.jsp");
                request.setAttribute("User",UsersControllerView.getUser(sesion.getAttribute("userID").toString()));
                dispatcher.forward(request, response);
                break;

            case "update":

                Key a = KeyFactory.stringToKey(request.getParameter("key"));

                Role role1 = pm.getObjectById(Role.class, a);

                role1.setName(request.getParameter("roleName"));
                role1.setStatus(Boolean.parseBoolean(request.getParameter("roleStatus")));
                //role1.setImgUrl(userImg);
                request.getSession().setAttribute("serverResponse","{\"color\": \"#26a69a\",\"response\":\"Role updated successfully.\"}");

                break;

        }

        pm.close();
        try{
            response.sendRedirect("/e/roles");
        }
        //Al redirigr al jsp para crear, se usa RequestDispatcher, y este entra en conflicto con sendRedirect.
        catch (IllegalStateException e){
            System.err.println("IllegalStateException: There was a double redirect.");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public static String createRole(String name, boolean status, PersistenceManager pm){
        Role role = new Role(name,status);

        try{
            pm.makePersistent(role);
            return role.getKey();
        } finally {
            System.out.println("Role creado");
        }
    }

}
