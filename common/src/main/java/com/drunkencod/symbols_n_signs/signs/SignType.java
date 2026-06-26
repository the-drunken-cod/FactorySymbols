package com.drunkencod.symbols_n_signs.signs;

import com.drunkencod.symbols_n_signs.Constants;

import net.minecraft.resources.ResourceLocation;

public enum SignType {
        // #region Hazard
        HAZARD_STEEP_DOWNGRADE("hazard_steep_downgrade", SignCategory.HAZARD, SignSupportType.BOTTOM),
        HAZARD_STEEP_UPGRADE("hazard_steep_upgrade", SignCategory.HAZARD, SignSupportType.BOTTOM),
        HAZARD_UNCONTROLLED_INTERSECTION("hazard_uncontrolled_intersection", SignCategory.HAZARD,
                        SignSupportType.BOTTOM),
        HAZARD_UNEVEN_ROAD_SURFACE("hazard_uneven_road_surface", SignCategory.HAZARD,
                        SignSupportType.BOTTOM),
        HAZARD_DANGER_AHEAD("hazard_danger_ahead", SignCategory.HAZARD, SignSupportType.BOTTOM),
        HAZARD_PEDESTRIANS("hazard_pedestrians", SignCategory.HAZARD, SignSupportType.BOTTOM),
        HAZARD_NARROW_ROAD("hazard_narrow_road", SignCategory.HAZARD, SignSupportType.BOTTOM),
        HAZARD_NARROW_ROAD_LEFT("hazard_narrow_road_left", SignCategory.HAZARD, SignSupportType.BOTTOM),
        HAZARD_NARROW_ROAD_RIGHT("hazard_narrow_road_right", SignCategory.HAZARD, SignSupportType.BOTTOM),
        HAZARD_CURVE_LEFT_AHEAD("hazard_curve_left_ahead", SignCategory.HAZARD, SignSupportType.BOTTOM),
        HAZARD_CURVE_RIGHT_AHEAD("hazard_curve_right_ahead", SignCategory.HAZARD, SignSupportType.BOTTOM),
        HAZARD_DOUBLE_CURVE_LEFT_AHEAD("hazard_double_curve_left_ahead", SignCategory.HAZARD, SignSupportType.BOTTOM),
        HAZARD_DOUBLE_CURVE_RIGHT_AHEAD("hazard_double_curve_right_ahead", SignCategory.HAZARD, SignSupportType.BOTTOM),
        HAZARD_RAILWAY_CROSSING_AHEAD("hazard_railway_crossing_ahead", SignCategory.HAZARD, SignSupportType.BOTTOM),
        HAZARD_OPENING_BRIDGE_AHEAD("hazard_opening_bridge_ahead", SignCategory.HAZARD, SignSupportType.BOTTOM),
        HAZARD_ONCOMING_TRAFFIC("hazard_oncoming_traffic", SignCategory.HAZARD, SignSupportType.BOTTOM),
        HAZARD_TRAFFIC_LIGHT("hazard_traffic_light", SignCategory.HAZARD, SignSupportType.BOTTOM),
        HAZARD_CROSSWIND_LEFT("hazard_crosswind_left", SignCategory.HAZARD, SignSupportType.BOTTOM),
        HAZARD_CROSSWIND_RIGHT("hazard_crosswind_right", SignCategory.HAZARD, SignSupportType.BOTTOM),
        HAZARD_SKID_RISK("hazard_skid_risk", SignCategory.HAZARD, SignSupportType.BOTTOM),
        HAZARD_FLIGHT_OPERATION("hazard_flight_operation", SignCategory.HAZARD, SignSupportType.BOTTOM),
        HAZARD_FALLING_ROCKS_LEFT("hazard_falling_rocks_left", SignCategory.HAZARD, SignSupportType.BOTTOM),
        HAZARD_FALLING_ROCKS_RIGHT("hazard_falling_rocks_right", SignCategory.HAZARD, SignSupportType.BOTTOM),
        HAZARD_GRAVEL_ROAD("hazard_gravel_road", SignCategory.HAZARD, SignSupportType.BOTTOM),
        HAZARD_ANIMALS_CROSSING("hazard_animals_crossing", SignCategory.HAZARD, SignSupportType.BOTTOM),
        HAZARD_RAILWAY_CROSSING("hazard_railway_crossing", SignCategory.HAZARD, SignSupportType.BACK),
        HAZARD_RAILWAY_CROSSING_ELECTRIFIED("hazard_railway_crossing_electrified", SignCategory.HAZARD,
                        SignSupportType.BACK),

