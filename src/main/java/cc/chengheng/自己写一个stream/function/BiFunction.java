package cc.chengheng.自己写一个stream.function;

/**
 *
 * @param <R> 返回类型
 * @param <T> 输入的类型
 * @param <U> 输入的类型
 */
@FunctionalInterface
public interface BiFunction<R, T, U> {

    R apply(T t, U u);
}