//Es importante que el paquete sea models ya que si no play no lo reconocera como la carpeta
//en la que vienen los modelos
package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

import org.codehaus.jackson.JsonNode;
import org.w3c.dom.Document;

import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

//Extendemos la clase para que extienda de play.db.ebean.Model

@Entity
public class Usuario extends Model{
	@Id
	public Long id;
	@Required
	@MaxLength(20)
	public String nombre;
	
	@Required
	public String nick;

	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="user")
	public  List<Url> urls = new ArrayList<Url>();
	
	public List<Url> getUrls() {
		return urls;
	}

	public void setUrls(Url url) {
		this.urls.add(url);
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	//Constructor por defecto
	public Usuario(){
		
	}
	
	//Para asignar el nombre de usuario del body como JSON hacemos este constructor
	public Usuario(JsonNode dataUpdate){
		super();
		this.nombre=dataUpdate.get("nombre").asText();
	}
	
	//Para asignar el nombre de usuario del body como XML hacemos este otro constructor
	public Usuario(Document dataUpdate){
		super();
		this.nombre=dataUpdate.getElementsByTagName("nombre").item(0).getTextContent();
	}
	
	//Con esta funcion se comprueba si se ha cambiado los datos del usuario correctamente y se devuelve un Boolean
	public boolean changeData(Usuario newData) {
		boolean changed = false;
		
		if (newData.nombre != null) {
			this.nombre = newData.nombre;
			changed = true;
		}
		
		return changed;
	}
	
	public static Finder<Long,Usuario> finder = new Finder<Long,Usuario>(Long.class,Usuario.class);
	
	
	public static Usuario findByNombre(String nombre){
		//Pongo que el nombre sea Unique para que no me de problemas cuando haga la búsqueda	
		return finder.where().eq("nombre", nombre).findUnique();
		

	}
	public static Usuario findByNick(String nick){
		//Pongo que el nick sea Unique para que no me de problemas cuando haga la búsqueda	
		return finder.where().eq("nick", nick).findUnique();
		

	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", urls=" + urls
				+ "]";
	}
	
	

}
