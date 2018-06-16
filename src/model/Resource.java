package model;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable
public class Resource {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    //URL del recurso
    @Persistent
    private String Url;

    //Estado del recurso
    @Persistent
    private boolean status;

    //Fecha de creacion del recurso
    @Persistent
    private String created;

    //Constructor
    public Resource(String url,boolean status){
    this.Url=url;
    this.status=status;
    DateFormat df = new SimpleDateFormat("HH:mm:ss dd/MM/yy");
    created = df.format(Calendar.getInstance().getTime());
    }

    public String getKey() {
     return KeyFactory.keyToString(key);
    }
    public void setUrl(String url){
    this.Url=url;
    }
    public String getUrl(){
    return this.Url;
    }
    public void setStatus(boolean status){
    this.status=status;
    }
    public boolean getStatus(){
    return this.status;
   }

    public String getCreateDate(){
        return created;
    }

  public String toString(){
    return "Recurso url: " + Url +"\n";
  }
}
