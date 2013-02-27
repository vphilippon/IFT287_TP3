package tp3;

import java.sql.Date;
import java.sql.SQLException;

class GestionRoleEpisode  {
    
    private Serie serie;
    private Episode episode;
    private Personne personne;
    private RoleEpisode roleEpisode;
    private Connexion cx;

    GestionRoleEpisode(RoleEpisode re, Serie s, Episode e, Personne p) 
        throws Tp3Exception{
        this.serie = s;
        this.episode = e;
        this.personne = p;
        this.roleEpisode = re;
        if(serie.getConnexion() != episode.getConnexion() && 
           serie.getConnexion() != personne.getConnexion() && 
           serie.getConnexion() != roleEpisode.getConnexion()){
            throw new Tp3Exception("Connection non valide");
        }
        this.cx = s.getConnexion();
    }    

    void ajoutRole(String serieTitre, Date serieDate, int noSaison, int noEpisode, String acteur, String roleActeur) 
        throws Tp3Exception, SQLException{
        if(serie.existe(serieTitre, serieDate )){
            if(episode.existe(serieTitre, serieDate, noSaison, noEpisode)){
                if(personne.existe(acteur)){
                    TuplePersonne tupleActeur = personne.getPersonne(acteur);
                    if(tupleActeur.getDateNaissance().before(serieDate)){
                        if(!roleEpisode.existe(serieTitre, serieDate, noSaison, noEpisode, acteur, roleActeur)){
                            roleEpisode.ajouter(serieTitre, serieDate, noSaison, noEpisode, acteur, roleActeur);
                        }
                    }else{
                        throw new Tp3Exception("L'acteur : " + acteur + " joue deja dans l'episode " +
                                noEpisode + " de la saison " + noSaison + " dans le role de : " + roleActeur);
                    }
                }else{
                    throw new Tp3Exception("L'acteur : " + acteur + " n'existe pas.");
                }
            }else{
                throw new Tp3Exception("L'episode no : " + noEpisode + " de la saison : " + noSaison + " n'existe pas.");
            }
        }else{
            throw new Tp3Exception("La serie : " + serieTitre + " n'existe pas.");
        }
    }
}
