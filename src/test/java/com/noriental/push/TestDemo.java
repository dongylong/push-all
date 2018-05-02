package com.noriental.push;

import org.junit.Test;

import java.nio.charset.Charset;
import java.util.*;

/**
 * @author dongyl
 * @date 17:37 1/22/18
 * @project message-svr
 */
public class TestDemo {
    @Test
    public void HashDemo() {
        Set set = new HashSet();
        HashMap map = new HashMap();
        map.put("key1", "val1");
        map.put("key3", "val3");
        Object putIfAbsent = map.putIfAbsent("key3", "val2");
        Set x = map.keySet();
        System.out.println("putIfAbsent:"+putIfAbsent);
        String key1 = "key1";
        String val1 = "val1";
        String val2 = "val3";
        String key2 = "key3";

        Object merge = map.merge(key1, val1, (key, val) -> ("vv3"));
        String str = "12wqd345POU";
        byte[] xxx = str.getBytes();
        String charsetName = "GBK";
        String str1 = new String(xxx, Charset.defaultCharset());
        char dst[] = new char[5];
        str.getChars(0,2,dst,0);
        System.out.println(dst + "...dst");
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        String xx = str.toUpperCase();
        String intern = str.intern();
        System.out.println("intern:"+intern);
    }

}
