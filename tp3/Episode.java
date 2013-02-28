package tp3;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class Episode {

    private Connexion cx;
    private PreparedStatement stmtEpisodeExiste;
    
    public Episode(Connexion cx) throws SQLException {
        this.cx = cx;
        init();
    }
    
    private void init() throws SQLException {
        try{
            stmtEpisodeExiste = cx.getConnection().prepareStatement("SELECT * FROM Episode WHERE titreSerie = ? AND anneeSortieSerie = ? AND noSaison = ? AND noEpisode = ?");
        }catch(SQLException e){
            throw e;
        }
    }

    public Connexion getConnexion() {
        return cx;
    }

    public boolean existe(String serieTitre, Date serieDate, int noSaison, int noEpisode) throws SQLException{
        boolean retour = false;
        stmtEpisodeExiste.setString(1,serieTitre);
        stmtEpisodeExiste.setDate(2,serieDate);
        stmtEpisodeExiste.setInt(3,noSaison);
        stmtEpisodeExiste.setInt(4,noEpisode);
        try{
            ResultSet rs = stmtEpisodeExiste.executeQuery();
            if(rs.next()) retour = true;
        }catch(SQLException e){
            throw e;
        }
        return retour;
    }
    
}
