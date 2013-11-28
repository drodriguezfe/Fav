package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Tag extends Model{
	@Id
	public String id;
	
	@Required
	public String nombre;
	@Required

	@ManyToMany(mappedBy="tags")
	public List<Url> urls = new ArrayList<Url>();
	
	public static Finder<Long,Tag> finder = new Finder<Long,Tag>(Long.class,Tag.class);
	
	public static Tag FindByNameTag(String tag){
		
		return finder.where().eq("nombre", tag).findUnique();
		
	}

	public List<Url> getUrls() {
		return urls;
	}
	
}
