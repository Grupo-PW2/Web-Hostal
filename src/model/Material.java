package model;

import javax.jdo.annotations.*;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@PersistenceCapable(identityType=IdentityType.APPLICATION)
public class Material {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String name;

    //Precio por unity
    @Persistent
    private int price;

    //Tipo de unidad -> Peso, cantidad, medida, volumen, etc.
    @Persistent
    private String unity;

    @Persistent
    private int amount;

    @Persistent
    private String createDate;

    public Material(String name, int price, int quantity, String unity) {
        this.name = name;
        this.price = price;
        this.amount = quantity;
        this.unity = unity;

        DateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yy");
        this.createDate = df.format(Calendar.getInstance().getTime());
    }

    public String getKey() {
        return KeyFactory.keyToString(key);
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }

    public String getUnity() {
        return unity;
    }
    public void setUnity(String unity) {
        this.unity = unity;
    }

    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCreateDate() {
        return createDate;
    }
}

