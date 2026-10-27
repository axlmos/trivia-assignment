# Quad Solutions Trivia App

This project contains a Spring Boot backend and a React frontend.

## Prerequisites
- **Java** (JDK 21)
- **Node.js** and **npm**

## Running the Backend
The Spring backend is located in the `trivia` directory.

1. Navigate to the backend folder:
   ```bash
   cd trivia
   ```

3. Start the application using:
   ```bash
   ./gradlew bootRun
   ```
The Trivia API will (typically) be available at `http://localhost:8080`.

## Running the Frontend
The site is located in the `trivia-site` directory.

1. Open a separate terminal and navigate to the frontend folder:
   ```bash
   cd trivia-site
   ```
2. Install the dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm run dev
   ```
The site will (typically) be available at `http://localhost:5173`.

To change the server API address, you can change the proxy in `vite.config.js` (default is localhost:8080).


## (Nu in het Nederlands) Development keuzes:

Bij het lezen en krijgen van de opdracht is het grootste probleem hoe je de data opslaat tussen de 2 API requests. Hier heb ik 3 opties overwogen:

1. Bij elke `/questions` request de juiste antwoorden opslaan in een database met een UUID, en de UUID terugsturen samen met de vragen. Bij `/checkanswers` haal je het weer uit de database op. Om de database niet vol te laten lopen, kan je met een cron job alle data ouder dan een uur of dag weer weghalen.
2. Bij elke `/questions` request de JSON van de juiste antwoorden encrypten, en de ge-encrypte 'token' meegeven samen met de vragen. Bij `/checkanswers` hoef je alleen de token te decrypten, en vergelijken met de meegegeven antwoorden.
3. Bij elke `/questions` request de juiste antwoorden opslaan in een hashmap met een UUID, en de UUID terugsturen samen met de vragen. Bij `/checkanswers` haal je het weer uit de hashmap op.

Uiteindelijk heb ik gekozen voor optie 2, omdat je geen data hoeft op te slaan. Met optie 1 creeer je een overhead voor de database, die niet nodig is, sinds de data toch niet lang opgeslagen hoeft te worden. Bij optie 3 kan je heel makkelijk heel veel memory gaan gebruiken, het houdt zich niet aan de REST principes, en als je wilt scalen naar meerdere servers, is dat niet mogelijk.  Daarom heb ik voor optie 2 gekozen, sinds de oplossing voor geen database zorgt, en door de encryption ervoor zorgt dat een slimme gebruiker de antwoorden niet kan uitvogelen.

**Auteur**: Axl Jacob Mostert