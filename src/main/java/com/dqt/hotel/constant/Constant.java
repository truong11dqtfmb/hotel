package com.dqt.hotel.constant;

public class Constant {
    public static final Integer ACTIVE = 1;
    public static final Integer INACTIVE = 0;

    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public static final String CREATED_DATE = "createdDate";
    public static final String CREATED_AT = "createAt";

    public static final String UPLOAD_DIR = "uploads";
    public static final long MAX_FILE_SIZE = 20 * 1024 * 1024;
    public static final String[] ALLOWED_EXTENSIONS = {"jpg", "jpeg", "png", "gif"};

    public static final Integer TYPE_HOTEL = 1;
    public static final Integer TYPE_ROOM = 2;
    public static final Integer TYPE_PREVIEW = 2;
}
