package VigieMedias;

public class PersonneProprietaire {
    public Personne personne;
    public float pourcentage;

    public PersonneProprietaire(Personne proprietaire, float part) {
        personne = proprietaire;
        pourcentage = part;
    }

    @Override
    public String toString() {
        return personne + " propriétaire à " + pourcentage + "%";
    }
}
