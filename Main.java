package tp2;

/*
 * Projet : Tp2
 *
 * Membres :
 * - Guillaume Harvey 12 059 595
 * - Kevin Labrie 12 113 777
 * - Vincent Philippon 12 098 838
 * - Mathieu Larocque 10 129 032
 *
 * Tache :
 * - Guillaume Harvey : fonction effectuerAjoutActeurFilm
 * - Kevin Labrie : fonction effectuerAjoutPersonne
 * - Vincent Philippon : fonction effectuerSupPersonne, ajustement final
 * - Mathieu Larocque : fonction effectuerAjoutFilm, effectuerSupFilm, effectuerAjoutDescFilm et merge du document
 * 
 */

import java.io.*;
import java.util.StringTokenizer;
import java.sql.*;

public class Main {

    private static Connexion cx;

    private static PreparedStatement pstmCheckPersonne;
    private static PreparedStatement pstmInsertPersonne;
    private static PreparedStatement pstmDeletePersonne;

    private static PreparedStatement pstmCountRole;
    private static PreparedStatement pstmCountRealisateur;

    private static PreparedStatement pstmCheckFilm;
    private static PreparedStatement pstmCheckRole;

    private static PreparedStatement pstmInsertFilm;
    private static PreparedStatement pstmDeleteFilm;
    private static PreparedStatement pstmAddDescriptionFilm;

