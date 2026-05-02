// Nikoleta Tourounoglou A.M : 5106
// Dimitra Christina Gkaravela A.M : 5051
// Konstantinos Koutsidis A.M : 5114

import java.util.Scanner;

public class CardGame {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //System.out.println("We assume you know the rules of the game.");
       // String answer = scanner.next();
        System.out.println("Enter M (total cards), K (number of groups), and A_i (cards per group) separated by spaces:");

        int M = scanner.nextInt();
        int K = scanner.nextInt();
        int[] A = new int[K];
        int totalCardsInGroups = 0;
        for (int i = 0; i < K; i++) {
            A[i] = scanner.nextInt();
            if(A[i]<2){
                A[i] = 0;
                System.out.println("You can't put less than two cards in a group. ");
                return;
            }
            totalCardsInGroups += A[i];
        }
        if (totalCardsInGroups != M) {
            System.out.println("The sum of cards in the groups does not equal the total number of cards (M).");
            return;
        }

        int[] B = new int[K];
        for (int i = 0; i < K; i++) {
            B[i] = A[i] - 1;
            
        }
        

        int[] currentState = A.clone();
        boolean playerMaxTurn = true;
        System.out.println("Initial state and maximum cards you can draw from each group:");
        printState(currentState, B);
        while (!isGameOver(currentState)) {
            System.out.println("Current state: ");
            printState(currentState);

            if (playerMaxTurn) {
                int[] optimalMove = minimax(currentState, B, true);
                currentState[optimalMove[0]] -= optimalMove[1];
                System.out.println("MAX's move: Removing " + optimalMove[1] + " cards from group " + (optimalMove[0] + 1));
            } else {
                int group, cards;
                do {
                    System.out.println("Enter your move (group number and cards to remove):");
                    group = scanner.nextInt() - 1;
                    System.out.println("The max number of cards you can draw: " + group);
                    cards = scanner.nextInt();
                } while (!validateUserMove(currentState, B, group, cards));
                currentState[group] -= cards;
            }

            playerMaxTurn = !playerMaxTurn;
        }

        System.out.println("Game over! " + (playerMaxTurn ? "You" : "MAX") + " won!");
        
    }

    public static boolean validateUserMove(int[] state, int[] B, int group, int cards) {
        if (group < 0 || group >= state.length) {
            System.out.println("Invalid group number. Please enter a valid move.");
            return false;
        }
        if (cards < 1 || cards > Math.min(B[group], state[group])) {
            System.out.println("Invalid number of cards. Please enter a valid move.");
            return false;
        }
        return true;
    }

    public static boolean isGameOver(int[] state) {
        for (int cards : state) {
            if (cards > 0) return false;
        }
        return true;
    }

    public static void printState(int[] state) {
        for (int i = 0; i < state.length; i++) {
            System.out.println("Group " + (i + 1) + ": " + state[i] + " cards");
        }
    }

    public static void printState(int[] state, int[] maxCards) {
        for (int i = 0; i < state.length; i++) {
            System.out.println("Group " + (i + 1) + ": " + state[i] + " cards, max cards to draw: " + maxCards[i]);
        }
    }

    public static int[] minimax(int[] state, int[] B, boolean isMax) {
        if (isGameOver(state)) {
            return new int[]{-1, -1};
        }

        int bestGroup = -1;
        int bestCards = -1;
        int bestScore = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int group = 0; group < state.length; group++) {
            for (int cards = 1; cards <= Math.min(B[group], state[group]); cards++) {
                int[] nextState = state.clone();
                nextState[group] -= cards;

                int[] result = minimax(nextState, B, !isMax);

                if (isMax) {
                    
                    if (result[0] == -1 || result[1] > bestScore) {
                        bestGroup = group;
                        bestCards = cards;
                        bestScore = result[1];
                    }
                } else {
                    if (result[0] == -1 || result[1] < bestScore) {
                        bestGroup = group;
                        bestCards = cards;
                        bestScore = result[1];
                    }
                }
            }
        }

        return new int[]{bestGroup, bestCards};
    }
}
