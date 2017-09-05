/**
 * Author        : Rohan Singh (rohans1)
 * Date          : 07/14/2017
 * Last Modified : 07/15/2017 17:44:00
 * Description   : This class is inherits from GAME class.
 *                 1. It overrides the gurss method of the game class
 *                 2. The overridden method enables it to play automatically
 *                 3. The guess function is designed as such it enables any new guess technique
 *                    to be implemented without any change is code elsewhere
 *                 4. As an example, the weighted bias of letters as per frequency could be added to the guess
 *                    method in this class, without any change anywhere else.
 */
import java.util.*;
import static java.lang.Thread.sleep;

public class Autoplay extends Game {
    Question question;

    /**
     * Default constructor
     */
    public Autoplay() {
    }

    /**
     * This constructor takes a word to guess
     * @param word
     */
    public Autoplay(String word) {
        super(word);
    }

    /**
     * This constructor takes a word to guess and a question object
     * @param word word to be guessed
     * @param question the attributes of the question object can be used to optimize the guessing strategy
     */
    public Autoplay(String word, Question question) {
        super(word);
        this.question = question;
    }

    //mutator
    public void setQuestion(Question question) {
        this.question = question;
    }

    //accessor
    public Question getQuestion() {
        return question;
    }

    /**
     * This method overrides the base class guess() method
     * @return returns randomly guessed words by the computer
     * @throws InterruptedException the sleep function throws this exception
     */
    public String guess() throws InterruptedException {

        final String englishletters = "abcdefghijklmnopqrstuvwxyz";
        final int N = englishletters.length();

        Random r = new Random();
        String guessed_letter = String.format("%c", englishletters.charAt(r.nextInt(N)));
        sleep(1000);
        return guessed_letter;
    }
}
