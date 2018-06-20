package com.alessandrodias.miniredis.model;

import com.alessandrodias.miniredis.fixture.MiniRedisServiceFixture;
import com.alessandrodias.miniredis.model.MiniRedisBaseData;
import com.alessandrodias.miniredis.model.MiniRedisBaseDataString;
import com.alessandrodias.miniredis.model.MiniRedisDatabase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;

public class MiniRedisDatabaseTest {

    private MiniRedisDatabase miniRedisDatabase;
    private ConcurrentHashMap database = new ConcurrentHashMap();
    private ConcurrentHashMap databaseExpiration = new ConcurrentHashMap();
    private Calendar calendar;

    @Before
    public void setUp() {
        miniRedisDatabase = MiniRedisServiceFixture.get().build();
        calendar = Calendar.getInstance();
    }

    @Test
    public void testShouldReturnSizeZeroedWhenIsEmpty() {
        assertEquals(0, miniRedisDatabase.dbSize());
    }

    @Test
    public void testReturnProperDBSizeWhenIsNotEmpty() {
        database.put("key", "value");
        miniRedisDatabase = MiniRedisServiceFixture.get().withDatabase(database).build();
        assertEquals(1, miniRedisDatabase.dbSize());
    }

    @Test
    public void testShouldReturnProperAnswerWhenAKeyIsSet() {
        assertEquals("OK", miniRedisDatabase.set("key", "value"));
    }

    @Test
    public void testShouldReturnProperDBSizeWhenAKeyIsSet() {
        miniRedisDatabase.set("key", "value");
        assertEquals(1 , miniRedisDatabase.dbSize());
    }

    @Test
    public void testShouldReturnProperValueWhenAKeyIsGet() {
        database.put("key", new MiniRedisBaseDataString("value"));
        MiniRedisDatabase miniRedisDatabase = MiniRedisServiceFixture.get().withDatabase(database).build();
        assertEquals("value", miniRedisDatabase.get("key"));
    }

    @Test
    public void testShouldReturnNilValueWhenANonExistingKeyIsGet() {
        Assert.assertEquals(MiniRedisBaseData.NIL, miniRedisDatabase.get("key"));
    }


    @Test
    public void testShouldDeleteOneKey() {
        database.put("key", new MiniRedisBaseDataString("value"));
        miniRedisDatabase = MiniRedisServiceFixture.get().withDatabase(database).build();
        assertEquals(1, miniRedisDatabase.del("key"));
    }

    @Test
    public void testShouldDeleteTwoKeys() {
        database.put("key", new MiniRedisBaseDataString("value"));
        database.put("key1", new MiniRedisBaseDataString("value1"));
        miniRedisDatabase = MiniRedisServiceFixture.get().withDatabase(database).build();

        assertEquals(2, miniRedisDatabase.del("key", "key1"));
    }

    @Test
    public void testShouldDeleteTwoKeysAndIgnoreOneNonExistentKey() {
        database.put("key", new MiniRedisBaseDataString("value"));
        database.put("key1", new MiniRedisBaseDataString("value1"));
        miniRedisDatabase = MiniRedisServiceFixture.get().withDatabase(database).build();

        assertEquals(2, miniRedisDatabase.del("key", "key1", "key2"));
    }

    @Test
    public void testShouldIncrementExistingNumericKey(){
        database.put("key", new MiniRedisBaseDataString("1"));
        miniRedisDatabase = MiniRedisServiceFixture.get().withDatabase(database).build();

        assertEquals(2, miniRedisDatabase.incr("key"));
    }

    @Test
    public void testShouldIncrementNonExistingNumericKey(){
        assertEquals(1, miniRedisDatabase.incr("key"));
    }

    @Test(expected = NumberFormatException.class)
    public void testShouldThrowExceptionWhenIncrOfNonIntKeyIsCalled() {
        database.put("key", new MiniRedisBaseDataString("a"));
        miniRedisDatabase = MiniRedisServiceFixture.get().withDatabase(database).build();

        miniRedisDatabase.incr("key");
    }

    @Test
    public void testShouldCreateAExpirableKeyAndGetValueWithinTime() throws InterruptedException {
        database.put("key", new MiniRedisBaseDataString("a", 1));
        miniRedisDatabase = MiniRedisServiceFixture.get().withDatabase(database).build();

        Thread.sleep(500);
        assertEquals("a", miniRedisDatabase.get("key"));
    }

