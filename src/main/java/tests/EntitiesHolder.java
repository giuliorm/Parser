package tests;

import ifmo.escience.newscrawler.Utils;
import ifmo.escience.newscrawler.entities.WebEntity;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zotova on 23.06.2016.
 */
public class EntitiesHolder {
    public static Map<String, WebEntity> existingEntities;
    static {
        existingEntities = Utils.getEntitiesList("multiConfig.json")
                .stream()
                .collect(Collectors.toMap(item -> Utils.getUrlStd(item.getEntityUrl()), item->item));
    }

}
