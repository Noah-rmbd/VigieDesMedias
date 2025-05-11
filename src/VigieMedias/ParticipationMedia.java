package VigieMedias;

public class ParticipationMedia {
    public Media media;
    public float pourcentage;

    public ParticipationMedia(Media Media, float Pourcentage) {
        media = Media;
        pourcentage = Pourcentage;
    }

    @Override
    public String toString() {
        return media.toString() + " possédé à : " + String.valueOf(pourcentage) + "%";
    }
}
