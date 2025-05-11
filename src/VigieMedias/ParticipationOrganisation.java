package VigieMedias;

public class ParticipationOrganisation {
    public Organisation organisation;
    public float pourcentage;

    public ParticipationOrganisation(Organisation Organisation, float Pourcentage) {
        organisation = Organisation;
        pourcentage = Pourcentage;
    }

    @Override
    public String toString() {
        return organisation.toString() + " possédé à : " + String.valueOf(pourcentage) + "%";
    }
}
