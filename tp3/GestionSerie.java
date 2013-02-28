package tp3;

import java.sql.Date;
import java.sql.SQLException;

class GestionSerie {
    
    private Serie serie;
    private Episode episode;
    private Personne personne;
    private RoleEpisode roleEpisode;
    private Connexion cx;

    public GestionSerie(Serie serie, Episode episode, Personne personne, RoleEpisode roleEpisode) throws Tp3Exception{
        this.cx = personne.getConnexion();
        if (cx != serie.getConnexion() || cx != episode.getConnexion() ||
            cx != personne.getConnexion() || cx != roleEpisode.getConnexion()){
            throw new Tp3Exception("Les instances de connexions dans GestionPersonne sont différentes");
        }
        this.episode = episode;
        this.personne = personne;
        this.serie = serie;
        this.roleEpisode = roleEpisode;
    }

    public void ajoutSerie(String titre, Date dateSortie, String realisateur) throws Exception {
        try {
            // Vérifie si le film existe déja 
            if (serie.existe(titre, dateSortie)) {
                throw new Tp3Exception("Serie existe deja: " + titre + " " + dateSortie);
            }
            // S'assure que le réalisateur existe
            if (!personne.existe(realisateur)){
                throw new Tp3Exception("Le réalisateur n'existe pas: " + realisateur);
            }
            Date realisateurNaissance = personne.getPersonne(realisateur).getDateNaissance();
            // S'assure que le réalisateur est né avant la sortie du film
            if (realisateurNaissance.after(dateSortie)){
                throw new Tp3Exception("Le réalisateur est né le: " + realisateurNaissance + 
                        " et ne peut pas participer à un film créé le: " + dateSortie);
            }  
            // Ajout du livre dans la table des livres
            serie.ajouter(titre, dateSortie, realisateur);
            cx.commit();
        } catch (Exception e) {
            cx.rollback();
            throw e;
        }
    }
    
    public void ajoutEpisode(String titre, String titreSerie, Date anneeSortieSerie, int noSaison, int noEpisode, String description, Date dateDiffusion) {
        try {
            // Vérifie si le film existe déja 
            if (serie.existe(titreSerie, anneeSortieSerie)) {
                throw new Tp3Exception("Serie existe deja: " + titreSerie + " " + anneeSortieSerie);
            }
            if (episode.existe(titre, dateDiffusion, noSaison, noEpisode)) {
                throw new Tp3Exception("L'episode existe deja: " + titre + " " + dateDiffusion + " saison: " + noSaison + " episode: " + noEpisode);
            } 
            // S'assure que l'épisode d'avant existe
            if (noEpisode > 1) {
                if (!episode.existe(titre, dateDiffusion, noSaison, noEpisode-1)){
                    throw new Tp3Exception("Impossible d'ajouter l'épisode car elle na pas de prédécesseur: " + (noEpisode-1) + " est manquante");
                }
            } 
            // Ajout du livre dans la table des livres
            episode.ajouter(titre, dateDiffusion, noSaison, noEpisode);
            cx.commit();
        } catch (Exception e) {
            cx.rollback();
            throw e;
        }
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

    public void listeActeursSerie(String readString, Date readDate) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
