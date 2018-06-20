package com.alessandrodias.miniredis.model;

import com.alessandrodias.miniredis.fixture.MiniRedisServiceFixture;
import com.alessandrodias.miniredis.service.MiniRedisService;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;

public class MiniRedisServiceTest {

    private MiniRedisService miniRedisService;
    private ConcurrentHashMap database = new ConcurrentHashMap();
    private ConcurrentHashMap databaseExpiration = new ConcurrentHashMap();
    private Calendar calendar;

    @Before
    public void setUp() {
        miniRedisService = MiniRedisServiceFixture.get().build();
        calendar = Calendar.getInstance();
    }

    @Test
    public void testShouldReturnSizeZeroedWhenIsEmpty() {
        assertEquals(0, miniRedisService.dbSize());
    }

    @Test
    public void testReturnProperDBSizeWhenIsNotEmpty() {
        database.put("key", "value");
        miniRedisService = MiniRedisServiceFixture.get().withDatabase(database).build();
        assertEquals(1, miniRedisService.dbSize());
    }

    @Test
    public void testShouldReturnProperAnswerWhenAKeyIsSet() {
        assertEquals("OK", miniRedisService.set("key", "value"));
    }

    @Test
    public void testShouldReturnProperDBSizeWhenAKeyIsSet() {
        miniRedisService.set("key", "value");
        assertEquals(1 , miniRedisService.dbSize());
    }

    @Test
    public void testShouldReturnProperValueWhenAKeyIsGet() {
        database.put("key", new MiniRedisString("value"));
        MiniRedisService miniRedisService = MiniRedisServiceFixture.get().withDatabase(database).build();
        assertEquals("value", miniRedisService.get("key"));
    }

    @Test
    public void testShouldReturnNilValueWhenANonExistingKeyIsGet() {
        assertEquals(MiniRedisData.NIL, miniRedisService.get("key"));
    }


    @Test
    public void testShouldDeleteOneKey() {
        database.put("key", new MiniRedisString("value"));
        miniRedisService = MiniRedisServiceFixture.get().withDatabase(database).build();
        assertEquals(1, miniRedisService.del("key"));
    }

    @Test
    public void testShouldDeleteTwoKeys() {
        database.put("key", new MiniRedisString("value"));
        database.put("key1", new MiniRedisString("value1"));
        miniRedisService = MiniRedisServiceFixture.get().withDatabase(database).build();

        assertEquals(2, miniRedisService.del("key", "key1"));
    }

    @Test
    public void testShouldDeleteTwoKeysAndIgnoreOneNonExistentKey() {
        database.put("key", new MiniRedisString("value"));
        database.put("key1", new MiniRedisString("value1"));
        miniRedisService = MiniRedisServiceFixture.get().withDatabase(database).build();

        assertEquals(2, miniRedisService.del("key", "key1", "key2"));
    }

    @Test
    public void testShouldIncrementExistingNumericKey(){
        database.put("key", new MiniRedisString("1"));
        miniRedisService = MiniRedisServiceFixture.get().withDatabase(database).build();

        assertEquals(2, miniRedisService.incr("key"));
    }

    @Test
    public void testShouldIncrementNonExistingNumericKey(){
        assertEquals(1, miniRedisService.incr("key"));
    }

    @Test(expected = NumberFormatException.class)
    public void testShouldThrowExceptionWhenIncrOfNonIntKeyIsCalled() {
        database.put("key", new MiniRedisString("a"));
        miniRedisService = MiniRedisServiceFixture.get().withDatabase(database).build();

        miniRedisService.incr("key");
    }

    @Test
    public void testShouldCreateAExpirableKeyAndGetValueWithinTime() throws InterruptedException {
        miniRedisService.set("key", "a", 1);

        Thread.sleep(500);
        assertEquals("a", miniRedisService.get("key"));
    }

    @Test
    public void testShouldCreateAExpirableKeyAndGetNilAsExpireTimeHasPassed() throws InterruptedException {
        miniRedisService.set("key", "a", 1);

        Thread.sleep(1500);
        assertEquals(MiniRedisData.NIL, miniRedisService.get("key"));
    }

    @Test(expected = RuntimeException.class)
    public void testShouldNotCreateAExpirableKeyWhenExpirationIsZero() throws InterruptedException {
        miniRedisService.set("key", "a", 0);
    }

    @Test
    public void testShouldReturnOneWhenAddOneElementToOrderedSet() {
        assertEquals(1, miniRedisService.zadd("teste", 10.0, "item 2"));
    }

    @Test
    public void testShouldReturnZeroWhenAddOneElementToOrderedSetWhichValueAlreadyExistsThere() {
        miniRedisService.zadd("teste", 10.0, "item 2");
        assertEquals(0, miniRedisService.zadd("teste", 1.0, "item 2"));
    }

    @Test
    public void testShouldReturnProperDbSizeWhenAddOneElementToOrderedSet() {
        miniRedisService.zadd("teste", 10.0, "item 2");
        assertEquals(1, miniRedisService.dbSize());
    }

    @Test
    public void testShouldReturnProperDbSizeWhenMixedElementsAreAddedToDatabase() {
        miniRedisService.set("key", "value");
        miniRedisService.set("key2", "value");
        miniRedisService.zadd("teste", 10.0, "item 2");
        miniRedisService.zadd("teste", 1.0, "item 1");
        assertEquals(3, miniRedisService.dbSize());
    }

