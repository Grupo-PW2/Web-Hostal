package controller.financesManagement.transactions;

import controller.PMF;
import controller.resourcesManagement.services.ServicesControllerView;
import controller.usersManagement.users.UsersControllerView;
import model.Service;
import model.Transaction;
import model.User;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TransactionsControllerAdd extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");

        try{

            String action = request.getParameter("action");

            //Para evitar errores, si no hay ninguna accion, se establece a vacio.
            if (action == null)
                action = "";

            if (action.equals("userAddTransaction")){

                PersistenceManager pm = PMF.get().getPersistenceManager();

                String userId = request.getSession().getAttribute("userID").toString();
                Service service = ServicesControllerView.getService(request.getParameter("serviceKey").toString());

                Transaction transaction = new Transaction(userId,service.getKey(),service.getPrice() + "");
                User user = pm.getObjectById(User.class, request.getSession().getAttribute("userID").toString());

                pm.makePersistent(transaction);

                user.addTransaction(transaction.getKey());

                request.getSession().setAttribute("serverResponse","{\"color\": \"#26a69a\",\"response\":\"¡Listo! Tu transacción se completó.\"}");

                pm.close();
            }

            try{
                response.sendRedirect("/");
            }
            //Al redirigr al jsp para crear, se usa RequestDispatcher, y este entra en conflicto con sendRedirect.
            catch (IllegalStateException e){
                System.err.println("IllegalStateException: There was a double redirect.");
            }

        } catch (NullPointerException e){
            System.err.println("Error NPE ->");
            e.printStackTrace();
            response.sendRedirect("/");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
