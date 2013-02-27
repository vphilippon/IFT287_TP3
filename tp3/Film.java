package tp3;

import java.sql.Date;
import java.sql.SQLException;

class Film {

    private Connexion cx;

    public Film(Connexion cx) throws SQLException {
        this.cx = cx;
        init();
    }

    private void init() throws SQLException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Connexion getConnexion() {
        return cx;
    }

    public boolean existe(String titre, Date dateSortie) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void ajouter(String titre, Date dateSortie, String realisateur) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int enlever(String titre, Date dateSortie) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public TupleFilm getFilm(String titre, Date dateSortie) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void ajouterDescription(TupleFilm film, String description, int duree) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void listerRealisateur() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
