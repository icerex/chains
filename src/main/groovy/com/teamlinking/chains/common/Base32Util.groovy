package com.teamlinking.chains.common

import com.google.common.base.Charsets
import com.google.common.io.BaseEncoding

/**
 * Created by daniel on 15/10/14.
 */
class Base32Util {

    public static String enCode32(String s){

        def encoding = new BaseEncoding.StandardBaseEncoding("base32()", Constants.ENCODE_32_SALT, null);

        return encoding.encode(s.getBytes(Charsets.UTF_8))
    }

    public static String deCode32(String s){

        def encoding = new BaseEncoding.StandardBaseEncoding("base32()", Constants.ENCODE_32_SALT, null);

        return  new String(encoding.decode(s))
    }
}
