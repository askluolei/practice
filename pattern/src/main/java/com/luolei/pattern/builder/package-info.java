/**

 建造者模式概述
 建造者模式是较为复杂的创建型模式，它将客户端与包含多个组成部分（或部件）的复杂对象的创建过程分离，客户端无须知道复杂对象的内部组成部分与装配方式，
 只需要知道所需建造者的类型即可。它关注如何一步一步创建一个的复杂对象，不同的具体建造者定义了不同的创建过程，且具体建造者相互独立，增加新的建造者非常方便，
 无须修改已有代码，系统具有较好的扩展性。


 建造者模式(Builder Pattern)：将一个复杂对象的构建与它的表示分离，使得同样的构建过程可以创建不同的表示。建造者模式是一种对象创建型模式。

 在建造者模式结构图中包含如下几个角色：
 ● Builder（抽象建造者）：它为创建一个产品Product对象的各个部件指定抽象接口，在该接口中一般声明两类方法，一类方法是buildPartX()，它们用于创建复杂对象的各个部件；另一类方法是getResult()，它们用于返回复杂对象。Builder既可以是抽象类，也可以是接口。
 ●ConcreteBuilder（具体建造者）：它实现了Builder接口，实现各个部件的具体构造和装配方法，定义并明确它所创建的复杂对象，也可以提供一个方法返回创建好的复杂产品对象。
 ●Product（产品角色）：它是被构建的复杂对象，包含多个组成部件，具体建造者创建该产品的内部表示并定义它的装配过程。
 ● Director（指挥者）：指挥者又称为导演类，它负责安排复杂对象的建造次序，指挥者与抽象建造者之间存在关联关系，可以在其construct()建造方法中调用建造者对象的部件构造与装配方法，完成复杂对象的建造。客户端一般只需要与指挥者进行交互，在客户端确定具体建造者的类型，并实例化具体建造者对象（也可以通过配置文件和反射机制），然后通过指挥者类的构造函数或者Setter方法将该对象传入指挥者类中。

 关于Director的进一步讨论
 1.省略Director
 在有些情况下，为了简化系统结构，可以将Director和抽象建造者Builder进行合并，在Builder中提供逐步构建复杂产品对象的construct()方法。由于Builder类通常为抽象类，因此可以将construct()方法定义为静态(static)方法。如果将游戏角色设计中的指挥者类ActorController省略，ActorBuilder类的代码修改如下：
 abstract class ActorBuilder
 {
 protected static Actor actor = new  Actor();

 public  abstract void buildType();
 public  abstract void buildSex();
 public  abstract void buildFace();
 public  abstract void buildCostume();
 public  abstract void buildHairstyle();

 public static Actor  construct(ActorBuilder ab)
 {
 ab.buildType();
 ab.buildSex();
 ab.buildFace();
 ab.buildCostume();
 ab.buildHairstyle();
 return actor;
 }
 }
 对应的客户端代码也将发生修改，其代码片段如下所示：
 ……
 ActorBuilder  ab;
 ab  = (ActorBuilder)XMLUtil.getBean();

 Actor  actor;
 actor =  ActorBuilder.construct(ab);
 ……
 除此之外，还有一种更简单的处理方法，可以将construct()方法的参数去掉，直接在construct()方法中调用buildPartX()方法，代码如下所示：
 abstract class ActorBuilder
 {
 protected  Actor actor = new Actor();

 public  abstract void buildType();
 public  abstract void buildSex();
 public  abstract void buildFace();
 public  abstract void buildCostume();
 public  abstract void buildHairstyle();

 public Actor construct()
 {
 this.buildType();
 this.buildSex();
 this.buildFace();
 this.buildCostume();
 this.buildHairstyle();
 return actor;
 }
 }
 客户端代码代码片段如下所示：
 ……
 ActorBuilder  ab;
 ab  = (ActorBuilder)XMLUtil.getBean();

 Actor  actor;
 actor = ab.construct();
 ……
 此时，construct()方法定义了其他buildPartX()方法调用的次序，为其他方法的执行提供了一个流程模板，这与我们在后面要学习的模板方法模式非常类似。
 以上两种对Director类的省略方式都不影响系统的灵活性和可扩展性，同时还简化了系统结构，但加重了抽象建造者类的职责，如果construct()方法较为复杂，待构建产品的组成部分较多，建议还是将construct()方法单独封装在Director中，这样做更符合“单一职责原则”。
 2.钩子方法的引入
 建造者模式除了逐步构建一个复杂产品对象外，还可以通过Director类来更加精细地控制产品的创建过程，例如增加一类称之为钩子方法(HookMethod)的特殊方法来控制是否对某个buildPartX()的调用。
 钩子方法的返回类型通常为boolean类型，方法名一般为isXXX()，钩子方法定义在抽象建造者类中。例如我们可以在游戏角色的抽象建造者类ActorBuilder中定义一个方法isBareheaded()，用于判断某个角色是否为“光头(Bareheaded)”，在ActorBuilder为之提供一个默认实现，其返回值为false，代码如下所示：
 abstract class ActorBuilder
 {
 protected  Actor actor = new Actor();

 public  abstract void buildType();
 public  abstract void buildSex();
 public  abstract void buildFace();
 public  abstract void buildCostume();
 public  abstract void buildHairstyle();

 //钩子方法
 public boolean isBareheaded()
 {
 return false;
 }

 public  Actor createActor()
 {
 return  actor;
 }
 }
 如果某个角色无须构建头发部件，例如“恶魔(Devil)”，则对应的具体建造器DevilBuilder将覆盖isBareheaded()方法，并将返回值改为true，代码如下所示：
 class DevilBuilder extends ActorBuilder
 {
 public  void buildType()
 {
 actor.setType("恶魔");
 }
 public  void buildSex()
 {
 actor.setSex("妖");
 }
 public  void buildFace()
 {
 actor.setFace("丑陋");
 }
 public  void buildCostume()
 {
 actor.setCostume("黑衣");
 }
 public  void buildHairstyle()
 {
 actor.setHairstyle("光头");
 }
 //覆盖钩子方法
 public boolean isBareheaded()
 {
 return true;
 }
 }
 此时，指挥者类ActorController的代码修改如下：
 class ActorController
 {
 public  Actor construct(ActorBuilder ab)
 {
 Actor  actor;
 ab.buildType();
 ab.buildSex();
 ab.buildFace();
 ab.buildCostume();
 //通过钩子方法来控制产品的构建
 if(!ab.isBareheaded())
 {
 ab. buildHairstyle();
 }
 actor=ab.createActor();
 return  actor;
 }
 }
 当在客户端代码中指定具体建造者类型并通过指挥者来实现产品的逐步构建时，将调用钩子方法isBareheaded()来判断游戏角色是否有头发，如果isBareheaded()方法返回true，即没有头发，则跳过构建发型的方法buildHairstyle()；否则将执行buildHairstyle()方法。通过引入钩子方法，我们可以在Director中对复杂产品的构建进行精细的控制，不仅指定buildPartX()方法的执行顺序，还可以控制是否需要执行某个buildPartX()方法。



 建造者模式总结
 建造者模式的核心在于如何一步步构建一个包含多个组成部件的完整对象，使用相同的构建过程构建不同的产品，在软件开发中，如果我们需要创建复杂对象并希望系统具备很好的灵活性和可扩展性可以考虑使用建造者模式。
 1.主要优点
 建造者模式的主要优点如下：
 (1) 在建造者模式中，客户端不必知道产品内部组成的细节，将产品本身与产品的创建过程解耦，使得相同的创建过程可以创建不同的产品对象。
 (2) 每一个具体建造者都相对独立，而与其他的具体建造者无关，因此可以很方便地替换具体建造者或增加新的具体建造者，用户使用不同的具体建造者即可得到不同的产品对象。由于指挥者类针对抽象建造者编程，增加新的具体建造者无须修改原有类库的代码，系统扩展方便，符合“开闭原则”
 (3) 可以更加精细地控制产品的创建过程。将复杂产品的创建步骤分解在不同的方法中，使得创建过程更加清晰，也更方便使用程序来控制创建过程。
 2.主要缺点
 建造者模式的主要缺点如下：
 (1) 建造者模式所创建的产品一般具有较多的共同点，其组成部分相似，如果产品之间的差异性很大，例如很多组成部分都不相同，不适合使用建造者模式，因此其使用范围受到一定的限制。
 (2) 如果产品的内部变化复杂，可能会导致需要定义很多具体建造者类来实现这种变化，导致系统变得很庞大，增加系统的理解难度和运行成本。
 3.适用场景
 在以下情况下可以考虑使用建造者模式：
 (1) 需要生成的产品对象有复杂的内部结构，这些产品对象通常包含多个成员属性。
 (2) 需要生成的产品对象的属性相互依赖，需要指定其生成顺序。
 (3) 对象的创建过程独立于创建该对象的类。在建造者模式中通过引入了指挥者类，将创建过程封装在指挥者类中，而不在建造者类和客户类中。
 (4) 隔离复杂对象的创建和使用，并使得相同的创建过程可以创建不同的产品。

 * @author luolei
 * @date 2017-03-30 10:01
 */
package com.luolei.pattern.builder;