    @Test
    public void testShouldCreateAExpirableKeyAndGetNilAsExpireTimeHasPassed() throws InterruptedException {
        database.put("key", new MiniRedisBaseDataString("a", 1));
        miniRedisDatabase = MiniRedisServiceFixture.get().withDatabase(database).build();

        Thread.sleep(1500);
        assertEquals(MiniRedisBaseData.NIL, miniRedisDatabase.get("key"));
    }

    @Test(expected = RuntimeException.class)
    public void testShouldNotCreateAExpirableKeyWhenExpirationIsZero() throws InterruptedException {
        miniRedisDatabase.set("key", "a", 0);
    }

    @Test
    public void testShouldReturnOneWhenAddOneElementToOrderedSet() {
        assertEquals(1, miniRedisDatabase.zadd("teste", 10.0, "item 2"));
    }

    @Test
    public void testShouldReturnZeroWhenAddOneElementToOrderedSetWhichValueAlreadyExistsThere() {
        miniRedisDatabase.zadd("teste", 10.0, "item 2");
        assertEquals(0, miniRedisDatabase.zadd("teste", 1.0, "item 2"));
    }

    @Test
    public void testShouldReturnProperDbSizeWhenAddOneElementToOrderedSet() {
        miniRedisDatabase.zadd("teste", 10.0, "item 2");
        assertEquals(1, miniRedisDatabase.dbSize());
    }

    @Test
    public void testShouldReturnProperDbSizeWhenMixedElementsAreAddedToDatabase() {
        miniRedisDatabase.set("key", "value");
        miniRedisDatabase.set("key2", "value");
        miniRedisDatabase.zadd("teste", 10.0, "item 2");
        miniRedisDatabase.zadd("teste", 1.0, "item 1");
        assertEquals(3, miniRedisDatabase.dbSize());
    }

    @Test
    public void testShouldZeroCardFromNonExistingOrderedSet() {
        assertEquals(0, miniRedisDatabase.zCard("teste"));
    }

    @Test
    public void testShouldGetOneCardFromOrderedSetLoadedWithOneMember() {
        miniRedisDatabase.zadd("teste", 10.0, "item 2");
        assertEquals(1, miniRedisDatabase.zCard("teste"));
    }

    @Test
    public void testShouldGetTwoCardsFromOrderedSetLoadedWithTwoMembers() {
        miniRedisDatabase.zadd("teste", 10.0, "item 2");
        miniRedisDatabase.zadd("teste", 5.0, "item 1");
        assertEquals(2, miniRedisDatabase.zCard("teste"));
    }

    @Test
    public void testShouldGetFirstRankPositionWhenFirstMemberIsQueriedFromRank() {
        miniRedisDatabase.zadd("teste", 10.0, "item 2");
        miniRedisDatabase.zadd("teste", 5.0, "item 1");
        assertEquals(Integer.valueOf(0) , miniRedisDatabase.zRank("teste","item 1"));
    }

    @Test
    public void testShouldGetNullRankPositionWhenNonExistingMemberIsQueriedFromRank() {
        miniRedisDatabase.zadd("teste", 10.0, "item 2");
        miniRedisDatabase.zadd("teste", 5.0, "item 1");
        assertEquals(MiniRedisBaseData.NIL, miniRedisDatabase.zRank("teste","item 3"));
    }

    @Test
    public void testShouldGetProperRankPositionsWhenEachMemberIsQueriedFromRank() {
        miniRedisDatabase.zadd("teste", 11.0, "item 2");
        miniRedisDatabase.zadd("teste", 12.0, "item 3");
        miniRedisDatabase.zadd("teste", 13.0, "item 4");
        miniRedisDatabase.zadd("teste", 14.0, "item 5");
        miniRedisDatabase.zadd("teste", 15.0, "item 6");
        miniRedisDatabase.zadd("teste", 16.0, "item 7");
        miniRedisDatabase.zadd("teste", 17.0, "item 8");
        miniRedisDatabase.zadd("teste", 18.0, "item 9");
        miniRedisDatabase.zadd("teste", 19.0, "item 10");
        miniRedisDatabase.zadd("teste", 20.0, "item 11");
        miniRedisDatabase.zadd("teste", 10.0, "item 1");
        assertEquals(Integer.valueOf(0), miniRedisDatabase.zRank("teste","item 1"));
        assertEquals(Integer.valueOf(1), miniRedisDatabase.zRank("teste","item 2"));
        assertEquals(Integer.valueOf(2), miniRedisDatabase.zRank("teste","item 3"));
        assertEquals(Integer.valueOf(3), miniRedisDatabase.zRank("teste","item 4"));
        assertEquals(Integer.valueOf(4), miniRedisDatabase.zRank("teste","item 5"));
        assertEquals(Integer.valueOf(5), miniRedisDatabase.zRank("teste","item 6"));
        assertEquals(Integer.valueOf(6), miniRedisDatabase.zRank("teste","item 7"));
        assertEquals(Integer.valueOf(7), miniRedisDatabase.zRank("teste","item 8"));
        assertEquals(Integer.valueOf(8), miniRedisDatabase.zRank("teste","item 9"));
        assertEquals(Integer.valueOf(9), miniRedisDatabase.zRank("teste","item 10"));
        assertEquals(Integer.valueOf(10), miniRedisDatabase.zRank("teste","item 11"));
    }

