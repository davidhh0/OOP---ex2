# Welcome to Pokemon Game!

![Main photo](https://i.ibb.co/xGsyV83/Pok-mon-Logo-Gotta-catch-em-all.png)
The project is a game based on graph theory and implentation of it's algorithms. The main purpose of the game is getting the highest score by catching as most as possible pokemons considering the value of each one.

## How to play ?

 - Clone the project
 ## Game algorithm
 Let G=(V,E) be the the graph in some scenario and n the number of agents.
 The first thing the game does is determine how many connected components are in G by DFS through it. The operation returns us list of connected graphs where each graph G′=(V′,E′): 
 V′⊆V
  E′⊆E
  By the list we know how to map agent(s) to the graph(s).
  ### case of one connected component
 In case of exactly one connected components the algorithm breaks the graph into n (equals the number of agents). Each component is a responsibility for an agent. In this particular way of game, we promise efficiency by not sending two agents to the same pokemon as target and minimize the area of catching pokemon for every agent.
### case of more than one connected component
In case of more than one connected components we are constrained to the connected components that each of them is represented by a graph.
We want to allocate the most agents to the graph with the most nodes and the one with the least node just one agent.
  

## Icons dictionary
### Pokemon values
 1. ![less than 6](https://i.ibb.co/ZXFQZgH/2.png) range [0 - 6)
 2. ![6 - 9](https://i.ibb.co/9vzgvfc/3.png) range [6 - 9)
 3. ![exactly 9](https://i.ibb.co/3mM0yZb/4.png)  9
 4. ![10-13](https://i.ibb.co/kM4zpmt/5.png) range [10,13)
 5. ![13+](https://i.ibb.co/tZFQxF9/1.png) 13+
### Agent speeds
 1. ![minimum](https://i.ibb.co/xhQByXT/p1.png) Slow
 2. ![medium](https://i.ibb.co/7RZRNDh/p2.png) Medium
 3. ![Fast](https://i.ibb.co/K78rztD/p5.png) Fast
