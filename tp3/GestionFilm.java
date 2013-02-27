package tp3;

import java.sql.Date;
import java.sql.SQLException;

class GestionFilm {
    
    private Film film;
    private Personne personne;
    private Connexion cx;

    /**
     * Création d'une instance
     * @param film
     * @param personne
     * @throws Tp3Exception 
     */
    GestionFilm(Film film, Personne personne) throws Tp3Exception{
        this.cx = film.getConnexion();
        if (film.getConnexion() != personne.getConnexion()){
            throw new Tp3Exception("Les instances de film et de personne n'utilisent pas la même connexion");
        }
        this.film = film;
        this.personne = personne;
    }

    void ajoutFilm(String titre, Date dateSortie, String realisateur) throws SQLException, Tp3Exception, Exception {
        try {
            // Vérifie si le film existe déja 
            if (film.existe(titre, dateSortie)) {
                throw new Tp3Exception("Film existe deja: " + titre + " " + dateSortie);
            }
            // S'assure que le réalisateur existe
            if (!personne.existe(realisateur)){
                throw new Tp3Exception("Le réalisateur n'existe pas: " + realisateur);
            }
                Date realisateurNaissance = personne.getPersonne(realisateur).getDateNaissance();
                // S'assure que le réalisateur est né avant la sortie du film
            if (realisateurNaissance.after(dateSortie)){
                throw new Tp3Exception("Le réalisateur est né le: " + realisateurNaissance + " et ne peut pas participer à un film créé le: " + dateSortie);
            }  
            // Ajout du livre dans la table des livres
            film.ajouter(titre, dateSortie, realisateur);
            cx.commit();
        }
    catch (Exception e)
        {
            cx.fermer();
            throw e;
        }
    }
    
    /**
     * 
     * @param titre
     * @param dateSortie 
     */
    void supFilm(String titre, Date dateSortie) throws SQLException, Tp3Exception, Exception {
        try {
            // Vérifie si le film existe déja
            if (!film.existe(titre, dateSortie)) {
                    throw new Tp3Exception("Film n'existe pas: " + titre + " " + dateSortie);
                }
            // Supression du livre dans la table des livres
            int nb = film.enlever(titre, dateSortie);
            System.out.println(nb + " film ajouté");
            cx.commit();
        }
        catch (Exception e)
        {
            cx.fermer();
            throw e;
        }
    }
    
    /**
     * 
     * @param titre
     * @param anneeSortie
     * @param description
     * @param duree 
     */
    void ajoutDescFilm(String titre, Date anneeSortie, String description, int duree) throws Tp3Exception, SQLException, Exception {
        try{
            if(!film.existe(titre, anneeSortie)){
                throw new Tp3Exception("Film n'existe pas: " + titre + " " + anneeSortie);
            }
            film.ajouterDescription(film.getFilm(titre, anneeSortie), description, duree);
            cx.commit();
        }
        catch(Exception e){
            cx.fermer();
            throw e;
        }
    }
    
    void listeRealisateur() throws Tp3Exception, SQLException, Exception {
        try{
            film.listerRealisateur();
            cx.commit();
        }
        catch(Exception e)
        {
            cx.fermer();
            throw e;
        }
    }
}
