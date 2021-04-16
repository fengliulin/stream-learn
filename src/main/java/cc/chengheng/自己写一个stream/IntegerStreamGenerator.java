package cc.chengheng.自己写一个stream;

import cc.chengheng.自己写一个stream.MyStream.CustomStream;
import cc.chengheng.自己写一个stream.MyStream.MyStream;
import cc.chengheng.自己写一个stream.MyStream.NextItemEvalProcess;

/**
 * 整数流生成器
 */
public class IntegerStreamGenerator {

    /**
     * 获得一个有限的整数流 介于 low - high 之间
     *
     * @param low
     * @param high
     * @return
     */
    public static MyStream<Integer> getIntegerStream(int low, int high) {
        return getIntegerStreamInner(low, high, true);
    }

    /**
     * 递归函数，配合 配合getIntegerStream(int low,int high)
     *
     * @param low
     * @param high
     * @param isStart
     * @return
     */
    private static MyStream<Integer> getIntegerStreamInner(int low, int high, boolean isStart) {
        // 到达边界条件，返回空的流
        if (low > high) {
            return CustomStream.makeEmptyStream();
        }

        if (isStart) {
            return new MyStream.Builder<Integer>()
                    .nextItemEvalProcess(new NextItemEvalProcess(() -> getIntegerStreamInner(low, high, false))).build();
        } else {
            return new MyStream.Builder<Integer>()
                    .head(low)
                    .nextItemEvalProcess(new NextItemEvalProcess(() -> getIntegerStreamInner(low + 1, high, false)))
                    .build();
        }
    }
}
