/**
 * Author        : Rohan Singh (rohans1)
 * Date          : 08/01/2017
 * Last Modified : 08/05/2017 13:40:00
 * Description   : This is a class to analyze data
 */
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.*;
import java.util.function.Function;

import com.opencsv.CSVWriter;

import static java.util.stream.Collectors.*;

public class DataAnalyzer {
    ArrayList<EmacsRecord> data;
    String timeChunkType = null;
    int timeChunkVal  = 0;
    ArrayList<TimeUser> timeVsUsers = new ArrayList<>();

    /**
     *
     * @param data
     * @param tcType timechunk type
     */
    public DataAnalyzer(ArrayList<EmacsRecord> data, String tcType){
        this.data = data;
        this.timeChunkType = tcType;
    }

    /**
     *
     * @param data
     * @param tcType
     * @param tcVal value of timechunk e.g. 5 hours
     */
    public DataAnalyzer(ArrayList<EmacsRecord> data, String tcType, int tcVal){
        this.data = data;
        this.timeChunkType = tcType;
        this.timeChunkVal  = tcVal;
    }


    /**
     * This method uses stream() class tp group data into relevant factors
     * @throws Exception
     */
    public void analyzeUsersbyTime() throws Exception{
        getUsersCommitbyTime();
        Map<Integer,List<String>> usersByTimeChunk = new HashMap<>();

        if(timeChunkType.toLowerCase().contains("year")) {
            usersByTimeChunk =timeVsUsers.stream().collect(
                    groupingBy(TimeUser::getYear
                            , mapping(TimeUser::getUser
                                    , toList())
                    )
            );
        }
        else if(timeChunkType.toLowerCase().contains("month")) {
            usersByTimeChunk =timeVsUsers.stream().collect(
                    groupingBy(TimeUser::getMonth
                            , mapping(TimeUser::getUser
                                    , toList())
                    )
            );
        }

        else if(timeChunkType.toLowerCase().contains("weekday")) {
            usersByTimeChunk =timeVsUsers.stream().collect(
                    groupingBy(TimeUser::getDayofWeek
                            , mapping(TimeUser::getUser
                                    , toList())
                    )
            );
        }

        else if(timeChunkType.toLowerCase().contains("hour")) {
            usersByTimeChunk =timeVsUsers.stream().collect(
                    groupingBy(TimeUser::getHourofDate
                            , mapping(TimeUser::getUser
                                    , toList())
                    )
            );
        }

        else if(timeChunkType.toLowerCase().contains("min")) {
            usersByTimeChunk =timeVsUsers.stream().collect(
                    groupingBy(TimeUser::getMinuteofDate
                            , mapping(TimeUser::getUser
                                    , toList())
                    )
            );
        }

        else if(timeChunkType.toLowerCase().contains("week")) {
            usersByTimeChunk =timeVsUsers.stream().collect(
                    groupingBy(TimeUser::getWeekOfYear
                            , mapping(TimeUser::getUser
                                    , toList())
                    )
            );
        }

        else if(timeChunkType.toLowerCase().contains("day")) {
            usersByTimeChunk =timeVsUsers.stream().collect(
                    groupingBy(TimeUser::getDay
                            , mapping(TimeUser::getUser
                                    , toList())
                    )
            );
        }

        else{
            System.out.printf("Not Implemented %s\n",timeChunkType);
            throw new Exception();
        }


        Object[][] table = new Object[100][usersByTimeChunk.keySet().size()];
        int max = 0;

        int k = 0;
        HashMap<Object,Integer> countUsers = new HashMap<>();

        for(Integer timeChunk : usersByTimeChunk.keySet()){
            int j = 0;
            Object timeChunkMeaning = null;
            if(timeChunkVal == 0)
                timeChunkMeaning = getTimeChunkMeaning(timeChunk,timeChunkType);
            else
                timeChunkMeaning = getTimeChunkMeaning(timeChunk,timeChunkType,timeChunkVal);
            table[0][k] = timeChunkMeaning;
            Map<String,Long> usersCommit = usersByTimeChunk.get(timeChunk).stream()
                    .collect(groupingBy(Function.identity(),
                            counting()));

            Map<String,Long> sortedUserCommits = usersCommit.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (e1,e2) -> e1, LinkedHashMap::new));

            for(String user : sortedUserCommits.keySet()){
                j++;
                table[j][k] = String.format("%s: %d",user,usersCommit.get(user));
                if(j>max) max = j;
            }
            countUsers.put(timeChunkMeaning,j);
            k++;
        }

        /**
         * Write CSV File using a 2-D Array for better control
         */
        System.out.println();
        CSVWriter writer = null;
        String CSVFileName = "LogAnalyzer_CSVFile.csv";
        try {
            writer = new CSVWriter(new FileWriter(CSVFileName));
        }
        catch(IOException e){
            System.out.println("ExceptionCaught :" + e );
        }
        String[] sb = new String[usersByTimeChunk.keySet().size()];
        for(int i = 0; i <= Math.ceil((max/5))+1; i++) {
            for(int j = 0; j < usersByTimeChunk.keySet().size(); j++) {
                sb[j] = null;
                if(table[i][j] != null && i <= Math.ceil(countUsers.get(table[0][j])/5)+1) {
                    sb[j] = (String)table[i][j];
                }
            }
            writer.writeNext(sb);
        }

        System.out.printf("**** CSV file written at : %s ******\n",CSVFileName);

        try {
            writer.close();
        }
        catch(IOException e){
            System.out.println("Exception while closing the file: " + e);
        }
    }

    public void getUsersCommitbyTime(){
        for(EmacsRecord record : data){
            if(record.getRds().size() > 0){
                for(EmacsRecord.RevisionData rd : record.getRds()){
                    if(rd.getRevisionDate() == null){
                        System.out.println(rd.getAuthor());
                        continue;
                    }
                    if(timeChunkVal != 0)
                        timeVsUsers.add(new TimeUser(rd.getRevisionDate(),rd.getAuthor(),timeChunkVal));
                    else
                        timeVsUsers.add(new TimeUser(rd.getRevisionDate(),rd.getAuthor()));
                }
            }
        }
    }

    public Object getTimeChunkMeaning(int tc, String tcType){
        if(tcType.contains("month"))
            return Month.of(tc);
        else if(tcType.equals("dayofweek") || tcType.contains("weekday"))
            return DayOfWeek.of(tc);
        else
            return new Integer(tc).toString();
    }

    public Object getTimeChunkMeaning(int tc, String tcType, int tcval){
        if(tcType.contains("month"))
            return Month.of(tc);
        else if(tcType.contains("dayofweek") || tcType.contains("weekday"))
            return DayOfWeek.of(tc);
        else
            return new Integer(tc%tcval).toString();
    }

}
