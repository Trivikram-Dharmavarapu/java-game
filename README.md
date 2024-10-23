# Game with Java

This is a Java-based game built using `Swing` for graphical user interface components. The game involves clicking on moving objects to score points before they disappear, and the player's progress is tracked with score and lives.

## Features

- **Interactive Gameplay**: The player must click on red objects before they disappear into the green area to score points.
- **Score and Life Tracking**: The game displays the score and remaining lives at the top of the screen. If the player misses too many objects, the game is over.
- **Difficulty Levels**: The player can select different levels, each increasing the game's difficulty.
- **Menu Options**: The game includes a menu system with options like starting a new game, viewing high scores, accessing help, and quitting the game.

## How to Play

1. **Start the Game**: When you run the game, you are prompted to enter your name. This name will be displayed during the game.
2. **Gameplay**: The objective is to click on the red objects before they disappear into the green area. Your score will increase with each successful click.
3. **Lives**: You have a limited number of lives. If you miss clicking on the red objects more times than the available lives, the game will end.
4. **Menu**: You can access the menu to start a new game, view the high score, or select different levels of difficulty.

## Setup

### Prerequisites

- Java Development Kit (JDK) installed.
- Basic understanding of Java Swing components.

### Running the Game

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   ```
2. **Compile and Run**:
   Compile the project using your preferred IDE or directly through the command line:
   ```bash
   javac LaunchGame.java
   java LaunchGame
   ```
   This will launch the game, and you can start playing.

## Code Structure

- **Constants.java**:
  - Defines the help and about information for the game.
  
- **Coordinates.java**:
  - Handles the coordinates of the objects that appear in the game window.
  
- **DisplayGame.java**:
  - Manages the game window and the graphical components, including the menu and the game canvas.
  
- **Game.java**:
  - Contains the core game logic, including the game loop, level progression, and score tracking.
  
- **LaunchGame.java**:
  - Initializes the game by prompting the user for their name and starting the game.

## Future Enhancements

- **Enhanced Graphics**: Improve the visual appearance of the game with more detailed sprites and animations.
- **Power-Ups**: Add power-ups that the player can collect to gain temporary advantages.
- **Multiple Levels**: Implement more levels with increasing difficulty, each with its own unique challenges.
