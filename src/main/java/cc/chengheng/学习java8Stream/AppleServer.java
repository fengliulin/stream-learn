package cc.chengheng.学习java8Stream;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AppleServer<K, V> {

    private static List<Apple> appleStore = new ArrayList<>();

    static {
        appleStore.add(new Apple(1, "red", 500, "湖南"));
        appleStore.add(new Apple(2, "red", 100, "天津"));
        appleStore.add(new Apple(3, "green", 300, "湖南"));
        appleStore.add(new Apple(4, "green", 200, "天津"));
        appleStore.add(new Apple(5, "green", 100, "湖南"));
    }

    public static void main(String[] args) {
        // 过滤自定义条件
//        test2(a->a.getColor().equals("red") && a.getWeight() > 300);
        test4();
//        test3();
    }


    // 找出红色苹果， 重量 产地 -传统方法
    private static void test1() {
        for (Apple apple : appleStore) {
            if (apple.getColor().equals("red")) {

            }
        }
    }

    // 过滤颜色 - stream
    public static void test2(Predicate<? super Apple> pr) {
        List<Apple> red = appleStore.stream()
                .filter(pr) // 可以弄条件
                .collect(Collectors.toList());
    }

    // 求出每个颜色的平均重量- 传统方法
    public static void test3() {
        // 1、 基于颜色分组
        Map<String, List<Apple>> maps = new HashMap<>();
        for (Apple apple : appleStore) {
            // computeIfAbsent 如果不存在就创建一个
            List<Apple> list = maps.computeIfAbsent(apple.getColor(), key -> new ArrayList<>());
            list.add(apple);
        }

        for (Map.Entry<String, List<Apple>> stringListEntry : maps.entrySet()) {
            int weights = 0;
            for (Apple apple : stringListEntry.getValue()) {
                weights += apple.getWeight();
            }

            System.out.println(String.format("颜色%s 平均重量%s", stringListEntry.getKey(), weights / stringListEntry.getValue().size()));
        }
    }

    // 求出每个颜色的平均重量- Stream流
    public static void test4() {

        appleStore.stream()
                .collect(
                        Collectors.groupingBy(
                                Apple::getColor, // 基于颜色分组
                                Collectors.averagingInt(Apple::getWeight) // 统计平均重量
                        )
                )
                .forEach((k, v) -> System.out.println(k + ":" + v));
    }
}
