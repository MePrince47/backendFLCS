# backendFLCS

# 🎓 GESTION_FLCS 

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![Security](https://img.shields.io/badge/Security-JWT-red)

## 📌 Description

**GESTION_FLCS** est une application backend développée avec **Spring Boot**, destinée à un centre de formation linguistique et d’accompagnement administratif pour des candidats souhaitant étudier, se former ou travailler en Allemagne.

L’application permet :
- la gestion des **élèves**
- la gestion des **rentrées scolaires**
- la gestion des **niveaux linguistiques**
- la gestion des **evaluationset des resultats**
- la gestion des **partenaires**
- la gestion des **paiements par virement bancaire**
- la gestion des **utilisateurs** avec rôles
- une **recherche avancée multi-critères**
- l’**export PDF des paiements**
- l’**export PDF des resultats d'un niveau**
- une **sécurité basée sur JWT**

Ce projet a été conçu avec des **bonnes pratiques professionnelles backend** (DTO, sécurité, séparation des couches, pagination, etc.).

---

## 🛠️ Technologies utilisées

- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA**
- **Spring Security + JWT**
- **PostgreSQL**
- **Hibernate**
- **Lombok**
- **Maven**
- **PDF (ReportLab / iText selon config)**
- **Swagger (OpenAPI)**

---

## 🏗️ Architecture du projet

```text
src/main/java/FLCS/GESTION
│
├── CONFIG          # Configuration (sécurité, initialisation)
├── CONTROLLER      # API REST (Endpoints)
├── DTO             # Objets de transfert (Request / Response)
├── ENTITEES        # Entités JPA
├── EXCEPTION       # Gestion centralisée des erreurs
├── EXPORT          # Génération PDF
├── REPOSITORY      # Accès base de données
├── SECURITY        # JWT, UserDetails, filtres
├── SERVICE         # Logique métier

```
# 🔐 Sécurité & rôles

## Authentification

- Basic Auth
- Utilisateurs stockés en base de données
- Mots de passe encodés avec BCrypt
  
 L’API sera sécurisée avec JWT.

## 👤 Rôles disponibles

- **ADMIN**  
- **SECRETAIRE**  
- **ENSEIGNANT**

# 👨‍🎓 Gestion des élèves
## 🔒 Accès aux fonctionnalités

| Fonctionnalité       | ADMIN | SECRETAIRE | ENSEIGNANT |
|---------------------|:-----:|:----------:|:----------:|
| Créer un élève       | ✅    | ✅         | ❌         |
| Modifier un élève    | ✅    | ✅         | ❌         |
| Supprimer un élève   | ✅    | ✅         | ❌         |
| Lire les élèves      | ✅    | ✅         | ✅         |
| Recherche avancée    | ✅    | ✅         | ✅         |

## 📦 Modèle métier (simplifié)

### Eleve
- nom, prénom, date de naissance  
- niveau scolaire  
- statut  
- partenaire  
- rentree  
- niveau linguistique
- type de procedure
- montant total

  #### Exemple JSON – Création élève
```json
{
  "nom": "MIKAM",
  "prenom": "Borel",
  "dateNaiss": "2012-05-14",
  "niveauScolaire": "LICENCE",
  "typeProcedure": "FORMATION",
  "montantTotal": 900000,
  "telCandidat": "690000000",
  "telParent": "677000000",
  "statut": "ACTIF",

  "nomPartenaire": "FLCS",
  "codeNiveau": "B2",
  "nomRentree": "SEPTEMBRE_2024"
}
```
## 🔍 Recherche avancée

La recherche avancée permet de filtrer les élèves sans obligation de fournir tous les paramètres.

**Paramètres possibles :**  
`nom`, `niveauScolaire`, `niveauLangue`, `rentree`, `partenaire`

**Exemple :**
GET /api/eleves/search?niveauLangue=B1&partenaire=FLCS

**Réponse standardisée :**
```json
{
  "count": 2,
  "message": "2 élément(s) trouvé(s)",
  "data": [
    {
      "id": 1,
      "nom": "MIKAM FOKOUA",
      "prenom": "Borel",
      "niveauLangue": "B1",
      "rentree": "SEPTEMBRE_2024",
      "partenaire": "FLCS"
    }
  ]
}
```
## 📄 Utilisation des DTO

- Les entités JPA **ne sont jamais exposées** directement.  
- Les réponses utilisent **EleveResponse**.  
- Les créations utilisent **EleveRequest**.  

**Résultat :** sécurité, stabilité de l’API et facilité pour le frontend.

---

# 🤝 Gestion des partenaires

### ✔ Fonctionnalités

- Création
- Liste
- Association aux élèves
- Un partenaire est identifié par un nom unique

  ---

  # 💳 Gestion des paiements (Virement bancaire)

  ### ✔ Règles métier implémentées

- Paiement uniquement par virement
-Référence de virement unique
- Impossible de payer :
   - plus que le reste à payer
   - si le solde est déjà réglé

### ✔ Fonctionnalités

- Enregistrement des paiements
- Historique par élève
- Calcul automatique :
- Total payé
- Reste à payer
- Résumé financier
- Export PDF

#### 📥 Exemple JSON – Paiement

```json
{
  "montant": 200000,
  "datePaiement": "2026-01-02",
  "referenceVirement": "VIR-2026-001",
  "eleveId": 1
}
```
### 📊 Résumé financier élève

Retourne :
- Montant total
- Total payé
- Reste à payer

### 📄 Export PDF des paiements

Génération d’un PDF récapitulatif

Contenu :
- Élève
- Montant total
- Total payé
- Reste à payer
- Historique des paiements
---
# 📝 Gestion des notes, résultats et progression académique

Ce module permet de gérer l’évaluation linguistique des élèves, la validation des niveaux
et le suivi de leur progression, conformément aux règles pédagogiques du centre FLCS.

---

## ✏️ Modifications possibles (logique PUT)

### Notes hebdomadaires
- Modifier les notes hebdomadaires d’un élève pour une semaine donnée
- Corriger une ou plusieurs compétences :
  - Lesen
  - Hören
  - Schreiben
  - Grammatik
  - Sprechen
- Recalcul automatique des moyennes après modification

### Examen final (Endprüfung)
- Modifier les notes finales d’un élève pour un niveau donné
- Recalcul automatique de la moyenne de l’examen
- Recalcul automatique de la moyenne finale du niveau

### Progression de l’élève
- Faire progresser un élève vers le niveau supérieur :
  - A1 → A2 → B1 → B2
- La progression est conditionnée à la clôture du niveau en cours
- Historisation automatique du parcours académique

### Clôture d’un niveau
- Clôturer un niveau pour un élève après validation des résultats
- La clôture :
  - fige les notes hebdomadaires et l’examen final
  - empêche toute modification ultérieure
  - valide définitivement la moyenne finale
- Action réservée à l’administration

---

## 👀 Consultations possibles (logique GET)

### Par élève
- Consulter toutes les notes hebdomadaires d’un élève
- Consulter le résultat de l’examen final
- Consulter la moyenne finale par niveau
- Consulter l’état du niveau (en cours / clôturé)
- Consulter l’historique académique complet

### Par niveau
- Consulter les notes hebdomadaires de tous les élèves d’un niveau
- Consulter les résultats finaux d’un niveau
- Consulter les moyennes globales par niveau
- Consulter l’état d’avancement du niveau

### Par rentrée
- Consulter la liste des élèves d’une rentrée
- Consulter les niveaux clôturés et en cours pour une rentrée
- Consulter les résultats globaux d’une rentrée
- Suivre la progression des élèves par niveau dans une rentrée

---

## 🔐 Accès par rôle (notes, clôture et progression)

| Fonctionnalité | ADMIN | SECRETAIRE | ENSEIGNANT |
|---------------|:----:|:----------:|:----------:|
| Modifier notes hebdomadaires | ❌ | ❌ | ✅ |
| Modifier examen final | ❌ | ❌ | ✅ |
| Clôturer un niveau | ✅ | ❌ | ❌ |
| Faire progresser un élève | ✅ | ✅ | ❌ |
| Consulter notes d’un élève | ✅ | ✅ | ✅ |
| Consulter notes par niveau | ✅ | ✅ | ✅ |
| Consulter résultats par niveau | ✅ | ✅ | ✅ |
| Consulter progression par rentrée | ✅ | ✅ | ✅ |

---

## 📌 Règles métier appliquées

- Un niveau clôturé est immuable (notes et résultats figés)
- La progression n’est possible qu’après clôture du niveau
- Une Endprüfung est unique par élève et par niveau
- Les moyennes finales sont calculées automatiquement
- Les enseignants n’ont aucun accès administratif
- Les secrétaires ne peuvent pas modifier les notes

## 🔄 Évolution des notions de Rentrée et Niveau

### Rentrée
La rentrée représente une cohorte académique.
Elle structure le parcours des élèves et sert de base pour la recherche,
la consultation des résultats et le suivi de la progression.

### Niveau
Le niveau est l’unité de validation pédagogique.
Il supporte les évaluations, les examens finaux, le calcul des moyennes
et la progression de l’élève.

Chaque niveau possède un état :
- EN_COURS
- CLOTURE

Un niveau clôturé est définitivement figé.

  
### Rentree
- nom (ex: `SEPTEMBRE_2024`)
- création automatique de niveaux  

### Niveau
- code (A1, A2, B1, B2…)
- barème ( 20 pour A et 100 pour  B et C )
- lié à une rentrée et à des notes
- création automatique des 7 semaines

## 🧪 Exemples de requêtes JSON

### ➕ Créer un partenaire
```json
{
  "nomPartenaire": "FLCS"
}
```
### ➕ Créer une rentrée
```json
{
  "nomRentree": "SEPTEMBRE_2024",
  "dateDebut": "2026-01-05"
}
```
## ⚙️ Installation & lancement

### Prérequis
- Java 17+  
- Maven  
- PostgreSQL  

### Configuration
Dans `application.yml` ou `application.properties` :

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gestion_flcs
spring.datasource.username=postgres
spring.datasource.password=ton mdp
```
### Lancer l’application
```bash
mvn clean spring-boot:run
```
## 📌 Bonnes pratiques appliquées

- Séparation Controller / Service / Repository  
- DTO pour les échanges API  
- Gestion centralisée des exceptions  
- Sécurité JWT  
- Transactions maîtrisées (`@Transactional`)  
- Code lisible et maintenable
- Tests unitaires éffectués 

---

### Documentation

- Swagger / OpenAPI :http://localhost:8080/swagger-ui.html


## 🚀 Évolutions possibles

- Pagination & tri avancés
- Finalisation de la sécurité JWT( pour l'instant Basic Auth) 
- Tests d’intégration  
- Statistiques et tableaux de bord  
- Déploiement Docker
