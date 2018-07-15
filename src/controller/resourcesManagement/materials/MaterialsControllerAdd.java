package controller.resourcesManagement.materials;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import controller.PMF;
import controller.usersManagement.access.AccessControllerView;
import controller.usersManagement.users.UsersControllerView;
import model.Material;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class MaterialsControllerAdd extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try{

            if (AccessControllerView.checkPermission(request.getSession().getAttribute("userID").toString(),request.getRequestURI())){

                PersistenceManager pm = PMF.get().getPersistenceManager();

                String action = request.getParameter("action");

                if (action == null)
                    action = "";

                if (action.equals("create")){

                    String name = request.getParameter("Name");
                    int price = Integer.parseInt(request.getParameter("Price"));
                    int amount = Integer.parseInt(request.getParameter("Amount"));
                    String unity = request.getParameter("Unity");

                    Material material = new Material(name, price, amount, unity);

                    request.getSession().setAttribute("serverResponse","{\"color\": \"#26a69a\",\"response\":\"Material creado con éxito.\"}");

                    pm.makePersistent(material);


                } else if (action.equals("update")){

                    Key k = KeyFactory.stringToKey(request.getParameter("key"));

                    Material material = pm.getObjectById(Material.class,k);

                    material.setName(request.getParameter("Name"));
                    material.setPrice(Integer.parseInt(request.getParameter("Price")));
                    material.setAmount(Integer.parseInt(request.getParameter("Amount")));
                    material.setUnity(request.getParameter("Unity"));

                    pm.close();

                    request.getSession().setAttribute("serverResponse","{\"color\": \"#26a69a\",\"response\":\"Material actualizado con éxito.\"}");

                } else if (action.equals("redirect")){

                    HttpSession sesion= request.getSession();
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/Materials/add.jsp");
                    request.setAttribute("User",UsersControllerView.getUser(sesion.getAttribute("userID").toString()));
                    dispatcher.forward(request, response);

                }

                pm.close();
                try{
                    response.sendRedirect("/e/materials");
                }
                //Al redirigr al jsp para crear, se usa RequestDispatcher, y este entra en conflicto con sendRedirect.
                catch (IllegalStateException e){
                    System.err.println("IllegalStateException: There was a double redirect.");
                }

            } else {

                request.getSession().setAttribute("serverResponse","{\"color\": \"red\",\"response\":\"No tienes permiso para acceder.\"}");
                response.sendRedirect("/e/materials");

            }

        } catch (NullPointerException e){
            response.sendRedirect("/");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
