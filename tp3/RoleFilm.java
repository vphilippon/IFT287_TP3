/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tp3;

import java.sql.Date;
import java.util.List;

/**
 *
 * @author guillaume
 */
class RoleFilm {

    RoleFilm(Connexion cx) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    Connexion getConnexion() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void ajouter(String acteur, String titre, Date anneeSortie) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    boolean existe(String acteur, String titre, Date anneeSortie) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    List<TupleRoleFilm> getActeurs(String titre, Date anneeSortie) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    List<TupleRoleFilm> getFilmWhereActeur(String nom) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
}
