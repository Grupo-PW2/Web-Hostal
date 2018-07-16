package controller;

import controller.resourcesManagement.services.ServicesControllerView;
import controller.usersManagement.access.AccessControllerView;
import controller.usersManagement.users.UsersControllerView;
import model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class index extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");

        HttpSession sesion= request.getSession();

        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");

        User loggedUser;

        try {
            loggedUser = UsersControllerView.getUser(sesion.getAttribute("userID").toString());
        } catch (NullPointerException e){
            loggedUser = null;
        }

        if (loggedUser != null){

            request.setAttribute("hasAccess",AccessControllerView.checkPermission(sesion.getAttribute("userID").toString(),"/e/"));
            request.setAttribute("User",loggedUser);
            request.setAttribute("isUserLogged",true);
            request.setAttribute("servicesList",ServicesControllerView.getAllServices());
            request.setAttribute("serverResponse",request.getSession().getAttribute("serverResponse"));
            request.getSession().setAttribute("serverResponse","!");

        } else {
            request.setAttribute("isUserLogged",false);
        }

        dispatcher.forward(request,response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}
