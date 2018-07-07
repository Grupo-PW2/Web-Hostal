package controller.users;

import controller.access.AccessControllerView;
import controller.roles.RolesControllerAdd;
import controller.roles.RolesControllerView;
import model.Role;
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
public class UsersControllerAdd extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try{

            if(request.getParameter("action").equals("logIn") ||
                    AccessControllerView.checkPermission(request.getSession().getAttribute("userID").toString(),request.getRequestURI())){

                PersistenceManager pm = controller.PMF.get().getPersistenceManager();

                //Accion a realizar
                String action = request.getParameter("action");

                //Respuesta del servidor
                String serverResponse = "!";

                if (action == null)
                    action = "";

                //Email del usuario
                String userEmail = request.getParameter("userEmail");

                //Solo se usa al actualizar un usuario.
                String prevUserID = request.getParameter("userID");

                //El ID del usuario. Este id se obtiene del email -> en richard@gmail.com el ID es richard
                String userID;
                try {
                    userID = userEmail.substring(0,userEmail.indexOf("@"));
                } catch (NullPointerException e){
                    userID = prevUserID;
                }

                //Parametros necesarios.
                String userName = request.getParameter("userName");
                String userImg = request.getParameter("userImg");
                String userRole = request.getParameter("userRole");

                String rutaRedireccion = "/users";

                switch (action) {
                    //Si se quiere iniciar sesion y/o registrar un usuario desde el inicio de sesion de Google
                    case "logIn":

                        //Busca si ya existe una sesion iniciada
                        HttpSession misesion = request.getSession();

                        List<Role> roleList = RolesControllerView.searchRole(userRole);

                        if (roleList.size() > 0){
                            userRole = roleList.get(0).getKey();
                        } else {
                            userRole = RolesControllerAdd.createRole(userRole,true,pm);
                        }

                        createUser(userID, userEmail, userName, userImg, userRole, pm);

                        //Si no existe la sesion, la crea usando el ID del usuario
                        if (!sesionExist(misesion)) {

                            misesion.invalidate();

                            misesion = request.getSession(true);
                            misesion.setAttribute("userID", userID);

                            //La sesion perdurara sin actividad durante 1h.
                            misesion.setMaxInactiveInterval(3600);
                        }

                        serverResponse = "{\"color\": \"#26a69a\",\"response\":\"You are logged in.\"}";

                        rutaRedireccion = "/";

                        break;

                    //Si lo que se quiere es redirigir al Form para crear usuario
                    case "redirect":
                        HttpSession sesion= request.getSession();
                        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/Users/add.jsp");
                        request.setAttribute("User",UsersControllerView.getUser(sesion.getAttribute("userID").toString()));
                        request.setAttribute("Roles",RolesControllerView.getAllRoles());
                        dispatcher.forward(request, response);
                        break;

                    //Si lo que se quiere es Crear (proviene del formulario)
                    case "create":
                        createUser(userID, userEmail, userName, userImg, userRole, pm);
                        serverResponse = "{\"color\": \"#26a69a\",\"response\":\"User created successfully.\"}";
                        break;

                    //Si lo que se quiere es actualizar un Usuario
                    case "update":

                        User user = pm.getObjectById(User.class, prevUserID);

                        user.setName(userName);
                        user.setEmail(userEmail);
                        user.setImgUrl(userImg);
                        user.setRoleKey(userRole);

                        serverResponse = "{\"color\": \"#26a69a\",\"response\":\"User updated successfully.\"}";
                        break;

                }

                pm.close();
                try{
                    request.getSession().setAttribute("serverResponse",serverResponse);
                    response.sendRedirect(rutaRedireccion);
                }
                //Al redirigr al jsp para crear, se usa RequestDispatcher, y este entra en conflicto con sendRedirect.
                catch (IllegalStateException e){
                    System.err.println("IllegalStateException: There was a double redirect.");
                }

            } else {

                request.getSession().setAttribute("serverResponse","{\"color\": \"red\",\"response\":\"You don\\'t have permission to create a user.\"}");
                response.sendRedirect("/users");

            }


        }catch (NullPointerException e){
            e.printStackTrace();
            response.sendRedirect("/");
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Redirige a doPost
        doPost(request, response);
    }

    //Revisa si un usuario existe: id -> ID del usuario (ejm. en richard@gmail.com el ID es richard)
    private boolean userExists(String userID, PersistenceManager pm){
        try{
            //Intenta buscar en el DataStore un usuario con el ID respectivo.
            User usr = pm.getObjectById(User.class, userID);

            //Si lo encuentra devuelve true (el usuario si existe)
            return true;
        } catch (JDOObjectNotFoundException exc){
            //Si no lo encuentra, se lanza una Excepción, se captura, y se devuelve false (el usuario no existe)
            return false;
        }
    }

    //Comprueba si existe una sesion: sesion -> Objeto HttpSesion que contiene la sesion actual
    private boolean sesionExist(HttpSession sesion){
        try{
            //Intenta buscar el atributo userID dentro de la sesion
            String a = sesion.getAttribute("userID").toString();
            System.out.println("Sesion existe -> " + a);
            //Si lo encuentra, la sesion si existe
            return true;
        } catch (NullPointerException e){
            //Si no, la sesion no existe
            System.out.println("Sesion no existe");
            return false;
        }
    }

    private void createUser(String userID, String userEmail, String userName, String userImg, String userRole, PersistenceManager pm){

        //Revisa si el usuario con su ID ya tiene un objeto User Persistente almacenado.
        //Si no existe, crea el objeto de tipo User con los datos que se obtienen del request, y lo hace Persistente.
        if (!userExists(userID, pm)){

            //El new Role es provisional, hasta que termine la implementacion del CRUD de Role.
            User user = new User(userID, userName, userImg, userEmail, userRole);

            try{
                pm.makePersistent(user);
            } finally {
                System.out.println("Usuario creado con exito -> " + user);
            }

        }
        //Si el usuario ya existe, lo obtiene, revisa su imagen de perfil, y la actualiza (si al iniciar sesion con Google no coinciden).
        else {

            User loggedUser = pm.getObjectById(User.class,userID);

            if (!userImg.equals(loggedUser.getImgUrl()))
                loggedUser.setImgUrl(userImg);

        }

    }


}
