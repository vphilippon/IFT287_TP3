/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tp3;

import java.sql.Date;
import java.sql.SQLException;

/**
 *
 * @author guillaume
 */
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
            // Vérifie si le livre existe déja 
            if (film.existe(titre, dateSortie)) {
                throw new Tp3Exception("Film existe deja: " + titre + " " + dateSortie);
            }
            else
                // Vérifie si le réalisateur n'existe pas
                if (!personne.existe(realisateur)){
                    throw new Tp3Exception("Le réalisateur n'existe pas: " + realisateur);
                }
                else 
                {
                    Date realisateurNaissance = personne.getPersonne(realisateur).getDateNaissance();
                    // Verifie si le réalisateur est né après la sortie du film
                    if (realisateurNaissance.after(dateSortie)){
                        throw new Tp3Exception("Le réalisateur est né le: " + realisateurNaissance + " et ne peut pas participer à un film créé le: " + dateSortie);
                    }
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

    void supFilm(String titre, Date dateSortie) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void ajoutDescFilm(String titre, Date anneeSortie, String description, int duree) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void listeRealisateur() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
