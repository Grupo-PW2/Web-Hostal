package model;

import javax.jdo.annotations.*;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import controller.usersManagement.users.UsersControllerView;

@PersistenceCapable(identityType=IdentityType.APPLICATION)
public class Service {

	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private String name;
	
	@Persistent
	private double price;
	
	@Persistent
	private String description;

	@Persistent
    private String creatorUserId;

	public Service(String name, double price, String description, String creatorUserKey) {
		this.name = name;
		this.price = price;
		this.description = description;
		this.creatorUserId = creatorUserKey;
	}
	

	/*Getters and Setters*/
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
		return "Name: " + name + "\n Price: " + price + "\n Description: " + description + ".\n";
	}
	
}
