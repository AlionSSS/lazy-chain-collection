package com.skey.chainprogrammingdemo.core;

import com.skey.chainprogrammingdemo.func.MyConsumer;
import com.skey.chainprogrammingdemo.func.MyFunction;
import com.skey.chainprogrammingdemo.func.MyPredicate;

import java.util.Comparator;
import java.util.List;

/**
 * Description: 链式、函数式接口
 * <br/>
 * Date: 2019/12/3 18:15
 *
 * @author ALion
 */
public interface Chain<In> {

    /**
     * map操作
     * @param function Map函数 [one to one]
     * @param <Out> 返回的类型
     * @return Chain<Out>
     */
    <Out> Chain<Out> map(MyFunction<In, Out> function);

    /**
     * flatMap操作
     * @param function FlatMap函数 [one to multi]
     * @param <Out> 返回的类型
     * @return Chain<Out>
     */
    <Out> Chain<Out> flatMap(MyFunction<In, Iterable<Out>> function);

    /**
     * filter过滤操作
     * @param predicate Predicate函数 [one to one/zero]
     * @return Chain<In>
     */
    Chain<In> filter(MyPredicate<In> predicate);

    /**
     * sort排序操作
     * @param comparator 比较器 [multi to sorted multi]
     * @return Chain<In>
     */
    Chain<In> sort(Comparator<In> comparator);

    /**
     * distinct去重操作
     * @return Chain<In>
     */
    Chain<In> distinct();

    /**
     * 处理操作，无返回
     * @param consumer Consumer函数
     */
    void foreach(MyConsumer<In> consumer);

    /**
     * 获取处理结果
     * @return List<In>
     */
    List<In> collect();

}
