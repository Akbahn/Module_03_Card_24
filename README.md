# Card 24 Game (JavaFX Implementation)

## Overview
Card 24 Game is a JavaFX-based application that allows users to play the mathematical card game **"24"**. The game generates four random playing cards, and the player must use basic arithmetic operations (addition, subtraction, multiplication, and division) to make the number 24.

## Features
- Interactive JavaFX user interface.
- Random card generation.
- User input validation for correct expressions.
- AI-powered hint generation using OpenAI's API.
- Dynamic evaluation of mathematical expressions.

## Technologies Used
- **Java 23**
- **JavaFX 23**
- **Maven** for dependency management
- **OpenAI API** for hints
- **exp4j** for expression evaluation
- **Apache Commons JEXL** for parsing expressions

## Installation & Setup
### Prerequisites
- Java 23 installed
- Maven installed
- OpenJFX dependencies configured
- OpenAI API key (if using AI hints)


## Usage
1. The game will display four randomly generated cards.
2. Enter a mathematical expression using the numbers on the cards.
3. Press the **Submit** button to check your answer.
4. If stuck, press **Hint** to get AI-generated suggestions.
5. The game will validate your input and provide feedback.

## Troubleshooting
### OpenAI API Errors (HTTP 429)
If you see an error like `java.io.IOException: Server returned HTTP response code: 429`, it means you're making too many API requests. To fix:
- Reduce hint request frequency.
- Implement a retry mechanism with exponential backoff.
- Check your API key usage on OpenAI's dashboard.

### JavaFX Not Found
Ensure JavaFX dependencies are correctly set up in your `pom.xml`:
```xml
<dependencies>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>23.0.1</version>
    </dependency>
    <!-- Add other JavaFX dependencies as needed -->
</dependencies>
```


