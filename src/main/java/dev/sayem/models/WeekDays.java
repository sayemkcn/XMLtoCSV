package dev.sayem.models;

import java.util.Arrays;

public enum WeekDays {
    SUNDAY(1, "Sunday"),
    MONDAY(2, "Monday"),
    TUESDAY(3, "Tuesday"),
    WEDNESDAY(4, "Wednesday"),
    THURSDAY(5, "Thursday"),
    FRIDAY(6, "Friday"),
    SATURDAY(7, "Saturday");

    private int number;
    private String title;

    WeekDays(int number, String title) {
        this.number = number;
        this.title = title;
    }

    public int getNumber() {
        return number;
    }

    public String getTitle() {
        return title;
    }

    public static WeekDays fromNumber(int number){
        return Arrays.stream(values()).filter(v->v.number==number)
                .findFirst().orElse(null);
    }
}
