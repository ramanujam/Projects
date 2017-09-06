/**
 * Author        : Rohan Singh (rohans1)
 * Date          : 08/01/2017
 * Last Modified : 08/05/2017 13:40:00
 * Description   : This is a class parses the file lines from log
 *                  to generate objects
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmacsRecord implements REGEXCONSTANTS {

    private ArrayList<RevisionData>                               rds  = new ArrayList<>();

    public EmacsRecord(ArrayList<String> lines) {
        Pattern revision_sep_pattern = Pattern.compile(REVISION_SEPARATOR);

        int readFileDetails     = 1;
        int readRevisionDetails = 0;
        int count = 0;
        RevisionData rd = null;
        for (String line : lines) {
            count++;
            Matcher match_rvs_sep = revision_sep_pattern.matcher(line);

            if(match_rvs_sep.find()) {
                readFileDetails = 0;
                readRevisionDetails = 1;
                if(rd != null) rds.add(rd);
                rd = new RevisionData();
                continue;
            }
            else if (readFileDetails == 1) {
                continue;
            }
            else if(readRevisionDetails == 1){
                rd.add(line);
                if(count == lines.size()){
                    rds.add(rd);
                }
            }
        }
    }

    class RevisionData{
        /**
         * Sample revision data
        revision 1.13
        date: 2013-08-27 10:48:48 -0700;  author: gm;  state: Exp;  lines: +2 -0;  commitid: UaiamU9PT74Sp73x;
        Add more redirects for old MS Windows FAQ location; remove .symlinks
         */
        Pattern revision_line_pattern = Pattern.compile(REVISION_LINE);

        private String revisionNumber;
        private Date   revisionDate;
        private String author;
        private String  state;
        private int    lines_added;
        private int    lines_deleted;
        private String commitid;
        private StringBuilder comment = new StringBuilder();

        public Date getRevisionDate() {
            return revisionDate;
        }

        public String getAuthor() {
            return author;
        }

        /**
         * The given date in the file is GMT time with offsets
         * @param datestr
         * @return
         * @throws ParseException
         */
        public Date RevisionDate(String datestr) throws ParseException{
            String[] date_tz = datestr.split(BLANK);
            SimpleDateFormat sdf = new SimpleDateFormat(REVISION_DATE_FMT);
            Date dt = (Date)sdf.parse(date_tz[0].concat("T"+date_tz[1]+date_tz[2]));
            return dt;
        }

        void add(String line){
            if(line.isEmpty()) return;

            Matcher match_rev_line = revision_line_pattern.matcher(line);

            if(match_rev_line.find())
                revisionNumber = match_rev_line.group(1);
            else if(line.contains("author: ") && line.startsWith("date: ")) {
                String[] line_data = line.split(ATTRIBUTE_SEP);
                for(String rev_data : line_data){
                    String[] keyVal = rev_data.split(FIELD_SEP);
                    if(keyVal[0].contains("date")) {
                        try {
                            revisionDate = RevisionDate(keyVal[1].trim());
                        }
                        catch (ParseException e){
                            System.out.printf("The data %s cannot be parsed - %s\n",keyVal[1], e);
                        }
                    }
                    else if(keyVal[0].contains("author")) {
                        author = keyVal[1].trim();
                    }
                    else if(keyVal[0].contains("state"))  state = keyVal[1].trim();
                    else if(keyVal[0].contains("lines")) {
                        String[] add_del = keyVal[1].trim().split(" ");
                        lines_added   = Integer.parseInt(add_del[0]);
                        lines_deleted = Integer.parseInt(add_del[1]);
                    }
                    else if(keyVal[0].contains("commitid")){
                        commitid = keyVal[1].trim();
                    }
                }
            }
            else if(revisionNumber != null){
                comment.append(line);
            }
            else{
                System.out.printf("Ignoring %s\n",line);
            }
        }
    }


    public ArrayList<RevisionData> getRds() {
        return rds;
    }

}
