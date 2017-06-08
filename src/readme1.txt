1,WeakHashMap key为null时,gc后,key没被引用则被自动remove

2,点对点的dubbo需要依赖jvm参数配置映射interface - url 还需要加一个解释文件dubbo-resolve.properties配置映射,但只支持xml版

3,spring-boot集成dubbo进行点对点暂时找不到突破口,里面注册的ServiceBean ServiceConfig都没有跳过注册中心的N/A的设置,等研究好了zookeeper可以
  先尝试ali的原生包配置,暂时不明白springboot-dubbo-start有啥好处,网上资料貌似很简单
