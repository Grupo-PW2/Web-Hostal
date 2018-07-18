package controller.clientControllers;

import controller.usersManagement.users.UsersControllerView;
import model.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

public class transactionsHistory extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");

        //Se usa para revisar si hay una sesion activa
        HttpSession sesion= request.getSession();

        //Intenta hallar una sesion activa
        try{
            User user = UsersControllerView.getUser(sesion.getAttribute("userID").toString());
            if (user == null) throw new NullPointerException("UsersControllerIndex: El usuario recibido es nulo.");

            request.setAttribute("User",user);
            request.setAttribute("serverResponse",sesion.getAttribute("serverResponse"));

            ArrayList<String> lista = crearArrayList(user.getTransactionList());

            if (lista != null){
                request.setAttribute("transactionList",lista);

                sesion.setAttribute("serverResponse","!");
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/View/ClientViews/transactionsHistory.jsp");
                dispatcher.forward(request,response);

            } else {

                request.getSession().setAttribute("serverResponse","{\"color\": \"orange\",\"response\":\"Aún no tienes ninguna transacción..\"}");
                response.sendRedirect("/");

            }

        }
        //Si no la encuentra, redirige a la pagina inicial.
        catch (Exception e){
            System.err.println("Error catched. " + e.getMessage());
            e.printStackTrace();
            response.getWriter().println("<html><head><script>window.location.replace(\"../\")</script></head></html>");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private ArrayList<String> crearArrayList(String datos){
        ArrayList<String> lista = new ArrayList<>();

        if (datos.length() > 2){
            do
            {
                lista.add(datos.substring(0,datos.indexOf(",")));
                datos = datos.substring(datos.indexOf(",")+1);
            }
            while (datos.length() > 2);

            return lista;
        }

        return null;
    }

}
