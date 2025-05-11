package VigieMedias;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TsvReader {
    public static void init(){
        TsvReader.openPersonnes();
        TsvReader.openOrganisations();
        TsvReader.openMedias();
        TsvReader.openPersonneMedia();
        TsvReader.openPersonneOrganisation();
        TsvReader.openOrganisationOrganisation();
        TsvReader.openOrganisationMedia();
    }

    /**
     * Crée les objets personnes à partir du fichier personnes.tsv
     */
    public static void openPersonnes() {
        try (InputStream input = TsvReader.class.getResourceAsStream("/data/personnes.tsv");
             BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\t");
                new Personne(values[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crée les objets organisations à partir du fichier organisations.tsv
     */
    public static void openOrganisations() {
        try (InputStream input = TsvReader.class.getResourceAsStream("/data/organisations.tsv");
             BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\t");
                new Organisation(values[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crée les objets medias à partir du fichier medias.tsv
     */
    public static void openMedias() {
        try (InputStream input = TsvReader.class.getResourceAsStream("/data/medias.tsv");
             BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\t");
                new Media(values[0], values[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crée les relations entre personnes et médias à partir du fichier personne-media.tsv
     */
    public static void openPersonneMedia() {
        try (InputStream input = TsvReader.class.getResourceAsStream("/data/personne-media.tsv");
             BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\t");

                // La personne qui participe dans le média
                Personne personne = Personne.mapPersonnes.get(values[1]);
                Media media = Media.mapMedias.get(values[4]);
                // Sa participation dans le média
                float pourcentage = Float.parseFloat(values[3].substring(0, values[3].length() - 1));
                // Création des objets participations
                ParticipationMedia participation = new ParticipationMedia(media, pourcentage);
                PersonneProprietaire proprietaire = new PersonneProprietaire(personne, pourcentage);
                // Ajout de la participation à la liste de la personne
                personne.mediaPossedes.add(participation);
                media.personnesProprietaires.add(proprietaire);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Crée les relations entre personnes et organisations à partir du fichier personne-organisation.tsv
     */
    public static void openPersonneOrganisation() {
        try (InputStream input = TsvReader.class.getResourceAsStream("/data/personne-organisation.tsv");
             BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\t");
                float pourcentage;

                // La personne qui participe dans l'organisation
                Personne personne = Personne.mapPersonnes.get(values[1]);
                // L'organisation possédée par la personne
                Organisation organisation = Organisation.mapOrganisations.get(values[4]);

                // Sa participation dans l'organisation
                if (values[2].equals("égal à")) {
                    pourcentage = Float.parseFloat(values[3].substring(0, values[3].length() - 1));
                } else if (values[2].equals("contrôle")) {
                    pourcentage = 100.0F;
                } else {
                    pourcentage = 0.1F;
                }

                // Création des objets participation/propriété
                ParticipationOrganisation participation = new ParticipationOrganisation(organisation, pourcentage);
                PersonneProprietaire proprietaire = new PersonneProprietaire(personne, pourcentage);
                // Ajout de la participation à la liste de la personne et de l'organisation
                personne.organisationsPossedees.add(participation);
                organisation.personnesProprietaires.add(proprietaire);

            }
        } catch (IOException e) {
            e.printStackTrace();
            }
        }

    /**
     * Crée les relations entre organisations et organisations à partir du fichier organisation-organisation.tsv
     */
    public static void openOrganisationOrganisation() {
            try (InputStream input = TsvReader.class.getResourceAsStream("/data/organisation-organisation.tsv");
                 BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
                String line;
                br.readLine();
                while ((line = br.readLine()) != null) {
                    String[] values = line.split("\t");
                    float pourcentage;

                    // L'organisation propriétaire
                    Organisation organisationP = Organisation.mapOrganisations.get(values[1]);
                    // L'organisation possédée
                    Organisation organisationp = Organisation.mapOrganisations.get(values[4]);
                    // Sa participation dans l'organisation
                    if (values[2].equals("égal à") || values[2].equals("supérieur à")) {
                        pourcentage = Float.parseFloat(values[3].substring(0, values[3].length() - 1));
                    }
                    else if (values[2].equals("contrôle")) {
                        pourcentage = 100.0F;
                    }
                    else {
                        pourcentage = 0.1F;
                    }

                    // Création de l'objet participation
                    ParticipationOrganisation participation = new ParticipationOrganisation(organisationp, pourcentage);
                    OrganisationProprietaire actionnaire = new OrganisationProprietaire(organisationP, pourcentage);
                    // Ajout de la participation à la liste des participations de l'organisation propriétaire
                    organisationP.organisationsPossedees.add(participation);
                    // Ajout de la participation à la liste des actionnaires de l'organisation possédée
                    organisationp.organisationsProprietaires.add(actionnaire);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    /**
     * Crée les relations entre organisations et medias à partir du fichier organisation-media.tsv
     */
    public static void openOrganisationMedia() {
            try (InputStream input = TsvReader.class.getResourceAsStream("/data/organisation-media.tsv");
                 BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
                String line;
                br.readLine();
                while ((line = br.readLine()) != null) {
                    String[] values = line.split("\t");
                    float pourcentage;

                    // L'organisation actionnaire du média
                    Organisation organisation = Organisation.mapOrganisations.get(values[1]);
                    // Le média possédé
                    Media media = Media.mapMedias.get(values[4]);
                    // Sa participation dans le média
                    if (values[2].equals("égal à") || values[2].equals("supérieur à")) {
                        pourcentage = Float.parseFloat(values[3].substring(0, values[3].length() - 1));
                    }
                    else if (values[2].equals("contrôle")) {
                        pourcentage = 100.0F;
                    }
                    else {
                        pourcentage = 0.1F;
                    }

                    // Création de l'objet participation
                    ParticipationMedia participation = new ParticipationMedia(media, pourcentage);
                    OrganisationProprietaire organisationProprietaire = new OrganisationProprietaire(organisation, pourcentage);
                    // Ajout de la participation à la liste des participations de l'organisation actionnaire
                    organisation.mediasPossedes.add(participation);
                    // Ajout de la participation à la liste des actionnaires du média possédé
                    media.organisationsProprietaires.add(organisationProprietaire);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}
