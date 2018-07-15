package model;

import javax.jdo.annotations.*;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import controller.usersManagement.users.UsersControllerView;

@PersistenceCapable(identityType=IdentityType.APPLICATION)
public class Employee {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String name;

    @Persistent
    private String phone;

    @Persistent
    private String email;

    @Persistent
    private Long dni;

    @Persistent
    private boolean status;

    @Persistent
    private String creatorUserId;

    public Employee(String name, String phone, String email, Long dni,boolean status,String creatorUserKey) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.dni = dni;
        this.status = status;
        this.creatorUserId = creatorUserKey;
    }


    /*Getters and Setters*/
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Long getDni() {
        return dni;
    }
    public void setDni(Long dni) {
        this.dni = dni;
    }

    public boolean getStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getKey() {
        return KeyFactory.keyToString(key);
    }

    public String getCreatorUserId(){
        return creatorUserId;
    }

    public String getCreatorUserName(){
        String name;
        try{
            name = UsersControllerView.getUser(creatorUserId).getName();
        } catch (Exception e){
            name = "<span style=\"color: red; font-weight: bold\">The User doesn't exists.</span>";
        }
        return name;
    }

    /*To String*/
    public String toString(){
        return "Name: " + name + "\n Dni: " + dni + "\n Email: " + email + "\n Phone: " + phone + ".\n";
    }

}