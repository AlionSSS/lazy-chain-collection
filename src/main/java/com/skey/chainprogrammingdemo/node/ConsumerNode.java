package com.skey.chainprogrammingdemo.node;


import com.skey.chainprogrammingdemo.func.MyConsumer;

/**
 * Description:
 * <br/>
 * Date: 2020/1/11 19:46
 *
 * @author ALion
 */
public class ConsumerNode<In> extends Node<In> {

    public MyConsumer<In> consumer;

    public ConsumerNode(MyConsumer<In> consumer) {
        this.consumer = consumer;
    }

}
