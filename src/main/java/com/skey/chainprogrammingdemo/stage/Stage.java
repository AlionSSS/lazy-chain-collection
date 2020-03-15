package com.skey.chainprogrammingdemo.stage;

import com.skey.chainprogrammingdemo.node.Node;

/**
 * Description:
 * <br/>
 * Date: 2020/1/12 14:32
 *
 * @author ALion
 */
public class Stage<T> {

    public Node<T> node;

    public Stage(Node<T> node) {
        this.node = node;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" + getNodeString(new StringBuilder(), node) + "]";
    }

    private String getNodeString(StringBuilder builder, Node<?> node) {
        if (node == null) {
            return builder.substring(0, builder.length() - 1);
        } else {
            return getNodeString(builder.append(node).append(","), node.next);
        }
    }

}
