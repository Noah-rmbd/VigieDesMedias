package VigieMedias;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Personne {
    static public List<Personne> listePersonnes = new ArrayList<>();
    static public HashMap<String, Personne> mapPersonnes = new HashMap<>();

    protected String name;
    protected ArrayList<ParticipationMedia> mediaPossedes = new ArrayList<>();
    protected ArrayList<ParticipationOrganisation> organisationsPossedees = new ArrayList<>();
    protected ArrayList<Publication> historiqueMentions = new ArrayList<>();

    public Personne(String completeName){
        name = completeName;
        listePersonnes.add(this);
        mapPersonnes.put(completeName, this);
    }

    /**
     * Renvoit le nombre de médias dans laquelle la personne possède des parts
     * @return count
     */
    public int getNombreMedia() {
        int count = 0;
        for (ParticipationOrganisation participation : organisationsPossedees) {
            count += participation.organisation.getNombreMedia();
        }
        count += mediaPossedes.size();
        return count;
    }

    /**
     * Renvoit la liste des médias dans laquelle la personne possède des parts
     * @return listeMedia
     */
    public ArrayList<ParticipationMedia> getListeMedia() {
        ArrayList<ParticipationMedia> listeMedia = new ArrayList<>(mediaPossedes);
        for (ParticipationOrganisation participation : organisationsPossedees) {
            listeMedia.addAll(participation.organisation.getListeMedia(participation.pourcentage/100.0f));
        }
        return listeMedia;
    }

    /**
     * Affiche la liste des publications mentionnant la personne
     */
    public void suivi() {
        if (!historiqueMentions.isEmpty()) {
            System.out.println("Mentions : ");
            historiqueMentions.forEach(System.out::println);
        } else {
            System.out.println("Aucune mention");
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
