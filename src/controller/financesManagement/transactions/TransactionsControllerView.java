package controller.financesManagement.transactions;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import controller.PMF;
import model.Transaction;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class TransactionsControllerView extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @SuppressWarnings("unchecked")
    static List<Transaction> getAllTransactions (){
        PersistenceManager pm = controller.PMF.get().getPersistenceManager();
        List<Transaction> transactions = (List<Transaction>) pm.newQuery("select from " + Transaction.class.getName()).execute();
        pm.close();
        return transactions;
    }

    public static Transaction getTransaction(String key){
        PersistenceManager pm = PMF.get().getPersistenceManager();

        Key k = KeyFactory.stringToKey(key);
        Transaction service = pm.getObjectById(Transaction.class,k);

        pm.close();
        return service;
    }
}
