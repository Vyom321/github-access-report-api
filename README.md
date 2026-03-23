# GitHub Access Report API

##  Overview
This project is a Spring Boot application that connects to the GitHub API and generates a report showing which users have access to which repositories within a given organization.

---

##  How to Run the Project

### 1. Clone Repository

git clone https://github.com/Vyom321/github-access-report-api.git

cd github-access-report-api


---

### 2. Configure Application

Go to:


src/main/resources/application.properties


Add:


github.token=YOUR_GITHUB_TOKEN
github.org=Vyom321
server.port=8080


---

### 3. Run Application


mvn spring-boot:run


---

### 4. Access API

Open browser:


http://localhost:8080/api/report


---

## 🔐 Authentication Configuration

- This project uses **GitHub Personal Access Token (PAT)** for authentication.
- The token is stored in `application.properties`.

### Example:

github.token=YOUR_TOKEN


- The token is sent in API requests using:

Authorization: Bearer <token>


### Required Permissions:
- `repo`
- `read:org`

---

## 📡 API Endpoint

### GET `/api/report`

Returns a JSON response mapping users to repositories.

### Example Response:

[
{
"username": "user1",
"repositories": ["repo1", "repo2"]
},
{
"username": "user2",
"repositories": ["repo3"]
}
]


---

## 🧠 Assumptions

- The GitHub organization exists
- The provided token has sufficient permissions
- GitHub API is available and reachable
- Repositories are accessible

---

## ⚙️ Design Decisions

### 1. WebClient (Reactive)
- Used `WebClient` instead of `RestTemplate`
- Enables non-blocking API calls

---

### 2. Parallel Processing
- Used `Flux.flatMap()` to fetch collaborators in parallel
- Improves performance for large organizations

---

### 3. Pagination Handling
- GitHub API returns paginated data
- Implemented loop using:

?per_page=100&page=n


---

### 4. Thread Safety
- Used `ConcurrentHashMap`
- Prevents data inconsistency during parallel execution

---

### 5. DTO Usage
- Created `UserRepoResponse` class
- Provides clean and structured API response

---

## 📦 Tech Stack

- Java 21
- Spring Boot
- WebClient (Reactive)
- Maven

---

## 🔮 Future Improvements

- Add caching for API responses
- Handle GitHub rate limiting
- Add retry mechanism for failures
- Include permission levels (read/write/admin)
- Build frontend dashboard
