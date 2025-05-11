package VigieMedias;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Publication {
    Media media;
    Date date;
    String type;
    ArrayList<Media> mediasMentionnes;
    ArrayList<Organisation> organisationsMentionnees;
    ArrayList<Personne> personnesMentionnees;

    public Publication(String Type, Media Media, ArrayList<Media> MediasMentionnes, ArrayList<Organisation> OrganisationsMentionnees, ArrayList<Personne> PersonnesMentionnees, Date Date) {
        type = Type;
        media = Media;
        date = Date;
        mediasMentionnes = MediasMentionnes;
        organisationsMentionnees = OrganisationsMentionnees;
        personnesMentionnees = PersonnesMentionnees;
    }

    @Override
    public String toString() {
        // Format the date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "- Le " + formatter.format(date) + ",  " + type + " de " + media +  " qui mentionne : " + mediasMentionnes +
                organisationsMentionnees + personnesMentionnees;
    }

}
