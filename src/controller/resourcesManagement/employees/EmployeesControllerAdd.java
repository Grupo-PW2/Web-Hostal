package controller.resourcesManagement.employees;

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
import model.Employee;

@SuppressWarnings("serial")
public class EmployeesControllerAdd extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

        try{

            if (AccessControllerView.checkPermission(request.getSession().getAttribute("userID").toString(),request.getRequestURI())){
                PersistenceManager pm = PMF.get().getPersistenceManager();

                String action = request.getParameter("action");

                if (action == null)
                    action = "";

                if (action.equals("create")){

                    String name = request.getParameter("Name");
                    String phone = request.getParameter("Phone");
                    String email = request.getParameter("Email");
                    Long dni = Long.parseLong(request.getParameter("Dni"));
                    String userCreatorKey = request.getParameter("userId");

                    Employee employee = new Employee (name, phone, email,dni,true,userCreatorKey);

                    request.getSession().setAttribute("serverResponse","{\"color\": \"#26a69a\",\"response\":\"Employee created successfully.\"}");

                    pm.makePersistent(employee);


                } else if (action.equals("update")){

                    Key k = KeyFactory.stringToKey(request.getParameter("key"));

                    Employee employee = pm.getObjectById(Employee.class,k);

                    employee.setName(request.getParameter("Name"));
                    employee.setDni(Long.parseLong(request.getParameter("Dni")));
                    employee.setEmail(request.getParameter("Email"));
                    employee.setPhone(request.getParameter("Phone"));

                    pm.close();

                    request.getSession().setAttribute("serverResponse","{\"color\": \"#26a69a\",\"response\":\"Empleado creado con éxito.\"}");

                } else if (action.equals("redirect")){

                    HttpSession sesion= request.getSession();
                    RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/Employees/add.jsp");
                    request.setAttribute("User",UsersControllerView.getUser(sesion.getAttribute("userID").toString()));
                    dispatcher.forward(request, response);

                }

                pm.close();
                try{
                    response.sendRedirect("/e/employees");
                }
                //Al redirigr al jsp para crear, se usa RequestDispatcher, y este entra en conflicto con sendRedirect.
                catch (IllegalStateException e){
                    System.err.println("IllegalStateException: There was a double redirect.");
                }

            } else {

                request.getSession().setAttribute("serverResponse","{\"color\": \"red\",\"response\":\"No tienes permiso para crear un Empleado.\"}");
                response.sendRedirect("/e/users");

            }

        } catch (NullPointerException e){
            response.sendRedirect("/");
        }


    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        doGet(request, response);
    }
}