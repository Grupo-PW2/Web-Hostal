package controller.usersManagement;

import controller.PMF;
import controller.usersManagement.access.AccessControllerView;
import controller.usersManagement.users.UsersControllerView;
import model.Access;
import model.User;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class index extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try{

            if (AccessControllerView.checkPermission(request.getSession().getAttribute("userID").toString(),request.getRequestURI())){

                // create the persistence manager instance
                PersistenceManager pm = PMF.get().getPersistenceManager();

                //Se usa para revisar si hay una sesion activa
                HttpSession sesion= request.getSession();

                //Intenta hallar una sesion activa
                try{
                    User user = UsersControllerView.getUser(sesion.getAttribute("userID").toString());
                    if (user == null) throw new NullPointerException("UsersControllerIndex: El usuario recibido es nulo.");

                    request.setAttribute("User",user);

                    request.setAttribute("serverResponse",sesion.getAttribute("serverResponse"));
                    sesion.setAttribute("serverResponse","!");

                    // forward the request to the jsp
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/eIndex.jsp");
                    dispatcher.forward(request, response);

                }
                //Si no la encuentra, redirige a la pagina inicial para que se cree la sesion.
                catch (Exception e){
                    e.printStackTrace();
                    response.getWriter().println("<html><head><script>window.location.replace(\"../\")</script></head><body></body></html>");
                } finally {
                    pm.close();
                }

            } else {
                request.getSession().setAttribute("serverResponse","{\"color\": \"red\",\"response\":\"Oops. No tienes permiso para acceder.\"}");
                response.sendRedirect("/");
            }

        } catch (NullPointerException e){
            response.sendRedirect("/");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
