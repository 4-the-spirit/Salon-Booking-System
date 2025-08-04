# 💇‍♀️ Salon Booking System

## 📖 Overview

Salon Booking System is a microservices-based application designed to facilitate seamless appointment scheduling for salon customers and efficient management for salon owners. It offers robust service discovery, API gateway routing, and secure OAuth2-based authentication, ensuring scalability, reliability, and security.

---

## ✨ Features

- **💈 Salon Service:** Manage salons, services, and offerings.
- **📅 Booking Service:** Customers can book appointments with salon owners.
- **👤 User Service:** Handles user registration, profiles, and authentication.
- **🔑 Keycloak:** Centralized identity and access management for secure authentication and authorization.
- **🔍 Eureka Server:** Centralized service discovery for microservices.
- **🚪 API Gateway:** Single entry point with request routing and cross-cutting concerns.
- **🔐 OAuth2 Resource Server:** Secures services with JWT-based authentication.
- **🗄️ PostgreSQL Databases:** Each microservice has its dedicated database for data isolation.
- **🐳 Docker & Docker Compose:** Simplified local deployment of the full microservices architecture.
- **🌱 Spring Boot & Spring Cloud:** Robust Java framework supporting microservices development.

---

## 🛠️ Tech Stack

- ☕ Java 21
- 🌱 Spring Boot
- ☁️ Spring Cloud (Eureka, Gateway, OAuth2 Resource Server)
- 🔐 Keycloak (Identity and Access Management)
- 🐘 PostgreSQL
- 🐳 Docker & Docker Compose
- 🧪 Postman
- 💳 Stripe API
- 📦 Maven

---

## 🚀 Getting Started

### ⚙️ Prerequisites

- 🐳 Docker & Docker Compose installed
- ☕ Java 21 SDK installed
- 📦 Maven installed

### 🔧 Setup Environment Variables

1. 📂 Navigate to the Docker Compose configuration directory:
   
   ```bash
   cd docker-compose/default
   ```
3. 📝 Create a .env file in this directory with your environment-specific variables, such as database credentials and passwords. Example .env contents:

   ```
   POSTGRES_USER=postgres
   POSTGRES_PASSWORD=your_password
   ```
5. 🚀 Run the following command inside docker-compose/default: `docker-compose up --build`
