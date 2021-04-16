package cc.chengheng.自己写一个stream.function;

/**
 * 和系统的R T互换了，结果还是一样
 * @param <R> 返回结果
 * @param <T> 输入的类型
 */
@FunctionalInterface
public interface Function<R, T> {

    R apply(T t);
}