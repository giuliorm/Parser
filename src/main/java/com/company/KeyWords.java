package com.company;

import java.io.*;


public class KeyWords {

    public static BufferedReader inp;
    public static BufferedWriter out;

    public static String[] doIt(String news) throws IOException, InterruptedException {

        String cmd = "python3 " + System.getProperty("user.dir") + "/src/main/python/KeyWords.py";
        Process p = Runtime.getRuntime().exec(cmd);
        inp = new BufferedReader( new InputStreamReader(p.getInputStream()) );
        out = new BufferedWriter( new OutputStreamWriter(p.getOutputStream()) );
        String ret;
        try {
            out.write(news + "\n");
            out.flush();
            ret = inp.readLine();
            p.waitFor();
            inp.close();
            out.close();
            System.out.println(ret);
            return ret.split(" ");
        }
        catch (Exception err) {
        }

        return null;
    }
}
