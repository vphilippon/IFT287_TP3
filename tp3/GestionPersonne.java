package tp3;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

class GestionPersonne {
    
    private Personne personne;
    private Film film;
    private RoleFilm roleFilm;
    private Connexion cx;
    
    public GestionPersonne(Personne personne, Film film, RoleFilm roleFilm) throws Tp3Exception {
        this.cx = personne.getConnexion();
        if (cx != film.getConnexion() ||
            cx != roleFilm.getConnexion()){
            throw new Tp3Exception("Les instances de connexions dans GestionPersonne sont différentes");
        }
        this.personne = personne;
        this.film = film;
        this.roleFilm = roleFilm;
    }

    public void ajoutPersonne(String nom, Date dateNaissance, String lieuNaissance, int sexe) throws Exception {
        try {
            // Vérifie si la personne existe déjà 
            if (personne.existe(nom)) {
                throw new Tp3Exception("Impossible d'ajouter, la personne " + nom + " existe deja.");
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
                throw new Tp3Exception("Impossible de supprimer, la personne " + nom + " n'existe pas.");
            }
            // S'il est le realisateur d'au moins un film
            if(!film.filmDeRealisateur(nom).isEmpty()) {
                throw new Tp3Exception("Impossible de supprimer, la personne " + nom + " a realise au moins un film.");
            }
            // S'il n'a aucun role
            // XXX SHITZ
            
            personne.enlever(nom);
            cx.commit();
        } catch (Exception e) {
            cx.rollback();
            throw e;
        }
    }
    
    void listeSerieActeur(String nom) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    public void listeRealisateur() throws Exception {
        try{
            film.listerRealisateur();
            // XXX Affichage ici à faire
            cx.commit();
        }
        catch(Exception e)
        {
            cx.rollback();
            throw e;
        }
    }
    
    public void afficherFilmDActeur(String nom) throws Tp3Exception, SQLException{
        if(!personne.existe(nom)){
            throw new Tp3Exception("L'acteur " + nom + " n'existe pas.");
        }
        List<TupleRoleFilm> tuples = roleFilm.getFilmsDActeur(nom); 
        StringBuilder output = new StringBuilder();
        Iterator<TupleRoleFilm> it = tuples.iterator();
        while(it.hasNext()){
            output.append(it.next().getTitre()).append(it.hasNext() ?", ":".");
        }
        System.out.println(output.toString());
    }
}
