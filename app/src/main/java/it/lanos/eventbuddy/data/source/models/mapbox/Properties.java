package it.lanos.eventbuddy.data.source.models.mapbox;

import java.util.ArrayList;

public class Properties {
    public String name;
    public String mapbox_id;
    public String feature_type;
    public String address;
    public String full_address;
    public String place_formatted;
    public Context context;
    public Coordinates coordinates;
    public String maki;
    public ArrayList<String> poi_category;
    public ArrayList<String> poi_category_ids;
    public ExternalIds external_ids;
    public Metadata metadata;
}
