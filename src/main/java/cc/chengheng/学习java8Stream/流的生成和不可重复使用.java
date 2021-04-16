package cc.chengheng.学习java8Stream;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class 流的生成和不可重复使用 {

    private static List<Apple> appleStore = new ArrayList<>();

    static {
        appleStore.add(new Apple(1, "red", 500, "湖南"));
        appleStore.add(new Apple(2, "red", 100, "天津"));
        appleStore.add(new Apple(3, "green", 300, "湖南"));
        appleStore.add(new Apple(4, "green", 200, "天津"));
        appleStore.add(new Apple(5, "green", 100, "湖南"));
    }

    public static void main(String[] args) {
        // list -> stream

        // 生成流的几种方式
        appleStore.stream();
        Arrays.stream(new int[]{1, 2, 3});
        Stream.of(1, 2, 3, 4);

        // 流是一直走下去，不可重复
        Stream<Apple> stream1 = appleStore.stream();
        Stream<Apple> stream2 = stream1.filter(a -> a.getColor().equals("red"));
        Stream<Apple> stream3 = stream2.filter(a -> a.getWeight() > 100);
//        Stream<Apple> stream4 = stream3.filter(a -> a.getWeight() > 100); // 错误

        /*
         * 中间节点-》懒节点
         * 区分：返回Stream的就是中间节点，返回结果的就是终值节点
         */
        appleStore.stream().filter(a->{
//            System.out.println("hello");
            return true;
        }).toArray();

        /*
         * 流是一次执行一条记录，而不是全部执行
         * 上个节点，可以影响下一个节点
         *
         */
        appleStore.stream()
                .filter(a->a.getColor().equals("red") || a.getColor().equals("green")) // 上个节点处理的结果，可以影响下一个节点
                .map(Apple::getColor)// 转换
                .distinct() // 去重
                .peek(System.out::println)
//                .peek(a-> System.out.println(a.getColor())) // 相当于责任链模式
//                .peek(a-> System.out.println(a.getWeight())) // peek就是执行一个函数, 中间节点
                .forEach(a-> System.out.println(a)); // 终值节点
//                .toArray();


        /*
         * 采集
         * 1、list
         * 2、map
         * 3、group by
         * 4、数组
         * 5、求出最大值
         * 6、求任意值
         *
         * 使用场景，数据源来源很多的地方用
         */
        appleStore.stream()
                // 第三个参数去重复
                .collect(Collectors.toMap(a->a.getColor(), a->a, (a1, a2) -> a1));
    }
}

interface map<k,v>{

}
class a<E>{

}

class bb<K,V> {

    final class b extends a<Map.Entry<K,V>> {

    }
}
