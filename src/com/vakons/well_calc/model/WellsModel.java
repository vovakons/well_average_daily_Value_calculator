package com.vakons.well_calc.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WellsModel {
    private Map<String, WellModel> wells = new HashMap<>();

    public WellModel get(String id) {
        if(!wells.containsKey(id)) {
            var model = new WellModel();
            model.objectId = id;
            wells.put(id, model);
        }
        return wells.get(id);
    }

    public Collection<WellModel> getModels() {
        return wells.values();
    }
}
