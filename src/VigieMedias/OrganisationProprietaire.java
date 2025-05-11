package VigieMedias;

public class OrganisationProprietaire {
    public Organisation organisation;
    public float pourcentage;

    public OrganisationProprietaire(Organisation proprietaire, float part) {
        organisation = proprietaire;
        pourcentage = part;
    }

    @Override
    public String toString() {
        return organisation + " propriétaire à " + pourcentage + "%";
    }
}
