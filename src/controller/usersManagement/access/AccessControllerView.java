package controller.usersManagement.access;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import controller.PMF;
import controller.usersManagement.resources.ResourcesControllerView;
import controller.usersManagement.roles.RolesControllerView;
import controller.usersManagement.users.UsersControllerView;
import model.Access;
import model.Resource;
import model.Role;
import model.User;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("serial")
public class AccessControllerView extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// create the persistence manager instance
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try{

		    if (AccessControllerView.checkPermission(request.getSession().getAttribute("userID").toString(),request.getRequestURI())) {

                Key k = KeyFactory.createKey(Access.class.getSimpleName(), new Long(request.getParameter("id")));
                Access a = pm.getObjectById(Access.class, k);

                request.setAttribute("access", a);

                Role rol = RolesControllerView.getRole(a.getRoleKey());

                String nrol = rol.getName();

                Resource res = ResourcesControllerView.getResource(a.getResourceKey());
                String nres = res.getUrl();

                request.setAttribute("User",UsersControllerView.getUser(request.getSession().getAttribute("userID").toString()));

                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/Access/view.jsp");
                dispatcher.forward(request, response);

            } else {

                request.getSession().setAttribute("serverResponse","{\"color\": \"red\",\"response\":\"No tienes permiso para ver un Acceso.\"}");
                response.sendRedirect("/e/access");

            }


		} catch(javax.jdo.JDOObjectNotFoundException nf) {
            System.err.println("JDOObjectNotFound -> AccessControllerView");
            nf.printStackTrace();
            request.getSession().setAttribute("serverResponse","{\"color\": \"darkorange\",\"response\":\"Ocurrio un error.\"}");
            response.sendRedirect("/e/access");

		} catch (NullPointerException e){
		    System.err.println("NPE -> Trying to access a servlet without logging in.");
		    e.printStackTrace();
		    response.sendRedirect("/");
        }
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	@SuppressWarnings("unchecked")
	private static List<Access> getAllAccess(){
	    PersistenceManager pm = PMF.get().getPersistenceManager();
	    List<Access> accessList = (List<Access>) pm.newQuery("select from " + Access.class.getName()).execute();
	    pm.close();
	    return accessList;
    }

    /**
     * Metodo estatico checkPermission.
     *
     * Al llamarlo, revisa si un usuario tiene acceso a una URI, devuelve true si es cierto, false si no.
     * Se debe colocar como primer condicional del Servlet:
     *
     * public doGet/doPost ()~~~~{
     *  if (checkPermission(userID,uri){
     *      //El usuario tiene permiso, realizar las acciones necesarias
     *  } else {
     *      //El usuario no tiene permiso, mostrar mensaje de error.
     *  }
     * }
     *
     * Los usuarion con rol admin tienen acceso completo por defecto.
     *
     * @param userID    El id del usuario que ha iniciado sesion. Ver UsersControllerAdd
     * @param uri       El URI al que se intenta acceder. request.getRequestURI()
     *
     * */
	public static boolean checkPermission(String userID, String uri){

	    User user = UsersControllerView.getUser(userID);
	    String userRoleKey = user.getRoleKey();
	    if (userRoleKey == null)
	        userRoleKey = "";

	    if (user.getRoleName().equals("admin")) {
            return true;
        }

	    for (Access access: getAllAccess()){
            if (access.getRoleKey().equals(userRoleKey) && access.getStatus()){
                if (access.getResourceName().equals(uri) && ResourcesControllerView.getResource(access.getResourceKey()).getStatus()){
                    return true;
                }
            }
        }

	    return false;
    }

}