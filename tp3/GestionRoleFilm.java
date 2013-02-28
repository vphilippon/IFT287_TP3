/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tp3;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

class GestionRoleFilm {
    
    private Film film;
    private Personne personne;
    private RoleFilm roleFilm;
    private Connexion cx;
    
    public GestionRoleFilm(RoleFilm rf, Film f, Personne p) throws Tp3Exception {
        
        this.film = f;
        this.personne = p;
        this.roleFilm = rf;
        if(film.getConnexion() != personne.getConnexion() ||  // XXX VP : || au lieu de && 
           film.getConnexion() != roleFilm.getConnexion()){
            throw new Tp3Exception("Connexion différente dans GestionRoleFilm");
        }
        this.cx = f.getConnexion();
    } 

    public void ajoutActeurFilm(String titre, Date anneeSortie, String nomActeur, String role) throws Exception {
        try {
        // XXX Partout ou nulle part, à décider
//            if (!Main.isStringNotEmpty(titre) || !Main.isStringNotEmpty(nomActeur)
//                    || !Main.isStringNotEmpty(role)) {
//                //une donner est invalide
//                throw new Tp3Exception("Un parametre est invalide (titre = '"
//                        + titre + "', date de sortie = '" + anneeSortie
//                        + "', acteur = '" + nomActeur + "', role = '" + role + "').\n");
//            } 
            if (!personne.existe(nomActeur)) {
                throw new Tp3Exception("Impossible d'ajouter l'acteur au film puisque que l'acteur n'existe pas.");
            }
                
            if (!film.existe(titre, anneeSortie)) {
                throw new Tp3Exception("Impossible d'ajouter l'acteur au film puisque que le film n'existe pas.");
            }
            
            TuplePersonne acteur = personne.getPersonne(nomActeur);
            if(acteur.getDateNaissance().after(anneeSortie)){
                throw new Tp3Exception("L'acteur semble etre nee avant la date de sortie du film.");
            }
            
            if (roleFilm.existe(nomActeur, titre, anneeSortie)) {
                throw new Tp3Exception("Impossible d'ajouter l'acteur au film puisque que celui-ci y joue deja.");
            } 

            roleFilm.ajouter(nomActeur, titre, anneeSortie);
            cx.commit();
        } catch (Exception e) {
            cx.rollback();
            throw e;
        }
    }

    public void afficherActeurDeFilm(String titre, Date anneeSortie) throws Tp3Exception {
        if(!film.existe(titre, anneeSortie)){
            throw new Tp3Exception("Le film " + titre + " paru en " + anneeSortie + " n'existe pas.");
        }
        
        List <TupleRoleFilm> tuples = roleFilm.getActeurs(titre, anneeSortie);
        StringBuffer output = new StringBuffer(); // XXX VP : Jamais += avec String, c'est mortel (démo si vous voulez)
        Iterator<TupleRoleFilm> it = tuples.iterator();
        while(it.hasNext()){
            output.append(it.next().getNom() + (it.hasNext() ?", ":"."));
        }
        System.out.println(output.toString());
    }

    public void afficherFilmDActeur(String nom) throws Tp3Exception, SQLException{
        if(!personne.existe(nom)){
            throw new Tp3Exception("L'acteur " + nom + " n'existe pas.");
        }
        List<TupleRoleFilm> tuples = roleFilm.getFilmsDActeur(nom); 
        StringBuffer output = new StringBuffer(); // XXX VP : Jamais += avec String, c'est mortel (démo si vous voulez)
        Iterator<TupleRoleFilm> it = tuples.iterator();
        while(it.hasNext()){
            output.append(it.next().getTitre() + (it.hasNext() ?", ":"."));
        }
        System.out.println(output.toString());
    }
}
