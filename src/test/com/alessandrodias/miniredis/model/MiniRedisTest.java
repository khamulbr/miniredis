package com.alessandrodias.miniredis.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MiniRedisTest {

    @Test
    public void testShouldCreateEmptyMiniRedis() {
        MiniRedis miniRedis = new MiniRedis();
        assertEquals(1, miniRedis.dbSize());

    }

//    @Test
//    public void shouldSetSomeValue(){
//        MiniRedis miniRedis = new MiniRedis();
//
//        miniRedis.set("KEY", "VALUE");
//
//        assertEquals(1, miniRedis.)
//    }

}
