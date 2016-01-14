package com.teamlinking.chains.storage;

/**
 * Created by admin on 16/1/13.
 */
public class AvItem {
    /*
      1 = {java.util.HashMap$Node@13122} "returnOld" -> "1"
  2 = {java.util.HashMap$Node@13123} "cmd" -> "avthumb/wav"
  3 = {java.util.HashMap$Node@13124} "hash" -> "FouncjDqFmj2eR2jN1WPT3XY61Gm"
  4 = {java.util.HashMap$Node@13125} "key" -> "gmmEuoOYU3CGJCsvcgJYR6pFb4w=/Fl3aw3pAmPPL2kAGmgWOAV0Y6bqi"
  5 = {java.util.HashMap$Node@13126} "desc" -> "The fop was completed successfully"
       */
    String returnOld;
    String cmd;
    String hash;
    String key;
    String desc;
    int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getReturnOld() {
        return returnOld;
    }

    public void setReturnOld(String returnOld) {
        this.returnOld = returnOld;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
