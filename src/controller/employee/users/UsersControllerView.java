package controller.employee.users;

import controller.employee.access.AccessControllerView;
import controller.employee.roles.RolesControllerView;
import model.User;

import javax.jdo.JDOObjectNotFoundException;
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
public class UsersControllerView extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try{

            if (AccessControllerView.checkPermission(request.getSession().getAttribute("userID").toString(),request.getRequestURI())) {

                String action = request.getParameter("action");

                //Para evitar errores, si no hay ninguna accion, se establece a vacio.

                if (action == null)
                    action = "";

                String userID = request.getParameter("userID");

                //Si se quiere cerrar la sesion actual
                if (action.equals("closeSession")){
                    closeSession(request,response);
                }
                //Redirige al formulario para editar un usario (user/view)
                else if (action.equals("editRedirect") && userID != null){
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/Users/view.jsp");
                    request.setAttribute("User",getUser(userID));
                    request.setAttribute("UserLogged",getUser(request.getSession().getAttribute("userID").toString()));


                    //Ya que se quiere editar, el atributo permitirEdicion es verdadero. Este atributo se comprueba en el JSP.
                    request.setAttribute("editAllowed",true);
                    request.setAttribute("action","Edit");
                    request.setAttribute("Roles",RolesControllerView.getAllRoles());
                    try{
                        dispatcher.forward(request,response);
                    } catch (javax.servlet.ServletException e){
                        e.printStackTrace();
                    }
                }
                //Redirige al formulario para ver un usuario (user/view)
                else if (action.equals("viewRedirect") && userID != null){
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/Users/view.jsp");
                    request.setAttribute("User",getUser(userID));
                    request.setAttribute("UserLogged",getUser(request.getSession().getAttribute("userID").toString()));
                    request.setAttribute("Roles",RolesControllerView.getAllRoles());

                    //Ya que no quiere editar, el atributo permitirEdicion es falso. Este atributo se comprueba en el JSP.
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

            } else {

                request.getSession().setAttribute("serverResponse","{\"color\": \"red\",\"response\":\"You don\\'t have permission to edit/view a user.\"}");
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

    /**
     * Metodo Estatico getUser:
     *
     * Este metodo permite obtener un usuario dese cualquier parte del proyecto. Toma un userID para buscar el Usuario, y lo devuelve.
     *
     * @param userID    ID del usuario que queremos recibir.
     * @return          El Usuario encontrado con ese userID. Si no lo encuentra, devuelve null.
     * @see             User
     *
    * */
    public static User getUser(String userID){
        PersistenceManager pm = controller.PMF.get().getPersistenceManager();
        try{
            User user = pm.getObjectById(User.class, userID);
            pm.close();
            return user;
        } catch (JDOObjectNotFoundException e){
            pm.close();
            return null;
        }
    }

    /**
     * Metodo estatico getAllUsers
     *
     * Este metodo permite obtener una lista con TODOS los Users registrados desde cualquier parte del codigo.
     *
     * @return          Un List<User> con todos los usuarios encontrados. Como minimo, siempre habra 1, el que inicio sesion.
     * @see             User
     *
     * */
    @SuppressWarnings("unchecked")
    static List<User> getAllUsers(){
        PersistenceManager pm = controller.PMF.get().getPersistenceManager();
        List<User> users = (List<User>) pm.newQuery("select from " + User.class.getName()).execute();
        pm.close();
        return users;
    }

    /**
     * Metodo closeSession
     *
     * Este metodo cierra la sesion actual y la sesion de Google. Primero, obtiene la sesion actual del objeto HttpSession y la elimina,
     * e imprime un documento HTML basico, que se encarga de cerrar la sesion de Google y redirigir a la pagina de inicio.
     *
     *
     * @param request   Request
     * @param response  Response
     *
     * */
    private void closeSession(HttpServletRequest request, HttpServletResponse response){
        HttpSession sesion = request.getSession();
        sesion.invalidate();
        String urlRef = request.getRequestURL().toString();

        if (urlRef.contains("8/")){
            urlRef =(urlRef.substring(0,urlRef.indexOf("8/")+2));
        } else {
            urlRef = (urlRef.substring(0,urlRef.indexOf("m/")+2));
        }

        try {
            response.getWriter().println("<html><head><script>window.location.replace(\"https://www.google.com/accounts/" +
                    "Logout?continue=https://appengine.google.com/_ah/logout?continue=" + urlRef + "\");</script></head></html>");
        }catch (IOException e){
            e.printStackTrace();
        }

    }


}
