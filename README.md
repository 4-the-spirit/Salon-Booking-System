# 💇‍♀️ Salon Booking System

## 📖 Overview

The **Salon Booking System** is a complete microservices-based backend platform designed for seamless salon management and appointment scheduling.
It supports two primary user roles:

* **Salon Owners** – create and manage salons, categories, and service offerings.
* **Customers** – browse salons and services, book appointments, and complete payments.

The system emphasizes **scalability**, **security**, and **loose coupling** through modern backend practices such as:

* Centralized authentication with Keycloak
* Distributed service discovery via Eureka
* Gateway routing & JWT validation
* Event-driven communication with RabbitMQ
* Independent databases per microservice
* Fully containerized architecture

This project served as a deep learning experience in distributed backend systems architecture, Spring Cloud, and production-grade authentication workflows.

## 🧩 General Backend Architecture

![](architecture-images/general-architecture.png)

## 🔄 Salon Owner & Customer Flows

![](architecture-images/owner-and-user-flows.png)

## ✨ Features

* ✂️ **Salon Service**<br>
CRUD operations for salons. Managed by salon owners, discoverable by customers.

* 📅 **Booking Service**<br>
Create appointments, handle booking statuses, and coordinate multi-service bookings.

* 👤 **User Service**<br>
Manages user accounts, roles, and synchronizes new users with Keycloak for secure authentication.

* 🔑 **Keycloak (IAM)**<br>
Issues JWT tokens, stores user credentials, and enforces role-based access.

* 🔍 **Eureka Server**<br>
Allows microservices to discover each other dynamically without hardcoded URLs.

* 🚪 **API Gateway**<br>
One entry point for all client requests. Routes traffic and validates JWT tokens.

* 📨 **RabbitMQ (Event-Driven)**<br>
Used for booking events, notifications, and asynchronous flows across the system.

* 🗄️ **PostgreSQL Databases**<br>
Each microservice owns its data for proper domain isolation.

* 🐳 **Docker & Docker Compose**<br>
One command to spin up the entire architecture locally.

* 🌱 **Spring Boot & Spring Cloud**<br>
Core framework powering the microservices, including Feign clients, security, routing, and discovery.


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

## 🚀 Getting Started

### ⚙️ Prerequisites

- 🐳 Docker & Docker Compose installed
- ☕ Java 21 SDK installed
- 📦 Maven installed

### 🐳 Running with Docker Compose

**1. Navigate to the Docker Compose directory:**

   ```bash
   cd docker-compose/default
   ```
**2. Start all services:**
   ```bash
   docker-compose up --build
  ```
**3. Keycloak Setup & Environment Variables:**
  
  * **Create the Keycloak client**<br>
    * Open the Keycloak Admin Console at: `http://localhost:8080`
    * Create a new client named salon-booking-client:
      * Enable Authentication.
      * Enable Authorization.
   
  * **Create roles**<br>
    * Inside the salon-booking-client, create the following roles:
      * `CUSTOMER`
      * `SALON_OWNER`
  
  * **Create the `.env` file**<br>
    * In the project root, create a file named .env containing the following variables:
      * `POSTGRES_USER`
      * `POSTGRES_PASSWORD`
      * `KEYCLOAK_CLIENT_SECRET`
      (copy this from the Credentials tab of salon-booking-client)
      * `KEYCLOAK_ADMIN_USERNAME`
      * `KEYCLOAK_ADMIN_PASSWORD`
      * `KEYCLOAK_DB_ID`
      * `STRIPE_SECRET_KEY`
