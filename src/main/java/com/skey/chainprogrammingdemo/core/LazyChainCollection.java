package com.skey.chainprogrammingdemo.core;

import com.skey.chainprogrammingdemo.func.MyConsumer;
import com.skey.chainprogrammingdemo.func.MyFunction;
import com.skey.chainprogrammingdemo.func.MyPredicate;
import com.skey.chainprogrammingdemo.node.*;

import java.util.*;


/**
 * Description: 核心容器 惰性
 * <br/>
 * Date: 2019/12/3 18:15
 *
 * @author ALion
 */
public class LazyChainCollection<In> implements Chain<In> {

    Collection<Object> data;

    Node<?> currentNode;

    private LazyChainCollection(Collection<Object> data, Node<?> currentNode) {
        this.data = data;
        this.currentNode = currentNode;
    }

    @SafeVarargs
    public static <T> Chain<T> valueOf(T... elements) {
        return new LazyChainCollection<>(
                new ArrayList<>(Arrays.asList(elements)),
                new Node<>()
        );
    }

    @Override
    public <Out> Chain<Out> map(MyFunction<In, Out> function) {
        return link(new FunctionNode<>(function));
    }

    @Override
    public <Out> Chain<Out> flatMap(MyFunction<In, Iterable<Out>> function) {
        return link(new FlatFunctionNode<>(function));
    }

    @Override
    public Chain<In> filter(MyPredicate<In> predicate) {
        return link(new PredicateNode<>(predicate));
    }

    @Override
    public Chain<In> sort(Comparator<In> comparator) {
        return link(new SortNode<>(comparator));
    }

    @Override
    public Chain<In> distinct() {
        return link(new DistinctNode<>());
    }

    @Override
    public void foreach(MyConsumer<In> consumer) {
        LazyChainCollection<In> collection =
                (LazyChainCollection<In>) link(new ConsumerNode<>(consumer));
        new StageScheduler<>(collection).schedule();
    }

    @Override
    public List<In> collect() {
        List<In> result = new ArrayList<>();
        LazyChainCollection<In> collection =
                (LazyChainCollection<In>) link(new ConsumerNode<In>(result::add));
        new StageScheduler<>(collection).schedule();
        return result;
    }


    /**
     * 创建新的Collection，并与当前的node连接
     * <pre>
     *     [Node/Collection链接示意图]
     *     +-----------------------------------------+
     *     | collection1 | collection2 | collection3 |
     *     |      ↓      |      ↓      |      ↓      |
     *     |    node1  -> <-  node2  -> <-  node3    |
     *     |      ↓      |      ↓      |      ↓      |
     *     |    func1    |    func2    |    func3    |
     *     +-----------------------------------------+
     * </pre>
     *
     * @param node 新的Node
     * @param <X>  Node最终类型
     * @return Chain
     */
    private <X> Chain<X> link(Node<X> node) {
        LazyChainCollection<X> collection = new LazyChainCollection<>(
                data,
                node
        );

        this.currentNode.next = collection.currentNode;
        collection.currentNode.pre = this.currentNode;

        return collection;
    }

}