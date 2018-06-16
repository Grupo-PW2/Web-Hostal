package model;

public class Resource {
  
  @Persistent
  @PrimaryKey
  private String id;
 
  //URL del recurso 
  @Persistent
  private String Url;
 
  //Estado del recurso
  @Persistent
  private boolean status;
 
 //Fecha de creacion del recurso
  @Persistent
  private Date created;
  
  //Constructor 
  public Resource(String id,String url,boolean status,Date create){
    this.id=id;
    this.Url=url;
    this.status=status;
    this.created=create;
  }
  
  public void setId(String id){
    this.id=id;
  }
  public String getId(){
    return this.id;
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
}
