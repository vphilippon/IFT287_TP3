/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tp3;

import java.sql.Date;
import java.sql.SQLException;
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
    
    GestionRoleFilm(RoleFilm rf, Film f, Personne p) throws Tp3Exception, SQLException, Exception {
        
        this.film = f;
        this.personne = p;
        this.roleFilm = rf;
        if(film.getConnexion() != personne.getConnexion() && 
           film.getConnexion() != roleFilm.getConnexion()){
            throw new Tp3Exception("Connection non valide");
        }
        this.cx = f.getConnexion();
    } 

    void ajoutActeurFilm(String titre, Date anneeSortie, String acteur, String role) 
            throws Tp3Exception, SQLException{
        
        if (!Main.isStringNotEmpty(titre) || !Main.isStringNotEmpty(acteur)
                || !Main.isStringNotEmpty(role)) {
            //une donner est invalide
            throw new Tp3Exception("Un parametre est invalide (titre = '"
                    + titre + "', date de sortie = '" + anneeSortie
                    + "', acteur = '" + acteur + "', role = '" + role + "').\n");
        } else {
            if (personne.existe(acteur)) {
                if (film.existe(titre, anneeSortie)) {
                    TuplePersonne tupleActeur = personne.getPersonne(acteur);
                    if(tupleActeur.getDateNaissance().before(anneeSortie)){
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
            int nb = 0;
            while(nb < tuples.size()){
                String tmp = (tuples.size() > 1)?", ":"";
                output += tuples.get(nb).getNom() + tmp;
                nb++;
            }
            System.out.println(output);
        }else{
            throw new Tp3Exception("Le film : " + titre + ", " + anneeSortie + " n'existe pas.");
        }
    }

    void getFilmFromActeur(String nom) throws Tp3Exception, SQLException{
        if(personne.existe(nom)){
            List<TupleRoleFilm> tuples = roleFilm.getFilmWhereActeur(nom); 
            String output = "";
            int nb = 0;
            while(nb < tuples.size()){
                String tmp = (tuples.size() > 1)?", ":"";
                output += tuples.get(nb).getTitre() + tmp;
            }
            System.out.println(output);
        }else{
            throw new Tp3Exception("L'acteur " + nom + " n'existe pas.");
        }
    }
}
