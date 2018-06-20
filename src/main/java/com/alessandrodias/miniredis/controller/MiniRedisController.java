package com.alessandrodias.miniredis.controller;

import com.alessandrodias.miniredis.model.MiniRedisDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/miniredis")
public class MiniRedisController {

    @Autowired
    private HttpSession httpSession;

    private static final String NEW_LINE = "\n";
    private MiniRedisDatabase miniRedisDatabase;

    @Autowired
    public MiniRedisController(MiniRedisDatabase miniRedisDatabase) {
        this.miniRedisDatabase = miniRedisDatabase;
    }

    @RequestMapping(value="/{key}",method=RequestMethod.PUT)
    public String set(@PathVariable(value = "key") String key, @RequestParam(value="value") String value){
        System.out.println(httpSession.getId());
        return miniRedisDatabase.set(key, value) + NEW_LINE;
    }

    @RequestMapping(value="/{key}", method=RequestMethod.GET)
    public String get(@PathVariable(value = "key") String key){
        System.out.println(httpSession.getId());
        return miniRedisDatabase.get(key) + NEW_LINE;
    }

    @RequestMapping(value="/{key}", method=RequestMethod.DELETE)
    public String delete(@PathVariable(value = "key") String key){
        System.out.println(httpSession.getId());
        return miniRedisDatabase.del(key) + NEW_LINE;
    }

    @RequestMapping(value="/{key}/incr",method=RequestMethod.PUT)
    public String incr(@PathVariable(value = "key") String key){
        System.out.println(httpSession.getId());
        return miniRedisDatabase.incr(key) + NEW_LINE;
    }

    @RequestMapping(value="/dbsize", method=RequestMethod.GET)
    public String dbsize(){
        System.out.println(httpSession.getId());
        return miniRedisDatabase.dbSize() + NEW_LINE;
    }

}
