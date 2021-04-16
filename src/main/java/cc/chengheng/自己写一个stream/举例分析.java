package cc.chengheng.自己写一个stream;

import cc.chengheng.自己写一个stream.IntegerStreamGenerator;
import cc.chengheng.自己写一个stream.MyStream.MyStream;

public class 举例分析 {
    public static void main(String[] args) {
        /*IntegerStreamGenerator.getIntegerStream(1, 10)
                .filter(item -> item % 2 == 0)
                .map(item -> item * item)
                .limit(2)
                .forEach(System.out::println);*/
//                .reduce(0, (i1, i2) -> i1 + i2);

        MyStream<Integer> integerStream = IntegerStreamGenerator.getIntegerStream(1, 10);
//        MyStream<Integer> filter = integerStream.filter(item -> item % 2 == 0);
        MyStream<Integer> map = integerStream.map(item -> item * item);
//        MyStream<Integer> limit = map.limit(2);

        map.forEach(System.out::println);

        System.out.println();
    }
}
