package controller.usersManagement.resources;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import controller.usersManagement.access.AccessControllerView;
import controller.usersManagement.users.UsersControllerView;
import model.Resource;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@SuppressWarnings("serial")
public class ResourcesControllerAdd extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");

        try {

            if (AccessControllerView.checkPermission(request.getSession().getAttribute("userID").toString(),request.getRequestURI())){

                PersistenceManager pm = controller.PMF.get().getPersistenceManager();

                //Accion a realizar
                String action = request.getParameter("action");

                if (action == null)
                    action = "";

                switch (action){
                    //Crea
                    case "create":

                        String url = request.getParameter("url");
                        Boolean status = Boolean.parseBoolean(request.getParameter("status"));

                        createRole(url,status,pm);
                        request.getSession().setAttribute("serverResponse","{\"color\": \"#26a69a\",\"response\":\"Recurso creado con éxito.\"}");
                        break;

                    case "redirect":
                        HttpSession sesion= request.getSession();
                        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/Resources/add.jsp");
                        request.setAttribute("User",UsersControllerView.getUser(sesion.getAttribute("userID").toString()));
                        dispatcher.forward(request, response);
                        break;

                    case "update":

                        Key a = KeyFactory.stringToKey(request.getParameter("key"));

                        Resource resourc = pm.getObjectById(Resource.class, a);

                        resourc.setUrl(request.getParameter("url"));
                        resourc.setStatus(Boolean.parseBoolean(request.getParameter("status")));

                        request.getSession().setAttribute("serverResponse","{\"color\": \"#26a69a\",\"response\":\"Recurso Actualizado con éxito.\"}");
                        break;

                }

                pm.close();
                try{
                    response.sendRedirect("/e/resources");
                }
                //Al redirigr al jsp para crear, se usa RequestDispatcher, y este entra en conflicto con sendRedirect.
                catch (IllegalStateException e){
                    System.err.println("IllegalStateException: There was a double redirect.");
                }

            } else {
                request.getSession().setAttribute("serverResponse","{\"color\": \"red\",\"response\":\"No tienes permiso para crear un Recurso.\"}");
                response.sendRedirect("/e/resources");
            }

        } catch (NullPointerException e){
            response.sendRedirect("/");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public static String createRole(String url, boolean status, PersistenceManager pm){
        Resource role = new Resource(url,status);

        try{
            pm.makePersistent(role);
            return role.getKey();
        } finally {
            System.out.println("Role creado");
        }
    }
}
