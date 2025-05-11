package VigieMedias;
import java.util.*;

public class Vigie {
    // Dictionnaire de commandes
    private static HashMap<String, Command> commands = new HashMap<>();
    public Vigie(){}

    // Interface pour les commandes
    interface Command {
        void execute(String[] args);
    }

    /**
     * Fait tourner le terminal dans la console
     */
    public static void terminal() {
        // Ajouter des commandes
        // Commande "lister" pour afficher la liste des entités du type sélectionné
        commands.put("lister", (cmdArgs) -> {
            if (cmdArgs.length == 0) {
                System.out.println("Usage : lister [personnes|organisations|medias] [ (ordre alphabétique)| --medias (nombre décroissant de médias possédés]");
                return;
            }

            String type = cmdArgs[0];
            String option = cmdArgs.length > 1 ? cmdArgs[1] : "--alpha";

            switch (type) {
                case "personnes":
                    List<Personne> listep = Personne.listePersonnes;
                    if (option.equals("--medias")) {
                        listep.sort(Comparator.comparingInt(Personne::getNombreMedia).reversed());
                    } else {
                        listep.sort(Comparator.comparing(p -> p.name));
                    }
                    listep.forEach(p -> System.out.println(" - " + p + " - " + p.getNombreMedia() +
                            " media(s) possédé(s)"));
                    break;
                case "organisations":
                    List<Organisation> listeo = Organisation.listeOrganisations;
                    if (option.equals("--medias")) {
                        listeo.sort(Comparator.comparingInt(Organisation::getNombreMedia).reversed());
                    } else {
                        listeo.sort(Comparator.comparing(o -> o.name));
                    }
                    listeo.forEach(o -> System.out.println(" - " + o));
                    break;

                case "medias":
                    List<Media> medias = Media.listeMedias;
                    medias.sort(Comparator.comparing(m -> m.name));
                    medias.forEach(m -> System.out.println(" - " + m));
                    break;

                default:
                    System.out.println("Type inconnu : " + type);
                }
        });

        // Commande "participations" pour obtenir la liste des participations d'une personne dans les médias
        commands.put("participations", (cmdArgs) -> {
            if (cmdArgs.length == 0) {
                System.out.println("Usage : participations [nom de la personne]");
                return;
            }
            StringBuilder personne = new StringBuilder();
            for (String arg : cmdArgs){
                personne.append(arg).append(" ");
            }
            personne.setLength(personne.length() - 1);
            if (Personne.mapPersonnes.containsKey(personne.toString())){
                Personne.mapPersonnes.get(personne.toString()).getListeMedia().forEach(m ->
                        System.out.println(" - " + m));
            } else {
                System.out.println("Personne inconnue : " + personne);
            }

        });

        // Commande "actionnaires" pour obtenir la liste des actionnaires d'un média ou d'une organisation
        commands.put("actionnaires", (cmdArgs) -> {
            if (cmdArgs.length == 0) {
                System.out.println("Usage : actionnaires [organisation|media]");
                return;
            }
            String type = cmdArgs[0];
            switch (type){
                case "média":
                    Vigie.show_actionnaires_media();
                    break;
                case "organisation":
                    Vigie.show_actionnaires_organisation();
                    break;
                default:
                    System.out.println("Type inconnu : " + type);
            }
        });

        // Commande "historique" pour obtenir l'évolution des rachats de parts d'un média/organisation'
        commands.put("historique", (cmdArgs) -> {
            if (cmdArgs.length == 0) {
                System.out.println("Usage : historique [organisation|media]");
                return;
            }
            String type = cmdArgs[0];
            switch (type){
                case "média":
                    Vigie.show_historique_media();
                    break;
                case "organisation":
                    Vigie.show_historique_organisation();
                    break;
                default:
                    System.out.println("Type inconnu : " + type);
            }
        });

        // Commande "suivre" pour observer les médias et personnes
        commands.put("suivre", (cmdArgs) -> {
            // Si l'utilisateur ne donne pas d'argument
            if (cmdArgs.length == 0) {
                System.out.println("Usage : suivre nom de la personne|nom du média");
                return;
            }
            // On concatène le nom donné en paramètre qui est découpé par le terminal
            StringBuilder entree = new StringBuilder();
            for (String arg : cmdArgs){
                entree.append(arg).append(" ");
            }
            entree.setLength(entree.length() - 1);

            // Si ce nom est un nom de média
            if (Media.mapMedias.containsKey(entree.toString())) {
                Media media = Media.mapMedias.get(entree.toString());
                media.suivi();
            // Sinon si ce nom est un nom de personne
            }else if (Personne.mapPersonnes.containsKey(entree.toString())) {
                Personne personne = Personne.mapPersonnes.get(entree.toString());
                personne.suivi();
            } else {
                System.out.println(entree + " n'est pas un média ou une personne");
            }
        });

        // Commande "rachat" pour acheter un média/organisation
        commands.put("rachat", (cmdArgs) -> {
            System.out.println("Module de rachat de parts\n");
            Vigie.module_rachat();
        });

        // Commande "publier" pour publier un article, interview, reportage
        commands.put("publier", (cmdArgs) -> {
            if (cmdArgs.length == 0) {
                System.out.println("Usage : publier [article|interview|reportage]");
                return;
            }
            String type = cmdArgs[0];
            if(type.equals("article")|| type.equals("interview") || type.equals("reportage")) {
                Vigie.module_publication(type);
            } else {
                System.out.println("Type de publication inconnu : " + type);
            }
        });
        // Commande "aide"
        commands.put("aide", (cmdArgs) -> {
            System.out.println("Commandes disponibles :");
            for (String cmd : commands.keySet()) {
                System.out.println(" - " + cmd);
            }
        });
        // Commande "quitter"
        commands.put("quitter", (cmdArgs) -> {
            System.out.println("Fermeture de la console.");
            System.exit(0);
        });

        // Boucle principale de la console
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            // On découpe les différents éléments de l'input
            String[] parts = input.split(" ");
            String commandName = parts[0];
            String[] commandArgs = new String[parts.length - 1];
            System.arraycopy(parts, 1, commandArgs, 0, commandArgs.length);
            // On appelle la commande donnée par l'utilisateur
            Command command = commands.get(commandName);
            if (command != null) {
                command.execute(commandArgs);
            } else {
                System.out.println("Commande inconnue : " + commandName);
            }
        }
    }

    /**
     * Fonction d'affichage de l'historique des rachats de parts d'un média
     */
    public static void show_historique_media(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nom du média > ");
        String input = scanner.nextLine().trim();
        if (Media.mapMedias.containsKey(input)){
            Media.mapMedias.get(input).historiqueRachat.forEach(p -> System.out.println(" - " + p));
        } else {
            System.out.println("Média inconnu : " + input);
        }
    }

    /**
     *  Fonction d'affichage de l'historique des rachats de parts d'une organisation
     */
    public static void show_historique_organisation(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nom de l'organisation > ");
        String input = scanner.nextLine().trim();
        if (Organisation.mapOrganisations.containsKey(input)){
            Organisation.mapOrganisations.get(input).historiqueRachat.forEach(p ->
                    System.out.println(" - " + p));
        } else {
            System.out.println("Organisation inconnue : " + input);
        }
    }

    /**
     * Fonction d'affichage des personnes actionnaires d'un média
     */
    public static void show_actionnaires_media(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nom du média > ");
        String input = scanner.nextLine().trim();
        if (Media.mapMedias.containsKey(input)){
            Media.mapMedias.get(input).getListeProprietaires().forEach(p ->
                    System.out.println(" - " + p));
        } else {
            System.out.println("Média inconnu : "+input);
        }
    }

    /**
     * Fonction d'affichage des personnes actionnaires d'une organisation
     */
    public static void show_actionnaires_organisation(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nom de l'organisation > ");
        String input = scanner.nextLine().trim();
        if (Organisation.mapOrganisations.containsKey(input)){
            Organisation.mapOrganisations.get(input).getListeProprietaires(1.0f).forEach(p ->
                    System.out.println(" - " + p));
        } else {
            System.out.println("Organisation inconnue : " + input);
        }
    }

    /**
     * Demande à l'utilisateur les paramètres d'une publication.
     * Puis crée l'objet Publication et l'ajoute à la liste de publications du média responsable et à la liste des
     * mentions des entités mentionnées
     * @param type Type de publication (article, interview, reportage)
     */
    public static void module_publication(String type) {
        Date currentDate = new Date();
        ArrayList<String> typeMediasPossibles = new ArrayList<>();
        boolean mediaResponsableCorrect = false;
        boolean ajouterMentions = true;
        Media mediaResponsable = null;
        ArrayList<Media> mediasMentionnes = new ArrayList<>();
        ArrayList<Organisation> organisationsMentionnees = new ArrayList<>();
        ArrayList<Personne> personnesMentionnees = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        if (type.equals("article")){
            typeMediasPossibles.add("Presse (généraliste  politique  économique)");
            System.out.println("Quel média publie cet " + type + " ?");
        } else if (type.equals("interview")) {
            typeMediasPossibles.add("Presse (généraliste  politique  économique)");
            typeMediasPossibles.add("Télévision");
            typeMediasPossibles.add("Radio");
            System.out.println("Quel média publie cette " + type + " ?");
        } else {
            typeMediasPossibles.add("Télévision");
            typeMediasPossibles.add("Radio");
            System.out.println("Quel média publie ce " + type + " ?");
        }

        // Boucle de selection du média qui publie
        while (!mediaResponsableCorrect){
            System.out.print("Nom du média > ");
            String input = scanner.nextLine().trim();
            // Soit l'utilisateur n'entre rien
            if (input.isEmpty()) continue;
            // Soit il abandonne
            if (input.equals("abandon")){
                System.out.println("Abandon de la publication");
                return;
            // Soit il entre un nom de média qui existe
            } else if (Media.mapMedias.containsKey(input)) {
                mediaResponsable = Media.mapMedias.get(input);
                // Soit ce média est du bon type
                if (typeMediasPossibles.contains(mediaResponsable.type)) {
                    mediaResponsableCorrect = true;
                // Soit il n'est pas du bon type
                } else {
                    mediaResponsable = null;
                    System.out.println("Ce média ne peut pas publier : " + type);
                }
            // Soit il entre un nom de média inexistant
            } else {
                System.out.println("Média inconnu : " + input);
            }
        }

        // Boucle de selection des personnes/organisations/médias mentionnés
        while(ajouterMentions) {
            System.out.println("Qui est mentionné dans cette publication ?");
            System.out.print("Mention > ");
            String input = scanner.nextLine().trim();
            // Si l'utilisateur a entré le nom d'un média
            if (Media.mapMedias.containsKey(input)) {
                mediasMentionnes.add(Media.mapMedias.get(input));
            // Sinon si l'utilisateur a entré le nom d'une organisation
            } else if (Organisation.mapOrganisations.containsKey(input)) {
                organisationsMentionnees.add(Organisation.mapOrganisations.get(input));
            // Sinon si l'utilisateur a entré le nom d'une personne
            } else if (Personne.mapPersonnes.containsKey(input)) {
                personnesMentionnees.add(Personne.mapPersonnes.get(input));
            } else {
                System.out.println(input + " n'est pas un média ou une organisation ou une personne");
            }

            // On demande à l'utilisateur s'il veut ajouter d'autres mentions
            System.out.print("Ajouter une autre mention ? (Y/N) > ");
            input = scanner.nextLine().trim();
            if (input.equals("N")||input.equals("n")||input.equals("no")) {
                ajouterMentions = false;
            } else {
                if (mediasMentionnes.isEmpty() && organisationsMentionnees.isEmpty() && personnesMentionnees.isEmpty()) {
                    System.out.println("Votre publication ne mentionne rien");
                }
            }
        }

        // Création et diffusion de la publication
        Publication publication = new Publication(type, mediaResponsable, mediasMentionnes,
                organisationsMentionnees, personnesMentionnees, currentDate);
        // On ajoute la publication à la liste des publications du média
        mediaResponsable.historiquePublications.add(publication);
        // On ajoute la publication à la liste des mentions des entités mentionnées
        mediasMentionnes.forEach(m -> m.historiqueMentions.add(publication));
        organisationsMentionnees.forEach(o -> o.historiqueMentions.add(publication));
        for(Personne p : personnesMentionnees) {
            p.historiqueMentions.add(publication);
            // Si la personne mentionnée possède des parts du média
            if (Vigie.conflit_interet(mediaResponsable, p)){
                System.out.println("ALERTE VIGIE : Publication d'un article de " + mediaResponsable +
                        " au sujet de son actionnaire : " + p);
            }
        }
    }

    /**
     * Demande à l'utilisateur toutes les données relatives au rachat pour appeler operation_rachat
     */
    public static void module_rachat() {
        Personne personne = null;
        Media media;
        Organisation organisation;

        boolean quitter_rachat = false;
        boolean personne_correcte = false;
        boolean question2_answered = false;
        boolean racheter_media = false;
        boolean question3_answered = false;
        Scanner scanner = new Scanner(System.in);

        while (!quitter_rachat && !personne_correcte){
            System.out.println("Qui êtes-vous ?");
            System.out.print("Nom de la personne > ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;
            if (input.equals("abandon")){
                quitter_rachat = true;
                System.out.println("Abandon du rachat");
            }
            else if (Personne.mapPersonnes.containsKey(input)) {
                personne_correcte = true;
                personne = Personne.mapPersonnes.get(input);
            }
            else {
                System.out.println("Personne inconnue : " + input);
            }
        }

        if (quitter_rachat) {return;}

        while(!question2_answered){
            System.out.println("Que souhaitez-vous racheter ? 1 - média, 2 - organisation");
            System.out.print("Choix > ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;
            if (input.equals("1") || input.equals("média")) {
                racheter_media = true;
                question2_answered = true;
            } else if (input.equals("2") || input.equals("organisation")) {
                question2_answered = true;
            }
            else {
                System.out.println("Réponse incorrecte : " + input);
            }
        }

        if (quitter_rachat) {return;}

        while(!quitter_rachat && !question3_answered) {
            if (racheter_media) {
                System.out.println("Quel média souhaitez-vous racheter ?");
                System.out.print("Média >> ");
            }
            else {
                System.out.println("Quelle organisation souhaitez-vous racheter ?");
                System.out.print("Organisation >> ");
            }

            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;
            if (input.equals("abandon")){
                quitter_rachat = true;
                System.out.println("Abandon du rachat");
            } else if(racheter_media) {
                if (Media.mapMedias.containsKey(input)){
                    question3_answered = true;
                    media = Media.mapMedias.get(input);
                    System.out.println("Voici la liste des actionnaires de " + media + " : " + media.organisationsProprietaires + media.personnesProprietaires);
                    operation_rachat(personne, media, null);
                }
                else {
                    System.out.println("Media inconnu : " + input);
                }
            } else {
                if (Organisation.mapOrganisations.containsKey(input)){
                    question3_answered = true;
                    organisation = Organisation.mapOrganisations.get(input);
                    System.out.println("Voici la liste des actionnaires de " + organisation + " : " + organisation.organisationsProprietaires + organisation.personnesProprietaires);
                    operation_rachat(personne, null, organisation);
                }
                else {
                    System.out.println("Organisation inconnue : " + input);
                }
            }
        }
    }

    /**
     * Procédure de rachat des parts de l'entreprise ou organisation donnée en paramètre
     * @param acheteur
     * @param mediaAchete
     * @param organisationsAchetee
     */
    private static void operation_rachat(Personne acheteur, Media mediaAchete, Organisation organisationsAchetee) {
        boolean rachat_possible = true;
        boolean vendeur_valide = false;
        boolean pourcentage_valide = false;
        float pourcentage = 0.0F;

        ArrayList<PersonneProprietaire> listeActionnaires = null;
        ArrayList<PersonneProprietaire> personnesProprietaires = null;
        ArrayList<OrganisationProprietaire> organisationsProprietaires = null;
        PersonneProprietaire vendeurP = null;
        OrganisationProprietaire vendeurO = null;
        Scanner scanner = new Scanner(System.in);

        // Soit on achète un média
        if (mediaAchete != null) {
            personnesProprietaires = mediaAchete.personnesProprietaires;
            organisationsProprietaires = mediaAchete.organisationsProprietaires;
            listeActionnaires = mediaAchete.getListeProprietaires();
        }
        // Soit on achète une organisation
        else {
            personnesProprietaires = organisationsAchetee.personnesProprietaires;
            organisationsProprietaires = organisationsAchetee.organisationsProprietaires;
            listeActionnaires = organisationsAchetee.getListeProprietaires(1.0f);
        }

        if (listeActionnaires.size() == 1 && listeActionnaires.getFirst().personne == acheteur) {
            // Si l'unique actionnaire du média / organisation est l'acheteur, alors le rachat est impossible
            System.out.println("Vous êtes l'unique détenteur des parts, rachat impossible");
            rachat_possible = false;
        }

        // Boucle d'achat de parts du média
        while(rachat_possible) {
            System.out.println("Vous pouvez racheter les parts à : ");
            if (!personnesProprietaires.isEmpty()) {
                for (PersonneProprietaire proprietaire : personnesProprietaires) {
                    if (proprietaire.personne != acheteur) {
                        System.out.println(proprietaire.personne + " (" + proprietaire.pourcentage + " % des parts)");
                    }
                }
            }
            if (!organisationsProprietaires.isEmpty()) {
                for (OrganisationProprietaire proprietaire : organisationsProprietaires) {
                    System.out.println(proprietaire.organisation + " (" + proprietaire.pourcentage + " % des parts)");
                }
            }

            // Boucle de selection du vendeur
            while(!vendeur_valide) {
                System.out.println("À qui allez-vous racheter les parts ?");
                System.out.print("Propriétaire >> ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) continue;
                if (input.equals("abandon")) {
                    System.out.println("Abandon du rachat");
                    return;
                } else {
                    // On vérifie si le vendeur selectionné est une personne existante
                    if (!personnesProprietaires.isEmpty()) {
                        for (PersonneProprietaire proprietaire : personnesProprietaires) {
                            if (proprietaire.personne.name.equals(input)) {
                                vendeur_valide = true;
                                vendeurP = proprietaire;
                            }
                        }
                    }
                    // On vérifie si le vendeur selectionné est une organisation existante
                    if (!organisationsProprietaires.isEmpty()) {
                        for (OrganisationProprietaire proprietaire : organisationsProprietaires) {
                            if (proprietaire.organisation.name.equals(input)) {
                                vendeur_valide = true;
                                vendeurO = proprietaire;
                            }
                        }
                    }
                    if (!vendeur_valide) {
                        System.out.println("Vendeur inconnu : " + input);
                    }
                }
            }

            // On calcule le pourcentage maximal de parts pouvant être achetées
            float pourcentage_max;
            if (vendeurP != null) {
                pourcentage_max = vendeurP.pourcentage;
            }
            else {
                pourcentage_max = vendeurO.pourcentage;
            }

            // Boucle de selection du pourcentage
            while (!pourcentage_valide) {
                System.out.println("Quel pourcentage de parts souhaitez-vous acquérir ? (entre 0.0 et " + pourcentage_max + "%)");
                System.out.print("Pourcentage >> ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) continue;
                if (input.equals("abandon")) {
                    System.out.println("Abandon du rachat");
                    return;
                } else {
                    try {
                        pourcentage = Float.parseFloat(input);
                        if (pourcentage >= 0.0 && pourcentage <= pourcentage_max) {
                            pourcentage_valide = true;
                        } else {
                            System.out.println("Pourcentage incorrect : " + pourcentage);
                        }

                        // Cas où l'utilisateur n'a pas entré un float
                    } catch (NumberFormatException e) {
                        System.out.println("Ce n'est pas un pourcentage : " + input);
                    }
                }
            }

            // On achète un média
            if(mediaAchete != null) {
                // On modifie la participation de l'acheteur dans le média acheté
                Vigie.modifier_participation(acheteur, mediaAchete, pourcentage);

                // Alertes de la vigie
                EvolutionParts achat = new EvolutionParts(true, acheteur, pourcentage);
                EvolutionParts vente = null;

                // Si on achète à une personne
                if (vendeurP != null) {
                    // On modifie la participation du vendeur dans le média vendu
                    Vigie.modifier_participation(vendeurP.personne, mediaAchete, vendeurP.pourcentage-pourcentage);
                    vente = new EvolutionParts(false, vendeurP.personne, pourcentage);
                // Si on achète à une organisation
                } else {
                    // On modifie la participation du vendeur dans le média vendu
                    Vigie.modifier_participation(vendeurO.organisation, mediaAchete, vendeurO.pourcentage-pourcentage);
                    vente = new EvolutionParts(false, vendeurO.organisation, pourcentage);
                }
                // On ajoute la transaction à l'historique des transactions du média
                mediaAchete.historiqueRachat.add(vente);
                mediaAchete.historiqueRachat.add(achat);
                System.out.println("Rachat effectué : vous venez d'obtenir "+ pourcentage +"% des parts de "+ mediaAchete.name + "\n");
                // On met à jour la liste des actionnaires du média
                listeActionnaires = mediaAchete.getListeProprietaires();
            // On achète une organisation
            } else {
                // On modifie la participation de l'acheteur dans le média acheté
                Vigie.modifier_participation(acheteur, organisationsAchetee, pourcentage);

                // Alertes de la vigie
                EvolutionParts achat = new EvolutionParts(true, acheteur, pourcentage);
                EvolutionParts vente = null;

                // Si on achète à une personne
                if (vendeurP != null) {
                    // On modifie la participation du vendeur dans le média acheté
                    Vigie.modifier_participation(vendeurP.personne, organisationsAchetee, vendeurP.pourcentage-pourcentage);
                    vente = new EvolutionParts(false, vendeurP.personne, pourcentage);
                // On achète à une organisation
                } else {
                    // On modifie la participation du vendeur dans le média acheté
                    Vigie.modifier_participation(vendeurO.organisation, organisationsAchetee, vendeurO.pourcentage-pourcentage);
                    vente = new EvolutionParts(false, vendeurO.organisation, pourcentage);
                }
                // On ajoute la transaction à l'historique des transactions de l'organisation
                organisationsAchetee.historiqueRachat.add(vente);
                organisationsAchetee.historiqueRachat.add(achat);
                System.out.println("Rachat effectué : vous venez d'obtenir "+ pourcentage +"% des parts de "+ organisationsAchetee.name + "\n");
                // On met à jour la liste des actionnaires de l'organisation achetée
                listeActionnaires = organisationsAchetee.getListeProprietaires(1.0f);
            }

            if (listeActionnaires.size() == 1 && listeActionnaires.getFirst().personne == acheteur) {
                // Si l'unique actionnaire du média / organisation est l'acheteur, alors un autre rachat est impossible
                System.out.println("Vous êtes désormais l'unique détenteur des parts, fermeture du module de rachat");
                rachat_possible = false;
            }

            // On remet tout à zéro pour éventuellement racheter d'autres parts de ce média
            vendeurP = null;
            vendeurO = null;
            vendeur_valide = false;
            pourcentage_valide = false;
        }
    }

    /**
     * Crée l'objet participation et l'ajoute à la liste de la personne et de l'organisation
     * @param personne acheteur de l'organisation
     * @param organisation organisation rachetée
     * @param pourcentage pourcentage des parts
     */
    public static void creer_participation(Personne personne, Organisation organisation, float pourcentage) {
        personne.organisationsPossedees.add(new ParticipationOrganisation(organisation, pourcentage));
        organisation.personnesProprietaires.add(new PersonneProprietaire(personne, pourcentage));
    }

    /**
     * Crée l'objet participation et l'ajoute à la liste de la personne et du média
     * @param personne acheteur du média
     * @param media média racheté
     * @param pourcentage pourcentage des parts
     */
    public static void creer_participation(Personne personne, Media media, float pourcentage) {
        personne.mediaPossedes.add(new ParticipationMedia(media, pourcentage));
        media.personnesProprietaires.add(new PersonneProprietaire(personne, pourcentage));
    }

    /**
     * Cette fonction modifie les objets Participation de la personne qui possède l'organisation et de
     * l'organisation possédée
     * @param personne possesseur de l'organisation
     * @param organisation organisation possédée
     * @param pourcentage parts possédées
     */
    public static void modifier_participation(Personne personne, Organisation organisation, float pourcentage) {
        ParticipationOrganisation participationOrganisation = null;
        PersonneProprietaire participationPersonne = null;

        // Si l'acheteur avait déjà une participation dans le média
        for (ParticipationOrganisation participation : personne.organisationsPossedees) {
            if (participation.organisation == organisation) {
                participationOrganisation = participation;
            }
        }
        for (PersonneProprietaire participation : organisation.personnesProprietaires) {
            if (participation.personne == personne) {
                participationPersonne = participation;
            }
        }

        // Si l'acheteur n'avait pas une participation dans le média
        if (participationPersonne == null && participationOrganisation == null) {
            creer_participation(personne, organisation, pourcentage);
            System.out.println("ALERTE VIGIE : " + personne + " entre au capital de " + organisation);
            return;
        }

        // Si le vendeur ne vend pas l'intégralité de ses parts
        if (pourcentage != 0.0) {
            participationOrganisation.pourcentage = pourcentage;
            participationPersonne.pourcentage = pourcentage;
        }
        // Sinon on supprime le lien entre l'organisation et le média
        else {
            organisation.personnesProprietaires.remove(participationPersonne);
            personne.organisationsPossedees.remove(participationOrganisation);
        }
    }

    public static void modifier_participation(Organisation organisationActionnaire, Organisation organisationPossedee, float pourcentage) {
        ParticipationOrganisation participationOrganisation = null;
        OrganisationProprietaire proprieteOrganisation = null;

        for (ParticipationOrganisation participation : organisationActionnaire.organisationsPossedees) {
            if (participation.organisation == organisationPossedee) {
                participationOrganisation = participation;
            }
        }
        for (OrganisationProprietaire participation : organisationPossedee.organisationsProprietaires) {
            if (participation.organisation == organisationActionnaire) {
                proprieteOrganisation = participation;
            }
        }

        // Si le vendeur ne vend pas l'intégralité de ses parts
        if (pourcentage != 0.0) {
            participationOrganisation.pourcentage = pourcentage;
            proprieteOrganisation.pourcentage = pourcentage;
        }
        // Sinon on supprime le lien entre l'organisation et le média
        else {
            organisationPossedee.organisationsProprietaires.remove(proprieteOrganisation);
            organisationActionnaire.organisationsPossedees.remove(participationOrganisation);
        }
    }

    public static void modifier_participation(Personne personne, Media media, float pourcentage) {
        ParticipationMedia participationMedia = null;
        PersonneProprietaire participationPersonne = null;

        for (ParticipationMedia participation : personne.mediaPossedes) {
            if (participation.media == media) {
                participationMedia = participation;
            }
        }
        for (PersonneProprietaire participation : media.personnesProprietaires) {
            if (participation.personne == personne) {
                participationPersonne = participation;
            }
        }

        // Si l'acheteur n'avait pas une participation dans le média
        if (participationPersonne == null && participationMedia == null) {
            creer_participation(personne, media, pourcentage);
            System.out.println("ALERTE VIGIE : " + personne + " entre au capital de " + media);
            return;
        }

        // On modifie simplement le pourcentage
        if (pourcentage != 0.0) {
            participationMedia.pourcentage = pourcentage;
            participationPersonne.pourcentage = pourcentage;
        }
        // On supprime le lien entre l'organisation et le média
        else {
            media.personnesProprietaires.remove(participationPersonne);
            personne.mediaPossedes.remove(participationMedia);
        }
    }

    public static void modifier_participation(Organisation organisation, Media media, float pourcentage) {
        ParticipationMedia participationMedia = null;
        OrganisationProprietaire participationOrganisation = null;

        for (ParticipationMedia participation : organisation.mediasPossedes) {
            if (participation.media == media) {
                participationMedia = participation;
            }
        }
        for (OrganisationProprietaire participation : media.organisationsProprietaires) {
            if (participation.organisation == organisation) {
                participationOrganisation = participation;
            }
        }

        // On modifie simplement le pourcentage
        if (pourcentage != 0.0) {
            participationMedia.pourcentage = pourcentage;
            participationOrganisation.pourcentage = pourcentage;
        }
        // On supprime le lien entre l'organisation et le média
        else {
            media.organisationsProprietaires.remove(participationOrganisation);
            organisation.mediasPossedes.remove(participationMedia);
        }
    }

    /**
     *
     * @param Media média responsable de la publication
     * @param Personne personne mentionée
     * @return true si la personne possède une part du média, false sinon
     */
    public static boolean conflit_interet(Media Media, Personne Personne) {
        ArrayList<ParticipationMedia> mediasPossedes = Personne.getListeMedia();
        for (ParticipationMedia participation : mediasPossedes) {
            if (participation.media == Media) {
                return true;
            }
        }
        return false;
    }


    public static void main(String [] args){
        TsvReader.init();
        Vigie.terminal();
    }
}
