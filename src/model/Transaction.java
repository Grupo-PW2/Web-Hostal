package model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import controller.resourcesManagement.services.ServicesControllerView;

import javax.jdo.annotations.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Transaction {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String userID;

    @Persistent
    private String serviceKey;

    @Persistent
    private String servicePrice;

    @Persistent
    private String createDate;

    public Transaction(String userID, String serviceKey, String servicePrice) {
        this.userID = userID;
        this.serviceKey = serviceKey;
        this.servicePrice = servicePrice;

        DateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yy");
        this.createDate = df.format(Calendar.getInstance().getTime());
    }

    public String getKey() {
        return KeyFactory.keyToString(key);
    }

    public String getUserID() {
        return userID;
    }

    public String getServiceKey() {
        return serviceKey;
    }
    public String getServiceName(){
        return ServicesControllerView.getService(serviceKey).getName();
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public String getCreateDate() {
        return createDate;
    }
}
