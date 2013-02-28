package tp3;

import java.sql.Date;

class GestionRoleEpisode  {
    
    private Serie serie;
    private Episode episode;
    private Personne personne;
    private RoleEpisode roleEpisode;
    private Connexion cx;

    public GestionRoleEpisode(RoleEpisode re, Serie s, Episode e, Personne p) throws Tp3Exception{
        this.serie = s;
        this.episode = e; /// XXX VP : Empechait de compiler, j'ai ajouter param aux constr. checker si ok.
        this.personne = p;
        this.roleEpisode = re;
        if(serie.getConnexion() != episode.getConnexion() ||  // XXX VP : || au lieu de &&
           serie.getConnexion() != personne.getConnexion() || 
           serie.getConnexion() != roleEpisode.getConnexion()){
            throw new Tp3Exception("Connexion différente dans GestionRoleEpisode");
        }
        this.cx = s.getConnexion();
    }    

    public void ajoutRole(String serieTitre, Date serieDate, int noSaison, int noEpisode, 
            String acteur, String roleActeur) throws Exception {
        try {
            if(!serie.existe(serieTitre, serieDate)){
                throw new Tp3Exception("La serie : " + serieTitre + " n'existe pas.");
            }
            if(!episode.existe(serieTitre, serieDate, noSaison, noEpisode)){
                // XXX Message a checker
                throw new Tp3Exception("L'episode no : " + noEpisode + " de la saison : " + noSaison + " n'existe pas.");
            }
            if(!personne.existe(acteur)){
                throw new Tp3Exception("L'acteur : " + acteur + " n'existe pas.");
            }
            TuplePersonne tupleActeur = personne.getPersonne(acteur);
            if (tupleActeur.getDateNaissance().after(serieDate)){
                throw new Tp3Exception("L'acteur " + acteur + " est né le: " + tupleActeur.getDateNaissance() + 
                        " et ne peut pas participer à une serie créé le: " + serieDate);
            } 
            if(roleEpisode.existe(serieTitre, serieDate, noSaison, noEpisode, acteur, roleActeur)){
                throw new Tp3Exception("L'acteur : " + acteur + " joue deja dans l'episode " + 
                        noEpisode + " de la saison " + noSaison + " dans le role de : " + roleActeur);
            }
            roleEpisode.ajouter(serieTitre, serieDate, noSaison, noEpisode, acteur, roleActeur);
            cx.commit(); // XXX VP : Ajouter ici, vérifier si ok
        } catch (Exception e) {
            cx.rollback();
            throw e;
        }
    }
}
