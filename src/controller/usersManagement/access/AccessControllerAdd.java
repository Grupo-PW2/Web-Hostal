package controller.usersManagement.access;

import controller.PMF;
import controller.usersManagement.resources.ResourcesControllerView;
import controller.usersManagement.roles.RolesControllerView;
import controller.usersManagement.users.UsersControllerView;
import model.Access;
import model.Resource;
import model.Role;
import model.User;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("serial")
public class AccessControllerAdd extends HttpServlet {

    @SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try{

            if(AccessControllerView.checkPermission(request.getSession().getAttribute("userID").toString(),request.getRequestURI())){

                PersistenceManager pm = PMF.get().getPersistenceManager();

                String query = "select from " + Role.class.getName();
                String query2 = "select from " + Resource.class.getName();

                List<Role> roles = RolesControllerView.getAllRoles();
                List<Resource> resources = ResourcesControllerView.getAllResources();

                request.setAttribute("roles", roles);
                request.setAttribute("resources", resources);

                boolean redirect = true;

                //String queryp = "select " + Role.class.getName();

                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/Access/add.jsp");

                if (request.getParameter("info") == null){

                    try{
                        HttpSession sesion= request.getSession();

                        User user = UsersControllerView.getUser(sesion.getAttribute("userID").toString());
                        if (user == null) throw new NullPointerException("UsersControllerIndex: El usuario recibido es nulo.");

                        request.setAttribute("User",user);
                        request.setAttribute("ResourceList",ResourcesControllerView.getAllResources());

                        dispatcher.forward(request,response);
                        redirect = false;

                    }
                    //Si no la encuentra, redirige a la pagina inicial.
                    catch (Exception e){
                        e.printStackTrace();
                        response.getWriter().println("<html><head><script>window.location.replace(\"../\")</script></head><body></body></html>");
                    }

                }
                else if(request.getParameter("info").equals("agregar")){

                    String idRole = request.getParameter("rolesl");
                    String idResource = request.getParameter("resourcesl");

                    if(idRole == null || idRole.equals("")|| idResource == null || idResource.equals("")){
                        System.out.print("nombre vacio");
                    }
                    else {
                        Access a = new Access(idRole, idResource,true);
                        //persist the entity
                        pm.makePersistent(a);
                        request.getSession().setAttribute("serverResponse","{\"color\": \"#26a69a\",\"response\":\"Access created successfully.\"}");
                    }
                }
                else if (request.getParameter("info").equals("check")){
                    response.getWriter().print(accessExist(request.getParameter("rolesl"),request.getParameter("resourcesl")));
                    redirect = false;
                }

                pm.close();

                try{
                    if (redirect){
                        request.setAttribute("serverResponse","");
                        response.sendRedirect("/e/access");
                    }
                }
                //Al redirigr al jsp para crear, se usa RequestDispatcher, y este entra en conflicto con sendRedirect.
                catch (IllegalStateException e){
                    System.err.println("IllegalStateException: There was a double redirect.");
                }

            }else{

                request.getSession().setAttribute("serverResponse","{\"color\": \"red\",\"response\":\"You don\\'t have permission to create an access.\"}");
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

	@SuppressWarnings("unchecked")
	private boolean accessExist(String role, String resource){

        PersistenceManager pm = PMF.get().getPersistenceManager();

        List<Access> accessList = (List<Access>) pm.newQuery("select from " + Access.class.getName()).execute();

        try {
            String roleKey = RolesControllerView.getRole(role).getKey();
            String resourceKey = ResourcesControllerView.getResource(resource).getKey();
            for (Access access: accessList){
                if (access.getRoleKey().equals(roleKey)){
                    if (access.getResourceKey().equals(resourceKey)){
                        return true;
                    }
                }
            }
            return false;
        } catch (IllegalArgumentException e){
            return true;
        }

    }
}