package edu.photo_school.course.domain.enums;

public enum CourseFormat {

    ONLINE,
    OFFLINE,
    HYBRID;

    public boolean isOnline() {
        return this == ONLINE || this == HYBRID;
    }

    public boolean isOffline() {
        return this == OFFLINE || this == HYBRID;
    }
}
