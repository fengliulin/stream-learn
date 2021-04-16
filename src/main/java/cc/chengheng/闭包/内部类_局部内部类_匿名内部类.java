package cc.chengheng.闭包;

/**
 * 食物
 */
class Food {
    public static final String name = "Food";
    private int num = 20;

    public Food() {
        System.out.println("美味的食物");
    }


    public Active getEat() {
        return new EatActive();
    }

    /**
     * 内部类也是闭包，无论创建多少个对象，修改外不类的字段，修改的是一个
     * 因为外部对象是一个
     */
    private class EatActive implements Active {
        @Override
        public void eat() {
            if (num == 0) {
                System.out.println("吃货，已经吃没了");
            }
            num--;
            System.out.println("吃货，你吃了一份了");
        }
    }


    /**
     * 局部内部类闭包，就是在方法内定义的内部类
     * @return
     */
    public Active getEat局部内部类() {
        class EatActive implements Active {
            @Override
            public void eat() {
                if (num == 0) {
                    System.out.println("吃货，已经吃没了");
                }
                num--;
                System.out.println("吃货，你吃了一份了");
            }
        }

        return new EatActive();
    }

    public Active getEat匿名内部类() {
        return new Active() {
            @Override
            public void eat() {
                if (num == 0) {
                    System.out.println("吃货，已经吃没了");
                }
                num--;
                System.out.println("吃货，你吃了一份了");
            }
        };
    }

    public void currentNum() {
        System.out.println("还剩：" + num + "份");
    }
}

public class 内部类_局部内部类_匿名内部类 {
    public static void main(String[] args) {
        Food food = new Food();
        food.getEat().eat();
        food.getEat().eat();

        food.getEat局部内部类().eat();

        food.getEat匿名内部类().eat();

        food.currentNum();

        //在内部类是public修饰时，可以通过以下方式
        /*Food foodPub = new Food();
        Active eat = foodPub.new EatActive();
        eat.eat();*/
    }
}
