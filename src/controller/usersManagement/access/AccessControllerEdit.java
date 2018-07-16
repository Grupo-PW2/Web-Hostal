package controller.usersManagement.access;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import controller.PMF;
import controller.usersManagement.users.UsersControllerView;
import model.Access;
import model.Resource;
import model.Role;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("serial")
public class AccessControllerEdit extends HttpServlet {

    @SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");

    	try{

    	    if (AccessControllerView.checkPermission(request.getSession().getAttribute("userID").toString(),request.getRequestURI())){

                // create the persistence manager instance
                PersistenceManager pm = PMF.get().getPersistenceManager();
                try{

                    Key k = KeyFactory.createKey(Access.class.getSimpleName(), new Long(request.getParameter("id")));

                    Access a = pm.getObjectById(Access.class, k);

                    request.setAttribute("access", a);

                    String query = "select from " + Role.class.getName();
                    String query2 = "select from " + Resource.class.getName();

                    List<Role> roles = (List<Role>)pm.newQuery(query).execute();
                    List<Resource> resources = (List<Resource>)pm.newQuery(query2).execute();

                    request.setAttribute("roles", roles);
                    request.setAttribute("resources", resources);

                    try{
                        if(request.getParameter("info").equals("editar")){

                            String idRole = request.getParameter("rolesl");
                            String idResource = request.getParameter("resourcesl");
                            boolean status = Boolean.parseBoolean(request.getParameter("status"));

                            if(idRole == null || idRole.equals("")|| idResource == null || idResource.equals("")){

                                System.out.print("nombre vacio");

                            }
                            else{

                                if(!a.getRoleKey().equals(idRole)){
                                    a.setRoleKey(idRole);
                                }

                                if(!a.getResourceKey().equals(idResource)){
                                    a.setResourceKey(idResource);
                                }

                                a.setStatus(status);

                                request.getSession().setAttribute("serverResponse","{\"color\": \"#26a69a\",\"response\":\"Acceso editado con éxito.\"}");

                                response.sendRedirect("/e/access");

                            }
                        } else if(request.getParameter("info").equals("redirect")){

                            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/Access/edit.jsp");
                            request.setAttribute("User",UsersControllerView.getUser(request.getSession().getAttribute("userID").toString()));
                            dispatcher.forward(request, response);
                        }

                    }catch (java.lang.NullPointerException np){
                        System.err.println("AccessControllerEdit Exception -> NPE:");
                        np.printStackTrace();
                    }

                } catch(javax.jdo.JDOObjectNotFoundException nf) {
                    response.sendRedirect("/");
                } catch (NumberFormatException e){
                    response.sendRedirect("/e/users");
                }

            }else{

                request.getSession().setAttribute("serverResponse","{\"color\": \"red\",\"response\":\"No tienes permiso para editar un Acceso.\"}");
                response.sendRedirect("/e/access");

            }

        } catch (NullPointerException e){
    	    e.printStackTrace();
    	    response.sendRedirect("/");
        }


	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}