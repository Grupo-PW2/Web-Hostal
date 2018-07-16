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
import java.io.IOException;
import java.util.List;

public class MaterialsControllerView extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");

        try {

            if (AccessControllerView.checkPermission(request.getSession().getAttribute("userID").toString(),request.getRequestURI())){

                String action = request.getParameter("action");

                if (action == null)
                    action = "";

                PersistenceManager pm = PMF.get().getPersistenceManager();

                if (action.equals("editRedirect")) {
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/Materials/view.jsp");

                    request.setAttribute("User",UsersControllerView.getUser(request.getSession().getAttribute("userID").toString()));
                    request.setAttribute("Material",getMaterial(request.getParameter("materialKey")));

                    request.setAttribute("editAllowed",true);
                    request.setAttribute("action","Editar");

                    try{
                        dispatcher.forward(request,response);
                    } catch (javax.servlet.ServletException e){
                        e.printStackTrace();
                    }

                }
                else if (action.equals("viewRedirect")) {
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/Materials/view.jsp");

                    request.setAttribute("User",UsersControllerView.getUser(request.getSession().getAttribute("userID").toString()));
                    request.setAttribute("Material",getMaterial(request.getParameter("materialKey")));

                    request.setAttribute("editAllowed",false);
                    request.setAttribute("action","Ver");

                    try{
                        dispatcher.forward(request,response);
                    } catch (javax.servlet.ServletException e){
                        e.printStackTrace();
                    }

                }
                //Si no se encontró acción, regresa al inicio
                else {
                    response.getWriter().println("<html><head><script>window.location.replace(\"../\");</script><body></body></html>");
                }

                pm.close();

            } else {

                request.getSession().setAttribute("serverResponse","{\"color\": \"red\",\"response\":\"No tienes permiso para editar/ver un Material.\"}");
                response.sendRedirect("/e/materials");

            }

        } catch (NullPointerException e){
            response.sendRedirect("/");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @SuppressWarnings("unchecked")
    static List<Material> getAllMaterials(){
        PersistenceManager pm = controller.PMF.get().getPersistenceManager();
        List<Material> employees = (List<Material>) pm.newQuery("select from " + Material.class.getName()).execute();
        pm.close();
        return employees;
    }

    private static Material getMaterial(String key){
        PersistenceManager pm = PMF.get().getPersistenceManager();

        Key k = KeyFactory.stringToKey(key);
        Material material = pm.getObjectById(Material.class,k);

        pm.close();
        return material;
    }

}
