package cc.chengheng.自己写一个stream.MyStream;

import cc.chengheng.自己写一个stream.function.*;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class MyStream<T> implements CustomStream<T> {

    /** 流的头部 */
    protected T head;

    /** 流的下一个求值过程 */
    protected NextItemEvalProcess nextItemEvalProcess;

    /** 是否流的结尾 */
    protected boolean isEnd;

    public static class Builder<T> {
        private final MyStream<T> target;


        public Builder() {
            this.target = new MyStream<>();
        }

        public Builder<T> head(T head) {
            target.head = head;
            return this;
        }

        public Builder<T> isEnd(boolean isEnd) {
            target.isEnd = isEnd;
            return this;
        }

        /**
         * 下一项求值过程
         * NextItemEvalProcess 是流能够实现"惰性求值"的关键
         *
         * @param nextItemEvalProcess
         * @return
         */
        public Builder<T> nextItemEvalProcess(NextItemEvalProcess nextItemEvalProcess) {
            target.nextItemEvalProcess = nextItemEvalProcess;
            return this;
        }

        public MyStream<T> build() {
            return target;
        }
    }

    /**
     * 当前流强制求值
     *
     * @return 求值之后返回一个新的流
     */
    protected MyStream<T> eval() {
        return nextItemEvalProcess.eval();
    }

    /**
     * 判断当前流是否为空
     *
     * @return
     */
    public boolean isEmptyStream() {
        return isEnd;
    }


    //region 实现 CustomStream 接口的方法

    @Override
    public <R> MyStream<R> map(Function<R, T> mapper) {
        NextItemEvalProcess lastNextItemEvalProcess = this.nextItemEvalProcess;
        this.nextItemEvalProcess = new NextItemEvalProcess(
                () -> {
                    MyStream myStream = lastNextItemEvalProcess.eval();
                    return map(mapper, myStream);
                }
        );

        // 求值链条 加入一个新的process map
        return new MyStream.Builder<R>()
                .nextItemEvalProcess(this.nextItemEvalProcess)
                .build();
    }

    /**
     * 递归函数 配合API.map
     */
    private static <R, T> MyStream<R> map(Function<R, T> mapper, MyStream<T> myStream) {
        if (myStream.isEmptyStream()) {
            return CustomStream.makeEmptyStream();
        }

        R head = mapper.apply(myStream.head);

        return new MyStream.Builder<R>()
                .head(head)
                .nextItemEvalProcess(new NextItemEvalProcess(() -> map(mapper, myStream.eval())))
                .build();
    }

    @Override
    public <R> MyStream<R> flatMap(Function<? extends MyStream<R>, T> mapper) {
        NextItemEvalProcess lastNextItemEvalProcess = this.nextItemEvalProcess;
        this.nextItemEvalProcess = new NextItemEvalProcess(
                () -> {
                    MyStream myStream = lastNextItemEvalProcess.eval();
                    return flatMap(mapper, CustomStream.makeEmptyStream(), myStream);
                }
        );

        // 求值链条 加入一个新的process map
        return new MyStream.Builder<R>()
                .nextItemEvalProcess(this.nextItemEvalProcess)
                .build();
    }

    /**
     * 递归函数 配合API.flatMap
     */
    private static <R, T> MyStream<R> flatMap(Function<? extends MyStream<R>, T> mapper, MyStream<R> headMyStream, MyStream<T> myStream) {
        if (headMyStream.isEmptyStream()) {
            if (myStream.isEmptyStream()) {
                return CustomStream.makeEmptyStream();
            } else {
                T outerHead = myStream.head;
                MyStream<R> newHeadMyStream = mapper.apply(outerHead);

                return flatMap(mapper, newHeadMyStream.eval(), myStream.eval());
            }
        } else {
            return new MyStream.Builder<R>()
                    .head(headMyStream.head)
                    .nextItemEvalProcess(new NextItemEvalProcess(() -> flatMap(mapper, headMyStream.eval(), myStream)))
                    .build();
        }
    }

    @Override
    public MyStream<T> filter(Predicate<T> predicate) {
        NextItemEvalProcess lastNextItemEvalProcess = this.nextItemEvalProcess;
        this.nextItemEvalProcess = new NextItemEvalProcess(
                () -> {
                    MyStream myStream = lastNextItemEvalProcess.eval();
                    return filter(predicate, myStream);
                }
        );

        // 求值链条 加入一个新的process filter
        return this;
    }

    /**
     * 递归函数 配合API.filter
     */
    private static <T> MyStream<T> filter(Predicate<T> predicate, MyStream<T> myStream) {
        if (myStream.isEmptyStream()) {
            return CustomStream.makeEmptyStream();
        }

        if (predicate.test(myStream.head)) {
            return new Builder<T>()
                    .head(myStream.head)
                    .nextItemEvalProcess(new NextItemEvalProcess(() -> filter(predicate, myStream.eval())))
                    .build();
        } else {
            return filter(predicate, myStream.eval());
        }
    }

    @Override
    public MyStream<T> limit(int n) {
        NextItemEvalProcess lastNextItemEvalProcess = this.nextItemEvalProcess;
        this.nextItemEvalProcess = new NextItemEvalProcess(
                () -> {
                    MyStream myStream = lastNextItemEvalProcess.eval();
                    return limit(n, myStream);
                }
        );

        // 求值链条 加入一个新的process limit
        return this;
    }

    /**
     * 递归函数 配合API.limit
     */
    private static <T> MyStream<T> limit(int num, MyStream<T> myStream) {
        if (num == 0 || myStream.isEmptyStream()) {
            return CustomStream.makeEmptyStream();
        }

        return new MyStream.Builder<T>()
                .head(myStream.head)
                .nextItemEvalProcess(new NextItemEvalProcess(() -> limit(num - 1, myStream.eval())))
                .build();
    }

    @Override
    public MyStream<T> distinct() {
        NextItemEvalProcess lastNextItemEvalProcess = this.nextItemEvalProcess;
        this.nextItemEvalProcess = new NextItemEvalProcess(
                () -> {
                    MyStream myStream = lastNextItemEvalProcess.eval();
                    return distinct(new HashSet<>(), myStream);
                }
        );

        // 求值链条 加入一个新的process limit
        return this;
    }

    /**
     * 递归函数 配合API.distinct
     */
    private static <T> MyStream<T> distinct(Set<T> distinctSet, MyStream<T> myStream) {
        if (myStream.isEmptyStream()) {
            return CustomStream.makeEmptyStream();
        }

        if (!distinctSet.contains(myStream.head)) {
            // 加入集合
            distinctSet.add(myStream.head);

            return new Builder<T>()
                    .head(myStream.head)
                    .nextItemEvalProcess(new NextItemEvalProcess(() -> distinct(distinctSet, myStream.eval())))
                    .build();
        } else {
            return distinct(distinctSet, myStream.eval());
        }
    }

    @Override
    public MyStream<T> peek(ForEach<T> consumer) {
        NextItemEvalProcess lastNextItemEvalProcess = this.nextItemEvalProcess;
        this.nextItemEvalProcess = new NextItemEvalProcess(
                () -> {
                    MyStream myStream = lastNextItemEvalProcess.eval();
                    return peek(consumer, myStream);
                }
        );

        // 求值链条 加入一个新的process peek
        return this;
    }

    /**
     * 递归函数 配合API.peek
     */
    private static <T> MyStream<T> peek(ForEach<T> consumer, MyStream<T> myStream) {
        if (myStream.isEmptyStream()) {
            return CustomStream.makeEmptyStream();
        }

        consumer.apply(myStream.head);

        return new MyStream.Builder<T>()
                .head(myStream.head)
                .nextItemEvalProcess(new NextItemEvalProcess(() -> peek(consumer, myStream.eval())))
                .build();
    }

    //region 强制求值

    /**
     * 终结操作
     *
     * @param consumer 遍历逻辑
     */
    public void forEach(ForEach<T> consumer) {
        // 终结操作 直接开始求值
        forEach(consumer, this.eval());
    }

    /**
     * 递归函数 配合API.forEach
     */
    private static <T> void forEach(ForEach<T> consumer, MyStream<T> myStream) {
        if (myStream.isEmptyStream()) {
            return;
        }

        consumer.apply(myStream.head);
        forEach(consumer, myStream.eval());
    }

    @Override
    public <R> R reduce(R initVal, BiFunction<R, R, T> accumulator) {
//        if(myStream.isEmptyStream()){
//            return initVal;
//        }
//
//        T head = myStream.head;
//        R result = reduce(initVal, accumulator, myStream.eval());
//
//        return accumulator.apply(result,head);
        return null;
    }

    /**
     * 递归函数 配合API.reduce
     */

    private static <R, T> R reduce(R initVal, BiFunction<R, R, T> accumulator, MyStream<T> myStream) {
//        if(myStream.isEmptyStream()){
//            return initVal;
//        }
//
//        T head = myStream.head;
//        R result = reduce(initVal, accumulator, myStream.eval());
//
//        return accumulator.apply(result,head);

        return null;
    }

    /**
     * 递归函数 配合API.reduce
     */
//    private static <R,T> R reduce(R initVal, BiFunction<R,R,T> accumulator, MyStream<T> myStream){
//        if(myStream.isEmptyStream()){
//            return initVal;
//        }
//
//        T head = myStream.head;
//        R result = reduce(initVal,accumulator, myStream.eval());
//
//        return accumulator.apply(result,head);
//        return null;
//    }
    @Override
    public T max(Comparator<T> comparator) {
        // 终结操作 直接开始求值
        MyStream<T> eval = this.eval();

        if (eval.isEmptyStream()) {
            return null;
        } else {
            return max(comparator, eval, eval.head);
        }
    }

    /**
     * 递归函数 配合API.max
     */
    private static <T> T max(Comparator<T> comparator, MyStream<T> myStream, T max) {
        if (myStream.isEnd) {
            return max;
        }

        T head = myStream.head;
        // head 和 max 进行比较
        if (comparator.compare(head, max) > 0) {
            // head 较大 作为新的max传入
            return max(comparator, myStream.eval(), head);
        } else {
            // max 较大 不变
            return max(comparator, myStream.eval(), max);
        }
    }

    @Override
    public T min(Comparator<T> comparator) {
        // 终结操作 直接开始求值
        MyStream<T> eval = this.eval();

        if (eval.isEmptyStream()) {
            return null;
        } else {
            return min(comparator, eval, eval.head);
        }
    }

    /**
     * 递归函数 配合API.min
     */
    private static <T> T min(Comparator<T> comparator, MyStream<T> myStream, T min) {
        if (myStream.isEnd) {
            return min;
        }

        T head = myStream.head;
        // head 和 min 进行比较
        if (comparator.compare(head, min) < 0) {
            // head 较小 作为新的min传入
            return min(comparator, myStream.eval(), head);
        } else {
            // min 较小 不变
            return min(comparator, myStream.eval(), min);
        }
    }

    @Override
    public int count() {
        // 终结操作 直接开始求值
        return count(this.eval(), 0);
    }

    /**
     * 递归函数 配合API.count
     */
    private static <T> int count(MyStream<T> myStream, int count) {
        if (myStream.isEmptyStream()) {
            return count;
        }

        // count+1 进行递归
        return count(myStream.eval(), count + 1);
    }

    @Override
    public boolean anyMatch(Predicate<? super T> predicate) {
        // 终结操作 直接开始求值
        return anyMatch(predicate, this.eval());
    }

    /**
     * 递归函数 配合API.anyMatch
     */
    private static <T> boolean anyMatch(Predicate<? super T> predicate, MyStream<T> myStream) {
        if (myStream.isEmptyStream()) {
            // 截止末尾，不存在任何匹配项
            return false;
        }

        // 谓词判断
        if (predicate.test(myStream.head)) {
            // 匹配 存在匹配项 返回true
            return true;
        } else {
            // 不匹配，继续检查，直到存在匹配项
            return anyMatch(predicate, myStream.eval());
        }
    }

    @Override
    public boolean allMatch(Predicate<? super T> predicate) {
        // 终结操作 直接开始求值
        return allMatch(predicate, this.eval());
    }

    /**
     * 递归函数 配合API.anyMatch
     */
    private static <T> boolean allMatch(Predicate<? super T> predicate, MyStream<T> myStream) {
        if (myStream.isEmptyStream()) {
            // 全部匹配
            return true;
        }

        // 谓词判断
        if (predicate.test(myStream.head)) {
            // 当前项匹配，继续检查
            return allMatch(predicate, myStream.eval());
        } else {
            // 存在不匹配的项，返回false
            return false;
        }
    }
    //endregion

    //region 强制求值 最复杂也最强大的接口

    /**
     * collect方法是强制求值方法中，最复杂也最强大的接口，其作用是将流中的元素收集(collect)起来，并转化成特定的数据结构。
     * 从函数式编程的角度来看，collect方法是一个高阶函数，其接受三个函数作为参数(supplier，accumulator，finisher)，
     * 最终生成一个更加强大的函数。在java中，三个函数参数以Collector实现对象的形式呈现。
     * supplier 方法：用于提供收集collect的初始值。
     * accumulator 方法：用于指定收集过程中，初始值和流中个体元素聚合的逻辑。
     * finisher 方法：用于指定在收集完成之后的收尾转化操作(例如：StringBuilder.toString() ---> String)。
     *
     * @param collector 传入所需的函数组合子，生成高阶函数
     * @param <R>
     * @param <A>
     * @return
     */
    @Override
    public <R, A> R collect(Collector<T, A, R> collector) {
        // 终结操作 直接开始求值
        A result = collect(collector,this.eval());

        // 通过finish方法进行收尾
//        return collector.finisher().apply(result);
        return null;
    }

    /**
     * 递归函数 配合API.collect
     * */
    private static <R, A, T> A collect(Collector<T, A, R> collector, MyStream<T> myStream){
        if(myStream.isEmptyStream()){
            return collector.supplier().get();
        }

        T head = myStream.head;
        A tail = collect(collector, myStream.eval());

        return collector.accumulator().apply(tail,head);
    }
    //endregion

    //endregion
}
