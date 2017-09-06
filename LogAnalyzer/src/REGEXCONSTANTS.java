/**
 * Author        : Rohan Singh (rohans1)
 * Date          : 08/01/2017
 * Last Modified : 08/05/2017 13:40:00
 * Description   : This is an interface for REGEX
 */
public interface REGEXCONSTANTS {
    String RECORD_SEPARATOR     = "============";
    String RCS_FILE_LINE        = "(.*): (.*),(.*)";
    String REVISION_SEPARATOR   = "-----------";
    String REVISION_LINE        = "(revision) (\\d\\.\\d)";
    String ATTRIBUTE_SEP        = ";";
    String FIELD_SEP            = ": ";
    String BLANK                = " ";
    String REVISION_DATE_FMT    = "yyyy-MM-dd'T'HH:mm:ssZ";
    String TIMECHUNK1           = "(.*)(yyyy|year|years|months|month|weekday|hour|hours|minutes|minute|min|weeks)(.*)";
    String TIMECHUNK2           = "(.*)([0-9]+)(.*)(yyyy|year|years|months|month|days|day|hour|hours|min|minute|weeks|)(.*)";
}
