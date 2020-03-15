package com.skey.chainprogrammingdemo.node;


/**
 * Description:
 * <br/>
 * Date: 2020/1/11 19:46
 *
 * @author ALion
 */
public class Node<Out> {

    public Node<?> pre;

    public Node<?> next;

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}
