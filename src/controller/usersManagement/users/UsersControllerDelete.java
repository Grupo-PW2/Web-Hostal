package controller.usersManagement.users;

import controller.usersManagement.access.AccessControllerView;
import model.User;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("serial")
public class UsersControllerDelete extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try{

            if (AccessControllerView.checkPermission(request.getSession().getAttribute("userID").toString(),request.getRequestURI())){

                PersistenceManager pm = controller.PMF.get().getPersistenceManager();

                String userID = request.getParameter("userID");

                try{
                    pm.deletePersistent(pm.getObjectById(User.class, userID));
                    request.getSession().setAttribute("serverResponse","{\"color\": \"#26a69a\",\"response\":\"User deleted sucessfully.\"}");
                } catch (JDOObjectNotFoundException e){
                    System.err.println("Exception catched -> " + e.getMessage());
                }

                pm.close();

                response.sendRedirect("/e/users");

            } else {

                request.getSession().setAttribute("serverResponse","{\"color\": \"red\",\"response\":\"You don\\'t have permission to delete a user.\"}");
                response.sendRedirect("/e/users");

            }

        } catch (NullPointerException e){
            e.printStackTrace();
            response.sendRedirect("/");
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}
