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
 * @author mathieu
 */
class GestionRoleFilm {
    
    private Film film;
    private Personne personne;
    private RoleFilm roleFilm;
    private Connexion cx;
    
    GestionRoleFilm(RoleFilm rf, Film f, Personne p) {
        
        this.film = f;
        this.personne = p;
        this.roleFilm = rf;
        if(film.getConnection() != personne.getConnection() && 
           film.getConnection() != roleFilm.getConnection()){
            throw new Tp3Exception("Connection non valide");
        }
        this.cx = f.getConnection();
    } 

    void ajoutActeurFilm(String titre, Date anneeSortie, String acteur, String role) 
            throws Tp3Exception{
        
        if (!Main.isStringNotEmpty(titre) || !Main.isStringNotEmpty(acteur)
                || !Main.isStringNotEmpty(role)) {
            //une donner est invalide
            throw new Tp3Exception("Un parametre est invalide (titre = '"
                    + titre + "', date de sortie = '" + anneeSortie
                    + "', acteur = '" + acteur + "', role = '" + role + "').\n");
        } else {
            if (personne.existe(acteur)) {
                if (film.existe(titre, anneeSortie)) {
                    TuplePersonne acteur = personne.getPersonne(acteur);
                    if(acteur.getDateNaissance().before(anneeSortie)){
                        if (!roleFilm.existe(acteur, titre, anneeSortie)) {
                            roleFilm.ajouter(acteur, titre, anneeSortie);
                            cx.commit();
                        } else {
                            throw new Tp3Exception(
                                    "Impossible d'ajouter l'acteur au film puisque que celui-ci y joue deja.\n");
                        }
                    }else{
                        throw new Tp3Exception(
                                    "L'acteur semble etre nee avant la date de sortie du film.\n");
                    }
                } else {
                    throw new Tp3Exception(
                            "Impossible d'ajouter l'acteur au film puisque que le film n'existe pas.\n");
                }
            } else {
                throw new Tp3Exception(
                        "Impossible d'ajouter l'acteur au film puisque que l'acteur n'existe pas.\n");
            }
        }
    }

    void getActeurFromFilm(String titre, Date anneeSortie) throws Tp3Exception{
        if(film.existe(titre, anneeSortie)){
            List <TupleRoleFilm> tuples = roleFilm.getActeurs(titre, anneeSortie);
            String output = "";
            while(!tuples.empty()){
                output += tuples.front().getNom() + (tuples.size() > 1)?", ":"";
                tuples.pop_front();
            }
            System.out.println(output);
        }else{
            throw new Tp3Exception("Le film : " + titre + ", " + anneeSortie + " n'existe pas.");
        }
    }

    void getFilmFromActeur(String nom) throws Tp3Exception{
        if(personne.existe(nom)){
            list<TupleRoleFilm> tuples = roleFilm.getFilmWhereActeur(nom); 
            string output = "";
            while(!tuples.empty()){
                output += tuples.front().getTitre() + (tuples.size() > 1)?", ":"";
                tuples.pop_front();
            }
            System.out.println(output);
        }else{
            throw new Tp3Exception("L'acteur " + nom + " n'existe pas.");
        }
    }
}
