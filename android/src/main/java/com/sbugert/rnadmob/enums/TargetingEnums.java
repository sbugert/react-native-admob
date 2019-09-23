package com.sbugert.rnadmob.enums;

public class TargetingEnums {

    public enum TargetingTypes {
        CUSTOMTARGETING,
        CATEGORYEXCLUSIONS,
        KEYWORDS,
        GENDER,
        BIRTHDAY,
        CHILDDIRECTEDTREATMENT,
        CONTENTURL,
        PUBLISHERPROVIDEDID,
        LOCATION
    }

    public static String getEnumString(TargetingTypes targetingType) {
        switch (targetingType) {
            case CUSTOMTARGETING:
                return "customTargeting";
            case CATEGORYEXCLUSIONS:
                return "categoryExclusions";
            case KEYWORDS:
                return "keywords";
            case GENDER:
                return "gender";
            case BIRTHDAY:
                return "birthday";
            case CHILDDIRECTEDTREATMENT:
                return "childDirectedTreatment";
            case CONTENTURL:
                return "contentURL";
            case PUBLISHERPROVIDEDID:
                return "publisherProvidedID";
            case LOCATION:
                return "location";
            default:
                return "";
        }
    }
}
