package controller.financesManagement.transactions;

import controller.usersManagement.users.UsersControllerView;
import model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static controller.financesManagement.transactions.TransactionsControllerView.getAllTransactions;

public class TransactionsControllerIndex extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");

        //Se usa para revisar si hay una sesion activa
        HttpSession sesion= request.getSession();

        //Intenta hallar una sesion activa
        try{
            User user = UsersControllerView.getUser(sesion.getAttribute("userID").toString());
            if (user == null) throw new NullPointerException("UsersControllerIndex: El usuario recibido es nulo.");

            request.setAttribute("User",user);
            request.setAttribute("transactionsList",getAllTransactions());
            request.setAttribute("serverResponse",sesion.getAttribute("serverResponse"));
            sesion.setAttribute("serverResponse","!");
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/Transactions/index.jsp");
            dispatcher.forward(request,response);

        }
        //Si no la encuentra, redirige a la pagina inicial.
        catch (Exception e){
            System.err.println("UserControllerIndex: Error catched. " + e.getMessage());
            response.getWriter().println("<html><head><script>window.location.replace(\"../\")</script></head></html>");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
