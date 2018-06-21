package model;

import controller.resources.ResourcesControllerView;
import controller.roles.RolesControllerView;
import org.datanucleus.exceptions.NucleusObjectNotFoundException;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Access {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String idRole;

    @Persistent
    private String idResource;

    @Persistent
    private boolean status;

    public Access(String idRole, String idResource, boolean status) {
        this.idRole = idRole;
        this.idResource = idResource;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getIdRole() {
        return idRole;
    }
    public void setIdRole(String idRole) {
        this.idRole = idRole;
    }

    public String getIdResource() {
        return idResource;
    }
    public void setIdResource(String idResource) {
        this.idResource = idResource;
    }

    public boolean getStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getRoleName(){
        String ret;
        try {
            ret = RolesControllerView.getRole(idRole).getName();
        } catch (Exception e){
            ret = "<span style=\"color: red; font-weight: bold\">The Role doesn´t exists.</span>";
        }
        return ret;
    }

    public String getResourceName(){
        String ret;
        try {
            ret = ResourcesControllerView.getResource(idResource).getUrl();
        }catch (Exception e){
            ret = "<span style=\"color: red; font-weight: bold\">The Resource doesn´t exists.</span>";
        }
        return ret;
    }

}