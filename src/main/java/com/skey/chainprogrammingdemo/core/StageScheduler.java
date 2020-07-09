package com.skey.chainprogrammingdemo.core;

import com.skey.chainprogrammingdemo.node.*;
import com.skey.chainprogrammingdemo.stage.ShuffleMapStage;
import com.skey.chainprogrammingdemo.stage.ResultStage;
import com.skey.chainprogrammingdemo.stage.Stage;

import java.util.*;

/**
 * Description: Stage调度器
 * <br/>
 * Date: 2020/1/12 18:32
 *
 * @author ALion
 */
public class StageScheduler<T> {

    private LazyChainCollection<T> collection;

    public StageScheduler(LazyChainCollection<T> collection) {
        this.collection = collection;
    }

    /**
     * 开始调度
     * <pre>
     *     [Stage运行与数据流转]
     *     +--------------------------+
     *     | Stage1 | Stage2 | Stage3 |
     *     |--------------------------|
     *     | data1 => data2 => data3  |
     *     +--------------------------+
     * </pre>
     */
    public void schedule() {
        System.out.println("---> 开始执行任务！解析Stage...");
        List<Stage<?>> stages = parseStage(collection.currentNode);
        System.out.println("---> Stage 解析完毕！stages = " + stages);
        System.out.println("---> 提交Stage ===>");

        // 数据存储点
        Collection<Object> data = collection.data;

        for (Stage<?> stage : stages) {
            System.out.println("---> Start Stage[" + stage + "]");
            if (stage instanceof ShuffleMapStage) {
                // 处理 MapShuffleStage
                handleStageToStage(data, (ShuffleMapStage<?>) stage);
            } else if (stage instanceof ResultStage) {
                // 处理 ResultStage
                handleStage(data, stage);
            } else {
                throw new IllegalArgumentException(
                        "No such type Stage[" + stage + "]! " +
                                "Stage must be in [MapShuffleStage,ResultStage]!"
                );
            }
        }
    }

    /**
     * 为Node链划分Stage
     * <pre>
     *     [Stage划分示意图]
     *     +-------------------------------------------------------------------------+
     *     |    MapShuffleStage1    |    MapShuffleStage2    |      ResultStage3     |
     *     |-------------------------------------------------------------------------|
     *     | node1 -> node2 -> null |      node3 -> null     | node4 -> node5 -> ... |
     *     +-------------------------------------------------------------------------+
     * </pre>
     *
     * @return Stage列表
     */
    private List<Stage<?>> parseStage(Node<?> node) {
        List<Stage<?>> stages = new ArrayList<>();

        // 找到第一个Node，构建第一个Stage
        Node<?> current = findFirstNode(node).next; // 不要起始node，因为LazyChainCollection创建时，该node是空的
        stages.add(new ShuffleMapStage<>(current)); // 添加第一个Node到stages中

        while (current != null) {
            Node<?> next = current.next;

            if (current instanceof SortNode || current instanceof DistinctNode) {
                // 切割Stage
                current.next = null; // 当前Node向后指向null
                next.pre = null; // 后一个Node向前指向null

                // 新起一个Stage
                stages.add(new ShuffleMapStage<>(next));
            } else if (current instanceof ConsumerNode) {
                // 如果找到ConsumerNode，更新最后的Stage为ResultStage
                Node<?> stageFirstNode = findFirstNode(current);
                stages.set(stages.size() - 1, new ResultStage<>(stageFirstNode));
            }

            // 移动指针到下一个节点
            current = next;
        }

        return stages;
    }

    /**
     * 向前查找第一个Node
     *
     * @param node 任意节点
     * @return 第一个Node
     */
    private Node<?> findFirstNode(Node<?> node) {
        if (node.pre == null) {
            return node;
        } else {
            return findFirstNode(node.pre);
        }
    }

    /**
     * 向后查找最后一个Node
     *
     * @param node 任意节点
     * @return 最后一个Node
     */
    private Node<?> findLastNode(Node<?> node) {
        if (node.next == null) {
            return node;
        } else {
            return findLastNode(node.next);
        }
    }

    /**
     * 处理MapShuffleStage到下一个Stage的操作
     *
     * @param data  数据容器
     * @param stage 当前Stage
     */
    private <In> void handleStageToStage(Collection<In> data, ShuffleMapStage<?> stage) {
        Node<?> lastNode = findLastNode(stage.node);
        if (lastNode instanceof SortNode) {
            SortNode<In> sortNode = (SortNode<In>) lastNode;

            // TreeSet代表了接下来的 排序Shuffle
            // 最好找一个只排序不去重的容器(更快)，这里为了便利使用TreeSet
            TreeSet<In> treeSet = new TreeSet<>(new Comparator<In>() {
                @Override
                public int compare(In o1, In o2) {
                    // 防止去重
                    int num = sortNode.comparator.compare(o1, o2);
                    return num == 0 ? 1 : num;
                }
            });

            // 切换Stage最后一个Node为ConsumerNode[添加结果到treeSet]
            lastNode.pre.next = new ConsumerNode<>(treeSet::add);

            // 开始处理
            handleStage(data, stage);

            System.out.println("------> Current Stage: shuffle[sort] => data");
            data.clear();
            data.addAll(treeSet);
        } else if (lastNode instanceof DistinctNode) {
            // HashSet代表了接下来的 去重Shuffle
            HashSet<In> hashSet = new HashSet<>();

            // 切换Stage最后一个Node为ConsumerNode[添加结果到hashSet]
            lastNode.pre.next = new ConsumerNode<>(hashSet::add);

            // 开始处理
            handleStage(data, stage);

            System.out.println("------> Current Stage: shuffle[distinct] => data");
            data.clear();
            data.addAll(hashSet);
        } else {
            // 除了Sort/distinct外，可能你还有其他shuffle需求，在else处修改
            throw new IllegalArgumentException(
                    "No such type Node[" + lastNode + "]! " +
                            "Shuffle Node must be in [DistinctNode,SortNode]!"
            );
        }
    }

    /**
     * 处理 Stage
     */
    private <A> void handleStage(Collection<A> list, Stage<?> stage) {
        for (A element : list) {
            handleStageNode(element, stage.node);
        }
        System.out.println("------> Current Stage: map over");
    }

    /**
     * 递归处理Node后续所有节点
     */
    private <A, B> void handleStageNode(A element, Node<B> node) {
        if (node != null) {
            if (node instanceof FunctionNode) {
                FunctionNode<A, B> functionNode = (FunctionNode<A, B>) node;
                B apply = functionNode.function.apply(element);

                handleStageNode(apply, node.next);
            } else if (node instanceof FlatFunctionNode) {
                FlatFunctionNode<A, B> flatFunctionNode = (FlatFunctionNode<A, B>) node;
                Iterable<B> iter = flatFunctionNode.flatFunction.apply(element);
                for (B b : iter) {
                    handleStageNode(b, node.next);
                }
            } else if (node instanceof PredicateNode) {
                PredicateNode<A> predicateNode = (PredicateNode<A>) node;
                boolean test = predicateNode.predicate.test(element);
                if (test) {
                    handleStageNode(element, node.next);
                }
            } else if (node instanceof ConsumerNode) {
                ConsumerNode<A> consumerNode = (ConsumerNode<A>) node;
                consumerNode.consumer.accept(element);
            } else {
                throw new IllegalArgumentException(
                        "No such type Node[" + node + "]! " +
                                "Map Node must be in [FunctionNode,FlatFunctionNode,PredicateNode,ConsumerNode]!"
                );
            }
        }
    }

}
