package com.drunkencod.factory_symbols.signs;

public enum SignType {
    // #region Hazard
    HAZARD_STEEP_DOWNGRADE("hazard_steep_downgrade", SignCategory.HAZARD, SignSupportType.BOTTOM), // [x]
    HAZARD_STEEP_UPGRADE("hazard_steep_upgrade", SignCategory.HAZARD, SignSupportType.BOTTOM), // [x]
    HAZARD_UNCONTROLLED_INTERSECTION("hazard_uncontrolled_intersection", SignCategory.HAZARD,
            SignSupportType.BOTTOM), // [x]
    HAZARD_UNEVEN_ROAD_SURFACE("hazard_uneven_road_surface", SignCategory.HAZARD,
            SignSupportType.BOTTOM), // [x]
    HAZARD_DANGER_AHEAD("hazard_danger_ahead", SignCategory.HAZARD, SignSupportType.BOTTOM), // [x]
    HAZARD_PEDESTRIANS("hazard_pedestrians", SignCategory.HAZARD, SignSupportType.BOTTOM), // [x]
    HAZARD_NARROW_ROAD("hazard_narrow_road", SignCategory.HAZARD, SignSupportType.BOTTOM), // [x]
    HAZARD_NARROW_ROAD_LEFT("hazard_narrow_road_left", SignCategory.HAZARD, SignSupportType.BOTTOM), // [x]
    HAZARD_NARROW_ROAD_RIGHT("hazard_narrow_road_right", SignCategory.HAZARD, SignSupportType.BOTTOM), // [x]
    HAZARD_CURVE_LEFT_AHEAD("hazard_curve_left_ahead", SignCategory.HAZARD, SignSupportType.BOTTOM), // [x]
    HAZARD_CURVE_RIGHT_AHEAD("hazard_curve_right_ahead", SignCategory.HAZARD, SignSupportType.BOTTOM), // [x]
    HAZARD_DOUBLE_CURVE_LEFT_AHEAD("hazard_double_curve_left_ahead", SignCategory.HAZARD, SignSupportType.BOTTOM), // [x]
    HAZARD_DOUBLE_CURVE_RIGHT_AHEAD("hazard_double_curve_right_ahead", SignCategory.HAZARD, SignSupportType.BOTTOM), // [x]
    HAZARD_RAILWAY_CROSSING_AHEAD("hazard_railway_crossing_ahead", SignCategory.HAZARD, SignSupportType.BOTTOM), // [x]
    HAZARD_OPENING_BRIDGE_AHEAD("hazard_opening_bridge_ahead", SignCategory.HAZARD, SignSupportType.BOTTOM), // [x]
    HAZARD_ONCOMING_TRAFFIC("hazard_oncoming_traffic", SignCategory.HAZARD, SignSupportType.BOTTOM), // [x]
    HAZARD_TRAFFIC_LIGHT("hazard_traffic_light", SignCategory.HAZARD, SignSupportType.BOTTOM), // [x]
    HAZARD_ANIMALS_CROSSING("hazard_animals_crossing", SignCategory.HAZARD, SignSupportType.BOTTOM), // [x]
    HAZARD_RAILWAY_CROSSING("hazard_railway_crossing", SignCategory.HAZARD, SignSupportType.BOTTOM), // [x]
    HAZARD_RAILWAY_CROSSING_ELECTRIFIED("hazard_railway_crossing_electrified", SignCategory.HAZARD,
            SignSupportType.BOTTOM), // [x]
    HAZARD_MERGE_LEFT("hazard_merge_left", SignCategory.HAZARD, SignSupportType.VERTICAL), // [x]
    HAZARD_MERGE_RIGHT("hazard_merge_right", SignCategory.HAZARD, SignSupportType.VERTICAL), // [x]

