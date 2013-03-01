package tp3;

/*
 * Projet : Tp3
 *
 * Membres :
 * - Guillaume Harvey 12 059 595
 * - Kevin Labrie 12 113 777
 * - Vincent Philippon 12 098 838
 * - Mathieu Larocque 10 129 032
 *
 * Tache :
 * - Guillaume Harvey : 
 * - Kevin Labrie : 
 * - Vincent Philippon : 
 * - Mathieu Larocque : 
 * 
 */

import java.io.*;
import java.sql.*;
import java.util.StringTokenizer;

public class Main {

    private static PreparedStatement pstmCountRole;
    private static PreparedStatement pstmCountRealisateur;

    private static PreparedStatement pstmCheckFilm;
    private static PreparedStatement pstmCheckRole;

    private static PreparedStatement pstmInsertFilm;
    private static PreparedStatement pstmDeleteFilm;
    private static PreparedStatement pstmAddDescriptionFilm;

    private static PreparedStatement pstmAjoutActeurFilm;
    private static PreparedStatement pstmCheckActeurFilm;
    
    private static GestionTp3 gestionTp3;

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("Usage: java tp3.Main <bd> <user> <password> [<fichier-transactions>]");
            return;
        }
        
        try {
            gestionTp3 = new GestionTp3("postgres", args[0], args[1], args[2]);
            
            BufferedReader reader = ouvrirFichier(args);
            String transaction = lireTransaction(reader);
            while (!finTransaction(transaction)) {
                executerTransaction(transaction);
                transaction = lireTransaction(reader);
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            gestionTp3.fermer();
        }
    }
    //comment unsupported version this does not belong here anymore :(
    /*
    private static void initStatement() throws SQLException {

        pstmCheckFilm = cx.getConnection().prepareStatement(
                "SELECT * FROM Film WHERE titre = ? AND dateSortie = ?");
        
        pstmCheckRole = cx.getConnection().prepareStatement(
                "SELECT * FROM RoleFilm WHERE filmtitre = ? AND anneeSortie = ?");

        pstmInsertFilm = cx.getConnection().prepareStatement(
                "INSERT INTO Film (titre, dateSortie, realisateur) VALUES (?, ?, ?)");
        
        pstmDeleteFilm = cx.getConnection().prepareStatement(
                "DELETE FROM Film WHERE titre = ? AND dateSortie = ?");
        
        pstmAddDescriptionFilm = cx.getConnection().prepareStatement(
                "UPDATE Film SET description=?, duree=? WHERE titre = ? AND dateSortie = ?");
        
        pstmAjoutActeurFilm = cx.getConnection().prepareStatement(
                "INSERT INTO RoleFilm(filmtitre,anneesortie,nomacteur,roleacteur) VALUES(?,?,?,?)");
        
        pstmCheckActeurFilm = cx.getConnection().prepareStatement(
                "SELECT * FROM RoleFilm WHERE filmtitre = ? AND anneesortie = ? AND nomacteur = ?;");

        pstmCountRealisateur = cx.getConnection().prepareStatement(
                "SELECT * FROM Film WHERE realisateur = ?");
        
        pstmCountRole = cx.getConnection().prepareStatement(
                "SELECT * FROM RoleFilm WHERE nomActeur = ?");
    }
    */
    
    /**
     * Decodage et traitement d'une transaction :
     * @throws Exception 
     */
    static void executerTransaction(String transaction) throws Exception {
        try {
            System.out.println(transaction);
            // decoupage de la transaction en mots
            StringTokenizer tokenizer = new StringTokenizer(transaction, " ");
            if (tokenizer.hasMoreTokens()) {
                String command = tokenizer.nextToken();
                if ("ajoutPersonne".startsWith(command)){
                    gestionTp3.gestionPersonne.ajoutPersonne(readString(tokenizer) /* nom */,
                            readDate(tokenizer) /* dateNaissance */,
                            readString(tokenizer) /* lieuNaissance */,
                            readInt(tokenizer) /* sexe */);
                    
                }else if ("supPersonne".startsWith(command)){
                    gestionTp3.gestionPersonne.supprimerPersonne(readString(tokenizer) /* nom */);
                
                }else if ("ajoutFilm".startsWith(command)){
                    gestionTp3.gestionFilm.ajoutFilm(readString(tokenizer) /* titre */,
                            readDate(tokenizer) /* annee */,
                            readString(tokenizer) /* realisateur */);
                
                }else if ("supFilm".startsWith(command)){
                    gestionTp3.gestionFilm.supprimerFilm(readString(tokenizer) /* titre */,
                            readDate(tokenizer) /* annee */);
                
                }else if ("ajoutDescFilm".startsWith(command)){
                    gestionTp3.gestionFilm.ajoutDescFilm(readString(tokenizer) /* titre */,
                            readDate(tokenizer) /* annee */,
                            readString(tokenizer) /* description */,
                            readInt(tokenizer) /* duree */);
                
                }else if ("ajoutActeurFilm".startsWith(command)){
                    gestionTp3.gestionFilm.ajoutActeurFilm(
                            readString(tokenizer) /* titre */,
                            readDate(tokenizer) /* annee */,
                            readString(tokenizer) /* nom */,
                            readString(tokenizer) /* role */);
                
                }else if ("ajoutSerie".startsWith(command)){
                    gestionTp3.gestionSerie.ajoutSerie(
                            readString(tokenizer) /* titre */,
                            readDate(tokenizer) /* annee */,
                            readString(tokenizer) /* nom realisateur */);
                
                }else if ("ajoutEpisode".startsWith(command)){
                    gestionTp3.gestionSerie.ajoutEpisode(
                            readString(tokenizer) /* titre episode */,
                            readString(tokenizer) /* titre serie */,
                            readDate(tokenizer) /* annee serie */,
                            readInt(tokenizer) /* no saison */,
                            readInt(tokenizer) /* no episode */,
                            readString(tokenizer) /* description */,
                            readDate(tokenizer) /* date episode */);
                
                }else if ("ajoutActeurEpisode".startsWith(command)){
                    gestionTp3.gestionSerie.ajoutRoleAEpisode(
                            readString(tokenizer) /* titre serie */,
                            readDate(tokenizer) /* annee serie */,
                            readInt(tokenizer) /* no saison */,
                            readInt(tokenizer) /* no episode */,
                            readString(tokenizer) /* nom acteur */,
                            readString(tokenizer) /* Role de l'acteur */);
                
                }else if ("listeActeursSerie".startsWith(command)){
                    gestionTp3.gestionSerie.afficherActeursSerie(
                            readString(tokenizer) /* titre */,
                            readDate(tokenizer) /* annee */);
                
                }else if ("listeSerieActeur".startsWith(command)){
                    gestionTp3.gestionPersonne.afficherSerieAvecActeur(
                            readString(tokenizer) /* nom */);
                
                }else if ("listeRealisateurs".startsWith(command)){
                    gestionTp3.gestionPersonne.afficherRealisateur();
                
                }else if ("listeActeursFilm".startsWith(command)){
                    gestionTp3.gestionFilm.afficherActeurDeFilm(
                            readString(tokenizer) /* titre */,
                            readDate(tokenizer) /* annee */);
                
                }else if ("listeFilmsActeur".startsWith(command)){
                    gestionTp3.gestionPersonne.afficherFilmDeActeur(
                            readString(tokenizer) /* nom */);
                
                }else{
                    System.out.println(" : Transaction non reconnue");
                }
            }
        } catch (Tp3Exception e) {
            System.out.println("** " + e.toString());
        }
    }
    // comment because not needed but we can use the code in gestionnaire
