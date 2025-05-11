package VigieMedias;

public class EvolutionParts {
    String typeEvolution;
    String proprietaire;
    float valeurEvolution;

    public EvolutionParts(boolean achat, Personne personne, float variation) {
        if (achat){
            typeEvolution = "Achat";
        } else {
            typeEvolution = "Vente";
        }
        proprietaire = personne.name;
        valeurEvolution = variation;
    }

    public EvolutionParts(boolean achat, Organisation organisation, float variation) {
        if (achat){
            typeEvolution = "Achat";
        } else {
            typeEvolution = "Vente";
        }
        proprietaire = organisation.name;
        valeurEvolution = variation;
    }

    @Override
    public String toString() {
        return typeEvolution + " de " + valeurEvolution + "% des parts, par " + proprietaire;
    }
}
