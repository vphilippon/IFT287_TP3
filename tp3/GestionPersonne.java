package tp3;

import java.sql.Date;
import java.sql.SQLException;

class GestionPersonne {
    
    private Personne personne;
    private Connexion cx;
    
    GestionPersonne(Personne personne) {
        this.cx = personne.getConnexion();
        this.personne = personne;
    }

    void ajoutPersonne(String nom, Date dateNaissance, String lieuNaissance, int sexe) throws SQLException, Tp3Exception {
        // Vérifie si la personne existe déja 
        if (personne.existe(nom)) {
            throw new Tp3Exception("Personne existe deja: " + nom);
        }
        personne.ajouter(nom, dateNaissance, lieuNaissance, sexe);
        cx.commit();
        // XXX VP : On veut pas fermer la connexion si ça plante. 
        // XXX VP : On veut RollBack, dans gestionnaire tp3, en pitchan l'erreur, pas de try ici.
    }

    void supPersonne(String nom) throws Tp3Exception, SQLException {
        // Vérifie si la personne existe 
        if (!personne.existe(nom)) {
            throw new Tp3Exception("Personne n'existe pas : " + nom);
        }
        // XXX AUTRE VALIDATION
        // S'il n'est le realisateur d'aucun film
        // S'il n'a aucun role
        
        personne.enlever(nom);
        cx.commit();
    }
}
