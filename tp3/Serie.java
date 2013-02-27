package tp3;

import java.sql.SQLException;

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

}
