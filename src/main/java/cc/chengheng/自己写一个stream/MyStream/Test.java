package cc.chengheng.自己写一个stream.MyStream;

import cc.chengheng.自己写一个stream.function.Function;

public class Test {
    public static void main(String[] args) {
        new CustomStream1<String>("你好").map(a-> {
            System.out.println(a);
            return 123;
        });
    }
}

class CustomStream1<T> {

    private T abc;

    public CustomStream1(T abc) {
        this.abc = abc;
    }

    <R> MyStream<R> map(Function<R, T> mapper) {

        R 你好啊 = mapper.apply(abc); // 由回调的方法决定返回值
        return new MyStream<>();
    }
}