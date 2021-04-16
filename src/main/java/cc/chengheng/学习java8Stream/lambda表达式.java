package cc.chengheng.学习java8Stream;

/**
 * lambda表达式全部语法
 */
public class lambda表达式 {
    public static void main(String[] args) {
//        start(()-> System.out.println("hell"));

        run(
                // 参数特性：传入参数可以忽略类型, 参数只有一个可以省略括号
                // 等同于，只是可以省略掉类型(String name, int age) -> String.format("name:%s age:%s", name, age)
                (name, age) -> String.format("name:%s age:%s", name, age)
        );

        /*
         * 代码编写特性：
         *  1、单行表达式 省略 return，如果没有返回值就代表一句话
         *  2、代码块 一般多行才用
         *          (name, age) -> {
         *              return String.format("name:%s age:%s", name, age)
         *          }
         *  3、静态方法和普通方法引用 -参数要和调用的方法参数和类型相同
         *          run(lambda表达式::doFormat); // 静态方法引用
         *          run(new lambda表达式()::doFormat2); // 普通方法引用
         */
         run(lambda表达式::doFormat); // 静态方法引用
         run(new lambda表达式()::doFormat2); // 普通方法引用
    }

    private String doFormat2(String s, int i) {
        return null;
    }

    private static String doFormat(String s, int i) {
        return null;
    }

    public static void run(MyRun3 myRun3) {
        String 鲁班大师 = myRun3.run("鲁班大师", 30);
        System.out.println(鲁班大师);
    }

    /**
     * 传递lambda表达式
     *
     * @param myRun2 传递lambda表达式
     */
    public static void start(MyRun2 myRun2) {
        new Thread(myRun2).start();

        // result：cc.chengheng.学习java8Stream.lambda表达式$$Lambda$1/1828972342@5fd0d5ae
        System.out.println(myRun2);
    }
}

@FunctionalInterface
interface MyRun3 {
    String run(String name, int age);
}

/* 函数式接口的条件
 * 1、只能有一个方法-》函数式接口, 没有带@FunctionalInterface，也可以，只要式一个接口方法
 * 2、默认方法除外
 * 3、Object的方法除外
 */
@FunctionalInterface
interface MyRun2 extends Runnable {
    default void run2() {

    }

    //region Object的方法除外
    String toString();

    boolean equals(Object obj);
    //endregion
}