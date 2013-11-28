package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Tag;
import models.Url;
import models.Usuario;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.w3c.dom.Document;

import play.core.Router;
import play.mvc.*;
public class Application extends Controller {
//	getUsuariosAcabado
//	insertUsuarioAcabado
//	convertirenUsuarios acabados
//	updateUsuario acabado
//	getUserFromBody acabado
//	
    public static Result getUsuarios() {
    	List<Usuario> users = Usuario.finder.all();
    	String contentType = request().getHeader("Content-Type"); 
    	if (contentType.startsWith("application/json")) {
    		
    		return ok(views.txt.usuarios.render(users));
		}
		else if (contentType.startsWith("application/xml")) {
			
			return ok(views.xml.usuarios.render(users));
		}
		else {
			return badRequest();
		}
			
		
	}
    //Recurso insercion de usuario peticion POST /usuario
    public static Result insertUsuario() {
    	
    		
    		
    		String contentType = request().getHeader("Content-Type");
    		if(contentType.startsWith("application/json")){
    		JsonNode datos = request().body().asJson();
    		Usuario nuevoUser = convertirJsonEnUsuario(datos);
    		//Comprobar que el nick es único para que no me de un internal error cuando ejecute la búsqueda de urls por nick
    		if(Usuario.findByNick(nuevoUser.nick)!=null)
    			return forbidden();
    		
    			nuevoUser.save();
    			return ok();    		
    		

    		
    		
    		
    		}
//    		Para introducir datos por XML pongo el codigo, pero no me guarda los datos al pedir del body el XML,datos = null
//    		no se porque me da un null pointer
    		else if(contentType.startsWith("text/xml")){
    			Document datos = request().body().asXml();
    			Usuario user = convertirXmlEnUsuario(datos);
    			user.save();
    			return ok();
    		}
    		else{
   			
    			return badRequest();
    		}
    	}
    
    //Funcion para convertir los datos en XML a el bean Usuario del models	
    //no funciona el Document no recibe los datos, en teoría creo que esta bien no se si no se puede hacer o no encuentro el fallo
    
    private static Usuario convertirXmlEnUsuario(Document datos) {
    		
    		String nombre= datos.getElementsByTagName("nombre").item(0).getTextContent();
    		String nick= datos.getElementsByTagName("nick").item(0).getTextContent();
    		
    		Usuario user = new Usuario();
    		
    		user.nombre = nombre;
    		user.nick = nick;
    		return user;
	}
	
    //Funcion para convertir los datos en JSON a el bean Usuario del models	
   
    public static Usuario convertirJsonEnUsuario(JsonNode datos){
    	 String nombre = datos.get("nombre").asText();
    	 String nick = datos.get("nick").asText();
 		
		
		Usuario nuevoUser = new Usuario();
		nuevoUser.nombre=nombre;	
		nuevoUser.nick=nick;	
		
		return nuevoUser;
    }
   
    //Recurso deleteUsuario DELETE /usuario/:idUser
    
    public static Result deleteUsuario(Long idUser) {
    	    	
      		
      		
      		Usuario user = Usuario.finder.byId(idUser);
      		if (user == null) {
      			return notFound();
      		}
      		else {
      			user.delete();
      			return ok();
      		} 
    }
    
    //Recurso updateUsuario PUT /usuario/:idUser
    
    public static Result updateUsuario(Long idUser){
    	
    	Usuario user = Usuario.finder.byId(idUser);
		if (user == null) {
			return notFound();
		}
		else{
			Usuario newUser = getUserFromBody();
			if (newUser == null) {
				return badRequest();
			}else{
				if (user.changeData(newUser)) {
					user.save();
					return ok();
				}
				else {
					return status(NOT_MODIFIED);
				}
			}
		}
		
	
    	
    }
    
    /*Con esta función cogemos el Usuario del body y dependiendo de en que formato metan el body elegimos uno u otro

    
    Disponible JSON y XML*/
    
    public static Usuario getUserFromBody(){
		Usuario user;
		String contentType = request().getHeader("Content-Type"); 
		if(contentType.startsWith("application/json")){
			JsonNode dataUpdate = request().body().asJson();
			user= new Usuario(dataUpdate);
		}else if(contentType.startsWith("application/xml")){
			Document dataUpdate = request().body().asXml();
			user= new Usuario(dataUpdate);
		}else{
			user= null;
		}
		
		return user;
	}	
   
