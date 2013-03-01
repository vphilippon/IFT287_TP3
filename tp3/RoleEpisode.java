package tp3;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

class RoleEpisode {

    private Connexion cx;
    private PreparedStatement stmtRoleEpisodeExiste;
    private PreparedStatement stmtAjouterRoleEpisode;
    private PreparedStatement stmtGetRoleEpisodeWithActeur;
    
    public RoleEpisode(Connexion cx) throws SQLException {
        this.cx = cx;
        init();
    }
    
    private void init() throws SQLException {
        try{
            stmtRoleEpisodeExiste = cx.getConnection().prepareStatement(
                    "SELECT * FROM RoleEpisode WHERE nomActeur = ? AND roleActeur = ? "
                    + "AND titreSerie = ? AND noSaison = ? "
                    + "AND noEpisode = ? AND anneeSortieSerie = ?");
            stmtAjouterRoleEpisode = cx.getConnection().prepareStatement("INSERT INTO RoleEpisode (titreEpisode, nomActeur, roleActeur, titreSerie, noSaison, noEpisode, anneeSortieSerie) VALUES (?, ?, ?, ?, ?, ?, ?)");
            stmtGetRoleEpisodeWithActeur = cx.getConnection().prepareStatement("SELECT * FROM RoleEpisode WHERE nomActeur = ?");
        }catch(SQLException e){
            throw e;
        }
    }

    public Connexion getConnexion() {
        return cx;
    }

    public boolean existe(String serieTitre, Date serieDate, int noSaison,
            int noEpisode, String acteur, String roleActeur) throws SQLException {
        boolean retour = false;
        try{
            stmtRoleEpisodeExiste.setString(1,acteur);
            stmtRoleEpisodeExiste.setString(2,roleActeur);
            stmtRoleEpisodeExiste.setString(3,serieTitre);
            stmtRoleEpisodeExiste.setInt(4,noSaison);
            stmtRoleEpisodeExiste.setInt(5,noEpisode);
            stmtRoleEpisodeExiste.setDate(6,serieDate);
            ResultSet rs = stmtRoleEpisodeExiste.executeQuery();
            if(rs.next()){
                retour = true;
            }
        }catch(SQLException e){
            throw e;
        }
        return retour;
    }

    public void ajouter(String titre, String serieTitre, Date serieDate, int noSaison,
            int noEpisode, String acteur, String roleActeur) throws SQLException {
        try{
            stmtAjouterRoleEpisode.setString(1,titre);
            stmtAjouterRoleEpisode.setString(2,acteur);
            stmtAjouterRoleEpisode.setString(3,roleActeur);
            stmtAjouterRoleEpisode.setString(4,serieTitre);
            stmtAjouterRoleEpisode.setInt(5,noSaison);
            stmtAjouterRoleEpisode.setInt(6,noEpisode);
            stmtAjouterRoleEpisode.setDate(7,serieDate);
            stmtAjouterRoleEpisode.executeUpdate();
        }catch(SQLException e){
            throw e;
        }
    }

    public List<TupleRoleEpisode> rolesDeActeur(String nom) throws SQLException {
        List<TupleRoleEpisode> listeRoleEpisode = new ArrayList<TupleRoleEpisode>();
        try{
            stmtGetRoleEpisodeWithActeur.setString(1, nom);
            ResultSet rs = stmtGetRoleEpisodeWithActeur.executeQuery();
            while(rs.next()){
                listeRoleEpisode.add(new TupleRoleEpisode(rs.getString(1),rs.getString(2),rs.getString(3),
                        rs.getString(4),rs.getInt(5),rs.getInt(6),rs.getDate(7)));
            }
        }catch(SQLException e){
            throw e;
        }
        return listeRoleEpisode;
    }
    
}
