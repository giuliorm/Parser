package ifmo.escience.newscrawler.parser.date.handlers;

import java.util.ArrayList;

/**
 * Created by JuriaSan on 27.06.2016.
 */
public class DateStringSymbolsHandler extends DateStringHandler {

    private static ArrayList<String> symbols = new ArrayList<>();
    static {
        symbols.add(",*");
        symbols.add("\\+*");
        symbols.add("\\-*");
        symbols.add("\\.*");
        symbols.add("\\[*");
        symbols.add("\\]*");
        symbols.add("\\(*");
        symbols.add("\\)*");
        symbols.add("\"*");
        symbols.add("\\*");
        symbols.add( "[ ]{2,}");
    }
    @Override
    public String handle(String date) {
        for (String symbolPattern : symbols) {
            date = date.replaceAll(symbolPattern, "");
        }
        return date;
    }
}
