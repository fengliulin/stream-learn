package cc.chengheng.自己写一个stream.function;

import cc.chengheng.自己写一个stream.MyStream.MyStream;

@FunctionalInterface
public interface EvalFunction<T> {

    /**
     * stream流的强制求值方法
     *
     * @return
     */
    MyStream<T> apply();
}
