package tp3;

import java.sql.Date;

class GestionFilm {
    
    private Film film;
    private Personne personne;
    private Connexion cx;

    /**
     * Création d'une instance
     * @param film
     * @param personne
     * @throws Tp3Exception 
     */
    public GestionFilm(Film film, Personne personne) throws Tp3Exception{
        if (film.getConnexion() != personne.getConnexion()){
            throw new Tp3Exception("Connexions différentes dans GestionFilm");
        }
        this.cx = film.getConnexion();
        this.film = film;
        this.personne = personne;
    }

    public void ajoutFilm(String titre, Date dateSortie, String realisateur) throws Exception {
        try {
            // Vérifie si le film existe déja 
            if (film.existe(titre, dateSortie)) {
                throw new Tp3Exception("Film existe deja: " + titre + " " + dateSortie);
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
            film.ajouter(titre, dateSortie, realisateur);
            cx.commit();
        } catch (Exception e) {
            cx.rollback();
            throw e;
        }
    }
    
    /**
     * 
     * @param titre
     * @param dateSortie 
     */
    public void supprimerFilm(String titre, Date dateSortie) throws Exception {
        try {
            // Vérifie si le film existe déja
            if (!film.existe(titre, dateSortie)) {
                throw new Tp3Exception("Film n'existe pas: " + titre + " " + dateSortie);
            }
            // Supression du livre dans la table des livres
            System.out.println(film.enlever(titre, dateSortie) + " film supprimé");
            cx.commit();
        }
        catch (Exception e)
        {
            cx.rollback();
            throw e;
        }
    }
    
    /**
     * 
     * @param titre
     * @param anneeSortie
     * @param description
     * @param duree 
     */
    public void ajoutDescFilm(String titre, Date anneeSortie, String description, int duree) throws Exception {
        try{
            if(!film.existe(titre, anneeSortie)){
                throw new Tp3Exception("Film n'existe pas: " + titre + " " + anneeSortie);
            }
            film.ajouterDescription(titre, anneeSortie, description, duree);
            cx.commit();
        }
        catch(Exception e){
            cx.rollback();
            throw e;
        }
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
}
