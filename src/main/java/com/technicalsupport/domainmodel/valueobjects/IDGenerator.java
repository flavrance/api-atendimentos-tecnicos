package com.technicalsupport.domainmodel.valueobjects;


import java.util.concurrent.ThreadLocalRandom;

public class IDGenerator {
    private static final long CUSTOMEPOCH = 1300000000000L;

    public static long generateRowId(int shardId) {
        long ts = System.currentTimeMillis() - CUSTOMEPOCH; // limit to recent time
        long randid = ThreadLocalRandom.current().nextInt(0, 512); // random number between 0 and 511
        ts = (ts << 6);  // bit-shift left by 6
        ts = ts + shardId;
        return (ts << 9) + randid; // bit-shift left by 9
    }

}
