package controller;

		import javax.jdo.JDOHelper;
		import javax.jdo.PersistenceManagerFactory;

/* clase "pmf.PMF" que va a tener la variable encargada de controlar las peticiones
 * de registros y búsqueda de la información almacenada. */
public final class PMF {
	//Variable encargada de controlar las peticiones
	private static final PersistenceManagerFactory instance = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	//Para que no puedan instanciar la clase
	private PMF(){}
	//Retorna la instancia encargada de controlar las peticiones
	public static PersistenceManagerFactory get(){
		return instance;
	}
}
