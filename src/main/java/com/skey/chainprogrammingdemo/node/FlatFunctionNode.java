package com.skey.chainprogrammingdemo.node;


import com.skey.chainprogrammingdemo.func.MyFunction;

/**
 * Description:
 * <br/>
 * Date: 2020/1/11 19:46
 *
 * @author ALion
 */
public class FlatFunctionNode<In, Out> extends Node<Out> {

    public MyFunction<In, Iterable<Out>> flatFunction;

    public FlatFunctionNode(MyFunction<In, Iterable<Out>> flatFunction) {
        this.flatFunction = flatFunction;
    }

}
