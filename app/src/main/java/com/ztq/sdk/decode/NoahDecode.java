
package com.ztq.sdk.decode;

public abstract class NoahDecode implements IDecode {

    protected String content;

    public NoahDecode(String content) {
        super();
        this.content = content;
    }

    public abstract String getKey();

    @Override
    public String decode() {
        // TODO Auto-generated method stub
        if (content == null) {
            return null;
        }

        String key = getKey();
        String[] words = content.split("a");
        if (words == null) {
            return content;
        }
        int n = words.length;
        String result = "";
        int keyL = key.length();
        try {
            for (int i = 0; i < n; i++) {
                int r = i % keyL;
                int s = Integer.valueOf(words[i]);
                int ss = key.charAt(r);
                int w = (s - (ss * i)) + (ss * n);
                result += fromCharCode(w);
            }

        } catch (Exception e) {
            // TODO: handle exception
            return content;
        }
        // log("content = " + content);
        // log("result = " + result);
        return result;
    }

    private static String fromCharCode(int... codePoints) {
        StringBuilder builder = new StringBuilder(codePoints.length);
        for (int codePoint : codePoints) {
            builder.append(Character.toChars(codePoint));
        }
        return builder.toString();
    }

    final static String tag = NoahDecode.class.getSimpleName();
}