    // #region Regulatory
    REGULATORY_STOP("regulatory_stop", SignCategory.REGULATORY), // [x]
    REGULATORY_GIVE_WAY("regulatory_give_way", SignCategory.REGULATORY, SignSupportType.BOTTOM), // [x]
    REGULATORY_PRIORITY("regulatory_priority", SignCategory.REGULATORY, SignSupportType.BOTTOM), // [x]
    REGULATORY_PRIORITY_ROAD("regulatory_priority_road", SignCategory.REGULATORY), // [x]
    REGULATORY_PRIORITY_ROAD_END("regulatory_priority_road_end", SignCategory.REGULATORY), // [x]
    REGULATORY_GO_LEFT("regulatory_go_left", SignCategory.REGULATORY), // [x]
    REGULATORY_GO_RIGHT("regulatory_go_right", SignCategory.REGULATORY), // [x]
    REGULATORY_GO_STRAIGHT("regulatory_go_straight", SignCategory.REGULATORY), // [x]
    REGULATORY_GO_LEFT_HERE("regulatory_go_left_here", SignCategory.REGULATORY), // [x]
    REGULATORY_GO_RIGHT_HERE("regulatory_go_right_here", SignCategory.REGULATORY), // [x]
    REGULATORY_ALONG_LEFT("regulatory_along_left", SignCategory.REGULATORY), // [x]
    REGULATORY_ALONG_RIGHT("regulatory_along_right", SignCategory.REGULATORY), // [x]
    REGULATORY_STRAIGHT_OR_RIGHT("regulatory_straight_or_right", SignCategory.REGULATORY), // [ ]
    REGULATORY_STRAIGHT_OR_LEFT("regulatory_straight_or_left", SignCategory.REGULATORY), // [ ]
    REGULATORY_ROUNDABOUT("regulatory_roundabout", SignCategory.REGULATORY), // [ ]
    REGULATORY_PEDESTRIANS_ONLY("regulatory_pedestrians_only", SignCategory.REGULATORY), // [ ]
    REGULATORY_NO_SPEED_LIMIT("regulatory_no_speed_limit", SignCategory.REGULATORY), // [ ]
    REGULATORY_SPEED_LIMIT_5("regulatory_speed_limit_5", SignCategory.REGULATORY), // [ ]
    REGULATORY_SPEED_LIMIT_10("regulatory_speed_limit_10", SignCategory.REGULATORY), // [ ]
    REGULATORY_SPEED_LIMIT_20("regulatory_speed_limit_20", SignCategory.REGULATORY), // [ ]
    REGULATORY_SPEED_LIMIT_30("regulatory_speed_limit_30", SignCategory.REGULATORY), // [ ]
    REGULATORY_SPEED_LIMIT_40("regulatory_speed_limit_40", SignCategory.REGULATORY), // [ ]
    REGULATORY_SPEED_LIMIT_50("regulatory_speed_limit_50", SignCategory.REGULATORY), // [ ]
    REGULATORY_SPEED_LIMIT_60("regulatory_speed_limit_60", SignCategory.REGULATORY), // [ ]
    REGULATORY_SPEED_LIMIT_70("regulatory_speed_limit_70", SignCategory.REGULATORY), // [ ]
    REGULATORY_SPEED_LIMIT_80("regulatory_speed_limit_80", SignCategory.REGULATORY), // [ ]
    REGULATORY_SPEED_LIMIT_90("regulatory_speed_limit_90", SignCategory.REGULATORY), // [ ]
    REGULATORY_SPEED_LIMIT_100("regulatory_speed_limit_100", SignCategory.REGULATORY), // [ ]
    REGULATORY_SPEED_LIMIT_110("regulatory_speed_limit_110", SignCategory.REGULATORY), // [ ]
    REGULATORY_SPEED_LIMIT_120("regulatory_speed_limit_120", SignCategory.REGULATORY), // [ ]
    REGULATORY_SPEED_LIMIT_130("regulatory_speed_limit_130", SignCategory.REGULATORY), // [ ]
    REGULATORY_SPEED_LIMIT_140("regulatory_speed_limit_140", SignCategory.REGULATORY), // [ ]
    REGULATORY_SPEED_LIMIT_150("regulatory_speed_limit_150", SignCategory.REGULATORY), // [ ]
    REGULATORY_OVERTAKING_ALLOWED("regulatory_overtaking_allowed", SignCategory.REGULATORY), // [ ]
    REGULATORY_U_TURN_LEFT("regulatory_u_turn_left", SignCategory.REGULATORY), // [ ]
    REGULATORY_U_TURN_RIGHT("regulatory_u_turn_right", SignCategory.REGULATORY), // [ ]
    REGULATORY_ONE_WAY_LEFT("regulatory_one_way_left", SignCategory.REGULATORY, SignSupportType.HORIZONTAL), // [x]
    REGULATORY_ONE_WAY_RIGHT("regulatory_one_way_right", SignCategory.REGULATORY, SignSupportType.HORIZONTAL), // [x]
    REGULATORY_PARKING("regulatory_parking", SignCategory.REGULATORY), // [x]
    REGULATORY_PARKING_GARAGE("regulatory_parking_garage", SignCategory.REGULATORY), // [x]
    REGULATORY_DEAD_END("regulatory_dead_end", SignCategory.REGULATORY), // [x]
    REGULATORY_CURVE_MARKER_LEFT("regulatory_curve_marker_left", SignCategory.REGULATORY), // [ ]
    REGULATORY_CURVE_MARKER_RIGHT("regulatory_curve_marker_right", SignCategory.REGULATORY), // [ ]
    REGULATORY_PEDESTRIAN_CROSSING("regulatory_pedestrian_crossing", SignCategory.REGULATORY), // [x]
    REGULATORY_HIGHWAY("regulatory_highway", SignCategory.REGULATORY), // [x]
    REGULATORY_HIGHWAY_END("regulatory_highway_end", SignCategory.REGULATORY), // [x]
    REGULATORY_TUNNEL("regulatory_tunnel", SignCategory.REGULATORY), // [x]
    REGULATORY_GREEN_ARROW_LEFT("regulatory_green_arrow_left", SignCategory.REGULATORY,
            SignSupportType.BACK_ONLY), // [x]
    REGULATORY_GREEN_ARROW_RIGHT("regulatory_green_arrow_right", SignCategory.REGULATORY,
            SignSupportType.BACK_ONLY), // [x]

