
package com.ztq.sdk.decode;

public class NotExcellentUrlDecode extends NoahDecode {

    final String key = "NAOHEDUCOM1234567890QWERTYUIOPASDFGHJKLZXCVBNM201505081112mnbvcxzlkjhgfdsapoiuytrewq)(*&^%$#@!~";

    public NotExcellentUrlDecode(String content) {
        super(content);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String getKey() {
        // TODO Auto-generated method stub
        return key;
    }

}
