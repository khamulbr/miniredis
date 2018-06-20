package com.alessandrodias.miniredis.model;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

public class MiniRedisBaseDataOrderedSet extends MiniRedisBaseData {

    private ConcurrentSkipListMap<Double, String> scoredMembers = new ConcurrentSkipListMap<>();

    public MiniRedisBaseDataOrderedSet() {
    }

    public synchronized int putScoredMember(Double score, String value) {
        if (!scoredMembers.containsValue(value)) {
            this.scoredMembers.put(score, value);
            return 1;
        }
        return 0;
    }

    public synchronized int getScoredMemberSize() {
        return this.scoredMembers.size();
    }

    public synchronized Integer getScoredMemberRank(String value) {
        return getListOfValues().indexOf(value) >= 0 ? getListOfValues().indexOf(value) : null;
    }

    public synchronized List<String> getScoredMemberInRange(int start, int stop) {
        if (stop >= getScoredMemberSize())
            stop = getScoredMemberSize() - 1;
        if (start < 0)
            start = getScoredMemberSize() + start;
        if (stop < 0)
            stop = getScoredMemberSize() + stop;
        if (!isRangeValid(start, stop))
            return null;
        return getListOfValues().subList(start, stop + 1);
    }

    private synchronized boolean isRangeValid(int start, int stop) {
        return start < stop && start < (getScoredMemberSize() - 1);
    }

    private synchronized List<String> getListOfValues() {
        return scoredMembers.values().stream().map(e -> e.toString()).collect(Collectors.toList());
    }
}
