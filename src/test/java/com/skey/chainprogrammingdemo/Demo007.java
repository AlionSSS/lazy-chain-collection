package com.skey.chainprogrammingdemo;

import com.skey.chainprogrammingdemo.core.Chain;
import com.skey.chainprogrammingdemo.core.LazyChainCollection;

import java.util.Arrays;

/**
 * Description:
 * <br/>
 * Date: 2019/12/4 18:24
 *
 * @author ALion
 */
public class Demo007 {

    public static void main(String[] args) {
//        Stream.of("1", "4", "2", "3")
//                .map(Integer::valueOf)
//                .map(i -> i * i)
//                .filter(i2 -> i2 > 4)
//                .sorted((i1, i2) -> i2 - i1)
//                .forEach(System.out::println);
//
//        System.out.println("------------");
//        int a = 123456789;
//        while (a > 0) {
//            System.out.print(a % 10);
//            a /= 10;
//        }

        Chain<String> lazyChain = LazyChainCollection.valueOf(
                "1,2,4",
                "2,6",
                "3,2,8,10",
                "4,8"
        );

        lazyChain
                .flatMap(str -> Arrays.asList(str.split(",")))
                .map(c -> Integer.parseInt(c))
                .map(i -> i * i)
                .filter(i2 -> i2 > 10)
                .sort((o1, o2) -> o2 - o1)
                .foreach(r -> System.out.println(r));

    }




}
