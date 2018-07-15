package controller.resourcesManagement.services;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import controller.PMF;
import controller.usersManagement.access.AccessControllerView;
import controller.usersManagement.users.UsersControllerView;
import model.Service;

@SuppressWarnings("serial")
public class ServicesControllerAdd extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

	    try{

            if (AccessControllerView.checkPermission(request.getSession().getAttribute("userID").toString(),request.getRequestURI())){
                PersistenceManager pm = PMF.get().getPersistenceManager();

                System.out.println("Request URI Add->" + request.getRequestURI());

                String action = request.getParameter("action");

                if (action == null)
                    action = "";

                if (action.equals("create")){

                    String name = request.getParameter("Name");
                    Double price = Double.parseDouble(request.getParameter("Price"));
                    String description = request.getParameter("Description");
                    String userCreatorKey = request.getParameter("userId");

                    Service service = new Service (name, price, description,userCreatorKey);

                    request.getSession().setAttribute("serverResponse","{\"color\": \"#26a69a\",\"response\":\"Servicio creado con éxito.\"}");

                    pm.makePersistent(service);


                } else if (action.equals("update")){

                    Key k = KeyFactory.stringToKey(request.getParameter("key"));

                    Service service = pm.getObjectById(Service.class,k);

                    service.setName(request.getParameter("Name"));
                    service.setPrice(Double.parseDouble(request.getParameter("Price")));
                    service.setDescription(request.getParameter("Description"));

                    pm.close();

                    request.getSession().setAttribute("serverResponse","{\"color\": \"#26a69a\",\"response\":\"Servicio editado con éxito.\"}");

                } else if (action.equals("redirect")){

                    HttpSession sesion= request.getSession();
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/Services/add.jsp");
                    request.setAttribute("User",UsersControllerView.getUser(sesion.getAttribute("userID").toString()));
                    dispatcher.forward(request, response);

                }

                pm.close();
                try{
                    response.sendRedirect("/services");
                }
                //Al redirigr al jsp para crear, se usa RequestDispatcher, y este entra en conflicto con sendRedirect.
                catch (IllegalStateException e){
                    System.err.println("IllegalStateException: There was a double redirect.");
                }

            } else {

                request.getSession().setAttribute("serverResponse","{\"color\": \"red\",\"response\":\"No tienes permiso para crear un Servicio.\"}");
                response.sendRedirect("/services");

            }

        } catch (NullPointerException e){
	        response.sendRedirect("/services");
        }


	}

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
	    doGet(request, response);
    }
}
