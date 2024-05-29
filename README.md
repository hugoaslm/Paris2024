# Projet Java JO

<p align="center">
  <img src="src/main/resources/fr/isep/algo/projetjo/img/logo_paris_2024.png" alt="Logo Paris 2024" width="200"/>
</p>

**<span style="color:#d7c378">Description :</span>**
Ce projet Java est conçu pour gérer les événements et les athlètes des Jeux Olympiques. Il comprend plusieurs fonctionnalités telles que l'ajout d'athlètes, la gestion des événements, l'inscription des athlètes aux événements et bien plus encore.

## Fonctionnalités

- **<span style="color:#d7c378">Gestion des athlètes :</span>**
  - Ajouter un athlète
  - Modifier les informations d'un athlète
  - Voir tous les athlètes

- **<span style="color:#d7c378">Gestion des événements :</span>**
  - Ajouter un événement
  - Voir les détails de l'événement
  - Inscrire des athlètes à un événement

- **<span style="color:#d7c378">Interface utilisateur :</span>**
  - Interface intuitive pour naviguer entre les différentes sections
  - Tableaux de bord pour les résumés des données

## Structure du projet

```plaintext
java/
└── fr/isep/algo/projetjo/
    ├── controller/
    │   ├── addAthleteController.java
    │   ├── allAthletesController.java
    │   └── ...
    ├── dao/
    │   ├── athleteDAO.java
    │   ├── eventDAO.java
    │   └── ...
    ├── model/
    │   ├── Athlete.java
    │   ├── Event.java
    │   └── ...
    ├── service/
    │   └── PDFService.java
    └── util/
        └── SessionManager.java
resources/
└── fr/isep/algo/projetjo/
    ├── img/
    │   ├── Basketball.png
    │   ├── cover_ath.png
    │   └── logo_gold_paris2024.png
    └── view/
        ├── addAthlete.fxml
        ├── allAthletes.fxml
        └── ...
 
## Utilisation

1. **Clonez le dépôt :**
   ```bash
   git clone https://github.com/votre-utilisateur/projet-java-jo.git
