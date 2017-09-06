/**
 * Author        : Rohan Singh (rohans1)
 * Date          : 08/01/2017
 * Last Modified : 08/05/2017 13:40:00
 * Description   : This program analyses a log file to print statistics
 *                 on users committing by timechunks
 */
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogAnalyzer implements REGEXCONSTANTS {
    public static void main(String[] args) {
        System.out.printf("Please provide emacs log file path :");
        Scanner kb = new Scanner(System.in);
        String input = kb.nextLine();
        String fileName = "../data/emacs.log";
        if(input.length()>1) fileName = input;
        File file = new File(fileName);

        System.out.printf("Please provide the timechunk you want to use: ");
        String timechunktype = kb.nextLine();
        int    timeChunkValue = 0;

        Pattern patternTimeChunk1  = Pattern.compile(TIMECHUNK1);
        Pattern patternTimeChunk2  = Pattern.compile(TIMECHUNK2);
        Matcher mTimeChunk2        = patternTimeChunk2.matcher(timechunktype);
        Matcher mTimeChunk1        = patternTimeChunk1.matcher(timechunktype);

        if(mTimeChunk2.find()) {
            System.out.printf("TimeChunk : %s , TimeVal : '%s'\n",mTimeChunk2.group(3) ,mTimeChunk2.group(2));
            timeChunkValue = Integer.parseInt(mTimeChunk2.group(2));
            timechunktype  = mTimeChunk2.group(3).trim();
        }
        else if(mTimeChunk1.find()){
            System.out.printf("TimeChunk : %s\n",mTimeChunk1.group(2));
            timechunktype = mTimeChunk1.group(2);
        }
        else{
            System.out.printf("TimeChunk not accepted: %s\n",timechunktype);
        }

        ArrayList<EmacsRecord> emacsRecords = new ArrayList<>();

        Pattern pattern1 = Pattern.compile(RECORD_SEPARATOR);
        BufferedReader br;



        try {
            br = new BufferedReader(new FileReader(file));
            String line;
            ArrayList<String> recordLines = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                Matcher m1 = pattern1.matcher(line);
                if(!m1.find()){
                    if(line.isEmpty()) continue;
                    recordLines.add(line);
                    continue;
                }

                EmacsRecord emacsRecord = new EmacsRecord(recordLines);
                recordLines.clear();
                emacsRecords.add(emacsRecord);
            }
        }
        catch (FileNotFoundException fnf){
            System.out.printf("File not found : %s\n", fnf);
        }
        catch (IOException io){
            System.out.printf("IO Exception occured: %s\n",io);
        }

        DataAnalyzer da = null;
        try {
            if(timeChunkValue != 0) {
                da = new DataAnalyzer(emacsRecords,timechunktype, timeChunkValue);
            }
            else{
                da = new DataAnalyzer(emacsRecords,timechunktype);
            }
            da.analyzeUsersbyTime();
        }
        catch(Exception e){
            System.out.printf("%s functionality is not implemented yet! Please try 'I want to see by year' or 'hey divide the data in hours'",timechunktype);
        }
    }
}
