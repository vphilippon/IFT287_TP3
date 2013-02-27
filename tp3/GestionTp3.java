package tp3;
import java.sql.*;
/*
 * Projet : Tp3
 *
 * Membres :
 * - Guillaume Harvey 12 059 595
 * - Kevin Labrie 12 113 777
 * - Vincent Philippon 12 098 838
 * - Mathieu Larocque 10 129 032
 *
 * Tache :
 * - Guillaume Harvey : 
 * - Kevin Labrie : 
 * - Vincent Philippon : 
 * - Mathieu Larocque : 
 * 
 */

// Copié du fichier GestionBibliotheque.java fournit sur le site: http://pages.usherbrooke.ca/vducharme/ift287/
/**
  * Ouvre une connexion avec la BD relationnelle et
  * alloue les gestionnaires de transactions et de tables.
  * <pre>
  * 
  * @param serveur SQL
  * @param bd nom de la bade de données
  * @param user user id pour établir une connexion avec le serveur SQL
  * @param pwd mot de passe pour le user id
  *</pre>
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
    gestionPersonne = new GestionPersonne(personne);
    gestionRoleFilm = new GestionRoleFilm(roleFilm, film, personne);
    gestionSerie = new GestionSerie(serie, personne);
    gestionEpisode = new GestionEpisode(episode, serie, personne);
    gestionRoleEpisode = new GestionRoleEpisode(roleEpisode, serie, personne);
    }
    
    public void fermer() throws SQLException
    {
        // fermeture de la connexion
        cx.fermer();
    }
    
    
}
