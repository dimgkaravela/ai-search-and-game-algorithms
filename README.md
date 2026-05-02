# AI Search and Game Algorithms

Java implementations of Artificial Intelligence algorithms developed for AI lab assignments.

This repository contains two independent Java programs:

1. A cube-moving puzzle solved with Uniform Cost Search and A* Search.
2. A card game where an AI player selects moves using the Minimax algorithm.

## Overview

The project focuses on classic Artificial Intelligence search and decision-making algorithms.

The first lab models a cube movement problem as a state-space search problem and solves it using:

- Uniform Cost Search
- A* Search
- Manhattan distance heuristic

The second lab implements a two-player card game where the AI player uses:

- Minimax search
- recursive game-state evaluation
- optimal move selection

## Technologies
- Java
- Artificial Intelligence
- State-space search
- Uniform Cost Search
- A* Search
- Heuristic search
- Manhattan distance
- Minimax
- Game AI

## Example Usage
### Cube Movement Puzzle

The program asks for:

```K: initial positions of the cubes as x y pairs```

Then it runs both Uniform Cost Search and A* Search and prints:

- number of expanded states
- solution cost
- solution path

### Card Game

The program asks for:

```M K A_i```

where:

- M is the total number of cards
- K is the number of groups
- A_i is the number of cards in each group

The AI player then chooses moves using Minimax.
