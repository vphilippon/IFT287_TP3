package tp3;

import java.sql.Date;

class Film {

    Film(Connexion cx) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    Connexion getConnexion() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    boolean existe(String titre, Date dateSortie) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void ajouter(String titre, Date dateSortie, String realisateur) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void enlever(String titre, Date dateSortie) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    TupleFilm getFilm(String titre, Date dateSortie){
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void ajouterDescription(TupleFilm film, String description, int duree) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void listerRealisateur() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
