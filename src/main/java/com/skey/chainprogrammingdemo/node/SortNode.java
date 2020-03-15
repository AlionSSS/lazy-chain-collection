package com.skey.chainprogrammingdemo.node;


import java.util.Comparator;

/**
 * Description:
 * <br/>
 * Date: 2020/1/11 19:46
 *
 * @author ALion
 */
public class SortNode<In> extends Node<In> {

    public Comparator<In> comparator;

    public SortNode(Comparator<In> comparator) {
        this.comparator = comparator;
    }

}
