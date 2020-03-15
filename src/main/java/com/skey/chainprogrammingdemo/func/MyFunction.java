package com.skey.chainprogrammingdemo.func;

/**
 * Description: Function
 * <br/>
 * Date: 2019/12/3 18:19
 *
 * @author ALion
 */
public interface MyFunction<In, Out> {

    Out apply(In element);

}
