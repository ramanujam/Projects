/**
 * Author        : Rohan Singh (rohans1)
 * Date          : 08/01/2017
 * Last Modified : 08/05/2017 13:40:00
 * Description   : This is a class is responsible for getting various factors based on dates/user
 */
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUser {
    Date date;
    String user;
    int tc;

    TimeUser(Date date, String user ){
        this.date = date;
        this.user = user;
    }

    TimeUser(Date date, String user, int tc){
        this.date = date;
        this.user = user;
        this.tc   = tc;
    }

    public Date getDate() {
        return date;
    }

    public String getUser() {
        return user;
    }

    public int getYear(){
        if(date == null) return 0;
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if(tc != 0) return localDate.getYear()%tc;
        else return localDate.getYear();
    }

    public int getMonth(){
        if(date == null) return 0;
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getMonthValue();
    }

    public int getDay(){
        if(date == null) return 0;
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if(tc !=0) return localDate.getDayOfYear()%tc;
        else return localDate.getDayOfYear();
    }

    public int getDayofWeek(){
        if(date == null) return 0;
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getDayOfWeek().getValue();
    }

    public int getHourofDate(){
        if(date == null) return 0;
        LocalDateTime localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        if(tc != 0) return (localDate.getHour()%tc);
        else return localDate.getHour();
    }

    public int getMinuteofDate(){
        if(date == null) return 0;
        LocalDateTime localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        if(tc != 0) return (localDate.getMinute()%tc);
        else return localDate.getMinute();
    }

    public int getWeekOfYear(){
        if(date == null) return 0;
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        if (tc !=0 )
            return cal.get(Calendar.WEEK_OF_YEAR%tc);
        else
            return cal.get(Calendar.WEEK_OF_YEAR);
    }

    @Override
    public String toString() {
        return this.user;
    }


}
