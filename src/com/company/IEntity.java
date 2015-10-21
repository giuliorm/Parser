package com.company;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Easton on 19.10.2015.
 */
public interface IEntity {
    public IEntity entityFromCfg(String cfgPath) throws IOException;

    public String entityToCfg(IEntity entity) throws IOException;

    public ArrayList<String> ParsePage(String MainLink) throws Exception;

    public ArrayList<String> GetLinksFromTheMainSite(String MainLink) throws Exception;
}
