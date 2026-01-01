
📖 Description du Projet
FLCS Gestion est un système complet de gestion pédagogique développé pour le Centre FLCS qui accompagne des candidats souhaitant partir en Allemagne pour leurs études, formation professionnelle ou travail.

FLCS - Backend Pédagogique / Gestion des Notes
📌 Contexte
Ce module fait partie du système de gestion FLCS (Formation Linguistique et Accompagnement vers l'Allemagne). Il gère les aspects pédagogiques du logiciel, notamment la gestion des évaluations hebdomadaires, des examens finaux, et la génération des bulletins de notes.

🎯 Objectifs
Centraliser la gestion des évaluations pédagogiques

Automatiser le calcul des moyennes selon les règles métier

Générer des bulletins de notes en PDF

Fournir des statistiques de réussite par niveau

📋 Fonctionnalités implémentées
1. Modélisation de la Base de Données
EvaluationHebdomadaire : Stocke les 5 notes hebdomadaires (Lesen, Hören, Schreiben, Sprechen, Grammatik)

Endprüfung : Stocke les notes de l'examen final avec calcul automatique de la moyenne

2. API CRUD Rentrée/Niveau
POST /api/rentrees : Création d'une nouvelle rentrée

Fonctionnalité : Génération automatique des niveaux A1, A2, B1, B2 à la création

3. API Saisie des Notes Hebdomadaires
POST /api/evaluations-hebdo : Enregistrement des 5 notes hebdomadaires

Précision : Pas de calcul de moyenne immédiat (selon spécifications)

4. API Génération PDF
GET /api/bulletins/{eleveId}/{niveauId}/pdf : Génère et télécharge le bulletin de notes en PDF

Format : PDF structuré avec tableau des notes et moyenne finale

Calcul : Moyenne adaptée selon le niveau (A1/A2 vs B1/B2)

5. API Statistiques Pédagogiques
GET /api/statistiques/reussite-par-niveau : Retourne le pourcentage de réussite/échec par niveau

Métrique : Taux de réussite basé sur un seuil de 10/20

🏗️ Architecture Technique
Technologies utilisées
Langage : Java 17

Framework : Spring Boot 3.x

Base de données : PostgreSQL

ORM : JPA/Hibernate

Génération PDF : iText 7

Authentification : Spring Security (à intégrer)

Structure des packages

    FLCS.GESTION/
    ├── controller/
    │   ├── RentreeController.java
    │   ├── EvaluationHebdoController.java
    │   ├── BulletinController.java
    │   └── StatistiqueController.java
    ├── entity/
    │   ├── EvaluationHebdomadaire.java
    │   └── Endprüfung.java
    ├── service/
    │   ├── RentreeService.java
    │   ├── EvaluationHebdoService.java
    │   └── EndprüfungService.java
    ├── repository/
    │   ├── EvaluationHebdoRepository.java
    │   └── EndprüfungRepository.java
    └── dto/
        └── EvaluationHebdomadaireDto.java

🔧 Règles Métier Implémentées
1. Gestion des niveaux
Deux types de niveaux : "rentrée" (A1 à B2) et "indépendant" (C1, etc.)

Les niveaux A1, A2, B1, B2 sont générés automatiquement lors de la création d'une rentrée

2. Calcul des moyennes
Pour A1 et A2 :

text
Moyenne finale = 40% moyenne hebdomadaire + 60% Endprüfung
Pour B1 et B2 :

text
Moyenne finale = 100% Endprüfung
Endprüfung : Moyenne automatique des 5 notes

3. Évaluations hebdomadaires
5 notes à saisir chaque semaine



📊 Points d'API
Méthode	Endpoint	Description	Rôle requis
POST	/api/rentrees	Crée une rentrée avec ses niveaux	Admin
POST	/api/evaluations-hebdo	Enregistre notes hebdomadaires	Enseignant
GET	/api/bulletins/{eleveId}/{niveauId}/pdf	Génère bulletin PDF	Enseignant/Secrétaire
GET	/api/statistiques/reussite-par-niveau	Statistiques par niveau	Direction