    // #region Prohibition
    PROHIBITION_NO_ENTRY("prohibition_no_entry", SignCategory.PROHIBITION), // [x]
    PROHIBITION_NO_STOPPING("prohibition_no_stopping", SignCategory.PROHIBITION), // [ ]
    PROHIBITION_NO_PARKING("prohibition_no_parking", SignCategory.PROHIBITION), // [ ]
    PROHIBITION_NO_OVERTAKING("prohibition_no_overtaking", SignCategory.PROHIBITION), // [ ]
    PROHIBITION_U_TURN_PROHIBITED_LEFT("prohibition_u_turn_prohibited_left", SignCategory.PROHIBITION), // [ ]
    PROHIBITION_U_TURN_PROHIBITED_RIGHT("prohibition_u_turn_prohibited_right", SignCategory.PROHIBITION), // [ ]
    PROHIBITION_NO_VEHICLES("prohibition_no_vehicles", SignCategory.PROHIBITION), // [x]
    PROHIBITION_NO_HEAVY_VEHICLES("prohibition_no_heavy_vehicles", SignCategory.PROHIBITION), // [x]
    PROHIBITION_NO_CARS("prohibition_no_cars", SignCategory.PROHIBITION), // [x]
    PROHIBITION_NO_BICYCLES("prohibition_no_bicycles", SignCategory.PROHIBITION), // [x]
    PROHIBITION_NO_PEDESTRIANS("prohibition_no_pedestrians", SignCategory.PROHIBITION), // [x]

    // #region Extra
    EXTRA_NATO_BRIDGE_LOAD("extra_nato_bridge_load", SignCategory.EXTRA), // [ ]
    EXTRA_NATURE_RESERVE("extra_nature_reserve", SignCategory.EXTRA, SignSupportType.BOTTOM), // [ ]
    EXTRA_TRAFFIC_CALMED_ZONE("extra_traffic_calmed_zone", SignCategory.EXTRA,
            SignSupportType.HORIZONTAL), // [x]
    EXTRA_TRAFFIC_CALMED_ZONE_END("extra_traffic_calmed_zone_end", SignCategory.EXTRA,
            SignSupportType.HORIZONTAL), // [x]
    EXTRA_TRANSIT_STOP("extra_transit_stop", SignCategory.EXTRA,
            SignSupportType.BACK_ONLY), // [ ]
    EXTRA_TRANSIT_STOP_H("extra_transit_stop_h", SignCategory.EXTRA,
            SignSupportType.BACK_ONLY), // [ ]
    EXTRA_GAS_STATION("extra_gas_station", SignCategory.EXTRA,
            SignSupportType.BACK_ONLY), // [ ]
    EXTRA_RECHARGING_STATION("extra_recharging_station", SignCategory.EXTRA,
            SignSupportType.BACK_ONLY), // [ ]
    EXTRA_EMERGENCY_STOPPING_BAY("extra_emergency_stopping_bay", SignCategory.EXTRA,
            SignSupportType.BACK_ONLY), // [ ]
    EXTRA_EMERGENCY_TELEPHONE("extra_emergency_telephone", SignCategory.EXTRA,
            SignSupportType.BACK_ONLY), // [ ]
    EXTRA_INFORMATION("extra_information", SignCategory.EXTRA,
            SignSupportType.BACK_ONLY), // [ ]
    EXTRA_MOTEL("extra_motel", SignCategory.EXTRA,
            SignSupportType.BACK_ONLY), // [ ]
    EXTRA_INN("extra_inn", SignCategory.EXTRA,
            SignSupportType.BACK_ONLY), // [ ]
    EXTRA_TOILET("extra_toilet", SignCategory.EXTRA,
            SignSupportType.BACK_ONLY); // [ ]

    private final String id;
    private final SignCategory category;
    private final SignSupportType supportType;

    SignType(String id, SignCategory category, SignSupportType supportType) {
        this.id = id;
        this.category = category;
        this.supportType = supportType;
    }

    SignType(String id, SignCategory category) {
        this(id, category, SignSupportType.ANY);
    }

    /** Registry path segment, e.g. {@code "0"} or {@code "letter_a"}. */
    public String getId() {
        return id;
    }

    public SignCategory getCategory() {
        return category;
    }

    public SignSupportType getSupportType() {
        return supportType;
    }
}
