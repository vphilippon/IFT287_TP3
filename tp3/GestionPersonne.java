/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tp3;

import java.sql.Date;
import java.sql.SQLException;

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
class GestionPersonne {
    
    private Personne personne;
    private Connexion cx;
    
    GestionPersonne(Personne personne) {
        this.cx = personne.getConnexion();
        this.personne = personne;
    }

    void ajoutPersonne(String nom, Date dateNaissance, String lieuNaissance, int sexe) throws SQLException, Tp3Exception, Exception {
         try{   
            // Vérifie si la personne existe déja 
            if (personne.existe(nom)) {
                throw new Tp3Exception("Film existe deja: " + nom);
            }
            personne.ajouter(nom, dateNaissance, lieuNaissance, sexe);
            cx.commit();
         }
         catch(Exception e)
         {
             cx.fermer();
             throw e;
         }
    }

    void supPersonne(String readString) {
        throw new UnsupportedOperationException("Not yet implemented");
    
    }
}
