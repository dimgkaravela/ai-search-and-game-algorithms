// Nikoleta Tourounoglou A.M : 5106
// Dimitra Christina Gkaravela A.M : 5051
// Konstantinos Koutsidis A.M : 5114

import java.util.*;

class Cube {
    int x, y, number;

    public Cube(int x, int y, int number) {
        this.x = x;
        this.y = y;
        this.number = number;
    }

    public Cube cloneCube() {
        return new Cube(x, y, number);
    }

    @Override
    public String toString() {
        return "Cube{" + "x=" + x + ", y=" + y + ", number=" + number + '}';
    }
}

class State {
    List<Cube> cubeList;
    double cost;
    double heuristic;
    State parent;

    public State(List<Cube> cubeList, double cost, double heuristic) {
        this.cubeList = cubeList;
        this.cost = cost;
        this.heuristic = heuristic;
        this.parent = null;
    }

    public State cloneState() {
        List<Cube> newList = new ArrayList<>();
        for (Cube cube : cubeList) {
            newList.add(cube.cloneCube());
        }
        State newState = new State(newList, this.cost, this.heuristic);
        newState.parent = this.parent;
        return newState;
    }

    @Override
    public String toString() {
        return "State{" + "cubeList=" + cubeList + ", cost=" + cost + '}';
    }

    public boolean isEmptyPosition(Cube cube) {
        if (cube.x == 0 && cube.y == 0) {
            return true;
        }
        return false;

    }
}

public class CubesMoveGame {
    // private int K;

    public static boolean isValid(State state, int K) {
        boolean flag = false;
        for (Cube cubelist : state.cubeList) {
            // System.out.println("x: "+cubelist.x+" "+"y: "+cubelist.y);
            if ((cubelist.y == 1 && (cubelist.x <= 4 * K && cubelist.x >= 1))) {
                // System.out.println("Position is valid.");
                flag = true;
            } else if ((cubelist.y == 2 || cubelist.y == 3) && cubelist.x <= K && cubelist.x >= 1) {
                for (Cube cubelist2 : state.cubeList) {
                    if ((cubelist.x == cubelist2.x) && ((cubelist.y - 1) == cubelist2.y)) {
                        // System.out.println("there is cube to rest on ");
                        flag = true;
                        break;
                    } else {
                        flag = false;

                    }
                }
                if (flag == false) {
                    return false;
                }
            } else {
                return false;
            }
        }
        // System.out.println("Position is valid.");
        return flag;
    }

    public static boolean isCubeFree(State state, int cubeNumber) {
        Cube targetCube = null;
        for (Cube cube : state.cubeList) {
            if (cube.number == cubeNumber) {
                targetCube = cube;
                break;
            }
        }
        if (targetCube == null) {
            return false;
        }
        for (Cube cube : state.cubeList) {
            if (cube.y == targetCube.y + 1 && cube.x == targetCube.x) {
                return false;
            }
        }
        return true;
    }

    public static boolean isGoalState(State state, int K) {
        for (int i = 1; i <= K; i++) {
            Cube cube = state.cubeList.get(i - 1);
            if (cube.x != i || cube.y != 1) {
                return false;
            }
        }
        for (int i = K + 1; i <= 2 * K; i++) {
            Cube cube = state.cubeList.get(i - 1);
            if (cube.x != i - K || cube.y != 2) {
                return false;
            }
        }
        for (int i = 2 * K + 1; i <= 3 * K; i++) {
            Cube cube = state.cubeList.get(i - 1);
            if (cube.x != i - 2 * K || cube.y != 3) {
                return false;
            }
        }
        return true;
    }

    public static double heuristicFunction(List<Cube> cubeList, int K) {
        double totalManhattanDistance = 0;
        for (Cube cube : cubeList) {
            int goalX, goalY;
            if (cube.number <= K) {
                goalX = cube.number;
                goalY = 1;
            } else if (cube.number <= 2 * K) {
                goalX = cube.number - K;
                goalY = 2;
            } else {
                goalX = cube.number - 2 * K;
                goalY = 3;
            }
            int manhattanDistance = Math.abs(cube.x - goalX) + Math.abs(cube.y - goalY);
            totalManhattanDistance += manhattanDistance;
        }
        return totalManhattanDistance;
    }

    // public static double calculateHeuristic(State state, int K) {
    // double totalDistance = 0;
    // for (Cube cube : state.cubeList) {
    // int goalY = (cube.number - 1) / K + 1;
    // totalDistance += getCost(cube.y, goalY);
    // }
    // return totalDistance;
    // }

    public static String stateToString(State state) {
        List<Cube> sortedCubeList = new ArrayList<>(state.cubeList);
        sortedCubeList.sort(Comparator.comparingInt(c -> c.number));
        StringBuilder sb = new StringBuilder();
        for (Cube cube : sortedCubeList) {
            sb.append(cube.number).append("-").append(cube.x).append("-").append(cube.y).append(";");
        }
        return sb.toString();
    }

    public static State readInitialState(int K, int[] initialPositions) {
        List<Cube> cubeList = new ArrayList<>();
        for (int i = 0; i < initialPositions.length; i += 2) {
            int x = initialPositions[i];
            int y = initialPositions[i + 1];
            int number = i / 2 + 1;
            cubeList.add(new Cube(x, y, number));
        }
        double heuristic = heuristicFunction(cubeList, K);
        return new State(cubeList, 0, heuristic);
    }

