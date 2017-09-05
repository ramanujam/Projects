/**
 * Author        : Rohan Singh (rohans1)
 * Date          : 07/14/2017
 * Last Modified : 07/15/2017 17:10:00
 * Description   : This program does the following;
 *                 1. initializes players as demanded
 *                 2. Gets a question
 *                 3. support autoplay or manual play
 *                 4. displays summary after each game ends
 *                 5. displays overall multigame summary for multiple games in autoplay mode
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class WheelofFortune {

    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        System.out.printf("Please enter the text file name(or press return to use default): ");
        String input = kb.nextLine();

        //The question variable is declared outside if..else so that it can be initialized as required
        Question q;
        if(input.length()>1) q = new Question(input);
        else q = new Question();

        int num_players = 1;
        System.out.printf("How many players do you want: ");
        num_players = kb.nextInt();

        ArrayList<Player> players= new ArrayList<>();
        for (int i = 0; i < num_players; i++) {
            Player p = new Player();
            players.add(p);
        }

        for (Player player:players) {
            System.out.println("\n");
            //Welcome the players
            player.welcome();
        }

        System.out.printf("Do you want to play in auto mode(y/n) : ");
        String ip = kb.next();
        System.out.println();

        Game game;

        //if auto-mode enabled
        //In this case,
        //There could be multiple games played for all the players
        //A summary is displayed at the end
        if(ip.toLowerCase().equals("y") || ip.toLowerCase().equals("yes")) {
            HashMap<String,ArrayList<String>> summary = new HashMap<>();
            System.out.printf("How many games do you want to play: ");
            int num_games = kb.nextInt();
            System.out.println();
            for(int i = 0; i<num_games; i++) {

                System.out.printf("***** STARTING GAME %d *****\n", i+1);
                Question question = q.getQuestion();

                for(Player player : players) player.setQuestion(question);

                game = new Autoplay(question.getWord());
                initGame(question,game,players);
                for (Player player:players) {
                    if(player.getResult() != "WON") {
                        player.setResult("LOST");
                    }
                    if(summary.containsKey(player.getName())){
                        summary.get(player.getName()).add(player.getResult());
                    }
                    else {
                        ArrayList<String> res = new ArrayList<>();
                        res.add(player.getResult());
                        summary.put(player.getName(), res );
                    }
                }
                //reset the game specific parameters of a player to initial value
                for(Player player:players) player.reset();
            }

            System.out.println("------------------------------------SUMMARY-----------------------------------------\n");
            System.out.printf("\n%-20s    %-100s\n", "Player", "Summary");
            for (String key: summary.keySet()) {
                System.out.printf("%-20s    %-100s\n",key,summary.get(key).toString());
            }
        }
        //manual mode - multiple games is allowed in this mode
        //ONLY multiplayer code allowed
        else {
            Question question = q.getQuestion();
            for(Player player : players) player.setQuestion(question);
            game = new Game(question.getWord());
            initGame(question,game,players);
        }
    }

    /**
     * This function encaptulates the basic game playing operations for each game
     * @param question a random question
     * @param game     an instance of game
     * @param players  list of players
     */
    public static void initGame(Question question, Game game, ArrayList<Player> players){
        printInstructions(question);

        int exit_loop = 2;
        while(exit_loop != 1){
            int lost = 0;
            for (Player player:players) {
                if(player.getResult().equals("LOST")){
                    lost++;
                    if(Player.count == lost){
                        exit_loop = 1;
                        break;
                    }
                    continue;
                }
                else if(player.getResult().equals("WON")){
                    exit_loop = 1;
                    break;
                }
                else {
                    game.playGame(player);
                    if(player.getResult().equals("WON")){
                        exit_loop = 1;
                        break;
                    }
                    else exit_loop = 0;
                }

            }
            if(exit_loop == 1){
                //print result
                System.out.printf("%-30s    %-10s   %-30s\n","Player", "Result", "Status");
                System.out.println("----------------------------------------------------------------------------------------------");
                for (Player player: players) {
                    System.out.printf("%-30s    %-10s   %-30s\n", player.getName(), player.getResult(),player.getStatus());
                }
                System.out.println("----------------------------------------------------------------------------------------------");
            }
        }
    }

    /**
     * This method prints the instructions to play the game along with question hint
     * @param q question for displaying the hint
     */
    public static void printInstructions(Question q){
        String instructions = "";
        instructions += String.format("*** HINT : %s***\n", q.getHint());
        instructions += String.format("Instructions: \n");
        instructions += String.format("1. Guess a character my entering it and pressing return\n");
        instructions += String.format("2. You can also guess the complete word at any point\n");
        instructions += String.format("3. If you guess wrong character or word, you lose a TICK\n");
        instructions += String.format("4. You have six tries to guess the word\n");
        instructions += String.format("5. You the provided hint for clue\n");
        instructions += String.format("ALL THE BEST\n\n");

        System.out.println(instructions);
    }


}
