package VigieMedias;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Media {
    static public List<Media> listeMedias = new ArrayList<>();
    static public HashMap<String, Media> mapMedias = new HashMap<>();
    static public int nbMedias = 0;

    protected String name;
    protected String type;
    protected ArrayList<OrganisationProprietaire> organisationsProprietaires = new ArrayList<>();
    protected ArrayList<PersonneProprietaire> personnesProprietaires = new ArrayList<>();
    protected ArrayList<EvolutionParts> historiqueRachat = new ArrayList<>();
    protected ArrayList<Publication> historiquePublications = new ArrayList<>();
    protected ArrayList<Publication> historiqueMentions = new ArrayList<>();

    public Media(String Name, String Type) {
        name = Name;
        type = Type;
        listeMedias.add(this);
        mapMedias.put(name, this);
        nbMedias ++;
    }

    /**
     * Renvoit la liste des personnes actionnaires du média
     * @return listeProprietaire
     */
    public ArrayList<PersonneProprietaire> getListeProprietaires() {
        ArrayList<PersonneProprietaire> listeProprietaire = new ArrayList<>(personnesProprietaires);
        for (OrganisationProprietaire proprietaire : organisationsProprietaires){
            listeProprietaire.addAll(proprietaire.organisation.getListeProprietaires(proprietaire.pourcentage/100.0f));
        }
        return listeProprietaire;
    }

    /**
     * Affiche dans la console les historiques de publications du média, de mentions du média dans des publications et
     * l'historique de rachat des parts du média.
     */
    public void suivi() {
        if (!historiquePublications.isEmpty()) {
            System.out.println("Publications : ");
            historiquePublications.forEach(System.out::println);
        } else {
            System.out.println("Aucune publication de ce média");
        }

        if (!historiqueMentions.isEmpty()) {
            System.out.println("Mentions : ");
            historiqueMentions.forEach(System.out::println);
        } else {
            System.out.println("Aucune mention de ce média dans une autre publication");
        }

        if(!historiqueRachat.isEmpty()) {
            System.out.println("Évolution des parts : ");
            historiqueRachat.forEach(System.out::println);
            System.out.println("Propriétaires actuels: " + getListeProprietaires());
        } else {
            System.out.println("Il n'y a eu aucune évolution des parts");
            System.out.println("Propriétaires actuels: " + getListeProprietaires());
        }
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
