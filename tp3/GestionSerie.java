package tp3;

import java.sql.Date;

class GestionSerie {
    
    private Serie serie;
    private Episode episode;
    private Personne personne;
    private RoleEpisode roleEpisode;
    private Connexion cx;

    public GestionSerie(Serie serie, Personne personne) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void ajoutSerie(String readString, Date readDate, String readString0) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    public void ajoutEpisode(String titre, String titreSerie, Date anneeSortieSerie, int noSaison, int noEpisode, String description, Date dateDiffusion) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    public void ajoutRoleAEpisode(String serieTitre, Date serieDate, int noSaison, int noEpisode, 
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

    public void afficherActeursSerie(String readString, Date readDate) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
