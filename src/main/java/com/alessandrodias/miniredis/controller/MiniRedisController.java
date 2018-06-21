package com.alessandrodias.miniredis.controller;

import com.alessandrodias.miniredis.service.MiniRedisDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/miniredis")
public class MiniRedisController {

    private static final String NEW_LINE = "\n";
    private MiniRedisDatabase miniRedisDatabase;

    @Autowired
    public MiniRedisController(MiniRedisDatabase miniRedisDatabase) {
        this.miniRedisDatabase = miniRedisDatabase;
    }

    @RequestMapping(value="/{key}",method=RequestMethod.PUT, params="value")
    public String set(@PathVariable(value = "key") String key, @RequestParam(value="value") String value){
        return miniRedisDatabase.set(key, value) + NEW_LINE;
    }

    @RequestMapping(value="/{key}",method=RequestMethod.PUT, params={"value","expirationMode","expirationValue"})
    public String set(@PathVariable(value = "key") String key, @RequestParam(value="value") String value,
                      @RequestParam(value="expirationMode") String expirationMode,
                      @RequestParam(value="expirationValue") Integer expirationValue){
        if (expirationMode.toUpperCase().equals("EX")) {
            return miniRedisDatabase.set(key, value, expirationValue) + NEW_LINE;
        } else {
            return "(nil)"+ NEW_LINE;
        }
    }

    @RequestMapping(value="/{key}", method=RequestMethod.GET)
    public String get(@PathVariable(value = "key") String key){
        return miniRedisDatabase.get(key) + NEW_LINE;
    }

    @RequestMapping(value="/{key}", method=RequestMethod.DELETE)
    public String delete(@PathVariable(value = "key") String key){
        return miniRedisDatabase.del(key) + NEW_LINE;
    }

    @RequestMapping(value="/{key}",method=RequestMethod.POST)
    public String incr(@PathVariable(value = "key") String key){
        return miniRedisDatabase.incr(key) + NEW_LINE;
    }

    @RequestMapping(value="/dbsize", method=RequestMethod.GET)
    public String dbsize(){
        return miniRedisDatabase.dbSize() + NEW_LINE;
    }
}
