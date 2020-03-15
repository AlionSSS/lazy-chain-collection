package com.skey.chainprogrammingdemo.core;

import com.skey.chainprogrammingdemo.func.MyConsumer;
import com.skey.chainprogrammingdemo.func.MyFunction;
import com.skey.chainprogrammingdemo.func.MyPredicate;

import java.util.*;


/**
 * Description: 核心容器
 * <br/>
 * Date: 2019/12/3 18:15
 *
 * @author ALion
 */
public class ChainCollection<In> implements Chain<In> {

    private ArrayList<In> list;

    private ChainCollection() {
        list = new ArrayList<>();
    }

    @SafeVarargs
    public static <T> Chain<T> valueOf(T... elements) {
        ChainCollection<T> collection = new ChainCollection<>();
        for (T element : elements) {
            collection.add(element);
        }

        return collection;
    }

    private void add(In element) {
        list.add(element);
    }

    @Override
    public <Out> Chain<Out> map(MyFunction<In, Out> function) {
        ChainCollection<Out> collection = new ChainCollection<>();
        for (In a : list) {
            collection.add(function.apply(a));
        }

        return collection;
    }

    @Override
    public <Out> Chain<Out> flatMap(MyFunction<In, Iterable<Out>> function) {
        ChainCollection<Out> collection = new ChainCollection<>();
        for (In a : list) {
            for (Out b : function.apply(a)) {
                collection.add(b);
            }
        }
        return collection;
    }

    @Override
    public Chain<In> filter(MyPredicate<In> predicate) {
        ChainCollection<In> collection = new ChainCollection<>();
        for (In a : list) {
            if (predicate.test(a)) {
                collection.add(a);
            }
        }

        return collection;
    }

    @Override
    public void foreach(MyConsumer<In> consumer) {
        for (In a : list) {
            consumer.accept(a);
        }
    }

    @Override
    public Chain<In> sort(Comparator<In> comparator) {
        // 自己实现以一个排序算法
        // 这里为了简便，直接调Java自带的排序算法

        ChainCollection<In> collection = new ChainCollection<>();
        for (In a : list) {
            collection.add(a);
        }

        collection.list.sort(comparator);

        return collection;
    }

    @Override
    public Chain<In> distinct() {
        ChainCollection<In> collection = new ChainCollection<>();
        HashSet<In> set = new HashSet<>(list);
        for (In a : set) {
            collection.add(a);
        }

        return collection;
    }

    @Override
    public List<In> collect() {
        return list;
    }

}
