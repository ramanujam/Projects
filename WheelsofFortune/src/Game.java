/**
 * Author        : Rohan Singh (rohans1)
 * Date          : 07/15/2017
 * Last Modified : 07/15/2017 13:40:00
 * Description   : This is the game class
 *                 This class encaptulates the logic for ChipARoonie Game
 *                 The class provides a playGame method which takes a player as input
 *                 The guess function could be overridden by child classes to implement
 *                 new guessing strategies for the game
 */

import java.util.ArrayList;
import java.util.Scanner;

public class Game{

    StringBuilder revealedWord;
    String        wordToGuess;

    /**
     * Constructors
     */
    public Game(){

    }

    public Game(String wordToGuess){
        this.wordToGuess = wordToGuess;
        revealedWord     = getRevealedPH(wordToGuess);
    }

    /**
     * Accessors and Mutators
     */
    public String getWordToGuess() {
        return wordToGuess;
    }

    public void setWordToGuess(String wordToGuess) {
        this.wordToGuess = wordToGuess;
    }

    public StringBuilder getRevealedWord() {
        return revealedWord;
    }

    public void setRevealedWord(StringBuilder revealedWord) {
        this.revealedWord = revealedWord;
    }

    /**
     * The playgame method does the job of playing the ChipARoonie game
     * The method does the following:
     * 1. Check if the entered word or character is valid
     * 2. Maintain a list of inputted characters
     * 3. Set the retries/status/result for players
     */
    public void playGame(Player p){
        p.alertPlayerTurn();
        System.out.println(revealedWord.toString());
        int flag;
        do{
            String input = "";
            try {
                input = guess();
            }
            catch (InterruptedException e){
                System.out.println("Exception Caught" + e);
            }
            input = input.trim();
            if(p.getInputList().get(input) != null){
                flag = 1;
                continue; //if the user inputted the same letter multiple times, he/she is not penalized
            }

            if(input.equals(wordToGuess)) {
                //if the user guesses the complete the word correctly, he/she wins
                System.out.printf("You Guessed: %s\n", input);
                reveal(p,"all");
                if(won()) p.setResult("WON");
                return;
            }
            else {
                if (input.length() > 1) {
                    //it is assumed that if the user guesses more than one letter, the word should match the word
                    //if not, the user is penalized
                    System.out.println("Incorrect! Guess Complete word or enter one word");
                    System.out.println(revealedWord.toString());
                    p.setRetries();
                    flag = 0;
                }
                else {
                    p.setInputList(input,1);
                    if (isValid(input)) {
                        reveal(p,input);
                        if(won()) {
                            p.setResult("WON");
                            return;
                        }
                        flag = 1;
                    }
                    else{
                        System.out.printf("Incorrect\n");
                        System.out.println(revealedWord.toString());
                        p.setRetries();
                        flag = 0;
                    }
                }
            }
            if(p.getRetries() <= 0 && !(won())) {
                p.setResult("LOST");
                System.out.println("You Lost");
            }
        }
        while (flag == 1);


    }

    /**
     * The method lets a user guess a letter
     * @return the guessed letter
     * @throws InterruptedException
     */
    public String guess() throws InterruptedException {
        Scanner kb = new Scanner(System.in);
        String input = kb.next();
        return input;
    }

    /**
     * This function checks if the inputted character is valid
     * @param s the word to guess
     * @return return true if the inputted character is a part of the word to be guessed
     */
    public boolean isValid(String s){
        if(wordToGuess.contains(s))
            return true;
        else
            return false;
    }

    /**
     * This function checks if the player won
     * @return return true if the player won after the last input
     */
    public boolean won(){
        if(revealedWord.toString().equals(wordToGuess)){
            System.out.println("You WON!!!!");
            return true;
        }
        else
            return false;
    }

    /**
     * This functions reveals the characters guessed correctly by the player
     * @param p the player instance
     * @param what what to reveal - 'all; in case the player guessed the complete word, else the correctly guessed char
     */
    public void reveal(Player p, String what){
        if(what.equals("all")) {
            System.out.println(wordToGuess);
            revealedWord.replace(0, wordToGuess.length(), wordToGuess);
        }
        else{
            ArrayList<Integer> rp = p.getQuestion().getcMap().get(what);
            for (int i: rp) {
                revealedWord.replace(i,i+1,what);
            }
            System.out.println(revealedWord.toString());
        }
    }

    /**
     * This function builds the initial placeholder for the secret word
     * @param word_to_guess the word to be guessed
     * @return a string with the same number of underscores as the secret word
     */
    public StringBuilder getRevealedPH(String word_to_guess){
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < word_to_guess.length(); i++) {
            s.append("_");
        }
        return s;
    }
}
