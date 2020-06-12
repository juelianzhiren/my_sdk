package com.ztq.sdk;

import android.net.Uri;
import android.provider.BaseColumns;

public class LilyUser {

    public static class User {
        public static final Uri CONTENT_URI = Uri.parse("content://com.ianc.lilyprovider/" + "customer");
    }

    public static class UserColumns implements BaseColumns {
        public static String NAME = "name";
        public static String PASSWORD = "password";
    }

    public static class ShopList {
        public static final Uri CONTENT_URI = Uri.parse("content://com.ianc.lilyprovider/" + "shoplist");
    }

    public static class ShopListColumns implements BaseColumns {
        public static String USER_ID = "user_id";
        public static String PRODUCT = "product";
        public static String DATE = "date";
    }
}
