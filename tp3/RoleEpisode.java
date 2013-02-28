package tp3;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

class RoleEpisode {

    private Connexion cx;
    
    public RoleEpisode(Connexion cx) throws SQLException {
        this.cx = cx;
        init();
    }
    
    private void init() throws SQLException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Connexion getConnexion() {
        return cx;
    }

    public boolean existe(String serieTitre, Date serieDate, int noSaison,
            int noEpisode, String acteur, String roleActeur) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void ajouter(String serieTitre, Date serieDate, int noSaison,
            int noEpisode, String acteur, String roleActeur) {
        throw new UnsupportedOperationException("Not yet implemented");
        
    }

    public List<TupleRoleEpisode> rolesDeActeur(String nom) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
}
