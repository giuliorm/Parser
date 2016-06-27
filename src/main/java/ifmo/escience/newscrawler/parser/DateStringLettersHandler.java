package ifmo.escience.newscrawler.parser;

/**
 * Created by JuriaSan on 27.06.2016.
 */
public class DateStringLettersHandler extends DateStringHandler {

    private static final String LETTERS = "[À-ß¨][-À-ÿ¨¸]+";
    //how i need to be with russian and english letters?
    @Override
    public String handle(String date) {
        String rez = date.replaceAll(LETTERS, "");
        return rez;
    }
}
