# Cinéphile - Application de Gestion de Films

Application web complète de gestion de films avec React TypeScript (frontend) et Spring Boot (backend), intégrant l'API TMDB pour la recherche et les recommandations de films.

## 🎬 Fonctionnalités

- **Authentification** : Inscription et connexion avec JWT
- **Recherche de films** : Recherche avancée avec filtres (titre, genre, année)
- **Détails des films** : Informations complètes (synopsis, casting, réalisateur, bande-annonce)
- **Gestion des favoris** : Ajout, suppression, marquage comme "vu"
- **Recommandations personnalisées** : Basées sur les favoris, préférences et historique
- **Préférences de genres** : Configuration des genres préférés pour de meilleures recommandations

## 🏗️ Architecture

### Backend (Spring Boot)
- **Langage** : Java 17
- **Framework** : Spring Boot 3.2.0
- **Base de données** : MySQL
- **Sécurité** : JWT (JSON Web Tokens)
- **API externe** : TMDB API
- **Documentation** : Swagger/OpenAPI

### Frontend (React TypeScript)
- **Framework** : React 18 avec TypeScript
- **Styling** : Tailwind CSS
- **Routing** : React Router v6
- **HTTP Client** : Axios
- **Icônes** : Lucide React
- **Build Tool** : Vite

## 📋 Prérequis

- **Java** : JDK 17 ou supérieur
- **Maven** : 3.6+
- **Node.js** : 18+ et npm
- **MySQL** : 8.0+
- **Clé API TMDB** : Gratuite sur https://www.themoviedb.org/settings/api

## 🚀 Installation

### 1. Cloner le projet

```bash
git clone <repository-url>
cd Cinéphile
```

### 2. Configuration de la base de données

Créez la base de données MySQL :

```bash
mysql -u root -p < database/schema.sql
```

Ou manuellement :

```sql
CREATE DATABASE cinema_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Configuration du Backend

Éditez `backend/src/main/resources/application.properties` :

```properties
# MySQL Configuration
spring.datasource.username=root
spring.datasource.password=VOTRE_MOT_DE_PASSE

# TMDB API
tmdb.api.key=VOTRE_CLE_API_TMDB
```

Installez les dépendances et démarrez le backend :

```bash
cd backend
./mvnw clean install
./mvnw spring-boot:run
```

Le backend sera accessible sur `http://localhost:8080`

### 4. Configuration du Frontend

Installez les dépendances :

```bash
cd frontend
npm install
```

Démarrez le serveur de développement :

```bash
npm run dev
```

Le frontend sera accessible sur `http://localhost:5173`

## 📚 Documentation API

Une fois le backend démarré, accédez à la documentation Swagger :

```
http://localhost:8080/swagger-ui.html
```

## 🔑 Endpoints Principaux

### Authentification
- `POST /api/auth/register` - Inscription
- `POST /api/auth/login` - Connexion

### Films
- `GET /api/films` - Recherche de films
- `GET /api/films/{id}` - Détails d'un film
- `GET /api/films/populaires` - Films populaires
- `GET /api/films/genres` - Liste des genres

### Favoris
- `POST /api/favoris` - Ajouter un favori
- `GET /api/favoris` - Liste des favoris
- `PUT /api/favoris/{id}` - Mettre à jour un favori
- `DELETE /api/favoris/{id}` - Supprimer un favori

### Recommandations
- `GET /api/recommandations` - Recommandations personnalisées

### Utilisateur
- `PUT /api/utilisateurs/{id}/preferences` - Mettre à jour les préférences
- `GET /api/utilisateurs/{id}/preferences` - Récupérer les préférences

## 🎨 Structure du Projet

```
Cinéphile/
├── backend/
│   ├── src/main/java/com/cinema/
│   │   ├── config/          # Configuration (Security, CORS, Swagger)
│   │   ├── controller/      # Contrôleurs REST
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── model/           # Entités JPA
│   │   ├── repository/      # Repositories JPA
│   │   └── service/         # Services métier
│   └── src/main/resources/
│       └── application.properties
├── frontend/
│   ├── src/
│   │   ├── components/      # Composants réutilisables
│   │   ├── context/         # Context API (Auth)
│   │   ├── pages/           # Pages de l'application
│   │   ├── services/        # Services API
│   │   ├── types/           # Types TypeScript
│   │   ├── App.tsx          # Composant principal
│   │   └── main.tsx         # Point d'entrée
│   └── package.json
└── database/
    └── schema.sql           # Script de création de la BDD
```

## 🧪 Tests

### Backend
```bash
cd backend
./mvnw test
```

### Frontend
```bash
cd frontend
npm run lint
```

## 📦 Build Production

### Backend
```bash
cd backend
./mvnw clean package
java -jar target/cinema-backend-1.0.0.jar
```

### Frontend
```bash
cd frontend
npm run build
```

Les fichiers de production seront dans `frontend/dist/`

## 🔧 Configuration Avancée

### Modifier le port du backend

Dans `application.properties` :
```properties
server.port=8080
```

### Modifier le port du frontend

Dans `vite.config.ts` :
```typescript
server: {
  port: 5173
}
```

## 🐛 Dépannage

### Erreur de connexion à la base de données
- Vérifiez que MySQL est démarré
- Vérifiez les identifiants dans `application.properties`
- Vérifiez que la base de données `cinema_db` existe

### Erreur CORS
- Vérifiez que le frontend est dans `cors.allowed-origins` dans `application.properties`

### Erreur TMDB API
- Vérifiez que votre clé API est valide
- Vérifiez que vous n'avez pas dépassé les limites de requêtes

## 📝 Licence

Ce projet est un projet éducatif.

## 👥 Auteurs

Développé avec ❤️ pour les cinéphiles

## 🙏 Remerciements

- [TMDB API](https://www.themoviedb.org/documentation/api) pour les données de films
- [Spring Boot](https://spring.io/projects/spring-boot)
- [React](https://react.dev/)
- [Tailwind CSS](https://tailwindcss.com/)
