package VigieMedias;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Organisation {
    static public List<Organisation> listeOrganisations = new ArrayList<>();
    static public HashMap<String, Organisation> mapOrganisations = new HashMap<>();
    static public int nbOrganisations = 0;

    protected ArrayList<ParticipationOrganisation> organisationsPossedees = new ArrayList<>();
    protected ArrayList<ParticipationMedia> mediasPossedes = new ArrayList<>();
    protected ArrayList<OrganisationProprietaire> organisationsProprietaires = new ArrayList<>();
    protected ArrayList<PersonneProprietaire> personnesProprietaires = new ArrayList<>();
    protected ArrayList<EvolutionParts> historiqueRachat = new ArrayList<>();
    protected ArrayList<Publication> historiqueMentions = new ArrayList<>();
    protected String name;

    public Organisation(String completeName) {
        name = completeName;
        listeOrganisations.add(this);
        mapOrganisations.put(name, this);
        nbOrganisations++;
    }

    /**
     * Calcule le nombre de médias possédés par l'organisation
     * @return nbMedia
     */
    public int getNombreMedia() {
        int count = 0;
        for (ParticipationOrganisation participation : organisationsPossedees) {
            count += participation.organisation.getNombreMedia();
        }
        count += mediasPossedes.size();
        return count;
    }

    /**
     * Renvoit la liste des médias possédés par l'organisation ainsi que le pourcentage de parts possédées multiplié par
     * le facteur
     * @param facteur pourcentage de parts possédées dans l'organisation
     * @return listMedia
     */
    public ArrayList<ParticipationMedia> getListeMedia(float facteur) {
        ArrayList<ParticipationMedia> listeMedia = new ArrayList<>();
        for (ParticipationMedia participation : mediasPossedes) {
            listeMedia.add(new ParticipationMedia(participation.media, participation.pourcentage*facteur));
        }
        for (ParticipationOrganisation participation : organisationsPossedees) {
            listeMedia.addAll(participation.organisation.getListeMedia(facteur*participation.pourcentage/100.0f));
        }
        return listeMedia;
    }

    /**
     * Renvoit la liste des actionnaires de l'organisation ainsi que le pourcentage de parts possédées mutltipliées par
     * le facteur
     * @param facteur pourcentage de parts possédées par l'organisation dans l'organisation fille ou le média fils
     * @return listeProprietaire
     */
    public ArrayList<PersonneProprietaire> getListeProprietaires(float facteur) {
        ArrayList<PersonneProprietaire> listeProprietaire = new ArrayList<>();
        for (PersonneProprietaire proprietaire : personnesProprietaires) {
            listeProprietaire.add(new PersonneProprietaire(proprietaire.personne, proprietaire.pourcentage * facteur));
        }
        for (OrganisationProprietaire proprietaire : organisationsProprietaires) {
            listeProprietaire.addAll(proprietaire.organisation.getListeProprietaires(facteur * proprietaire.pourcentage / 100.0f));
        }
        return listeProprietaire;
    }

    @Override
    public String toString() {
        return name;
    }
}
