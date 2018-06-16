package controller.users;

import model.User;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class UsersControllerIndex extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //Se usa para revisar si hay una sesion activa
        HttpSession sesion= request.getSession();

        //Intenta hallar una sesion activa
        try{
            User usario = UsersControllerView.getUser(sesion.getAttribute("userID").toString());
            if (usario == null) throw new NullPointerException("UsersControllerIndex: El usuario recibido es nulo.");

            request.setAttribute("User",usario);
            request.setAttribute("UsersList",UsersControllerView.getAllUsers());
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/Users/index.jsp");
            dispatcher.forward(request,response);

        }
        //Si no la encuentra, redirige a la pagina inicial.
        catch (Exception e){
            System.err.println("UserControllerIndex: Error catched. " + e.getMessage());
            response.getWriter().println("<html><head><script>window.location.replace(\"../\")</script></head></html>");
        }

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }
}