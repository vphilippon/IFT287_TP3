package tp3;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class Serie {

    private Connexion cx;
    private PreparedStatement stmtSerieExiste;
    private PreparedStatement stmtAjouterSerie;
    private PreparedStatement stmtSerieDeRealisateur;
    private PreparedStatement stmtSerieAvecActeur;
    
    public Serie(Connexion cx) throws SQLException {
        this.cx = cx;
        init();
    }

    private void init() throws SQLException {
        try{
            stmtSerieExiste = cx.getConnection().prepareStatement("SELECT * FROM Serie WHERE titre = ? AND anneeSortie = ?");
            stmtAjouterSerie = cx.getConnection().prepareStatement("INSERT INTO Serie (titre, anneeSortie, realisateur, description, nbSaison) VALUES (?, ?, ?, ?, ?)");
            stmtSerieDeRealisateur = cx.getConnection().prepareStatement("SELECT * FROM Serie WHERE realisateur = ?");
            stmtSerieAvecActeur = cx.getConnection().prepareStatement("SELECT * FROM Serie WHERE titre IN (SELECT titreSerie FROM RoleEpisode WHERE nomActeur = ?)");
        }catch(SQLException e){
            throw e;
        }
    }

    public Connexion getConnexion() {
        return cx;
    }

    public boolean existe(String serieTitre, Date serieDate) throws SQLException {
        boolean retour = false;
        try{
            stmtSerieExiste.setString(1,serieTitre);
            stmtSerieExiste.setDate(2,serieDate);
            ResultSet rs = stmtSerieExiste.executeQuery();
            if(rs.next()) { 
                retour = true; 
            }
        }catch(SQLException e){
            throw e;
        }
        return retour;        
    }
    //method overloading to let the possibility of adding a description for a serie and the nbSaison
    public void ajouter(String titre, Date dateSortie, String realisateur, String description, int nbSaison) throws SQLException {
        try{
            stmtAjouterSerie.setString(1, titre);
            stmtAjouterSerie.setDate(2, dateSortie);
            stmtAjouterSerie.setString(3, realisateur);
            stmtAjouterSerie.setString(4, description);
            stmtAjouterSerie.setInt(5, nbSaison);
            stmtAjouterSerie.executeUpdate();            
        }catch(SQLException e){
            throw e;
        }        
    }
    
    //method overloading to fit prof data entry
    public void ajouter(String titre, Date dateSortie, String realisateur) throws SQLException {
        try{
            stmtAjouterSerie.setString(1, titre);
            stmtAjouterSerie.setDate(2, dateSortie);
            stmtAjouterSerie.setString(3, realisateur);
            stmtAjouterSerie.setString(4, ""); //to give default value
            stmtAjouterSerie.setInt(5, 1); //to give default value
            stmtAjouterSerie.executeUpdate();
        }catch(SQLException e){
            throw e;
        }
    }

    public List<TupleSerie> serieDeRealisateur(String nom) throws SQLException {
        List<TupleSerie> listeSerie = new ArrayList<TupleSerie>();
        try{
            stmtSerieDeRealisateur.setString(1,nom);
            ResultSet rs = stmtSerieDeRealisateur.executeQuery();
            while(rs.next()){
                listeSerie.add(new TupleSerie(rs.getString(1),rs.getDate(2),rs.getString(3),rs.getString(4), rs.getInt(5)));
            }
        }catch(SQLException e){
            throw e;
        }
        return listeSerie;
    }
    
    public List<TupleSerie> serieAvecActeur(String nom) throws SQLException {
        List<TupleSerie> listeSerie = new ArrayList<TupleSerie>();
        try{
            stmtSerieAvecActeur.setString(1,nom);
            ResultSet rs = stmtSerieAvecActeur.executeQuery();
            while(rs.next()){
                listeSerie.add(new TupleSerie(rs.getString(1),rs.getDate(2),rs.getString(3),rs.getString(4), rs.getInt(5)));
            }
        }catch(SQLException e){
            throw e;
        }
        return listeSerie;
    }


}
