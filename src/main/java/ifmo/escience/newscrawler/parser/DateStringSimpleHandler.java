package ifmo.escience.newscrawler.parser;

import java.text.DateFormat;

/**
 * Created by JuriaSan on 27.06.2016.
 */
public class DateStringSimpleHandler extends  DateStringHandler {


   @Override
    public String handle(String date) {
       return date.toLowerCase().trim();
   }
}
