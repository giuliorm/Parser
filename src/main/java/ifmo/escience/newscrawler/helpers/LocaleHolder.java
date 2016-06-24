package ifmo.escience.newscrawler.helpers;

import java.util.Locale;

/**
 * Created by zotova on 24.06.2016.
 */

public class LocaleHolder {

        private static class NestedLocaleHolder {
               private final static Locale rusLocale = new Locale.Builder().setLanguage("ru").build();
        }

        public static Locale getRusLocale() {
            return NestedLocaleHolder.rusLocale;
        }
     }


