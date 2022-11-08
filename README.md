# 目录
1. [Wordle项目框架](#Wordle项目框架)
2. [Wordle项目代码部分](#Wordle项目代码部分)

# Wordle项目框架
## 概要
相信第一眼看到这么多代码一定不知道从什么地方入手，这个也是我第一次写大项目的时候的具体感受。完全不知道从哪里开始看起来，完全不知道这个在写个
啥。这个代码行文逻辑是啥也不太清楚。没关系，我们一点点来看。

首先，我们知道的是我们每一个Java的项目（Project）都有一个main method（主方法），这个方法定义在
[Application.java](src/main/java/com/problem/sets/app/Application.java)中。但是我们发现在这个method中，只有一行，感觉不知道它到
底在干什么。没关系，我们之后会了解。我们也发现这里面几乎没有创建过Object（物），尤其是在
[service](src/main/java/com/problem/sets/app/service)这个文件夹下的Class（类）。这些都得益于Java的Spring/SpringBoot框架。我们来
一点点看。

## 什么是框架？
项目要能运行起来，需要依靠不同的Library/Package。很多package我们很熟悉，比如说
[java.util](https://docs.oracle.com/javase/8/docs/api/java/util/package-summary.html) ，
[java.lang](https://docs.oracle.com/javase/8/docs/api/java/lang/package-summary.html) 等。不过我们也知道像
[java.lang](https://docs.oracle.com/javase/8/docs/api/java/lang/package-summary.html) 这个Package是不需要import的。但是绝大
多数的package是需要import的。像是前文提到的
[java.util](https://docs.oracle.com/javase/8/docs/api/java/util/package-summary.html) 。这些是Java SE 
（Java Standard Edition）自带的package，当我们设置JDK（Java Development Kit）的时候，它会自动引用，所以不需要额外的设置。这里JDK就
是一个Library。不过很多我们的工业代码不会局限于JDK自带的这些Library。这样我们需要写的代码会有很多。比如我们前文提到的`new`这个关键词就几
乎没在这些代码当中出现过。为什么会这样呢？因为框架会帮我们自动生成对应的`new`的代码，也就是框架会帮我们生成代码，这就是我们用框架的好处。这
些框架都是定义在Library当中的，有些Library还会引用其他的Library，这都是后话了。

那么我们需要用这些Library，在哪里引用呢？我们就引入到下一个话题了。

## Maven项目
我们一般会说这是一个Maven project，相当于说的就是基于Maven这个Tool帮助我们搭建整个项目。这个Tool就会基于一个文件就是
[pom.xml](pom.xml)

### XML

XML全称是Extensible Markup Language。也就是说，它也是一种programming language。当我们看到Markup Language我们需要知道的第一反应应
该是，这种语言即使不符合它的syntax（语法），也是能运行的。像我们现在写的[README.md](README.md) 也是一种markup language。这种markup 
language叫做markdown所以后缀用`.md`来表示。

XML文件一般也有open tags和对应的close tags，比如`<build>`就是一个open tag，而`</build>`就是一个close tag。在open和close tag当中
写的内容就是这个tag定义相关的内容。当然有时候你也需要定义这个tag的attributes（属性），就会在open tag右尖括号`>`前定义它的attributes。

当然还有一些人为了写了简单一些我们会用像是`<build />`的方式来表示它即开了`build`这个tag也同时把这个tag关上了。需要注意的是，这个首先需要
在`>`前加上一个斜线`/`在这个斜线前加一个空格。这种写法不是规范的写法。

### pom.xml
我们来看看这个文件里面写了一些啥。
```xml
<?xml version="1.0" encoding="UTF-8"?>
```
这个定义了xml文件的格式。

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
```
这个定义了xml这段代码是写maven pom文件的

```xml
<modelVersion>4.0.0</modelVersion>
```
这是maven pom文件的版本。

```xml
    <groupId>com.problem.sets.app</groupId>
    <artifactId>wordle</artifactId>
    <version>1.0-SNAPSHOT</version>
```
这是我们要写的这个项目的基本信息，比如我们默认的路径是`com.problem.sets.app`，在这个路径下的`wordle`项目，项目的版本是`1.0-SNAPSHOT`。

```xml
    <build>
        <defaultGoal>install</defaultGoal>
    </build>
```
这是告诉maven如果我们需要build这个maven项目，maven需要帮我们做些什么。

```xml
    <properties>
        <maven.compiler.target>11</maven.compiler.target>
        <maven.compiler.source>11</maven.compiler.source>
    </properties>
```
这是告诉maven我们是用openjdk-11作为我们这个程序的语言。

在`<dependencies>`这个tag当中就是我们前文讲到的Library在哪里定义的。每个dependency都有对应的groupId、artifactId和version。有些时候
还会带有scope。

当我们运行这个pom文件的时候，它会自动生成对应的iml文件，告诉maven这个项目的library应该哪些library depend on哪些library，所以会有先引
用什么library再引用哪个library。然后一一下载对应的library。

## .gitignore
不过我们发现在git repo（repository）当中我们并没有我们前文提到的iml文件。那么它为什么没有呢？答案是我们带了[.gitignore](.gitignore)
这个文件。这个文件告诉了git哪些文件我们不需要放到repo上去。

在这个文件中，我们不仅仅定义了`wordle.iml`文件，还定义了一些其他的文件。比如`.idea`是IntelliJ IDEA这个IDE（integrated development 
environment 我会翻译为：代码编辑器）生成的cache的文件会放在这个文件夹下。我们之前也说过，在`Linux/Unix/Mac OS`这些环境下，文件或者文件
夹前面带上`.`表示这是个隐藏文件或者隐藏文件夹。

`.DS_Store`这是Mac OS在用Finder打开的时候生成的cache文件

`target`是我们运行这个代码的时候生成的那些`.class`文件。因为这些文件是environment dependent的，（就是取决于你是Mac OS/Windows或是其
他的系统环境的），所以我们需要在`.gitignore`当中定义

最后还有`src/main/resources/report.xlsx`，这个是我们生成的report，所以我们不需要include。

## Design Patterns
我们终于可以进入到我们实际的Java代码中了。但是进入实际代码之前，我们要介绍design patterns。这是我们写代码的一些风格。当我们需要实现一些目
标的时候我们就会用对应的design patterns。在这里的代码，我们用到了以下这些design pattern。详细的部分你可以在
[wiki](https://en.wikipedia.org/wiki/Software_design_pattern) 中查看。

### Builder
代码样例：

```java
public class SomeDataClass {
  private SomeFieldType field1;
  private SomeFieldType field2;
  
  public SomeDataClass() { }
  
  public SomeDataClass(SomeFieldType field1, SomeFieldType field2) {
    this.field1 = field1;
    this.field2 = field2;
  }
}
```

在这段代码当中我们看到有两个constructor（构造器），但是如果我们一个Data Structure中有很多的field，有些时候我们有这些数据，其他数据没有，
而其他的时候我们有另一部分的数据，但是没有这些数据，在之后可能才会收到对应的数据，不过我们需要先构造对应的object。怎么办呢？我们不可能为了
所有的field去排列组合去写下所有的constructor对吧。而且，我们需要记住每个构造器里面的所有参数传入时候的顺序，一般情况下是做不到的，而且很
容易发生错误。这些错误不容易被察觉，我们需要很多时间去debug，得不偿失。那么我们就需要builder这种design pattern了。

我们来看看下面这个例子：

```java
public class Model {
  private int field1;
  private String field2;
  private Object field3;

  public Model() { }

  public Model(int field1, String field2, Object field3) {
    this.field1 = field1;
    this.field2 = field2;
    this.field3 = field3;
  }

  public static ModelBuilder builder() {
    return new ModelBuilder();
  }

  public static class ModelBuilder {
    private int field1;
    private String field2;
    private Object field3;
  
    public ModelBuilder field1(int field1) {
      this.field1 = field1;
      return this;
    }

    public ModelBuilder field2(String field2) {
      this.field2 = field2;
      return this;
    }

    public ModelBuilder field3(Object field3) {
      this.field3 = field3;
      return this;
    }

    public Model build() {
      return new Model(field1, field2, field3);
    }
  }
}
```

这段代码当中我们发现有一个static class `ModelBuilder`，这个class是`Model`这个class的一个属性，有点类似于`Map.Entry`的样子。这样是不
是会好理解一些。Map表示的是a collection of key-value pairs。而一个entry就是一个key-value pair。

有了上面这个`Builder` class之后我们就只可以用下面这个方法去构建`Model`Object了：

```java
Model.builder().field1(field1).field2(field2).field3(field3).build();
```

我们发现我们其实可以交换`field1/2/3`的顺序，所以这会对我们构造Model的object方便很多。如果没有对应的数值，我们不需要传入相关的数据即可。
它依然可以生成对应的Model Object。

### Singleton
如果我们要生成一个class的object，但是全局只能有这样一个object的话，我们就不能让constructor变成public。怎么做呢？

我们来看看下面这个例子：

```java
public class Configuration {
  private static Configuration configuration = null;

  private Configuration () { }

  public static Configuration getConfiguration() {
    if (configuration == null) {
      configuration = new Configuration();
    }

    return configuration;
  }
}
```

我们每次需要获取`configuration`的时候，我们只需要`Configuration.getConfiguration()`即可。

### Factory
这里用到了Factory这种design pattern。不过我也不太了解这种design pattern，我尽量把这种design pattern讲得清楚一些。因为在我们这个项目
当中用到的Factory和我遇到的Factory有点不同。

```java
public class ShapeFactory {
  public Circle generateCircle(...) {
    ...
  }

  public Rectangle generateRectangle(...) {
    ...
  }
  
  ...
}
```

大致的意思就是创建图形会比较麻烦，我们用一个Factory来帮我们解决创建的难题。虽然你会觉得，诶，创建一个图形并没有那么难嘛。那么我们换一个例子
，如果是我们要创建一个图形界面，就像是这个IDE。我们有最上面的menu，有左边的side-bar，右边的main panel，下面可能有Terminal panel。那么
创建一个panel其实就是很复杂的过程。以menu为例，我们可能会传入我们的menu的一级分类，二级分类，有些时候有三级/四级分类。我们会把对应的menu
分类的内容的页面称之为链接。当用户点击分类的时候，我们需要导入对应链接的页面。创建这个menu就会很复杂，我们如果只传这些参数的时候，让
Factory帮我们解决这个问题，我们作为非资深程序员，就不需要知道它里面是怎么实现这个menu的。只需要知道，这样传入参数的话，我们就会生成对应的
menu就可以了。Factory就是资深程序员写的，然后非资深的程序员来引用。

### Abstract Factory
这个design pattern是有个interface，在当中定义了很多的method，但是实现方式其实取决于实现该interface的方式。举例说明：

```java
public interface 2DShape {
  public double getArea();
}
```

实现获取一个2DShape的面积取决于这是一个什么形状，所以我们需要该interface。

### Dependency Injection
说实话，我其实没觉得这是一个design pattern，但是在工业代码当中运用很多。就是告诉Constructor我们应该用哪个参数，而不是在Constructor当中
创建对应的Object。

```java
public class SomeClass {
  private SomeOtherClass field;
  
  public SomeClass(SomeOtherClass field) {
    this.field = field;
  }
}
```

## Annotations
我们终于可以进入到我们项目的代码中了。但是进入项目代码之前，我们要重新介绍annotation。这些annotation的library就是定义在我们前文说的
[pom.xml](pom.xml)文件下的。我们来一一说明。

### @SpringBootApplication
加在带有main method的class上。注明这是一个Spring Boot的Application。

### @Bean
加在method上。简单来说这个就是在运行整个程序前预先创建好的Object，在我们创建其他Object的时候就可以直接用Dependency Injection的方式去创建。需要注意的
是，如果没在`@Bean`注明的话，我们用Dependency Injection去注入的时候，我们注入的变量名和method的名字一摸一样。

### @Configuration
加在class上。所有`@Bean`的method需要在`@Configuration`的class下面。可以有很多个class都带有`@Configuration`的annotation。

### @Component, @Repository, @Service
加在class上。我们发现这个程序当中其实只有一处用了`@Bean`这个annotation，但是其他的class对应的Object为什么没有创建呢？答案就是有这个
annotation。其中`@Repository`和`@Service`继承了`@Component`这个annotation。它就是告诉我们它会自动创建它对应的整个系统运行时唯一的一
个Object。那么我们为什么要分三个呢？目的是区分你这个class的作用。`@Repository`主要是管数据的，而`@Service`主要表示这是一个集成的服务。

### @Getter, @Setter
既可以加在field上也可以加在class上。如果是加在field上，就是针对该field的getter/setter，如果是加在class上，那就是针对该class中的所有的
field。我们可以加对应的access level比如protected。默认是public。

### @Data
加在class上，它包含了`@Getter`, `@Setter`, `@RequiredArgsConstructor`等

### @Builder
加在class上。我们前面介绍了`Builder`这个design pattern，这个`@Builder`annotation自动帮我们生成了所有field对应的builder class。

### @NoArgsConstructor, @AllArgsConstructor
加在class上。就是创建对应class的constructor。如果是`@AllArgsConstructor`那么参数的顺序就是field先后顺序。

### @Autowired
加在field或者constructor上的。一般建议加在constructor上，代表我们要自动用这个constructor的方法进行注入object，如果是加在field上，那
么就是对这个field自动注入。

### @Value
加在field或者constructor/method参数上，相当于用value里面设定的值注入到该参数上。如果加在field上，那么就是直接注入参数，如果是在method
或者constructor的话，就是传入参数时自动用value里面的值传入。需要注意的是，我们一般针对这样的constructor上会带`@Autowired`，对于这样的
method会带有`@Bean`。

### @Slf4j
加在class上。是`@Log4j`的新版本，相当于是一个Logger。自动注入一个Logger的field。然后我们可以直接使用对应的method，像是
`log.info("something");`

# Wordle项目代码部分
简单来说，我们需要写代码的地方就是[Guesser.java](src/main/java/com/problem/sets/app/Guesser.java)里面的`findSecretWord`。最后只
需要提交这个文件即可。其他文件不需要改动，也不能改动。当然如果需要debug的话，你可以对其他的文件进行改变。

## 题面
Wordle是一个猜词游戏，可以点此[链接](https://www.nytimes.com/games/wordle/index.html) 进行游玩，了解游戏过程。

我们目的是提供一个该游戏的解法。我们首先提供一个`List<String> dictionary`是我们的词库，所有猜的词必须在这个dictionary当中。然后我们提
供一个guessService，当`guessService.guess(someString)`便返回对应是否match的结果。在`findSecretWord`这个method退出前必须要通过
`guessService`猜对正确的单词才能退出，否则默认判定没猜出单词。

你可以利用我前面介绍的那些annotation，改变我们的constructor，然后进行dependency injection。

### 参数以及备注
+ `dictionary`里面的单词长度都相同。里面的单词都是小写字母，没有特殊字符。
+ 每次猜词只能猜在字典当中的单词，如果没出现，自动返回空字符串。
+ 每次猜的单词必须都是小写字母，如果出现大写字母，自动返回空字符串。
+ 如果猜的单词在字典当中，则会返回和猜测单词相同长度的字符串。返回的字符串只会包含`0`、`1`或`2`。
+ 返回的字符串中，`1`表示该位置的字母正确。
+ 返回的字符串中，`2`表示出去正确位置的那些字母后，该字母出现在字符串的剩余位置中，但是位置出现错误。
+ 返回的字符串中，`0`表示该字母未出现在字符串中，或者该字母已经用尽。
+ 举例来说，如果我们的secret word是`aabbcc`当我们猜测`aaabbb`且假设这两个单词都在词库中时，我们会返回`110120`。需要注意的是`110102`
也可以作为返回的值，这里我们选择位置靠前（index更小）的字母先match。所以返回`110120`。

### 记分方式
最后我们会生成report，根据report计算分数。

如果单词未猜出就直接退出，则直接判定总分为0分。

该程序分别会对长度5/6/7/8的单词分别运行。每个长度各占百分之二十五。

对于每个长度的单词（比如长度为6的单词），我会随机生成五个不同的字典，每个字典单词数量为1000。对每个字典分别记分，也就是说对于一个字典的分数
占总分的百分之五。

对于每个字典，我会对里面的所有单词进行设置为secret word然后打乱进行运行你写好的代码，并记录你猜词的次数。

然后对该字典你猜测总数和我的代码的猜测总数进行比较然后评分。需要注意的是，每个字典的评分只取决于你对于该字典所有单词猜测次数总和。

分数计算方式如下：

$$f(dict)=\left ( 1 - log_2\frac{n}{m} \right ) * 5$$

其中n是你猜词的总数，m是我猜词的总数。也就是说，你是有可能得分超过100分的。

## Wordle项目代码框架
N/A
