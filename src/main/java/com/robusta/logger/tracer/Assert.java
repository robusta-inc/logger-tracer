package com.robusta.logger.tracer;

public class Assert {
    public static void checkState(boolean state, String whatToRaiseWhenStateIsInvalid) {
        if(!state) {
            throw new IllegalStateException(whatToRaiseWhenStateIsInvalid);
        }
    }

    public static void notNull(Object whatNeedsToBeCheckedForNull, String whatToRaiseWhenNull) {
        if(whatNeedsToBeCheckedForNull == null) {
            throw new IllegalArgumentException(whatToRaiseWhenNull);
        }
    }

    public static void notNullOrEmpty(String toBeChecked, String whatToRaiseWhenNullOrEmpty) {
        if(toBeChecked == null || toBeChecked.equals("")) {
            throw new IllegalArgumentException(whatToRaiseWhenNullOrEmpty);
        }
    }
}
