package tp3;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

class Serie {

    private Connexion cx;
    
    public Serie(Connexion cx) throws SQLException {
        this.cx = cx;
        init();
    }

    private void init() throws SQLException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Connexion getConnexion() {
        return cx;
    }

    public boolean existe(String serieTitre, Date serieDate) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    public void ajouter(String titre, Date dateSortie, String realisateur) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<TupleSerie> serieDeRealisateur(String nom) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
