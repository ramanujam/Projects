/**
 * Author        : Rohan Singh (rohans1)
 * Date          : 07/14/2017
 * Last Modified : 07/15/2017 11:10:00
 * Description   : This is player class and is very generic in nature
 *                 With little or no changes, this player class could be used for any GAME implementation
 *                 For every player, an instance of this class in initiated
 *                 The responsibilities of this class are :
 *                  a. To hold the player details
 *                  b. Track retries left
 *                  c. Track the current status
 *                  d. Track the result of game of each player
 */
import java.util.HashMap;
import java.util.Scanner;

public class Player {

    /**
     * The following constants are used to generate colored ticks and bombs
     */
    public static final String ANSI_RED         = "\u001B[31m";
    public static final String ANSI_ORANGE      = "\u001B[35m";
    public static final String ANSI_GREEN       = "\u001B[32m";
    public static final String ANSI_YELLOW      = "\u001B[33m";
    public static final String ANSI_BLUE        = "\u001B[34m";
    public static final String ANSI_PURPLE      = "\u001B[35m";
    public static final String ANSI_RESET       = "\u001B[30m";
    public static final String ANSI_TICK        = "\u2714";
    public static final String ANSI_BOMB        = "\u1F4A3";

    static  int      count   = 0;
    private int      retries = 6;
    private String   result  = "";
    private String   name;
    private String   status  = "";
    //The inputList HashMap is used to maintain a list of inputted characters
    //A HashMap is used for faster search operation
    private HashMap<String,Integer> inputList = new HashMap<>();
    private Question question;

    //Default constructor
    public Player(){
        count++;
        name = String.format("PLAYER %d", count);
    }

    //Question is a member if player so that it could be associate tightly to the player
    //If required, this could be modified to give this different question to each player
    public Player(Question question){
        this();
        this.question = question;
    }

    /**
     * This constructor initializes player name as well
     * @param name player name
     */
    public Player(String name){
        this();
        this.name = name;
    }

    /**
     * The methods below are accessors and mutators
     *
     */
    public void setInputList(String s, int i) {
        this.inputList.put(s,i);
    }

    public HashMap<String, Integer> getInputList() {
        return inputList;
    }

    public void setStatus(int retries) {
        if(retries == 6) this.status += String.format(       ANSI_RED    + ANSI_TICK + ANSI_RESET);
        if(retries == 5) this.status += String.format("\t" + ANSI_ORANGE + ANSI_TICK + ANSI_RESET);
        if(retries == 4) this.status += String.format("\t" + ANSI_YELLOW + ANSI_TICK + ANSI_RESET);
        if(retries == 3) this.status += String.format("\t" + ANSI_GREEN  + ANSI_TICK + ANSI_RESET);
        if(retries == 2) this.status += String.format("\t" + ANSI_BLUE   + ANSI_TICK + ANSI_RESET);
        if(retries == 1) this.status += String.format("\t" + ANSI_PURPLE + ANSI_BOMB + "BOMB!!!" + ANSI_RESET);
    }

    public String getStatus() {
        return status;
    }

    public void setRetries() {
        this.setStatus(retries);
        this.retries = retries - 1;
    }

    public int getRetries() {
        return retries;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Question getQuestion() {
        return question;
    }

    /**
     * This method welcomes individual players
     */
    public void welcome(){
        String welcome_message = "";
        String name = "";
        System.out.printf("Please enter your name: ");
        Scanner kb = new Scanner(System.in);
        name = kb.nextLine();
        name.trim();
        if (!name.isEmpty()) this.name = name;
        welcome_message += String.format("***** Welcome %s to ChipARoonie *****\n", this.name);

        System.out.println(welcome_message);
        System.out.println();
    }

    /**
     * The method displays relevant player state data and alerts the players for playing
     */
    public void alertPlayerTurn(){
        System.out.printf("Your turn    : %s\n",name);
        System.out.printf("Tried Letters: %s\n", inputList.keySet().toString());
        System.out.printf("Status       : %s\n", getStatus());
        System.out.printf("Retries left : %d\n", getRetries());
    }

    /**
     * This is a utility function to reset game specific data for new games
     */
    public void reset(){
        this.setQuestion(null);
        this.setResult("");
        this.retries = 6;
        this.status  = "";
        this.inputList = new HashMap<>();
    }

}
