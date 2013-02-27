/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tp3;

import java.sql.Date;

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
}
