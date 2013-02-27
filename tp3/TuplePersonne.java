package tp3;

import java.sql.Date;

public class TuplePersonne {
    private String nom;
    private Date dateNaissance;
    private String lieuNaissance;
    private int sexe;
    
    public TuplePersonne(String nom, Date dateNaissance, String lieuNaissance, int sexe){
        this.nom = nom;
        this.dateNaissance = dateNaissance;
        this.lieuNaissance = lieuNaissance;
        this.sexe = sexe;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public Date getDateNaissance() {
        return dateNaissance;
    }
    
    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
    
    public String getLieuNaissance() {
        return lieuNaissance;
    }
    
    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }
    
    public int getSexe() {
        return sexe;
    }
    
    public void setSexe(int sexe) {
        this.sexe = sexe;
    }
}
