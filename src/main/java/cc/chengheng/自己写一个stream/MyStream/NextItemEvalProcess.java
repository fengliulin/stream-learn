package cc.chengheng.自己写一个stream.MyStream;

import cc.chengheng.自己写一个stream.function.EvalFunction;

/**
 * 下一个求值过程
 */
public class NextItemEvalProcess {

    /** 求值方法 */
    private final EvalFunction evalFunction;

    public NextItemEvalProcess(EvalFunction evalFunction) {
        this.evalFunction = evalFunction;
    }

    public MyStream eval() {
        return evalFunction.apply();
    }
}
