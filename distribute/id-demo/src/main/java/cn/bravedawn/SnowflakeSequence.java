package cn.bravedawn;

import java.util.Date;

/**
 * @Author : fengx9
 * @Project : id-demo
 * @Date : Created in 2024-03-21 15:15
 */
public class SnowflakeSequence {

    /**
     * Twitter的官方实现
     */

    // 指定初始时间
    private final long twepoch = 1711012488360L;

    // 机器ID的最大位长度为5
    private final long workerIdBits = 5L;

    // 数据中心ID的最大长度为5
    private final long dataCenterIdBits = 5L;

    // 序列号的最大长度为12位
    private final long sequenceBits = 12L;

    // 机器ID需要左移的位数为12
    private final long workerIdLeftShift = sequenceBits;

    // 数据中心ID需要左移的位数12+5=17
    private final long dataCenterIdLeftShift = sequenceBits + workerIdBits;

    // 时间戳需要左移的位数 12+5+5=22
    private final long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;

    // 序列号的掩码，也就是一毫秒内可以生成的序列号的最大值，是4095，也就是2的12次减1
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    // 最大的机器ID值，十进制为31
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    // 最大的数据中心ID值，十进制为31
    private final long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);

    // 初始化上一个时间戳快照值为-1
    private long lastTimestamp = -1L;

    // 初始序列号
    private long sequence = 0L;

    private long workId;
    private long dataCenterId;


    public SnowflakeSequence(long dataCenterId, long workerId) {

        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException("机器ID不能大于31或者不能小于0");
        }

        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException("数据中心ID不能大于31或者不能小于0");
        }

        this.workId = workerId;
        this.dataCenterId = dataCenterId;
    }


    public long getNextNum() {

        // 获取系统时间戳，单位毫秒
        long timestamp = timeGen();

        // 如果当前时间戳下于上次的时间戳，说明发送了时钟回拨，抛出异常，拒绝生成ID
        if (timestamp < lastTimestamp) {
            throw new IllegalStateException("发生了时钟回拨，获取序列号失败");
        }

        // 高并发场景，同一毫秒生成多个ID
        if (timestamp == lastTimestamp) {

            // 确保sequence + 1之后不会溢出，最大值为4095，其实也就是保证1毫秒内最多生成4096个ID值
            sequence = (sequence + 1) & sequenceMask;

            // 如果sequence溢出则变为0，说明1毫秒内并发生成的ID数量超过了4096个，这个时候同1毫秒的第4097个生成的ID必须等待下一毫秒
            if (sequence == 0) {
                // 死循环等待下一个毫秒值，直到比lastTimestamp大
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // 低并发场景，不同毫秒数的时候生成ID，这里也就是timestamp > lastTimestamp的情况
            sequence = 0;
        }

        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << timestampLeftShift) |
                (dataCenterId << dataCenterIdLeftShift) |
                (workId << workerIdLeftShift) |
                sequence;
    }


    private long timeGen() {
        return System.currentTimeMillis();
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }


    public static void main(String[] args) {
        SnowflakeSequence snowflakeSequence = new SnowflakeSequence(0, 0);
        System.out.println(snowflakeSequence.getNextNum());
        System.out.println(-1L ^ (-1L << 64));
    }


}