/*
    public static void effectuerAjoutPersonne(String nom, Date dateNaissance, 
            String lieuNaissance, int sexe) throws SQLException, Tp3Exception {
        
        if (!isStringNotEmpty(nom) || !isStringNotEmpty(lieuNaissance) || sexe < 0 || sexe > 1) {
            //une donner est invalide
            throw new Tp3Exception("Un parametre est invalide "
                    + "(nom = '" + nom
                    + "', date de naissance = '" + dateNaissance
                    + "', sexe = '" + sexe + "').\n");
        } else {
            pstmGetPersonne.setString(1, nom);
            ResultSet rs = pstmGetPersonne.executeQuery();

            if (!rs.next()) {
                pstmInsertPersonne.setString(1, nom);
                pstmInsertPersonne.setDate(2, dateNaissance);
                pstmInsertPersonne.setString(3, lieuNaissance);
                pstmInsertPersonne.setInt(4, sexe);

                int nbAjout = pstmInsertPersonne.executeUpdate();
                System.out.println(nbAjout > 0 ? "Transaction reussit\n" : " \n");
            } else {
                throw new Tp3Exception("La personne " + nom + " existe deja.\n");
            }
            rs.close();
            cx.commit();
        }
    }

    public static void effectuerSupPersonne(String nom) throws SQLException, Tp3Exception {
        if (!isStringNotEmpty(nom)) {
            throw new Tp3Exception("Le nom est invalide (nom = '" + nom + "'.\n");
        } else {

            pstmGetPersonne.setString(1, nom);
            ResultSet rs = pstmGetPersonne.executeQuery();

            // Si la personne existe
            if (rs.next()) {
                pstmCountRealisateur.setString(1, nom);
                rs = pstmCountRealisateur.executeQuery();

                // S'il n'est le realisateur d'aucun film
                if (!rs.next()) {
                    pstmCountRole.setString(1, nom);
                    rs = pstmCountRole.executeQuery();

                    // S'il n'a aucun role
                    if (!rs.next()) {
                        pstmDeletePersonne.setString(1, nom);
                        int count = pstmDeletePersonne.executeUpdate();
                        System.out.println(count > 0 ? "Transaction reussit\n" : " \n");
                        rs.close();
                        cx.commit();
                    } else {
                        throw new Tp3Exception("La personne " + nom 
                                + " possede au moins un role dans un film.\n");
                    }
                } else {
                    throw new Tp3Exception("La personne " + nom
                            + " est realisateur de film(s) encore existant.\n");
                }
            } else {
                throw new Tp3Exception("La personne " + nom + "n'existe pas.\n");
            }
        }
    }

    public static void effectuerAjoutFilm(String titre, Date dateSortie, 
            String realisateur) throws SQLException, Tp3Exception {
        if (!isStringNotEmpty(titre) || !isStringNotEmpty(realisateur)) {
            //une donner est invalide
            throw new Tp3Exception("Un parametre est invalide " 
                    + "(titre = '" + titre 
                    + "', date de sortie = '" + dateSortie
                    + "', realisateur = '" + realisateur + "').\n");
        } else {
            pstmCheckFilm.setString(1, titre);
            pstmCheckFilm.setDate(2, dateSortie);
            ResultSet rs = pstmCheckFilm.executeQuery();
            if (!rs.next()) {
                pstmGetPersonne.setString(1, realisateur);
                rs = pstmGetPersonne.executeQuery();
                if (rs.next()) {
                    pstmInsertFilm.setString(1, titre);
                    pstmInsertFilm.setDate(2, dateSortie);
                    pstmInsertFilm.setString(3, realisateur);

                    int nbAjout = pstmInsertFilm.executeUpdate();
                    System.out.println(nbAjout > 0 ? "Transaction reussit\n" : " \n");
                    rs.close();
                    cx.commit();
                } else {
                    //le realisateur n existe pas
                    throw new Tp3Exception("Le realisateur : '" + realisateur + "' n'existe pas.\n");
                }
            } else {
                //un film existe deja avec le nom et la meme date de sortie
                throw new Tp3Exception(
                        "Le film que vous tentez de creer existe deja " 
                                + "(titre = '" + titre 
                                + "', annee de sortie = '" + dateSortie + "').\n");
            }
        }
    }

    public static void effectuerSupFilm(String titre, Date dateSortie)
            throws SQLException, Tp3Exception {
        if (!isStringNotEmpty(titre)) {
            //une donner est invalide
            throw new Tp3Exception("Un parametre est invalide (titre = '"
                    + titre + "', date de sortie = '" + dateSortie + "').\n");
        } else {
            pstmCheckFilm.setString(1, titre);
            pstmCheckFilm.setDate(2, dateSortie);
            ResultSet rs = pstmCheckFilm.executeQuery();
            if (rs.next()) {
                pstmCheckRole.setString(1, titre);
                pstmCheckRole.setDate(2, dateSortie);
                rs = pstmCheckRole.executeQuery();
                if (!rs.next()) {
                    pstmDeleteFilm.setString(1, titre);
                    pstmDeleteFilm.setDate(2, dateSortie);

                    int nbAjout = pstmDeleteFilm.executeUpdate();
                    System.out.println(nbAjout > 0 ? "Transaction reussit\n" : " \n");
                    rs.close();
                    cx.commit();

                } else {
                    //un role est associer au film
                    throw new Tp3Exception(
                            "On ne peut supprimer le film car au moins un role lui est relier (acteur = '"
                                    + rs.getString("nomActeur") + "').\n");
                }
            } else {
                //le film n existe pas
                throw new Tp3Exception(
                        "Le film : '"
                                + titre
                                + "', dont l'annee de sortie est : '"
                                + dateSortie
                                + "' n'existe pas, il est donc impossible de le supprimer.\n");
            }
        }
    }

    public static void effectuerAjoutDescFilm(String titre, Date anneeSortie,
            String description, int duree) throws SQLException, Tp3Exception {
        if (!isStringNotEmpty(titre) || !isStringNotEmpty(description)
                || duree < 0) {
            //une donner est invalide
            throw new Tp3Exception("Un parametre est invalide (titre = '"
                    + titre + "', date de sortie = '" + anneeSortie
                    + "', description = '" + description + "', duree = '"
                    + duree + "').\n");
        } else {
            pstmCheckFilm.setString(1, titre);
            pstmCheckFilm.setDate(2, anneeSortie);
            ResultSet rs = pstmCheckFilm.executeQuery();
            if (rs.next()) {
                pstmAddDescriptionFilm.setString(1, description);
                pstmAddDescriptionFilm.setInt(2, duree);
                pstmAddDescriptionFilm.setString(3, titre);
                pstmAddDescriptionFilm.setDate(4, anneeSortie);

                int nbAjout = pstmAddDescriptionFilm.executeUpdate();
                System.out.println(nbAjout > 0 ? "Transaction reussit\n" : " \n");
                rs.close();
                cx.commit();

            } else {
                //le film n existe pas
                throw new Tp3Exception("Le film : '" + titre
                        + "', sortant le '" + anneeSortie + "' n'existe pas.\n");
            }
        }
    }

    public static void effectuerAjoutActeurFilm(String titre, Date anneeSortie,
            String acteur, String role) throws SQLException, Tp3Exception {
        if (!isStringNotEmpty(titre) || !isStringNotEmpty(acteur)
                || !isStringNotEmpty(role)) {
            //une donner est invalide
            throw new Tp3Exception("Un parametre est invalide (titre = '"
                    + titre + "', date de sortie = '" + anneeSortie
                    + "', acteur = '" + acteur + "', role = '" + role + "').\n");
        } else {

            pstmAjoutActeurFilm.setString(1, titre);
            pstmAjoutActeurFilm.setDate(2, anneeSortie);
            pstmAjoutActeurFilm.setString(3, acteur);
            pstmAjoutActeurFilm.setString(4, role);
            pstmCheckActeurFilm.setString(1, titre);
            pstmCheckActeurFilm.setDate(2, anneeSortie);
            pstmCheckActeurFilm.setString(3, acteur);
            pstmGetPersonne.setString(1, acteur);

            pstmCheckFilm.setString(1, titre);
            pstmCheckFilm.setDate(2, anneeSortie);

            ResultSet rs = pstmCheckActeurFilm.executeQuery();

            if (!rs.next()) {
                rs = pstmGetPersonne.executeQuery();

                if (rs.next()) {
                    Date acteurDate = rs.getDate("dateNaissance");
                
                    if(acteurDate.before(anneeSortie)){
                        rs = pstmCheckFilm.executeQuery();

                        if (rs.next()) {
                            int nbAjout = pstmAjoutActeurFilm.executeUpdate();
                            System.out.println(nbAjout > 0 ? "Transaction reussit\n" : " \n");
                            cx.commit();
                        } else {
                            throw new Tp3Exception(
                                    "Impossible d'ajouter l'acteur au film puisque que celui-ci y joue deja.\n");
                        }
                    }else{
                        throw new Tp3Exception(
                                    "L'acteur semble etre nee avant la date de sortie du film.\n");
                    }
                } else {
                    throw new Tp3Exception(
                            "Impossible d'ajouter l'acteur au film puisque que le film n'existe pas.\n");
                }
            } else {
                throw new Tp3Exception(
                        "Impossible d'ajouter l'acteur au film puisque que l'acteur n'existe pas.\n");
            }

            rs.close();
        }

    }
*/
    /** Les methodes suivantes n'ont pas besoin d'etre modifiees */

    public static BufferedReader ouvrirFichier(String[] args)
            throws FileNotFoundException {
        if (args.length < 4)
            // lecture au clavier
            return new BufferedReader(new InputStreamReader(System.in));
        else
            // lecture dans le fichier passe en parametre
            return new BufferedReader(new InputStreamReader(
                    new FileInputStream(args[3])));
    }

    /**
     * Lecture d'une transaction
     */
    static String lireTransaction(BufferedReader reader) throws IOException {
        return reader.readLine();
    }

    /**
     * Verifie si la fin du traitement des transactions est atteinte.
     */
    static boolean finTransaction(String transaction) {
        // fin de fichier atteinte
        if (transaction == null || transaction.equals("exit"))
            return true;
        else
            return false;
    }

    /** lecture d'une chaine de caracteres de la transaction entree a l'ecran */
    static String readString(StringTokenizer tokenizer) throws Exception {
        if (tokenizer.hasMoreElements())
            return tokenizer.nextToken();
        else
            throw new Exception("autre parametre attendu");
    }

    /**
     * lecture d'un int java de la transaction entree a l'ecran
     */
    static int readInt(StringTokenizer tokenizer) throws Exception {
        if (tokenizer.hasMoreElements()) {
            String token = tokenizer.nextToken();
            try {
                return Integer.valueOf(token).intValue();
            } catch (NumberFormatException e) {
                throw new Exception("Nombre attendu a la place de \"" + token
                        + "\"");
            }
        } else
            throw new Exception("autre parametre attendu");
    }

    static Date readDate(StringTokenizer tokenizer) throws Exception {
        if (tokenizer.hasMoreElements()) {
            String token = tokenizer.nextToken();
            try {
                return Date.valueOf(token);
            } catch (IllegalArgumentException e) {
                throw new Exception("Date dans un format invalide - \"" + token
                        + "\"");
            }
        } else
            throw new Exception("autre parametre attendu");
    }

    public static boolean isStringNotEmpty(String s) {
        return (s != null && s.length() > 0);
    }

}