    @Test
    public void testShouldGetNoElementsWhenZRangeIsCalledWithStartAboveListMaxIndex() {
        miniRedisDatabase.zadd("teste", 10.0, "item 2");
        miniRedisDatabase.zadd("teste", 5.0, "item 1");
        miniRedisDatabase.zadd("teste", 1.0, "item 0");
        assertEquals(null, miniRedisDatabase.zRange("teste", 3, 4));
    }

    @Test
    public void testShouldGetNoElementsWhenZRangeIsCalledWithStartAboveStop() {
        miniRedisDatabase.zadd("teste", 10.0, "item 2");
        miniRedisDatabase.zadd("teste", 5.0, "item 1");
        miniRedisDatabase.zadd("teste", 1.0, "item 0");
        assertEquals(null, miniRedisDatabase.zRange("teste", 1, 0));
    }

    @Test
    public void testShouldGetSomeElementsWhenZRangeIsCalled() {
        miniRedisDatabase.zadd("teste", 10.0, "item 2");
        miniRedisDatabase.zadd("teste", 5.0, "item 1");
        miniRedisDatabase.zadd("teste", 1.0, "item 0");
        List<String> expected = Arrays.asList("item 0", "item 1");
        assertEquals(expected, miniRedisDatabase.zRange("teste", 0, 1));
    }

    @Test
    public void testShouldGetSomeElementsWhenZRangeIsCalledWithStopAboveListMaxIndex() {
        miniRedisDatabase.zadd("teste", 10.0, "item 2");
        miniRedisDatabase.zadd("teste", 5.0, "item 1");
        miniRedisDatabase.zadd("teste", 1.0, "item 0");
        List<String> expected = Arrays.asList("item 1", "item 2");
        assertEquals(expected, miniRedisDatabase.zRange("teste", 1, 3));
    }

    @Test
    public void testShouldGetNoElementsWhenZRangeIsCalledWithStartAboveStopBothNegatives() {
        miniRedisDatabase.zadd("teste", 10.0, "item 2");
        miniRedisDatabase.zadd("teste", 5.0, "item 1");
        miniRedisDatabase.zadd("teste", 1.0, "item 0");
        assertEquals(null, miniRedisDatabase.zRange("teste", -1, -2));
    }

    @Test
    public void testShouldGetSomeElementsWhenZRangeIsCalledWithStartAndStopWithNegativeValues() {
        miniRedisDatabase.zadd("teste", 10.0, "item 2");
        miniRedisDatabase.zadd("teste", 5.0, "item 1");
        miniRedisDatabase.zadd("teste", 1.0, "item 0");
        List<String> expected = Arrays.asList("item 1", "item 2");
        assertEquals(expected, miniRedisDatabase.zRange("teste",  -2, -1));
    }

    @Test
    public void testShouldGetSomeElementsWhenZRangeIsCalledWithStartWithPositiveAndStopWithNegativeValues() {
        miniRedisDatabase.zadd("teste", 10.0, "item 2");
        miniRedisDatabase.zadd("teste", 5.0, "item 1");
        miniRedisDatabase.zadd("teste", 1.0, "item 0");
        List<String> expected = Arrays.asList("item 1", "item 2");
        assertEquals(expected, miniRedisDatabase.zRange("teste",  1, -1));
    }

    @Test
    public void testShouldSetValuesForDifferentUsersAndGetProperValues(){

    }
}