/**
 * Author        : Rohan Singh (rohans1)
 * Date          : 07/14/2017
 * Last Modified : 07/15/2017 13:10:00
 * Description   : This is generic question class
 *                 For every question, an instance of this class in initiated
 *                 The responsibilities of this class are :
 *                  a. To read the inputFile provided with a list of words
 *                  b. Generate a HashMap of all words with hints
 *                  c. Select a random question with hint from the file
 *                  d. Generate a map of characters and their position in the secret word
 *                     This is done to prevent multiple parsing of the string
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Question {
    private String word;
    private String hint;
    private String inputFilePath;
    private HashMap<String,String> hm = new HashMap<>();
    private HashMap<String,ArrayList<Integer>> cMap = new HashMap<>();

    /**
     * Constructors
     */

    public Question(){
        inputFilePath = "../data/inputFile.txt";
    }

    public Question(String inputFilePath){
        this.inputFilePath = inputFilePath;
    }

    /**
     * Accessors and Mutators
     */

    public String getWord(){
        return word;
    }

    public void setWord(String w){
        word = w;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getHint() {
        return hint;
    }

    public HashMap<String, ArrayList<Integer>> getcMap() {
        return cMap;
    }

    private void setcMap(HashMap<String, ArrayList<Integer>> cMap) {
        this.cMap = cMap;
    }

    public HashMap<String, String> getHm() {
        return hm;
    }

    public void setHm(HashMap<String, String> hm) {
        this.hm = hm;
    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    public void setInputFilePath(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }


    /**
     * The method returns a randomly selected question
     * @return a randon question and hint
     */
    public Question getQuestion(){
        try {
            readFile();
        }
        catch (FileNotFoundException e) {
            System.out.println("Exception Occured: " + e);
        }

        Random rand = new Random();
        int val = rand.nextInt(hm.size());
        List<String> l = new ArrayList<String>(hm.keySet());
        String key   = l.get(val);
        String value = hm.get(key);

        Question q = new Question();
        q.setWord(key);
        q.setHint(value);
        q.generateCharPosMap(q.getWord());
        return q;

    }

    /**
     * The method reads a file with words and hints
     * @throws FileNotFoundException
     */
    public void readFile() throws FileNotFoundException{
        File file = new File(inputFilePath);
        String line;
        Scanner ip = new Scanner(file);
        while(ip.hasNextLine()){
            line = ip.nextLine();
            String[] splt = line.split(", ");
            hm.put(splt[0],splt[1]);
        }
    }

    /**
     * The method generates a Map for the characters and their positions in a word
     * This generic method can be used to get the character map of any string
     * @param str word_to_guess
     */
    private void generateCharPosMap(String str) {
        HashMap<String,ArrayList<Integer>> charMap = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            String x = str.substring(i,i+1);
            if (charMap.containsKey(x)) {
                charMap.get(x).add(i);
            }
            else {
                ArrayList<Integer> al = new ArrayList<>();
                al.add(i);
                charMap.put(x, al);
            }
        }
        this.setcMap(charMap);
    }
}
