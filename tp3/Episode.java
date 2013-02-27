package tp3;

import java.sql.Date;
import java.sql.SQLException;

class Episode {

    private Connexion cx;
    
    public Episode(Connexion cx) throws SQLException {
        this.cx = cx;
        init();
    }
    
    private void init() throws SQLException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Connexion getConnexion() {
        return cx;
    }

    boolean existe(String serieTitre, Date serieDate, int noSaison, int noEpisode) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
}
