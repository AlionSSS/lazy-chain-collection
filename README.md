# lazy-chain-collection
惰性运行的链式编程容器（类似于Java8的Stream）

## 普通链式容器示例
```java
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
```

## 惰性链式容器示例
```java
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
```
