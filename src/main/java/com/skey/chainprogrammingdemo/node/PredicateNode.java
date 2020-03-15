package com.skey.chainprogrammingdemo.node;


import com.skey.chainprogrammingdemo.func.MyPredicate;

/**
 * Description:
 * <br/>
 * Date: 2020/1/11 19:46
 *
 * @author ALion
 */
public class PredicateNode<In> extends Node<In> {

    public MyPredicate<In> predicate;

    public PredicateNode(MyPredicate<In> predicate) {
        this.predicate = predicate;
    }

}
