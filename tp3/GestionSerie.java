package tp3;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

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
                throw new Tp3Exception("Impossible d'ajouter, la serie " + titre + " paru le " + dateSortie + " existe deja.");
            }
            // S'assure que le réalisateur existe
            if (!personne.existe(realisateur)){
                throw new Tp3Exception("Impossible d'ajouter, le réalisateur " + realisateur + " n'existe pas.");
            }
            Date realisateurNaissance = personne.getPersonne(realisateur).getDateNaissance();
            // S'assure que le réalisateur est né avant la sortie du film
            if (realisateurNaissance.after(dateSortie)){
                throw new Tp3Exception("Impossible d'ajouter, le réalisateur est né le " + realisateurNaissance + 
                        " et ne peut pas participer à un film créé le: " + dateSortie);
            }  
            // Ajout de la serie la table des series
            serie.ajouter(titre, dateSortie, realisateur);
            cx.commit();
        } catch (Exception e) {
            cx.rollback();
            throw e;
        }
    }
    
    public void ajoutEpisode(String titre, String titreSerie, Date anneeSortieSerie, int noSaison, int noEpisode, String description, Date dateDiffusion) throws Exception {
        try {
            // Vérifie si la serie existe
            if (!serie.existe(titreSerie, anneeSortieSerie)) {
                throw new Tp3Exception("Impossible d'ajouter, la serie " + titreSerie + " paru le " + anneeSortieSerie + " n'existe pas.");
            }
            if (episode.existe(titre, dateDiffusion, noSaison, noEpisode)) {
                throw new Tp3Exception("Impossible d'ajouter, l'episode existe deja: " + titre + " " + dateDiffusion + " saison: " + noSaison + " episode: " + noEpisode);
            } 
            // S'assure que l'épisode d'avant existe
            if (noEpisode > 1) {
                if (!episode.existe(titre, dateDiffusion, noSaison, noEpisode-1)){
                    throw new Tp3Exception("Impossible d'ajouter l'episode, l'episode n'a pas de prédécesseur: " + (noEpisode-1) + " est manquant");
                }
            } 
            // Ajout de l'épisode dans la table des épisodes
            episode.ajouter(titre, titreSerie, anneeSortieSerie, noSaison, noEpisode, description, dateDiffusion);
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
                throw new Tp3Exception("Impossible d'ajouter, la serie : " + serieTitre + " n'existe pas.");
            }
            if(!episode.existe(serieTitre, serieDate, noSaison, noEpisode)){
                throw new Tp3Exception("Impossible d'ajouter, l'episode no : " + noEpisode + " de la saison : " + noSaison + " n'existe pas.");
            }
            if(!personne.existe(acteur)){
                throw new Tp3Exception("Impossible d'ajouter, l'acteur : " + acteur + " n'existe pas.");
            }
            TuplePersonne tupleActeur = personne.getPersonne(acteur);
            if (tupleActeur.getDateNaissance().after(serieDate)){
                throw new Tp3Exception("Impossible d'ajouter, l'acteur " + acteur + " est né le: " + tupleActeur.getDateNaissance() + 
                        " et ne peut pas participer à une serie créé le: " + serieDate);
            } 
            if(roleEpisode.existe(serieTitre, serieDate, noSaison, noEpisode, acteur, roleActeur)){
                throw new Tp3Exception("Impossible d'ajouter, l'acteur : " + acteur + " joue deja dans l'episode " + 
                        noEpisode + " de la saison " + noSaison + " dans le role de : " + roleActeur);
            }
            
            roleEpisode.ajouter(serieTitre, serieDate, noSaison, noEpisode, acteur, roleActeur);
            cx.commit();
        } catch (Exception e) {
            cx.rollback();
            throw e;
        }
    }

    public void afficherActeursSerie(String serieTitre, Date serieDate) throws SQLException, Tp3Exception {
        if(!serie.existe(serieTitre, serieDate)){
            throw new Tp3Exception("Impossible d'afficher, la serie " + serieTitre + " paru le " + serieDate + " n'existe pas.");
        }
        List<TuplePersonne> listeActeurs = personne.acteursDeSerie(serieTitre, serieDate);

        StringBuffer output = new StringBuffer();
        Iterator<TuplePersonne> it = listeActeurs.iterator();
        while(it.hasNext()){
            output.append(it.next().getNom() + (it.hasNext() ?", ":"."));
        }
        System.out.println("Voici les acteurs de la serie : ");
        System.out.println(output.toString());
    }
}