    private static PreparedStatement pstmAjoutActeurFilm;
    private static PreparedStatement pstmCheckActeurFilm;

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("Usage: java tp2.Main <bd> <user> <password> [<fichier-transactions>]");
            return;
        }
        cx = new Connexion("postgres", args[0], args[1], args[2]);
        initStatement();
        BufferedReader reader = ouvrirFichier(args);
        String transaction = lireTransaction(reader);
        while (!finTransaction(transaction)) {
            executerTransaction(transaction);
            transaction = lireTransaction(reader);
        }
    }

    private static void initStatement() throws SQLException {
        pstmCheckPersonne = cx.getConnection().prepareStatement(
                "SELECT * FROM Personne WHERE nom = ?");

        pstmInsertPersonne = cx.getConnection().prepareStatement(
                "INSERT INTO Personne (nom, datenaissance, lieunaissance, sexe) VALUES(?, ?, ?, ?)");
        
        pstmDeletePersonne = cx.getConnection().prepareStatement(
                "DELETE FROM Personne WHERE nom = ?");

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

    /**
     * Decodage et traitement d'une transaction :
     */
    static void executerTransaction(String transaction) throws Exception, Tp2Exception {
        try {
            System.out.println(transaction);
            // decoupage de la transaction en mots
            StringTokenizer tokenizer = new StringTokenizer(transaction, " ");
            if (tokenizer.hasMoreTokens()) {
                String command = tokenizer.nextToken();
                if ("ajoutPersonne".startsWith(command))
                    effectuerAjoutPersonne(readString(tokenizer) /* nom */,
                            readDate(tokenizer) /* dateNaissance */,
                            readString(tokenizer) /* lieuNaissance */,
                            readInt(tokenizer) /* sexe */);
                else if ("supPersonne".startsWith(command))
                    effectuerSupPersonne(readString(tokenizer) /* nom */);
                else if ("ajoutFilm".startsWith(command))
                    effectuerAjoutFilm(readString(tokenizer) /* titre */,
                            readDate(tokenizer) /* annee */,
                            readString(tokenizer) /* realisateur */);
                else if ("supFilm".startsWith(command))
                    effectuerSupFilm(readString(tokenizer) /* titre */,
                            readDate(tokenizer) /* annee */);
                else if ("ajoutDescFilm".startsWith(command))
                    effectuerAjoutDescFilm(readString(tokenizer) /* titre */,
                            readDate(tokenizer) /* annee */,
                            readString(tokenizer) /* description */,
                            readInt(tokenizer) /* duree */);
                else if ("ajoutActeurFilm".startsWith(command))
                    effectuerAjoutActeurFilm(
                            readString(tokenizer) /* titre */,
                            readDate(tokenizer) /* annee */,
                            readString(tokenizer) /* nom */,
                            readString(tokenizer) /* role */);
                else
                    System.out.println(" : Transaction non reconnue");
            }
        } catch (Exception e) {
            System.out.println(" " + e.toString());
            cx.rollback();
        }
    }

    public static void effectuerAjoutPersonne(String nom, Date dateNaissance, 
            String lieuNaissance, int sexe) throws SQLException, Tp2Exception {
        
        if (!isStringNotEmpty(nom) || !isStringNotEmpty(lieuNaissance) || sexe < 0 || sexe > 1) {
            //une donner est invalide
            throw new Tp2Exception("Un parametre est invalide "
                    + "(nom = '" + nom
                    + "', date de naissance = '" + dateNaissance
                    + "', sexe = '" + sexe + "').\n");
        } else {
            pstmCheckPersonne.setString(1, nom);
            ResultSet rs = pstmCheckPersonne.executeQuery();

            if (!rs.next()) {
                pstmInsertPersonne.setString(1, nom);
                pstmInsertPersonne.setDate(2, dateNaissance);
                pstmInsertPersonne.setString(3, lieuNaissance);
                pstmInsertPersonne.setInt(4, sexe);

                int nbAjout = pstmInsertPersonne.executeUpdate();
                System.out.println(nbAjout > 0 ? "Transaction reussit\n" : " \n");
            } else {
                throw new Tp2Exception("La personne " + nom + " existe deja.\n");
            }
            rs.close();
            cx.commit();
        }
    }

    public static void effectuerSupPersonne(String nom) throws SQLException, Tp2Exception {
        if (!isStringNotEmpty(nom)) {
            throw new Tp2Exception("Le nom est invalide (nom = '" + nom + "'.\n");
        } else {

            pstmCheckPersonne.setString(1, nom);
            ResultSet rs = pstmCheckPersonne.executeQuery();

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
                        throw new Tp2Exception("La personne " + nom 
                                + " possede au moins un role dans un film.\n");
                    }
                } else {
                    throw new Tp2Exception("La personne " + nom
                            + " est realisateur de film(s) encore existant.\n");
                }
            } else {
                throw new Tp2Exception("La personne " + nom + "n'existe pas.\n");
            }
        }
    }

    public static void effectuerAjoutFilm(String titre, Date dateSortie, 
            String realisateur) throws SQLException, Tp2Exception {
        if (!isStringNotEmpty(titre) || !isStringNotEmpty(realisateur)) {
            //une donner est invalide
            throw new Tp2Exception("Un parametre est invalide " 
                    + "(titre = '" + titre 
                    + "', date de sortie = '" + dateSortie
                    + "', realisateur = '" + realisateur + "').\n");
        } else {
            pstmCheckFilm.setString(1, titre);
            pstmCheckFilm.setDate(2, dateSortie);
            ResultSet rs = pstmCheckFilm.executeQuery();
            if (!rs.next()) {
                pstmCheckPersonne.setString(1, realisateur);
                rs = pstmCheckPersonne.executeQuery();
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
                    throw new Tp2Exception("Le realisateur : '" + realisateur + "' n'existe pas.\n");
                }
            } else {
                //un film existe deja avec le nom et la meme date de sortie
                throw new Tp2Exception(
                        "Le film que vous tentez de creer existe deja " 
                                + "(titre = '" + titre 
                                + "', annee de sortie = '" + dateSortie + "').\n");
            }
        }
    }

    public static void effectuerSupFilm(String titre, Date dateSortie)
            throws SQLException, Tp2Exception {
        if (!isStringNotEmpty(titre)) {
            //une donner est invalide
            throw new Tp2Exception("Un parametre est invalide (titre = '"
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
                    throw new Tp2Exception(
                            "On ne peut supprimer le film car au moins un role lui est relier (acteur = '"
                                    + rs.getString("nomActeur") + "').\n");
                }
            } else {
                //le film n existe pas
                throw new Tp2Exception(
                        "Le film : '"
                                + titre
                                + "', dont l'annee de sortie est : '"
                                + dateSortie
                                + "' n'existe pas, il est donc impossible de le supprimer.\n");
            }
        }
    }

    public static void effectuerAjoutDescFilm(String titre, Date anneeSortie,
            String description, int duree) throws SQLException, Tp2Exception {
        if (!isStringNotEmpty(titre) || !isStringNotEmpty(description)
                || duree < 0) {
            //une donner est invalide
            throw new Tp2Exception("Un parametre est invalide (titre = '"
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
                throw new Tp2Exception("Le film : '" + titre
                        + "', sortant le '" + anneeSortie + "' n'existe pas.\n");
            }
        }
    }

    public static void effectuerAjoutActeurFilm(String titre, Date anneeSortie,
            String acteur, String role) throws SQLException, Tp2Exception {
        if (!isStringNotEmpty(titre) || !isStringNotEmpty(acteur)
                || !isStringNotEmpty(role)) {
            //une donner est invalide
            throw new Tp2Exception("Un parametre est invalide (titre = '"
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
            pstmCheckPersonne.setString(1, acteur);

            pstmCheckFilm.setString(1, titre);
            pstmCheckFilm.setDate(2, anneeSortie);

            ResultSet rs = pstmCheckActeurFilm.executeQuery();

            if (!rs.next()) {
                rs = pstmCheckPersonne.executeQuery();

                if (rs.next()) {
                    rs = pstmCheckFilm.executeQuery();

                    if (rs.next()) {
                        int nbAjout = pstmAjoutActeurFilm.executeUpdate();
                        System.out.println(nbAjout > 0 ? "Transaction reussit\n" : " \n");
                        cx.commit();
                    } else {
                        throw new Tp2Exception(
                                "Impossible d'ajouter l'acteur au film puisque que celui-ci y joue deja.\n");
                    }
                } else {
                    throw new Tp2Exception(
                            "Impossible d'ajouter l'acteur au film puisque que le film n'existe pas.\n");
                }
            } else {
                throw new Tp2Exception(
                        "Impossible d'ajouter l'acteur au film puisque que l'acteur n'existe pas.\n");
            }

            rs.close();
        }

    }

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