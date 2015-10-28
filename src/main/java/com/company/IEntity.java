package com.company;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Easton on 19.10.2015.
 */
public interface IEntity {
    public IEntity getEntityFromConfig(String cfgPath) throws IOException;

    public String saveToConfig(IEntity entity) throws IOException;

    //public ArrayList<String> parsePage(String MainLink) throws Exception;

    //public ArrayList<String> getLinksFromTheMainSite(String MainLink) throws Exception;

    public ArrayList<WebEntity> getEntityListFromConfig(String cfgPath) throws IOException;
}