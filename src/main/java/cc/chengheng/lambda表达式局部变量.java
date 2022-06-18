package cc.chengheng;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@FunctionalInterface
interface LoadBalancerRequest {
    Integer[] apply();
}

class abc {
    private Long aLong;

    public LoadBalancerRequest createRequest(String request, byte[] body, Integer[] execution) {
        return () -> {
            System.out.println(new String(body));
            Arrays.stream(execution).forEach(System.out::println);
            aLong = 9500L;
            System.out.println(this.aLong);
            System.out.println(this);
            System.out.println(aLong);
            return execution;
        };
    }
}

public class lambda表达式局部变量 {
    public static void main(String[] args) {
        LoadBalancerRequest request = new abc().createRequest("第一个参数request", "第二个参数字节".getBytes(StandardCharsets.UTF_8), new Integer[]{1, 2, 3});
        Integer[] apply = request.apply();

        System.out.println();
    }
}
