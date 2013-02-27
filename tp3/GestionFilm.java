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

    void ajoutFilm(String readString, Date readDate, String readString0) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void supFilm(String readString, Date readDate) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void ajoutDescFilm(String readString, Date readDate, String readString0, int readInt) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void listeRealisateur() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
