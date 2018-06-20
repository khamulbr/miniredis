package com.alessandrodias.miniredis.model;

import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

public class MiniRedisDataOrderedSet extends MiniRedisData {

    private ConcurrentSkipListMap<Double, String> scoredMembers = new ConcurrentSkipListMap<>();

    public MiniRedisDataOrderedSet() {
    }

    public int putScoredMember(Double score, String value) {
        if (!scoredMembers.containsValue(value)) {
            this.scoredMembers.put(score, value);
            return 1;
        }
        return 0;
    }

    public int getScoredMemberSize() {
        return this.scoredMembers.size();
    }

    public Integer getScoredMemberRank(String value) {
        return getListOfValues().indexOf(value) >= 0 ? getListOfValues().indexOf(value) : null;
    }

    public List<String> getScoredMemberInRange(int start, int stop) {
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

    private boolean isRangeValid(int start, int stop) {
        return start < stop && start < (getScoredMemberSize() - 1);
    }

    private List<String> getListOfValues() {
        return scoredMembers.values().stream().map(e -> e.toString()).collect(Collectors.toList());
    }
}
