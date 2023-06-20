package com.yeoboya.lunch.api.memory;

public class Memory {

    public Memory(long used, long max) {
        this.used = used;
        this.max = max;
    }

    private long used;
    private long max;

    public long getUsed() {
        return used;
    }

    public void setUsed(long used) {
        this.used = used;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "Memory{" +
                "used=" + used +
                ", max=" + max +
                '}';
    }
}
