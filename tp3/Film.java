package tp3;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

class Film {

    private Connexion cx;
    private PreparedStatement stmtFilmExiste;
    private PreparedStatement stmtAjouterFilm;
    private PreparedStatement stmtSuppFilm;
    private PreparedStatement stmtAjoutDescFilm;

    public Film(Connexion cx) throws SQLException {
        this.cx = cx;
        init();
    }

    private void init() throws SQLException {
        try{
            stmtFilmExiste = cx.getConnection().prepareStatement("SELECT * FROM Film WHERE titre = ? AND dateSortie = ?");
            stmtAjouterFilm = cx.getConnection().prepareStatement("INSERT INTO Film (titre, dateSortie, realisateur) VALUES (?, ?, ?)");
            stmtSuppFilm = cx.getConnection().prepareStatement("INSERT INTO Film (titre, dateSortie, realisateur) VALUES (?, ?, ?)");
            stmtAjoutDescFilm = cx.getConnection().prepareStatement("UPDATE Film SET description=?, duree=? WHERE titre = ? AND dateSortie = ?");
        }catch(SQLException e){
            throw e;
        }
    }

    public Connexion getConnexion() {
        return cx;
    }

    public boolean existe(String titre, Date dateSortie) throws SQLException {
        boolean retour = false;
        stmtFilmExiste.setString(1,titre);
        stmtFilmExiste.setDate(2,dateSortie);
        try{
            ResultSet rs = stmtFilmExiste.executeQuery();
            if(rs.next()) retour = true;
        }catch(SQLException e){
            throw e;
        }
        return retour;
    }

    public void ajouter(String titre, Date dateSortie, String realisateur) throws SQLException {
        stmtAjouterFilm.setString(1,titre);
        stmtAjouterFilm.setDate(2,dateSortie);
        stmtAjouterFilm.setString(3,realisateur);
        try{
            stmtAjouterFilm.executeUpdate();
        }catch(SQLException e){
            throw e;
        }
    }

    public int enlever(String titre, Date dateSortie) throws SQLException {
        int rs = 0;
        stmtSuppFilm.setString(1,titre);
        stmtSuppFilm.setDate(2,dateSortie);
        try{
            rs = stmtSuppFilm.executeUpdate();
        }catch(SQLException e){
            throw e;
        }
        return rs;
    }

    public TupleFilm getFilm(String titre, Date dateSortie) throws SQLException {
        TupleFilm t;
        stmtFilmExiste.setString(1,titre);
        stmtFilmExiste.setDate(2,dateSortie);
        try{
            ResultSet rs = stmtFilmExiste.executeQuery();
            t = new TupleFilm(rs.getString(0),rs.getDate(1),rs.getString(2),rs.getInt(3),rs.getString(4));
        }catch(SQLException e){
            throw e;
        }
        return t;
    }

    public void ajouterDescription(String titre, Date anneeSortie, String description, int duree) throws SQLException {
        int rs = 0;
        stmtAjoutDescFilm.setString(1,description);
        stmtAjoutDescFilm.setInt(2,duree);
        stmtAjoutDescFilm.setString(3,titre);
        stmtAjoutDescFilm.setDate(4,anneeSortie);


        try{
            rs = stmtAjoutDescFilm.executeUpdate();
        }catch(SQLException e){
            throw e;
        }

    }

    public void listerRealisateur() throws SQLException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<TupleFilm> filmDeRealisateur(String nom) throws SQLException {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
