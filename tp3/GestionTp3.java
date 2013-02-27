/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tp3;
import java.sql.*;

/**
 *
 * @author guillaume
 */
public class GestionTp3 {
    public Connexion cx;
    public Film film;
    public Personne personne;
    public RoleFilm roleFilm;
    public Serie serie;
    public Episode episode;
    public RoleEpisode roleEpisode;
    public GestionFilm gestionFilm;
    public GestionPersonne gestionPersonne;
    public GestionRoleFilm gestionRoleFilm;
    public GestionSerie gestionSerie;
    public GestionEpisode gestionEpisode;
    public GestionRoleEpisode gestionRoleEpisode;
    
    public GestionTp3(String serveur, String bd, String user, String pwd) throws Tp3Exception, SQLException{
    cx = new Connexion(serveur,bd,user,pwd);
    film = new Film(cx);
    personne = new Personne(cx);
    roleFilm = new RoleFilm(cx);
    serie = new Serie(cx);
    episode = new Episode(cx);
    roleEpisode = new RoleEpisode(cx);
    gestionFilm = new GestionFilm(film, personne);
    
    }
    
    
}
