package tp3;

import java.sql.Date;

class GestionPersonne {
    
    private Personne personne;
    private Connexion cx;
    
    public GestionPersonne(Personne personne) {
        this.cx = personne.getConnexion();
        this.personne = personne;
    }

    public void ajoutPersonne(String nom, Date dateNaissance, String lieuNaissance, int sexe) throws Exception {
        try {
            // Vérifie si la personne existe déjà 
            if (personne.existe(nom)) {
                throw new Tp3Exception("Impossible d'ajouter, la personne existe deja: " + nom);
            }
            personne.ajouter(nom, dateNaissance, lieuNaissance, sexe);
            cx.commit();
        } catch (Exception e) {
            cx.rollback();
            throw e;
        }
    }

    public void supprimerPersonne(String nom) throws Exception {
        try {
            // Vérifie si la personne existe 
            if (!personne.existe(nom)) {
                throw new Tp3Exception("Impossible de supprimer, la personne n'existe pas : " + nom);
            }
            // XXX AUTRE VALIDATION
            // S'il n'est le realisateur d'aucun film
//            film.film
            // S'il n'a aucun role
            
            personne.enlever(nom);
            cx.commit();
        } catch (Exception e) {
            cx.rollback();
            throw e;
        }
    }
}
