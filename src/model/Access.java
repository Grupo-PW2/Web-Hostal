package model;

import controller.employee.resources.ResourcesControllerView;
import controller.employee.roles.RolesControllerView;

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
    private String roleKey;

    @Persistent
    private String resourceKey;

    @Persistent
    private boolean status;

    public Access(String idRole, String idResource, boolean status) {
        this.roleKey = idRole;
        this.resourceKey = idResource;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getRoleKey() {
        return roleKey;
    }
    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }

    public String getResourceKey() {
        return resourceKey;
    }
    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
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
            ret = RolesControllerView.getRole(roleKey).getName();
        } catch (Exception e){
            ret = "<span style=\"color: red; font-weight: bold\">The Role doesn't exists.</span>";
        }
        return ret;
    }

    public String getResourceName(){
        String ret;
        try {
            ret = ResourcesControllerView.getResource(resourceKey).getUrl();
        }catch (Exception e){
            ret = "<span style=\"color: red; font-weight: bold\">The Resource doesn't exists.</span>";
        }
        return ret;
    }

    @Override
    public String toString() {
        return  "[ ID: " + id +"\n" +
                "roleKey: " + roleKey + "\n" +
                "resourceKey: " + resourceKey + "\n" +
                "status: " + status +"\n" +
                "]";
    }
}