        // #region Regulatory
        REGULATORY_STOP("regulatory_stop", SignCategory.REGULATORY),
        REGULATORY_GIVE_WAY("regulatory_give_way", SignCategory.REGULATORY, SignSupportType.BOTTOM),
        REGULATORY_PRIORITY("regulatory_priority", SignCategory.REGULATORY, SignSupportType.BOTTOM),
        REGULATORY_PRIORITY_ROAD("regulatory_priority_road", SignCategory.REGULATORY),
        REGULATORY_PRIORITY_ROAD_END("regulatory_priority_road_end", SignCategory.REGULATORY),
        REGULATORY_PRIORITY_ROAD_COURSE_LEFT_CROSS("regulatory_priority_road_course_left_cross",
                        SignCategory.REGULATORY, SignSupportType.BACK),
        REGULATORY_PRIORITY_ROAD_COURSE_LEFT_T_HORIZONTAL("regulatory_priority_road_course_left_t_horizontal",
                        SignCategory.REGULATORY, SignSupportType.BACK),
        REGULATORY_PRIORITY_ROAD_COURSE_LEFT_T_VERTICAL("regulatory_priority_road_course_left_t_vertical",
                        SignCategory.REGULATORY, SignSupportType.BACK),
        REGULATORY_PRIORITY_ROAD_COURSE_RIGHT_CROSS("regulatory_priority_road_course_right_cross",
                        SignCategory.REGULATORY, SignSupportType.BACK),
        REGULATORY_PRIORITY_ROAD_COURSE_RIGHT_T_HORIZONTAL("regulatory_priority_road_course_right_t_horizontal",
                        SignCategory.REGULATORY, SignSupportType.BACK),
        REGULATORY_PRIORITY_ROAD_COURSE_RIGHT_T_VERTICAL("regulatory_priority_road_course_right_t_vertical",
                        SignCategory.REGULATORY, SignSupportType.BACK),
        REGULATORY_GO_LEFT("regulatory_go_left", SignCategory.REGULATORY),
        REGULATORY_GO_RIGHT("regulatory_go_right", SignCategory.REGULATORY),
        REGULATORY_GO_STRAIGHT("regulatory_go_straight", SignCategory.REGULATORY),
        REGULATORY_GO_LEFT_HERE("regulatory_go_left_here", SignCategory.REGULATORY),
        REGULATORY_GO_RIGHT_HERE("regulatory_go_right_here", SignCategory.REGULATORY),
        REGULATORY_ALONG_LEFT("regulatory_along_left", SignCategory.REGULATORY),
        REGULATORY_ALONG_RIGHT("regulatory_along_right", SignCategory.REGULATORY),
        REGULATORY_STRAIGHT_OR_RIGHT("regulatory_straight_or_right", SignCategory.REGULATORY),
        REGULATORY_STRAIGHT_OR_LEFT("regulatory_straight_or_left", SignCategory.REGULATORY),
        REGULATORY_LEFT_OR_RIGHT("regulatory_left_or_right", SignCategory.REGULATORY),
        REGULATORY_MERGE_LEFT("regulatory_merge_left", SignCategory.REGULATORY, SignSupportType.VERTICAL),
        REGULATORY_MERGE_RIGHT("regulatory_merge_right", SignCategory.REGULATORY, SignSupportType.VERTICAL),
        REGULATORY_ROUNDABOUT_CCW("regulatory_roundabout_ccw", SignCategory.REGULATORY),
        REGULATORY_ROUNDABOUT_CW("regulatory_roundabout_cw", SignCategory.REGULATORY),
        REGULATORY_SIDEWALK("regulatory_sidewalk", SignCategory.REGULATORY),
        REGULATORY_SIDEWALK_AND_BIKE_PATH_SHARED("regulatory_sidewalk_and_bicycle_path_shared",
                        SignCategory.REGULATORY),
        REGULATORY_SIDEWALK_AND_BIKE_PATH_SEPARATE_LEFT("regulatory_sidewalk_and_bicycle_path_separate_left",
                        SignCategory.REGULATORY),
        REGULATORY_SIDEWALK_AND_BIKE_PATH_SEPARATE_RIGHT("regulatory_sidewalk_and_bicycle_path_separate_right",
                        SignCategory.REGULATORY),
        REGULATORY_PRIORITY_OF_ONCOMING_TRAFFIC("regulatory_priority_of_oncoming_traffic", SignCategory.REGULATORY),
        REGULATORY_PRIORITY_OVER_ONCOMING_TRAFFIC("regulatory_priority_over_oncoming_traffic", SignCategory.REGULATORY),
        REGULATORY_END_OF_RESTRICTIONS("regulatory_end_of_restrictions", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_5("regulatory_speed_limit_5", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_10("regulatory_speed_limit_10", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_20("regulatory_speed_limit_20", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_30("regulatory_speed_limit_30", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_40("regulatory_speed_limit_40", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_50("regulatory_speed_limit_50", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_60("regulatory_speed_limit_60", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_67("regulatory_speed_limit_67", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_69("regulatory_speed_limit_69", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_70("regulatory_speed_limit_70", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_80("regulatory_speed_limit_80", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_90("regulatory_speed_limit_90", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_100("regulatory_speed_limit_100", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_110("regulatory_speed_limit_110", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_120("regulatory_speed_limit_120", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_130("regulatory_speed_limit_130", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_140("regulatory_speed_limit_140", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_150("regulatory_speed_limit_150", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_160("regulatory_speed_limit_160", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_170("regulatory_speed_limit_170", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_180("regulatory_speed_limit_180", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_190("regulatory_speed_limit_190", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_200("regulatory_speed_limit_200", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_210("regulatory_speed_limit_210", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_220("regulatory_speed_limit_220", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_230("regulatory_speed_limit_230", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_240("regulatory_speed_limit_240", SignCategory.REGULATORY),
        REGULATORY_SPEED_LIMIT_250("regulatory_speed_limit_250", SignCategory.REGULATORY),
        REGULATORY_OVERTAKING_ALLOWED("regulatory_overtaking_allowed", SignCategory.REGULATORY),
        REGULATORY_U_TURN_LEFT("regulatory_u_turn_left", SignCategory.REGULATORY),
        REGULATORY_U_TURN_RIGHT("regulatory_u_turn_right", SignCategory.REGULATORY),
        REGULATORY_ONE_WAY_LEFT("regulatory_one_way_left", SignCategory.REGULATORY, SignSupportType.HORIZONTAL),
        REGULATORY_ONE_WAY_RIGHT("regulatory_one_way_right", SignCategory.REGULATORY, SignSupportType.HORIZONTAL),
        REGULATORY_PARKING("regulatory_parking", SignCategory.REGULATORY),
        REGULATORY_PARKING_GARAGE("regulatory_parking_garage", SignCategory.REGULATORY),
        REGULATORY_DEAD_END("regulatory_dead_end", SignCategory.REGULATORY),
        REGULATORY_CURVE_MARKER_LEFT("regulatory_curve_marker_left", SignCategory.REGULATORY),
        REGULATORY_CURVE_MARKER_RIGHT("regulatory_curve_marker_right", SignCategory.REGULATORY),
        REGULATORY_STRIPE_MARKER_SINGLE_LEFT("regulatory_stripe_marker_single_left", SignCategory.REGULATORY,
                        SignSupportType.VERTICAL),
        REGULATORY_STRIPE_MARKER_DOUBLE_LEFT("regulatory_stripe_marker_double_left", SignCategory.REGULATORY,
                        SignSupportType.VERTICAL),
        REGULATORY_STRIPE_MARKER_TRIPLE_LEFT("regulatory_stripe_marker_triple_left", SignCategory.REGULATORY,
                        SignSupportType.VERTICAL),
        REGULATORY_STRIPE_MARKER_SINGLE_RIGHT("regulatory_stripe_marker_single_right", SignCategory.REGULATORY,
                        SignSupportType.VERTICAL),
        REGULATORY_STRIPE_MARKER_DOUBLE_RIGHT("regulatory_stripe_marker_double_right", SignCategory.REGULATORY,
                        SignSupportType.VERTICAL),
        REGULATORY_STRIPE_MARKER_TRIPLE_RIGHT("regulatory_stripe_marker_triple_right", SignCategory.REGULATORY,
                        SignSupportType.VERTICAL),
        REGULATORY_PEDESTRIAN_CROSSING("regulatory_pedestrian_crossing", SignCategory.REGULATORY),
        REGULATORY_HIGHWAY("regulatory_highway", SignCategory.REGULATORY),
        REGULATORY_HIGHWAY_END("regulatory_highway_end", SignCategory.REGULATORY),
        REGULATORY_TUNNEL("regulatory_tunnel", SignCategory.REGULATORY),
        REGULATORY_GREEN_ARROW_LEFT("regulatory_green_arrow_left", SignCategory.REGULATORY,
                        SignSupportType.BACK),
        REGULATORY_GREEN_ARROW_RIGHT("regulatory_green_arrow_right", SignCategory.REGULATORY,
                        SignSupportType.BACK),
        REGULATORY_TRAFFIC_CALMED_ZONE("regulatory_traffic_calmed_zone", SignCategory.REGULATORY,
                        SignSupportType.HORIZONTAL),
        REGULATORY_TRAFFIC_CALMED_ZONE_END("regulatory_traffic_calmed_zone_end", SignCategory.REGULATORY,
                        SignSupportType.HORIZONTAL),
        REGULATORY_NATIONAL_SPEED_LIMIT_UK("regulatory_national_speed_limit_uk", SignCategory.REGULATORY),

        // #region Prohibition
        PROHIBITION_NO_ENTRY("prohibition_no_entry", SignCategory.PROHIBITION),
        PROHIBITION_NO_STOPPING("prohibition_no_stopping", SignCategory.PROHIBITION),
        PROHIBITION_NO_STOPPING_LEFT("prohibition_no_stopping_left", SignCategory.PROHIBITION),
        PROHIBITION_NO_STOPPING_RIGHT("prohibition_no_stopping_right", SignCategory.PROHIBITION),
        PROHIBITION_NO_STOPPING_LEFT_RIGHT("prohibition_no_stopping_left_right", SignCategory.PROHIBITION),
        PROHIBITION_NO_PARKING("prohibition_no_parking", SignCategory.PROHIBITION),
        PROHIBITION_NO_PARKING_LEFT("prohibition_no_parking_left", SignCategory.PROHIBITION),
        PROHIBITION_NO_PARKING_RIGHT("prohibition_no_parking_right", SignCategory.PROHIBITION),
        PROHIBITION_NO_PARKING_LEFT_RIGHT("prohibition_no_parking_left_right", SignCategory.PROHIBITION),
        PROHIBITION_NO_OVERTAKING("prohibition_no_overtaking", SignCategory.PROHIBITION),
        PROHIBITION_NO_U_TURN_LEFT("prohibition_no_u_turn_left", SignCategory.PROHIBITION),
        PROHIBITION_NO_U_TURN_RIGHT("prohibition_no_u_turn_right", SignCategory.PROHIBITION),
        PROHIBITION_NO_VEHICLES("prohibition_no_vehicles", SignCategory.PROHIBITION),
        PROHIBITION_NO_HEAVY_VEHICLES("prohibition_no_heavy_vehicles", SignCategory.PROHIBITION),
        PROHIBITION_NO_CARS("prohibition_no_cars", SignCategory.PROHIBITION),
        PROHIBITION_NO_BICYCLES("prohibition_no_bicycles", SignCategory.PROHIBITION),
        PROHIBITION_NO_PEDESTRIANS("prohibition_no_pedestrians", SignCategory.PROHIBITION),
        PROHIBITION_NO_UNAUTHORIZED_PERSONS("prohibition_no_unauthorized_persons", SignCategory.PROHIBITION),

        // #region Extra
        EXTRA_NATO_BRIDGE_LOAD("extra_nato_bridge_load", SignCategory.EXTRA),
        EXTRA_NATURE_RESERVE_EAGLE("extra_nature_reserve_eagle", SignCategory.EXTRA, SignSupportType.BOTTOM),
        EXTRA_NATURE_RESERVE_FISH("extra_nature_reserve_fish", SignCategory.EXTRA, SignSupportType.BOTTOM),
        EXTRA_NATURE_RESERVE_OWL("extra_nature_reserve_owl", SignCategory.EXTRA, SignSupportType.BOTTOM),
        EXTRA_TRANSIT_STOP("extra_transit_stop", SignCategory.EXTRA),
        EXTRA_TRANSIT_STOP_H("extra_transit_stop_h", SignCategory.EXTRA),
        EXTRA_GAS_STATION("extra_gas_station", SignCategory.EXTRA),
        EXTRA_RECHARGING_STATION("extra_recharging_station", SignCategory.EXTRA),
        EXTRA_EMERGENCY_STOPPING_BAY_LEFT("extra_emergency_stopping_bay_left", SignCategory.EXTRA),
        EXTRA_EMERGENCY_STOPPING_BAY_RIGHT("extra_emergency_stopping_bay_right", SignCategory.EXTRA),
        EXTRA_EMERGENCY_TELEPHONE("extra_emergency_telephone", SignCategory.EXTRA),
        EXTRA_INFORMATION("extra_information", SignCategory.EXTRA),
        EXTRA_MOTEL("extra_motel", SignCategory.EXTRA),
        EXTRA_INN("extra_inn", SignCategory.EXTRA),
        EXTRA_TOILET("extra_toilet", SignCategory.EXTRA),
        EXTRA_SPEED_CAMERA("extra_speed_camera", SignCategory.EXTRA),
        EXTRA_EMERGENCY_EXIT_HERE("extra_emergency_exit_here", SignCategory.EXTRA, SignSupportType.HORIZONTAL),
        EXTRA_EMERGENCY_EXIT_AHEAD("extra_emergency_exit_ahead", SignCategory.EXTRA, SignSupportType.HORIZONTAL),
        EXTRA_EMERGENCY_EXIT_LEFT("extra_emergency_exit_left", SignCategory.EXTRA, SignSupportType.HORIZONTAL),
        EXTRA_EMERGENCY_EXIT_RIGHT("extra_emergency_exit_right", SignCategory.EXTRA, SignSupportType.HORIZONTAL),
        EXTRA_EMERGENCY_ASSEMBLY_POINT("extra_emergency_assembly_point", SignCategory.EXTRA);

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

        /** Item texture location, e.g. {@code symbols_n_signs:item/sign/hazard/steep_downgrade}. */
        public ResourceLocation getTextureLocation() {
                String catId = category.getId();
                String stripped = id.startsWith(catId + "_") ? id.substring(catId.length() + 1) : id;
                return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "item/sign/" + catId + "/" + stripped);
        }
}
