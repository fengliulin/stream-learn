package cc.chengheng.学习java8Stream;

/**
 * 苹果
 */
public class Apple {
    private int id;            // 编号
    private String color;      // 颜色
    private int weight;        // 重量
    private String birthplace; // 产地

    public Apple(int id, String color, int weight, String birthplace) {
        this.id = id;
        this.color = color;
        this.weight = weight;
        this.birthplace = birthplace;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }
}