    public static double moveCost(Cube from, Cube to) {
        if (from.y < to.y) {
            return to.y - from.y;
        } else if (from.y > to.y) {
            return 0.5 * (from.y - to.y);
        } else {
            return 0.75;
        }
    }

    public static double getCost(int initialY, int finalY) {
        if (initialY < finalY) {
            return finalY - initialY;
        } else if (initialY > finalY) {
            return 0.5 * (initialY - finalY);
        } else {
            return 0.75;
        }
    }

    public static List<State> getNeighbors(State state, int K) {
        List<State> neighbors = new ArrayList<>();

        for (int cubeNumber = 1; cubeNumber <= 3 * K; cubeNumber++) {
            if (isCubeFree(state, cubeNumber)) {
                Cube cube = state.cubeList.get(cubeNumber - 1);
                int initialX = cube.x;
                int initialY = cube.y;

                for (int y = 1; y <= 3; y++) {
                    int xLimit = y == 1 ? 4 * K : K;
                    for (int x = 1; x <= xLimit; x++) {
                        if (initialX != x || initialY != y) {
                            Cube newPosition = new Cube(x, y, 0);
                            boolean isEmpty = true;
                            for (Cube otherCube : state.cubeList) {
                                if (otherCube.x == newPosition.x && otherCube.y == newPosition.y
                                        && otherCube.number != cubeNumber) {
                                    isEmpty = false;
                                    break;
                                }
                            }
                            if (isEmpty) {
                                State newState = state.cloneState();
                                newState.parent = state;
                                newState.cubeList.get(cubeNumber - 1).x = x;
                                newState.cubeList.get(cubeNumber - 1).y = y;
                                newState.cost += getCost(initialY, y);

                                if (isValid(newState, K)) {
                                    newState.heuristic = heuristicFunction(newState.cubeList, K);
                                    neighbors.add(newState);
                                }
                            }
                        }
                    }
                }
            }
        }
        return neighbors;
    }

    public static State uniformCostSearch(State initialState, int K) {
        PriorityQueue<State> frontier = new PriorityQueue<>(Comparator.comparingDouble(s -> s.cost));
        Map<String, Double> visited = new HashMap<>();
        frontier.add(initialState);
        visited.put(stateToString(initialState), initialState.cost);

        int extensions = 0;

        while (!frontier.isEmpty()) {
            State current = frontier.poll();
            if (isGoalState(current, K)) {
                System.out.println("Uniform Cost Search Extensions: " + extensions);
                return current;
            }
            List<State> successors = getNeighbors(current, K);
            extensions++;

            for (State successor : successors) {
                String stateStr = stateToString(successor);
                if (!visited.containsKey(stateStr) || visited.get(stateStr) > successor.cost) {
                    visited.put(stateStr, successor.cost);
                    successor.parent = current;
                    frontier.add(successor);
                }
            }
        }
        return null;
    }

    public static State aStarSearch(State initialState, int K) {
        PriorityQueue<State> frontier = new PriorityQueue<>(Comparator.comparingDouble(s -> s.cost + s.heuristic));
        Map<String, Double> visited = new HashMap<>();
        frontier.add(initialState);
        visited.put(stateToString(initialState), initialState.cost + initialState.heuristic);
        int extensions = 0;

        while (!frontier.isEmpty()) {
            State current = frontier.poll();
            if (isGoalState(current, K)) {
                System.out.println("A* Search Extensions: " + extensions);
                return current;
            }
            List<State> successors = getNeighbors(current, K);
            extensions++;

            for (State successor : successors) {
                String stateStr = stateToString(successor);
                double totalCost = successor.cost + successor.heuristic;
                if (!visited.containsKey(stateStr) || visited.get(stateStr) > totalCost) {
                    visited.put(stateStr, totalCost);
                    successor.parent = current;
                    frontier.add(successor);
                }
            }
        }
        return null;
    }

    public static void printSolutionPath(State finalState) {
        List<State> path = new ArrayList<>();
        State currentState = finalState;
        while (currentState != null) {
            path.add(currentState);
            currentState = currentState.parent;
        }
        Collections.reverse(path);
        for (int i = 0; i < path.size(); i++) {
            State state = path.get(i);
            System.out.println("Step " + i + ": " + stateToString(state) + ", Cost: " + state.cost);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter K: ");
        int K = scanner.nextInt();
        int[] initialPositions = new int[6 * K];
        System.out.println("Enter initial positions (x, y) of the cubes with a space between them :");
        for (int i = 0; i < 6 * K; i++) {
            initialPositions[i] = scanner.nextInt();
        }
        scanner.close();
        State initialState = readInitialState(K, initialPositions);
        // System.out.println(stateToString(initialState));
        if (!isValid(initialState, K)) {
            System.out
                    .println("Invalid initial positions provided. Please ensure they meet the specified constraints.");
            return;
        }

        State ucsSolution = uniformCostSearch(initialState, K);
        if (ucsSolution != null) {
            System.out.println("Uniform Cost Search Solution: " + ucsSolution.cost);
            printSolutionPath(ucsSolution);
            System.out.println("Uniform Cost Search Solution: " + ucsSolution);
        } else {
            System.out.println("Uniform Cost Search failed to find a solution.");
        }

        State aStarSolution = aStarSearch(initialState, K);
        if (aStarSolution != null) {
            System.out.println("A* Search Solution: " + aStarSolution.cost);
            printSolutionPath(aStarSolution);
            System.out.println("aStar Solution: " + aStarSolution);
        } else {
            System.out.println("A* Search failed to find a solution.");
        }
    }
}
