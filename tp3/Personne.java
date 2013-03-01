package tp3;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Personne {

    private PreparedStatement pstmGetPersonne;
    private PreparedStatement pstmInsertPersonne;
    private PreparedStatement pstmDeletePersonne;
    private PreparedStatement pstmtGetRealisateur;
    private Connexion cx;

    public Personne(Connexion cx) throws SQLException {
        this.cx = cx;
        init();
    }

    private void init() throws SQLException {
        pstmGetPersonne = cx.getConnection().prepareStatement("SELECT * FROM Personne WHERE nom = ?");
        pstmInsertPersonne = cx.getConnection().prepareStatement("INSERT INTO Personne (nom, datenaissance, lieunaissance, sexe) VALUES(?, ?, ?, ?)");
        pstmDeletePersonne = cx.getConnection().prepareStatement("DELETE FROM Personne WHERE nom = ?");
        pstmtGetRealisateur = cx.getConnection().prepareStatement("SELECT * FROM personne WHERE nom IN (SELECT DISTINCT realisateur FROM Film)");
    }

    public Connexion getConnexion() {
        return cx;
    }
    
    public boolean existe(String nom) throws SQLException {
        pstmGetPersonne.setString(1, nom);
        ResultSet rs = pstmGetPersonne.executeQuery();
        boolean personneExiste = rs.next();
        rs.close();
        return personneExiste;
    }
    
    public TuplePersonne getPersonne(String nom) throws SQLException {
        pstmGetPersonne.setString(1, nom);
        ResultSet rs = pstmGetPersonne.executeQuery();
        rs.first();
        return new TuplePersonne(rs.getString("nom"), rs.getDate("dateNaissance"), 
                rs.getString("lieuNaissance"), rs.getInt("sexe"));
    }
    
    public void ajouter(String nom, Date dateNaissance, String lieuNaissance, int sexe) throws SQLException {
        pstmInsertPersonne.setString(1, nom);
        pstmInsertPersonne.setDate(1, dateNaissance);
        pstmInsertPersonne.setString(3, lieuNaissance);
        pstmInsertPersonne.setInt(4, sexe);
        pstmGetPersonne.executeUpdate();
    }
    
    public int enlever(String nom) throws SQLException {
        pstmDeletePersonne.setString(1, nom);
        return pstmDeletePersonne.executeUpdate();
    }
    
    public List<TuplePersonne> realisateurDeFilms() throws SQLException {
        List<TuplePersonne> listeRealisateur = new ArrayList<TuplePersonne>();
        ResultSet rs = pstmtGetRealisateur.executeQuery();
        while(rs.next()){
            listeRealisateur.add(new TuplePersonne(rs.getString(1),rs.getDate(2),rs.getString(3),rs.getInt(4)));
        }
        return listeRealisateur;
    }

    public List<TuplePersonne> acteursDeSerie(String serieTitre, Date serieDate) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