    @Test
    public void testShouldZeroCardFromNonExistingOrderedSet() {
        assertEquals(0, miniRedisService.zCard("teste"));
    }

    @Test
    public void testShouldGetOneCardFromOrderedSetLoadedWithOneMember() {
        miniRedisService.zadd("teste", 10.0, "item 2");
        assertEquals(1, miniRedisService.zCard("teste"));
    }

    @Test
    public void testShouldGetTwoCardsFromOrderedSetLoadedWithTwoMembers() {
        miniRedisService.zadd("teste", 10.0, "item 2");
        miniRedisService.zadd("teste", 5.0, "item 1");
        assertEquals(2, miniRedisService.zCard("teste"));
    }

    @Test
    public void testShouldGetFirstRankPositionWhenFirstMemberIsQueriedFromRank() {
        miniRedisService.zadd("teste", 10.0, "item 2");
        miniRedisService.zadd("teste", 5.0, "item 1");
        assertEquals(Integer.valueOf(0) , miniRedisService.zRank("teste","item 1"));
    }

    @Test
    public void testShouldGetNullRankPositionWhenNonExistingMemberIsQueriedFromRank() {
        miniRedisService.zadd("teste", 10.0, "item 2");
        miniRedisService.zadd("teste", 5.0, "item 1");
        assertEquals(MiniRedisData.NIL, miniRedisService.zRank("teste","item 3"));
    }

    @Test
    public void testShouldGetProperRankPositionsWhenEachMemberIsQueriedFromRank() {
        miniRedisService.zadd("teste", 11.0, "item 2");
        miniRedisService.zadd("teste", 12.0, "item 3");
        miniRedisService.zadd("teste", 13.0, "item 4");
        miniRedisService.zadd("teste", 14.0, "item 5");
        miniRedisService.zadd("teste", 15.0, "item 6");
        miniRedisService.zadd("teste", 16.0, "item 7");
        miniRedisService.zadd("teste", 17.0, "item 8");
        miniRedisService.zadd("teste", 18.0, "item 9");
        miniRedisService.zadd("teste", 19.0, "item 10");
        miniRedisService.zadd("teste", 20.0, "item 11");
        miniRedisService.zadd("teste", 10.0, "item 1");
        assertEquals(Integer.valueOf(0), miniRedisService.zRank("teste","item 1"));
        assertEquals(Integer.valueOf(1), miniRedisService.zRank("teste","item 2"));
        assertEquals(Integer.valueOf(2), miniRedisService.zRank("teste","item 3"));
        assertEquals(Integer.valueOf(3), miniRedisService.zRank("teste","item 4"));
        assertEquals(Integer.valueOf(4), miniRedisService.zRank("teste","item 5"));
        assertEquals(Integer.valueOf(5), miniRedisService.zRank("teste","item 6"));
        assertEquals(Integer.valueOf(6), miniRedisService.zRank("teste","item 7"));
        assertEquals(Integer.valueOf(7), miniRedisService.zRank("teste","item 8"));
        assertEquals(Integer.valueOf(8), miniRedisService.zRank("teste","item 9"));
        assertEquals(Integer.valueOf(9), miniRedisService.zRank("teste","item 10"));
        assertEquals(Integer.valueOf(10), miniRedisService.zRank("teste","item 11"));
    }

    @Test
    public void testShouldGetNoElementsWhenZRangeIsCalledWithStartAboveListMaxIndex() {
        miniRedisService.zadd("teste", 10.0, "item 2");
        miniRedisService.zadd("teste", 5.0, "item 1");
        miniRedisService.zadd("teste", 1.0, "item 0");
        assertEquals(null, miniRedisService.zRange("teste", 3, 4));
    }

    @Test
    public void testShouldGetNoElementsWhenZRangeIsCalledWithStartAboveStop() {
        miniRedisService.zadd("teste", 10.0, "item 2");
        miniRedisService.zadd("teste", 5.0, "item 1");
        miniRedisService.zadd("teste", 1.0, "item 0");
        assertEquals(null, miniRedisService.zRange("teste", 1, 0));
    }

    @Test
    public void testShouldGetSomeElementsWhenZRangeIsCalled() {
        miniRedisService.zadd("teste", 10.0, "item 2");
        miniRedisService.zadd("teste", 5.0, "item 1");
        miniRedisService.zadd("teste", 1.0, "item 0");
        List<String> expected = Arrays.asList("item 0", "item 1");
        assertEquals(expected, miniRedisService.zRange("teste", 0, 1));
    }

    @Test
    public void testShouldGetSomeElementsWhenZRangeIsCalledWithStopAboveListMaxIndex() {
        miniRedisService.zadd("teste", 10.0, "item 2");
        miniRedisService.zadd("teste", 5.0, "item 1");
        miniRedisService.zadd("teste", 1.0, "item 0");
        List<String> expected = Arrays.asList("item 1", "item 2");
        assertEquals(expected, miniRedisService.zRange("teste", 1, 3));
    }

    @Test
    public void testShouldGetSomeElementsWhenZRangeIsCalledWithStartAndStopWithNegativeValues() {
        miniRedisService.zadd("teste", 10.0, "item 2");
        miniRedisService.zadd("teste", 5.0, "item 1");
        miniRedisService.zadd("teste", 1.0, "item 0");
        List<String> expected = Arrays.asList("item 1", "item 2");
        assertEquals(expected, miniRedisService.zRange("teste",  -2, -1));
    }
}