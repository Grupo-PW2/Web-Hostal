package controller.services;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import controller.PMF;
import controller.access.AccessControllerView;
import controller.users.UsersControllerView;
import model.Service;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("serial")
public class ServicesControllerView extends HttpServlet {

	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try{

            if (AccessControllerView.checkPermission(request.getSession().getAttribute("userID").toString(),request.getRequestURI())){

                String action = request.getParameter("action");

                if (action == null)
                    action = "";

                PersistenceManager pm = PMF.get().getPersistenceManager();

                if (action.equals("editRedirect")) {
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/Services/view.jsp");

                    request.setAttribute("User",UsersControllerView.getUser(request.getSession().getAttribute("userID").toString()));
                    request.setAttribute("Service",getService(request.getParameter("serviceKey")));

                    request.setAttribute("editAllowed",true);
                    request.setAttribute("action","Edit");

                    try{
                        dispatcher.forward(request,response);
                    } catch (javax.servlet.ServletException e){
                        e.printStackTrace();
                    }

                }
                else if (action.equals("viewRedirect")) {
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/Services/view.jsp");

                    request.setAttribute("User",UsersControllerView.getUser(request.getSession().getAttribute("userID").toString()));
                    request.setAttribute("Service",getService(request.getParameter("serviceKey")));

                    request.setAttribute("editAllowed",false);
                    request.setAttribute("action","View");

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
                request.getSession().setAttribute("serverResponse","{\"color\": \"red\",\"response\":\"You don\\'t have permission to view/edit a Service.\"}");
                response.sendRedirect("/users");
            }

        } catch (NullPointerException e){
            response.sendRedirect("/users");
        }

	}

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	    doGet(req, resp);
    }

    @SuppressWarnings("unchecked")
    static List<Service> getAllServices(){
        PersistenceManager pm = controller.PMF.get().getPersistenceManager();
        List<Service> services = (List<Service>) pm.newQuery("select from " + Service.class.getName()).execute();
        pm.close();
        return services;
    }

    private static Service getService(String key){
	    PersistenceManager pm = PMF.get().getPersistenceManager();

	    Key k = KeyFactory.stringToKey(key);
	    Service service = pm.getObjectById(Service.class,k);

	    pm.close();
	    return service;
    }

}