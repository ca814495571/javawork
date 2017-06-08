package com.cqfc.common.service;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;

import static net.spy.memcached.AddrUtil.*;

/**
 * @author: giantspider@126.com
 */

public class MemcachedService {

    private static MemcachedClient memcachedClient = null;

    static {
        try {
            memcachedClient = new MemcachedClient(AddrUtil.getAddresses("203.195.182.109:11211"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MemcachedService() {}

    public static MemcachedClient getMemcachedClient() {
        return memcachedClient;
    }

    public static void main(String[] args) {
        MemcachedClient mcc = MemcachedService.getMemcachedClient();
        mcc.set("someKey", 3600, "somevalue");
        String value = (String)mcc.get("someKey");
        System.out.println(value);
    }

}
