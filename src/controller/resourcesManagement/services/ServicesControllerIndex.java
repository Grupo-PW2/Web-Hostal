package controller.resourcesManagement.services;

import controller.usersManagement.access.AccessControllerView;
import controller.usersManagement.users.UsersControllerView;
import model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static controller.resourcesManagement.services.ServicesControllerView.getAllServices;

public class ServicesControllerIndex extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try{

            if (AccessControllerView.checkPermission(request.getSession().getAttribute("userID").toString(),request.getRequestURI())){

                //Se usa para revisar si hay una sesion activa
                HttpSession sesion= request.getSession();

                //Intenta hallar una sesion activa
                try{
                    User user = UsersControllerView.getUser(sesion.getAttribute("userID").toString());
                    if (user == null) throw new NullPointerException("UsersControllerIndex: El usuario recibido es nulo.");

                    request.setAttribute("User",user);
                    request.setAttribute("ServicesList",getAllServices());
                    request.setAttribute("serverResponse",sesion.getAttribute("serverResponse"));
                    sesion.setAttribute("serverResponse","!");
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/Services/index.jsp");
                    dispatcher.forward(request,response);

                }
                //Si no la encuentra, redirige a la pagina inicial.
                catch (Exception e){
                    System.err.println("UserControllerIndex: Error catched. " + e.getMessage());
                    response.getWriter().println("<html><head><script>window.location.replace(\"../\")</script></head></html>");
                }

            } else {
                request.getSession().setAttribute("serverResponse","{\"color\": \"red\",\"response\":\"You don\\'t have permission to access /services.\"}");
                response.sendRedirect("/");
            }

        } catch (NullPointerException e){
            e.printStackTrace();
            response.sendRedirect("/");
        }


    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doPost(req, resp);
    }


}
