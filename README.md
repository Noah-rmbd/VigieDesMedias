# Projet Vigie des Médias

L’objectif de ce projet est de modéliser les liens entre les groupes médiatiques français et leurs actionnaires.
L’utilisateur peut en quelques commandes visualiser les données de ce document mais également simuler des événements tels qu’un rachat de parts d’un média ou organisation ainsi que la publication d'articles. Ces simulations d'événements sont enregistrées dans le programme et sont soumises à des alertes.
Source des données : https://github.com/mdiplo/Medias_francais

## 📦 Contenu du projet

- Lecture de fichiers TSV depuis un dossier `/data`
- Création d’objets `Personne`, `Organisation`, `Media`, et gestion de leurs relations
- Pas de dépendance externe (utilise uniquement les bibliothèques standard Java)

## 🛠️ Environnement de développement

- **Java** : version 23
- **IDE** : IntelliJ IDEA
- **Système** : macOS 15.4
- **Dépendances** : Aucune (Java standard uniquement)


## ▶️ Exécution

Assurez-vous d’avoir Java 23 installé sur votre machine.

### Lancer le programme :
```bash
java -jar /chemin/vers/le/projet/TSVReaderProject/out/TSVReaderProject.jar
