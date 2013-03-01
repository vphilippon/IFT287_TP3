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
    private PreparedStatement pstmtGetActeurDeSerie;
    private Connexion cx;

    public Personne(Connexion cx) throws SQLException {
        this.cx = cx;
        init();
    }

    private void init() throws SQLException {
        try{
            pstmGetPersonne = cx.getConnection().prepareStatement("SELECT * FROM Personne WHERE nom = ?");
            pstmInsertPersonne = cx.getConnection().prepareStatement("INSERT INTO Personne (nom, datenaissance, lieunaissance, sexe) VALUES(?, ?, ?, ?)");
            pstmDeletePersonne = cx.getConnection().prepareStatement("DELETE FROM Personne WHERE nom = ?");
            pstmtGetRealisateur = cx.getConnection().prepareStatement("SELECT * FROM personne WHERE nom IN (SELECT DISTINCT realisateur FROM Film)");
            pstmtGetActeurDeSerie = cx.getConnection().prepareStatement("SELECT * FROM personne WHERE nom IN (SELECT DISTINCT nomActeur FROM RoleEpisode WHERE titreSerie = ? AND anneeSortieSerie = ?)");
        }catch(SQLException e){
            throw e;
        }
    }

    public Connexion getConnexion() {
        return cx;
    }
    
    public boolean existe(String nom) throws SQLException {
        boolean personneExiste = false;
        try{
            pstmGetPersonne.setString(1, nom);
            ResultSet rs = pstmGetPersonne.executeQuery();
            personneExiste = rs.next();
            rs.close();
        }catch(SQLException e){
            throw e;
        }
        return personneExiste;
    }
    
    public TuplePersonne getPersonne(String nom) throws SQLException {
        TuplePersonne tuplePer;
        try{
            pstmGetPersonne.setString(1, nom);
            ResultSet rs = pstmGetPersonne.executeQuery();
            rs.first();
            tuplePer = new TuplePersonne(rs.getString("nom"), rs.getDate("dateNaissance"), 
                    rs.getString("lieuNaissance"), rs.getInt("sexe"));
        }catch(SQLException e){
            throw e;
        }
        return tuplePer;
    }
    
    public void ajouter(String nom, Date dateNaissance, String lieuNaissance, int sexe) throws SQLException {
        try{
            pstmInsertPersonne.setString(1, nom);
            pstmInsertPersonne.setDate(1, dateNaissance);
            pstmInsertPersonne.setString(3, lieuNaissance);
            pstmInsertPersonne.setInt(4, sexe);
            pstmGetPersonne.executeUpdate();
        }catch(SQLException e){
            throw e;
        }
    }
    
    public int enlever(String nom) throws SQLException {
        int retour = 0;
        try{
            pstmDeletePersonne.setString(1, nom);
            retour = pstmDeletePersonne.executeUpdate();
        }catch(SQLException e){
            throw e;
        }
        return retour;
    }
    
    public List<TuplePersonne> realisateurDeFilms() throws SQLException {
        List<TuplePersonne> listeRealisateur = new ArrayList<TuplePersonne>();
        try{
            ResultSet rs = pstmtGetRealisateur.executeQuery();
            while(rs.next()){
                listeRealisateur.add(new TuplePersonne(rs.getString(1),rs.getDate(2),rs.getString(3),rs.getInt(4)));
            }
        }catch(SQLException e){
            throw e;
        }
        return listeRealisateur;
    }

    public List<TuplePersonne> acteursDeSerie(String serieTitre, Date serieDate) throws SQLException {
        List<TuplePersonne> listeActeur = new ArrayList<TuplePersonne>();
        try{
            pstmtGetActeurDeSerie.setString(1, serieTitre);
            pstmtGetActeurDeSerie.setDate(1, serieDate);
            ResultSet rs = pstmtGetActeurDeSerie.executeQuery();
            while(rs.next()){
                listeActeur.add(new TuplePersonne(rs.getString(1),rs.getDate(2),rs.getString(3),rs.getInt(4)));
            }
        }catch(SQLException e){
            throw e;
        }
        return listeActeur;
    }

}