    //Con este método pasamos el JSON del body a el bean Url de models
    
    public static Url convertirJsonEnUrl(JsonNode datos){
    	 String nombre = datos.get("url").asText();
    	 JsonNode jsonTags = datos.get("tags");
    	 
    	 Url newUrl = new Url();
 		 newUrl.nombre=nombre;
    	 for(int i=0;i<jsonTags.size();i++){
    		 Tag tag = new Tag();
    		 
    		 tag.nombre = jsonTags.get(i).get("nombre").asText();
    		 if(Tag.FindByNameTag(tag.nombre)==null){
    			 tag.save();
    		 }
    		 newUrl.tags.add(tag);
    	 }
    	 newUrl.save();
    	return newUrl;
}
 
    //Este método permite meter Urls a los usuarios por id
    //Recurso POST /usuario/:idUser/favorito
    
    public static Result insertUrlToUser(Long idUser){
    	
    	Usuario user = Usuario.finder.byId(idUser);
    	JsonNode datos = request().body().asJson();
    	
    	Url newUrl = convertirJsonEnUrl(datos);
    	newUrl.user=user;
    	newUrl.save();
    	user.setUrls(newUrl);
    	System.out.println(user);
    	return ok(newUrl.getName());
    	
    }
    
    
    //Muestra el listado de las urls por el nick name del usuario como en la evaluación pone "Permite búsqueda por nombre de usuario"
    //He supuesto que la búsqueda será de las urls por nombre de usuario y que éste sera único,
    //ver comentario findByNombre pakage models bean Usuario
    //Recurso GET /usuario/:nombre/favoritos
    
    public static Result getURLsNick(String nick) {
    	Usuario user = Usuario.findByNick(nick);
    	List<Url> urls = user.getUrls();
    	
    	
    	//Gestion de las vistas JSON y XML dependiendo del Content type del body
    	
    	
    	String contentType = request().getHeader("Content-type");
    	if (contentType.startsWith("application/json")) {

        	
    		return ok(views.txt.favoritos.render(urls));
		}
		else if (contentType.startsWith("application/xml")) {
			
			return ok(views.xml.favoritos.render(urls));
		}
		else {
			return badRequest();
		}
    	
    }
  
			
//   Listando de Usuarios buscados por nombre  GET /usuario/$nombre<[a-z]>
    
    public static Result findUsersByNombre(String nombre){
    	Usuario user = Usuario.findByNombre(nombre);

    	if(user != null){
    		String contentType = request().getHeader("Content-Type");
    		if(contentType.startsWith("application/json")){
    			
    			return ok(views.txt.usuario.render(user));
    		
    		}else if(contentType.startsWith("application/xml")){
    			
    			return ok(views.xml.usuario.render(user));
    			
    		}
    	}else{
    		return badRequest();
    	}

    	return notFound();
    }
    
//  Limpieza de enlaces rotos
    
    public static Result deleteBrokenUrl(String nombreUrl, String nick){
    	Usuario user = Usuario.findByNick(nick);
    	List<Url> urls = user.getUrls();
    	System.out.println(urls);
    	for(Url url: urls){
    		if(Url.findByNombre(nombreUrl)!=null){
    		Url.findByNombre(nombreUrl).delete();
    		return ok();
    		}
    		}
    	
    
    return notFound();
    }
    
//    Búsqueda por tags
    
    public static Result findTags(String tag){
    	Tag tagUrls = Tag.FindByNameTag(tag);
    	if(tagUrls!= null){
    	List<Url> urls = tagUrls.getUrls();
    	
    	System.out.println(Tag.FindByNameTag(tag));
    	
    	//Gestion de las vistas JSON y XML dependiendo del Content type del body
    	
    	
    	String contentType = request().getHeader("Content-type");
    	if (contentType.startsWith("application/json")) {

        	
    		return ok(views.txt.tag.render(urls));
		}
		else if (contentType.startsWith("application/xml")) {
			
			return ok(views.xml.tag.render(urls));
		}
		else {
			return badRequest();
		}
    }else{
    	return notFound();
    }
    	
    }

}
