# Royal Game of Ur

## Overview
The Royal Game of Ur is an ancient board game with strategic gameplay involving movement of pieces across a grid of tiles. Here's a quick summary of the rules:

## Game Components
- **Board**: Consists of 20 navigable tiles arranged in a grid format.
- **Pieces**: Each player has 7 pieces.
- **Rosettes**: There are 5 rosettes on the board which grant extra turns and provide safety to pieces.

## Gameplay
- Players roll a set of dice that can yield a result between 0 and 4.
- Pieces move according to the dice roll, following the paths on the board.
- Rosettes grant extra turns and safety to pieces.
- Landing on an opponent's piece captures it, returning it to the opponent's layout.
- Players cannot stack their pieces on the same tile.
- To remove a piece from the board, a player must roll exactly the right number.

## Getting Started

### Running the Program
To run this program, follow these steps:

1. Open a terminal window.
2. Navigate to the directory containing the program files.
3. Make the build and run scripts executable by running the following commands:

    ```shell
    chmod +x build.sh
    chmod +x run.sh
    ```
4. Execute the run script by running:

    ```shell
    ./run.sh
    ```

### Finding Your IP Address
Before running the program, you'll might need to know the IP address of the machine where your server will run. You can find this by using the following command in the terminal:
```shell
curl ifconfig.me
```

## Project Structure

### Folders Descriptions

The project directory structure is organized as follows:

- **bin**: Contains compiled Java classes.
- **build**: Contains build and run scripts.
- **lib**: External libraries used in the project.
- **src**: Source code files in Java format.
    - **board**: Classes related to the game board.
    - **controller**: Classes responsible for controlling game logic.
    - **exceptions**: Custom exception classes.
    - **game**: Classes representing the game and player logic.
    - **main**: Main class to run the program.
    - **player**: Classes related to player management.
    - **ui**: User interface classes.
- **tests**: Unit tests for the project.


### File Descriptions

- **GameRules.text**: Text file containing the rules of the game.
- **README.md**: This file providing information about the project.

<br>
<br>
<br>
<br>

# TODO list

Create events for the UI: Clara
 - Create UI 

A list of controls:  Yehor/Emma/Ryan?

 - move piece 
 - roll dice
 - gamestate call (like check the current state of the game)
 - number of pieces taht have gotten out
 - location of local pieces 
 - let player a started tile 
 - number of pieces that are still in
 - class where you can acsess all controlers 

Random stuff:
 - Update README.md Yehor - today!
 - Report Yehor

Logic is done already

Controls:
	If we define them and then just create buttons – it would be nice
 	