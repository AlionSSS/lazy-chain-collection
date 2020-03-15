package com.skey.chainprogrammingdemo.node;


import com.skey.chainprogrammingdemo.func.MyFunction;

/**
 * Description:
 * <br/>
 * Date: 2020/1/11 19:46
 *
 * @author ALion
 */
public class FunctionNode<In, Out> extends Node<Out> {

    public MyFunction<In, Out> function;

    public FunctionNode(MyFunction<In, Out> function) {
        this.function = function;
    }

}
