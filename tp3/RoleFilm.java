package tp3;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

class RoleFilm {
    
    private Connexion cx;

    public RoleFilm(Connexion cx) throws SQLException {
        this.cx = cx;
        init();
    }
    
    private void init() throws SQLException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Connexion getConnexion() {
        return cx;
    }

    public boolean existe(String nomActeur, String titre, Date anneeSortie) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void ajouter(String nomActeur, String titre, Date anneeSortie) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<TupleRoleFilm> getActeurs(String titre, Date anneeSortie) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<TupleRoleFilm> rolesDeActeur(String nom) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
