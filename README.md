## Library management system - backend java springboot project

This is a production-style REST API developed using **Java Spring Boot**, built as part of my onboarding process in the company.

The app might not be completely finished because the focus shifted according to the priorities assigned to me by my senior.

Also, this app has a commonLibrary and a machine learning microservice for recommendations that goes with it.

---

## 🔍 Project Overview

This application serves as a basic **Library Management System**, exposing RESTful endpoints for managing books and reservations. It’s structured following industry best practices and simulates real-world features including:

- Business rules (e.g., reservation limits per user)
- Database integration (MySQL)
- Email notifications (SMTP with MailTrap)
- Messaging queue system (Apache Artemis for async tasks)

It was built to:

- Understand core Spring Boot modules (Web, JPA, Mail, JMS)
- Practice clean architecture (Controller → Service → Repository)
- Apply dependency injection, configuration management, and environment setup
- Gain hands-on experience with tools used in enterprise Java development

---

## 🧰 Tech Stack

- **Java 17**
- **Spring Boot 3.x**
- **Spring Web** – REST API
- **Spring Data JPA** – ORM and DB interaction
- **MySQL** – Persistent data storage
- **Apache Artemis** – Asynchronous message queueing (JMS)
- **Spring Mail + MailTrap** – SMTP email integration
- **Lombok** – Simplify Java boilerplate
- **Maven** – Dependency and build tool

---

## 🚀 Key Features

- ✅ Full CRUD API for books and reservations
- ✅ Layered, modular architecture (Controller → Service → Repository)
- ✅ User reservation limit enforced via configuration
- ✅ Emails sent on key actions (e.g., reservation confirmation)
- ✅ Asynchronous processing via JMS (Apache Artemis)
- ✅ MySQL integration
