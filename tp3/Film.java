package tp3;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class Film {

    private Connexion cx;
    private PreparedStatement stmtFilmExiste;
    private PreparedStatement stmtAjouterFilm;
    private PreparedStatement stmtSuppFilm;
    private PreparedStatement stmtAjoutDescFilm;
    private PreparedStatement stmtGetFilmFrom;

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
            stmtGetFilmFrom = cx.getConnection().prepareStatement("SELECT * FROM Film WHERE realisateur = ?");
        }catch(SQLException e){
            throw e;
        }
    }

    public Connexion getConnexion() {
        return cx;
    }

    public boolean existe(String titre, Date dateSortie) throws SQLException {
        boolean retour = false;
        try{
            stmtFilmExiste.setString(1,titre);
            stmtFilmExiste.setDate(2,dateSortie);
            ResultSet rs = stmtFilmExiste.executeQuery();
            if(rs.next()){
                retour = true;
            }
        }catch(SQLException e){
            throw e;
        }
        return retour;
    }

    public void ajouter(String titre, Date dateSortie, String realisateur) throws SQLException {
        try{
            stmtAjouterFilm.setString(1,titre);
            stmtAjouterFilm.setDate(2,dateSortie);
            stmtAjouterFilm.setString(3,realisateur);
            stmtAjouterFilm.executeUpdate();
        }catch(SQLException e){
            throw e;
        }
    }

    public int enlever(String titre, Date dateSortie) throws SQLException {
        int nb = 0;
        try{
            stmtSuppFilm.setString(1,titre);
            stmtSuppFilm.setDate(2,dateSortie);
            nb = stmtSuppFilm.executeUpdate();
        }catch(SQLException e){
            throw e;
        }
        return nb;
    }

    public TupleFilm getFilm(String titre, Date dateSortie) throws SQLException {
        TupleFilm t;
        try{
            stmtFilmExiste.setString(1,titre);
            stmtFilmExiste.setDate(2,dateSortie);
            ResultSet rs = stmtFilmExiste.executeQuery();
            t = new TupleFilm(rs.getString(1),rs.getDate(2),rs.getString(3),rs.getInt(4),rs.getString(5));
        }catch(SQLException e){
            throw e;
        }
        return t;
    }

    public void ajouterDescription(String titre, Date anneeSortie, String description, int duree) throws SQLException {
        try{
            stmtAjoutDescFilm.setString(1,description);
            stmtAjoutDescFilm.setInt(2,duree);
            stmtAjoutDescFilm.setString(3,titre);
            stmtAjoutDescFilm.setDate(4,anneeSortie);
            stmtAjoutDescFilm.executeUpdate();
        }catch(SQLException e){
            throw e;
        }
    }

    public List<TupleFilm> filmDeRealisateur(String nom) throws SQLException {
        List<TupleFilm> listeFilms = new ArrayList<TupleFilm>();
        try{
            stmtGetFilmFrom.setString(1,nom);
            ResultSet rs = stmtGetFilmFrom.executeQuery();
            while(rs.next()){
                listeFilms.add(new TupleFilm(rs.getString(1),rs.getDate(2),rs.getString(3),rs.getInt(4), rs.getString(5)));
            }
        }catch(SQLException e){
            throw e;
        }
        return listeFilms;
    }
}
