package controller.resourcesManagement.materials;

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

import static controller.resourcesManagement.materials.MaterialsControllerView.getAllMaterials;

public class MaterialsControllerIndex extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try{

            if (AccessControllerView.checkPermission(request.getSession().getAttribute("userID").toString(),request.getRequestURI())){

                //Se usa para revisar si hay una sesion activa
                HttpSession sesion= request.getSession();

                //Intenta hallar una sesion activa
                try{
                    User user = UsersControllerView.getUser(sesion.getAttribute("userID").toString());
                    if (user == null) throw new NullPointerException("UsersControllerIndex: El usuario recibido es nulo.");

                    request.setAttribute("User",user);
                    request.setAttribute("materialList",getAllMaterials());
                    request.setAttribute("serverResponse",sesion.getAttribute("serverResponse"));
                    sesion.setAttribute("serverResponse","!");
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/Materials/index.jsp");
                    dispatcher.forward(request,response);

                }
                //Si no la encuentra, redirige a la pagina inicial.
                catch (Exception e){
                    System.err.println("UserControllerIndex: Error catched. " + e.getMessage());
                    response.getWriter().println("<html><head><script>window.location.replace(\"../\")</script></head></html>");
                }

            } else {
                response.sendRedirect("/e/services");
            }

        }catch (NullPointerException e){
            response.sendRedirect("/");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
