package controller.resourcesManagement.employees;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import controller.PMF;
import controller.usersManagement.access.AccessControllerView;
import model.Employee;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("serial")
public class EmployeesControllerDelete extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try{

            if (AccessControllerView.checkPermission(request.getSession().getAttribute("userID").toString(),request.getRequestURI())){

                PersistenceManager pm = PMF.get().getPersistenceManager();

                String employeeKey = request.getParameter("employeeKey");

                try{
                    Key k = KeyFactory.stringToKey(employeeKey);
                    Employee employee = pm.getObjectById(Employee.class, k);

                    pm.deletePersistent(employee);

                    request.getSession().setAttribute("serverResponse","{\"color\": \"#26a69a\",\"response\":\"Empleado eliminado con éxito.\"}");
                } catch (JDOObjectNotFoundException e){
                    System.err.println("Exception catched -> " + e.getMessage());
                }

                pm.close();

                response.sendRedirect("/e/employees");

            } else {
                request.getSession().setAttribute("serverResponse","{\"color\": \"red\",\"response\":\"No tienes permiso para eliminar un Empleado.\"}");
                response.sendRedirect("/e/users");
            }


        } catch (NullPointerException e){
            response.sendRedirect("/");
        }


    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        doGet(request, response);
    }

}