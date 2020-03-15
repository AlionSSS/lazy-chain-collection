package com.skey.chainprogrammingdemo;

import com.skey.chainprogrammingdemo.core.ChainCollection;
import com.skey.chainprogrammingdemo.core.LazyChainCollection;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Description: Test for ChainCollection
 * <br/>
 * Date: 2019/12/3 18:13
 *
 * @author ALion
 */
public class ChainTest {


    @Test
    public void test1() {
        ChainCollection.valueOf("5", "3", "16", "5", "2", "8")
                .map(Integer::valueOf)
                .map(i -> i * i)
                .filter(i2 -> i2 > 10)
                .sort((o1, o2) -> o2 - o1)
                .distinct()
                .foreach(System.out::println);
    }

    @Test
    public void Test2() {
        ChainCollection.valueOf(
                "xiaowang,21,chongqing",
                "xiaoming,18,beijing",
                "xiaoli,27,chengdu",
                "zhangsan,16,guangzhou",
                "lisi,23,changsha"
        ).map(line -> {
            String[] fields = line.split(",");
            String name = fields[0];
            int age = Integer.parseInt(fields[1]);
            String address = fields[2];

            return new Person(name, age, address);
        }).filter(person -> person.age > 18)
                .sort((p1, p2) -> p2.age - p1.age)
                .collect()
                .forEach(System.out::println);

    }

    @Test
    public void Test3() {
        ChainCollection.valueOf(
                "xiaowang,21,chongqing",
                "xiaoming,18,beijing",
                "xiaoli,a2x7,chengdu",
                "zhangsan,16,guangzhou",
                "lisi,23,changsha"
        ).flatMap(line -> {
            List<Person> personList = new ArrayList<>();

            try {
                String[] fields = line.split(",");
                String name = fields[0];
                int age = Integer.parseInt(fields[1]);
                String address = fields[2];

                // 添加一个过滤条件
                if (age > 18) {
                    personList.add(new Person(name, age, address));
                }
            } catch (NumberFormatException e) {
                // int 解析异常，不要该数据，直接忽略
//                e.printStackTrace();
            }

            return personList;
        }).sort((p1, p2) -> p2.age - p1.age)
                .collect()
                .forEach(System.out::println);

    }

    @Test
    public void Test4() {
        LazyChainCollection.valueOf(
                "xiaowang,26,chongqing",
                "XIAOMING,18,beijing",
                "zhongsi,26,beijing",
                "xiaoli,a2x7,chengdu",
                "ZHANGSAN,16,guangzhou",
                "hanhan,19,chongqing",
                "LISI,23,changsha"
        ).map(line -> line.toLowerCase())
//        .filter(line -> !line.startsWith("xiao"))
        .flatMap(line -> {
            List<Person> personList = new ArrayList<>();

            try {
                String[] fields = line.split(",");
                String name = fields[0];
                int age = Integer.parseInt(fields[1]);
                String address = fields[2];

                // 添加一个过滤条件
                if (age > 18) {
                    personList.add(new Person(name, age, address));
                }
            } catch (NumberFormatException e) {
                // int 解析异常，不要该数据，直接忽略
//                e.printStackTrace();
            }

            return personList;
        }).sort((p1, p2) -> p2.age - p1.age)
          .map(p -> p)
          .collect().forEach(System.out::println);
//          .foreach(System.out::println);
    }

    @Test
    public void Test5() {
        // 不会触发
        LazyChainCollection.valueOf(
                "1,2,4",
                "2,6",
                "3,2,8,10",
                "4,8"
        )
        .flatMap(str -> Arrays.asList(str.split(",")))
        .map(c -> Integer.parseInt(c))
        .map(i -> i * i)
        .filter(i2 -> i2 > 10)
        .sort((o1, o2) -> o2 - o1);
    }

    @Test
    public void Test6() {
        LazyChainCollection.valueOf(
                "xiaowang,26,chongqing",
                "XIAOMING,18,beijing",
                "zhongsi,26,beijing",
                "xiaoli,a2x7,chengdu",
                "ZHANGSAN,16,guangzhou",
                "hanhan,19,chongqing",
                "LISI,23,changsha",
                "lisi,29,shanghai"
        ).map(line -> line.toLowerCase())
        .filter(line -> !line.startsWith("xiao"))
        .flatMap(line -> {
            List<Person> personList = new ArrayList<>();

            try {
                String[] fields = line.split(",");
                String name = fields[0];
                int age = Integer.parseInt(fields[1]);
                String address = fields[2];

                // 添加一个过滤条件
                if (age > 18) {
                    personList.add(new Person(name, age, address));
                }
            } catch (NumberFormatException e) {
                // int 解析异常，不要该数据，直接忽略
                // e.printStackTrace();
            }

            return personList;
        })
        .sort((p1, p2) -> p2.age - p1.age)
        .map(p -> p.name.toUpperCase())
        .distinct()
        .collect().forEach(System.out::println);
//          .foreach(System.out::println);
    }


}
