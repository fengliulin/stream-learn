package cc.chengheng.闭包;

public class Milk {



    public final static String name = "纯牛奶";//名称

    private static int num = 16;//数量

    public Milk() {
        System.out.println(name + "：16/每箱");
    }

    /**
     * 闭包
     *
     * @return 返回一个喝牛奶的动作
     */
    public Active HaveMeals() {
        return new Active() {
            @Override
            public void drink() {
                if (num == 0) {
                    System.out.println("没有了，都被你喝完了");
                    return;
                }
                num--;
                System.out.println("喝掉一瓶牛奶");
            }
        };
    }

    /**
     * 获取剩余数量
     */
    public void currentNum() {
        System.out.println(name + "剩余：" + num);
    }

    public static void main(String[] args) {
        // 买了一箱牛奶
        Milk milk = new Milk();

        Active haveMeals = milk.HaveMeals();

        // 没事喝一瓶
        haveMeals.drink();

        // 有事喝一瓶
        haveMeals.drink();

        // 看看还剩多少？
        milk.currentNum();

        milk = null;

        /**
         * 特别注意：
         * 闭包会导致资源不被回收，
         * 如上例，在main方法中将m设为null，
         * 使用haveMeals继续调用drink方法仍然会喝掉一瓶牛奶，
         * 说明Milk对象并没有被释放掉
         */
        haveMeals.drink();
    }
}
