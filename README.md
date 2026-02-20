# Film & Series Diary — Information System (Java)

A multi-module Java information system for managing films and series, including genres, collections and users.
The project demonstrates layered architecture, relational data modeling and desktop UI development.

---

## 🧩 Project Modules (Maven multi-module)

- `domain/` — domain model (entities, shared types)
- `data-access/` — database access layer (DAO / repositories)
- `service/` — business logic layer
- `presentation/` — JavaFX UI layer
- `data/` — optional seed/test data (text files)

> Note: Folder `src/` is a legacy/backup folder from earlier iterations and is not required for running the project.

---

## 🛠 Tech Stack

- Java
- Maven (multi-module project)
- JavaFX (UI)
- SQL / relational database (according to configuration)
- Git

---

## ✨ Features

- CRUD operations for films and related entities
- Genre management
- User management
- Collections / grouping of films
- Layered architecture with separation of concerns

---

## 🗄 Database

Relational model built around entities such as:
- Film
- Genre
- Collection
- User

The database layer is accessed through the `data-access` module.

---

## ▶️ How to Run

1. Clone the repository
2. Open the project in IntelliJ IDEA
3. Import as Maven project (root `pom.xml`)
4. Configure the database connection (if required)
5. Run the JavaFX application from the `presentation` module

---

## 📚 What I Learned

- Designing a relational data model
- Implementing CRUD operations and data access layer
- Building layered architecture (domain → data → service → UI)
- Structuring multi-module Maven projects
- Desktop UI development in JavaFX

---

## 👤 Author

Dominik Kontrik  
GitHub: https://github.com/Sekvojak
