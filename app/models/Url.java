package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
@Entity
public class Url extends Model{
	@Id
	public Long id;
	@Required
	@MaxLength(20)
	public String nombre;
	
	@Required
	@ManyToMany(cascade = CascadeType.ALL)
	public List<Tag> tags= new  ArrayList<Tag>();
	
	
	
	
	@ManyToOne
	public Usuario user;
	
	public String getName() {
		return nombre;
	}
	public void setUrl(String url) {
		this.nombre = nombre;
	}
	
	public static Finder<Long,Url> finder = new Finder<Long,Url>(Long.class,Url.class);
	public static Url findByNombre(String url){
		//Pongo que el nombre sea Unique para que no me de problemas cuando haga la búsqueda	
		return finder.where().eq("nombre", url).findUnique();
		

	}
	
	
	
	
}
