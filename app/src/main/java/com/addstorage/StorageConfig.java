package com.addstorage;

/**
 * Created by theny550 on 2015/8/8.
 */
public class StorageConfig {
    private long blockSize;
    private long blockCount;
    private long availCount;
    private long totalsize;
    private long totalavailsize;

    public long getTotalavailsize() {
        return totalavailsize;
    }

    public void setTotalavailsize(long totalavailsize) {
        this.totalavailsize = totalavailsize;
    }

    public long getTotalsize() {
        return totalsize;
    }

    public void setTotalsize(long totalsize) {
        this.totalsize = totalsize;
    }

    public long getAvailCount() {
        return availCount;
    }

    public void setAvailCount(long availCount) {
        this.availCount = availCount;
    }

    public long getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(long blockSize) {
        this.blockSize = blockSize;
    }

    public long getBlockCount() {
        return blockCount;
    }

    public void setBlockCount(long blockCount) {
        this.blockCount = blockCount;
    }

}
