### 缓存雪崩定义及解决方案

缓存雪崩是指在同一时段大量的缓存 key 同时失效或者 Redis 服务宕机，导致大量请求到达数据库，带来巨大压力。  
解决方案：  
● 给不同的 Key 的 TTL 添加随机值 （同一时段，所以给不同 key 设置不同的 TTL）  
● 利用 Redis 集群提高服务的可用性  
● 给缓存业务添加降级限流 策略 （微服务）  
● 给业务添加多级缓存


### 为什么要使用多级缓存

如果只使用 redis 来做缓存我们会有大量的请求到 redis，但是每次请求的数据都是一样的，假如这一部分数据就放在应用服务器本地，那么就**省去了请求 redis 的网络开销**，<u>请求速度</u>就会快很多；  
如果只使用 Caffeine 来做本地缓存，我们的应用服务器的 内存 是有限，并且单独为了缓存去扩展应用服务器是非常不划算。所以，只使用本地缓存也是有很大局限性的；  
因此在项目中，我们可以将**热点数据放本地缓存，作为一级缓存，将非热点数据放 redis 缓存，作为二级缓存，减少 Redis 的查询压力**

使用流程大致如下：

- 首先从一级缓存（caffeine-本地应用内）中查找数据；
- 如果没有的话，则从二级缓存（redis-内存）中查找数据；
- 如果还是没有的话，再从数据库（数据库-磁盘）中查找数据；


### 企业级项目在什么场景中引入多级缓存
在企业级项目中，引入本地缓存（如 Caffeine）并非为了替代 Redis，而是在特定场景下对系统性能和稳定性的**关键增强**。

简单来说，当你遇到以下几种情况时，就应该考虑引入本地缓存方案：

##### 追求极致性能，应对热点数据

当你的**系统中存在访问频率极高、但内容几乎不变的“热点数据”** 时，Redis 的毫秒级网络延迟可能成为瓶颈。

- **典型场景**：电商的商品基础信息、首页固定配置、数据字典、风控规则等
- **为什么有效**：本地缓存在应用进程的 JVM 内存中，读写速度是**纳秒级**，比经过网络的 Redis 快数百倍。在电商大促等场景下，将爆款商品信息放入本地缓存，可以将接口响应时间从上百毫秒降至个位数，轻松扛住数万 QPS 的冲击，同时极大减轻 Redis 集群的压力。

##### 增强架构韧性，实现服务降级

为了**提高系统的容灾能力，防止因 Redis 故障导致整个服务雪崩**，本地缓存可以作为一道重要的防线。

- **典型场景**：任何依赖 Redis 的核心业务链路。
- **为什么有效**：当 Redis 集群因网络抖动或服务宕机而不可用时，所有请求会直接穿透到数据库，可能导致数据库崩溃。如果核心静态数据在本地有缓存，即使 Redis 挂了，服务依然可以从本地获取数据，保证核心业务的可用性，这是一种非常关键的**降级策略**

##### 权衡一致性与效率，接受短暂不一致

当你的业务可以容忍数据在短时间内（几秒到几分钟）的不一致，以换取更高的读取效率时，本地缓存是一个绝佳选择。

- **典型场景**：用户个人信息展示、文章阅读量统计、非实时的排行榜等。
- **为什么有效**：本地缓存在每个服务实例上是独立的，天然存在数据不一致的问题。但对于上述场景，短暂的显示差异通常不影响核心业务逻辑。通过设置合理的过期时间（TTL），可以在数据新鲜度和高性能之间取得良好平衡。

##### 总结与警示

总而言之，引入本地缓存的核心思想是：**用极小的内存成本和可控的数据一致性复杂度，换取核心链路的==极致性能和整体架构的高可用性==**

**重要提示**：本地缓存不适合存储需要跨服务共享或强一致性的数据，例如订单状态、库存数量、分布式锁等。这类数据必须使用 Redis 这样的分布式缓存来保证全局一致性。


### 为什么要引入多级缓存


1. 网络开销
2. 给 redis 防弹
3. 提高系统容灾、降级能力
4. 

你之前的回答（如“减少网络开销”、“分担内存压力”）在方向上没错，但不够深入，没有击中要害，所以会被追问。下面我们来系统地拆解这个问题，让你在下次面试时能从容应对。

##### 有了 Redis，为什么还要引入本地缓存？

核心原因绝不是简单的“网络开销”，而是在于**极致的性能追求、架构的容灾能力以及成本与效率的平衡**。我们可以从以下三个层面来理解：

##### 1. 性能维度：不是“有开销”，而是“零延迟”

面试官说得对，*Redis 的网络开销在现代生产环境中通常是可以接受的*，毫秒级的响应对于大多数业务来说足够快。但问题的关键在于：**有没有更快的？**

- **本地缓存 (L1 Cache)**：访问速度是 **纳秒级 (ns)**。因为它就在你的应用进程内，是一次纯粹的内存读取操作，没有任何网络序列化和反序列化的过程。
- **Redis (L2 Cache)**：访问速度是 **毫秒级 (ms)**。虽然很快，但它终究是一次网络请求，涉及到 TCP 连接、数据序列化/反序列化等一系列步骤。

**两者的性能差距是成百上千倍的。**

**类比一下：**

- **本地缓存** 就像你办公桌上的常用文件，伸手就能拿到。
- **Redis** 就像公司楼下的共享档案室，跑一趟也很快，但总比你伸手拿要慢。

对于那些**访问频率极高、但几乎不变化**的数据（例如：系统配置、数据字典、风控规则），将其放入本地缓存，可以将接口响应时间从几十毫秒压缩到几毫秒甚至更低。这在电商秒杀、实时推荐等对延迟极其敏感的场景下，是决定性的优势。

##### 2. 架构维度：不仅是“加速”，更是“保护”和“容灾”

这是更关键的一点。引入本地缓存，不仅仅是为了快，更是为了保护 Redis 和整个系统的稳定性。

- **充当 Redis 的“防弹盾”**  
    在高并发场景下，即使是 Redis 也可能成为瓶颈。**一个 Redis 单节点的 QPS 上限大约在 10 万左右**。如果所有请求都打到 Redis，它依然可能扛不住。本地缓存可以作为第一道防线，**拦截掉绝大部分对热点数据的查询，将 Redis 的负载降低 70% 以上，从而让系统整体承载能力提升数倍**。
    
- **提供关键的==“降级”能力**==
    想象一下，**如果 Redis 集群因为某种原因（如网络抖动、机器故障）突然不可用**了，会发生什么？所有请求会瞬间穿透缓存，直接压向数据库，很可能导致数据库雪崩，整个系统瘫痪。  
    <u>但如果你的系统中有本地缓存，即使 Redis 挂了，那些不常变化的核心数据（比如商品基础信息、用户等级配置）依然可以从本地缓存中获取，保证核心业务的可用性</u>。这是一种非常重要的==**高可用（HA）设计**。==
    

##### 3. 资源维度：不是“浪费”，而是“好钢用在刀刃上”

你担心本地缓存会浪费 JVM 内存，这个想法可以理解，但并不准确。

- **集中式 vs. 分散式**
    
    - **Redis** 是**集中式**存储，所有服务实例共享一份数据。适合存储需要跨服务共享、数据量较大、动态变化的数据，比如用户会话（Session）、购物车、库存等。
    - **本地缓存** 是**分散式**存储，每个服务实例都有自己的一份。它不适合存大数据或需要强一致性的数据，但它非常适合存**小数据量、只读的静态数据**
- **性价比之选**  
    将一些只有几 MB 或几百 MB 的配置数据，在每个服务的几 GB 内存中都存一份，这点内存开销相对于它带来的性能提升和架构稳定性来说，是微不足道的。这恰恰是“好钢用在刀刃上”。反之，如果把所有数据都塞进 Redis，不仅会增加 Redis 的内存成本和运维复杂度，还会带来不必要的网络 IO。
    

---

##### 多级缓存的优势与弊端

现在再来看多级缓存（通常是 本地缓存 -> Redis -> 数据库），它的优缺点就非常清晰了。

| 维度     | 优势                                        | 弊端                                                                   |
| :----- | :---------------------------------------- | :------------------------------------------------------------------- |
| **性能** | **极致优化**。L1 提供纳秒级响应，L2 提供毫秒级响应，形成完美的性能梯队。 | -                                                                    |
| **架构** | **高可用与容灾**。L1 可作为 L2 故障时的降级方案，提升系统鲁棒性。    | **数据一致性复杂**。这是最大的挑战。当数据更新时，如何保证所有服务实例的 L1 和 L2 缓存同时失效或更新，是一个非常棘手的问题。 |
| **成本** | **保护后端资源**。有效减轻 Redis 和数据库的压力，延缓扩容需求      | **开发与运维成本增加**。需要维护两套缓存框架，并处理它们之间的同步、淘汰策略等问题。                         |

##### 总结与回答思路

所以，回到面试场景，当被问到“有了 Redis 为什么还要用本地缓存”时，你可以这样组织语言，展现你的深度思考：

> “引入本地缓存，主要是在性能、架构稳定性和成本之间做一个更优的权衡。
> 
> 1. **追求极致性能**：对于一些**访问频率极高且几乎不变的‘静态’数据**（如配置、字典），本地缓存能提供纳秒级的访问速度，这比经过网络的 Redis（毫秒级）要快几个数量级，可以<u>显著降低核心接口的响应时间</u>。
> 2. **增强架构韧性**：本地缓存可以**作为 Redis 的前置屏障**，在流量洪峰时吸收大量请求，保护 Redis 不被击穿。更重要的是，当 Redis 发生故障时，本地缓存可以提供降级能力，保证核心业务不至于完全瘫痪，提升了系统的高可用性
> 3. **合理的资源利用**：我们**并不是把所有数据都放到本地缓存，而是有选择地存放少量关键的静态数据**。这点内存开销，相比于它带来的巨大收益是完全值得的。而**需要共享和动态变化的数据，则继续放在 Redis 中**。”

通过这样的回答，你不仅能解释清楚“为什么”，还能体现出你对系统架构、性能瓶颈和高可用设计的深刻理解，这正是面试官想要听到的。


### Redis+Caffeine 实现应用层二级缓存原理

Redis 作为 分布式 缓存：

- Redis 具有高性能、丰富的数据结构和可扩展性，适合作为分布式缓存存储大量的数据。它可以在多服务器环境下共享缓存数据，提高系统的整体性能。
- 可以根据数据的特点选择合适的数据结构来存储数据，如使用哈希表存储对象、使用有序集合进行排行榜等操作。
- 配置 Redis 的持久化机制，以防止数据丢失。同时，考虑使用 Redis 的集群或主从复制来提高可用性和可扩展性。

Caffeine 作为本地缓存：

- Caffeine 是一个高效的本地缓存库，可以在应用程序内部实现缓存，减少对外部缓存服务的依赖，提高缓存的访问速度。
- Caffeine 支持自动过期功能，可以根据设定的时间自动清除过期的缓存数据，减少内存占用。
- 可以根据数据的访问频率和大小来调整 Caffeine 的缓存配置，如缓存的大小、过期时间等。  
    实现二级缓存架构

数据存储流程：

- 当应用程序需要访问数据时，首先从 Caffeine 本地缓存中查找数据。如果数据在 Caffeine 中存在，则直接返回数据，无需进一步访问 Redis 或数据库
- 如果数据不在 Caffeine 中，则从 Redis 分布式缓存中查找数据。如果数据在 Redis 中存在，则将数据加载到 Caffeine 中，并返回数据给应用程序。
- 如果数据不在 Redis 中，则从数据库中读取数据，并将数据同时存储到 Redis 和 Caffeine 中，然后返回数据给应用程序。


数据更新流程：

- 当数据在数据库中被更新时，需要同时更新 Redis 和 Caffeine 中的缓存数据，以保证数据的一致性
- 可以采用先更新数据库，然后删除 Redis 中的对应数据，让后续的访问从数据库中重新读取数据并更新到 Redis 和 Caffeine 中的方式来实现数据的更新。这种方式被称为 Cache Aside 模式。

缓存过期策略：

- 对于 Caffeine 本地缓存，可以设置自动过期时间，根据数据的变化频率和访问频率来调整过期时间，以避免内存占用过高。
- 对于 Redis 分布式缓存，可以根据业务需求设置合理的过期时间，或者采用主动更新的方式来保证缓存数据的有效性。


### 利用 Caffeine+Redis 解决 Redis 突然宕机导致的缓存雪崩问题

需求：修改根据 id 查询商铺的业务，基于二级缓存方式来解决缓存雪崩问题。  
思路分析：当用户开始查询时，先查询本地缓存 Caffeine，判断是否命中，如果没有命中则查询 Redis，命中则直接返回。

![1c8966b9889b4a698adb75fe65bcf9a1.png](技术学习/数据库/非关系型数据库/assets/1c8966b9889b4a698adb75fe65bcf9a1.png)



#### 引入相关依赖
```xml
<!--引入本地缓存Caffine-->
<dependency>
    <groupId>com.github.ben-manes.caffeine</groupId>
    <artifactId>caffeine</artifactId>
    <version>2.9.2</version>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>

```


#### 本地缓存配置类

Config 目录下新建本地缓存配置类，LocalCacheConfiguration

```java
package com.hmdp.config;


import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 本地缓存Caffeine配置类
 */
@Configuration
public class LocalCacheConfiguration {<!-- -->

    @Bean("localCacheManager")
    public Cache<String, Object> localCacheManager() {<!-- -->
        return Caffeine.newBuilder()
                //写入或者更新5s后，缓存过期并失效, 实际项目中肯定不会那么短时间就过期，根据具体情况设置即可
                .expireAfterWrite(120, TimeUnit.SECONDS)
                // 初始的缓存空间大小
                .initialCapacity(50)
                // 缓存的最大条数，通过 Window TinyLfu算法控制整个缓存大小
                .maximumSize(500)
                //打开数据收集功能
                .recordStats()
                .build();
    }

}

```



#### 使用
```java
private Cache<String,Object> caffeineCache;

// ...
Object o = caffeineCache.getIfPresent(CACHE_SHOP_KEY + id); if(Objects.nonNull(o)){<!-- --> log.info("从Caffeine中查询到数据..."); return Result.ok( o); }
```

- `caffeineCache.Put (user.GetId (), user)`：保存本地缓存；
- `caffeineCache.Invalidate (id)`：移除指定的本地缓存；
- `caffeineCache.GetIfPresent (id)`： 从本地缓存中获取值，如果缓存中不存指定的值，则方法将返回 null；
- `caffeineCache.Get (id, Function<>)`： 从本地缓存中获取值，该方法还支持将一个参数为 key 的 Function 作为参数传入。如果缓存中不存在该 key，则该函数将用于提供默认值，该值在计算后插入缓存中，如果缓存的元素无法生成或者在生成的过程中抛出异常而导致生成元素失败，则返回 null



### 工具类封装

```java

package top.hazenix.hazeaihub.config;  
  
import com.github.benmanes.caffeine.cache.Caffeine;  
import org.springframework.cache.CacheManager;  
import org.springframework.cache.caffeine.CaffeineCacheManager;  
import org.springframework.context.annotation.Bean;  
import org.springframework.context.annotation.Configuration;  
import org.springframework.context.annotation.Primary;  
  
import java.util.concurrent.TimeUnit;  
  
/**  
 * 缓存配置类  
 */  
@Configuration  
public class CacheConfiguration {  
  
    /**  
     * Caffeine 本地缓存管理器  
     */  
    @Bean  
    @Primary    public CacheManager cacheManager() {  
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();  
        cacheManager.registerCustomCache(  
                "models",  
                Caffeine.newBuilder()  
                        .maximumSize(100)  
                        .expireAfterWrite(30, TimeUnit.MINUTES)  
                        .recordStats()  
                        .build()  
        );  
        cacheManager.registerCustomCache(  
                "groups",  
                Caffeine.newBuilder()  
                        .maximumSize(500)  
                        .expireAfterWrite(30, TimeUnit.MINUTES)  
                        .recordStats()  
                        .build()  
        );  
        cacheManager.registerCustomCache(  
                "libraries",  
                Caffeine.newBuilder()  
                        .maximumSize(500)  
                        .expireAfterWrite(30, TimeUnit.MINUTES)  
                        .recordStats()  
                        .build()  
        );  
        return cacheManager;  
    }  
}
```


```java
package top.hazenix.hazeaihub.utils;  
  
import cn.hutool.json.JSONUtil;  
import lombok.RequiredArgsConstructor;  
import lombok.extern.slf4j.Slf4j;  
import org.springframework.cache.Cache;  
import org.springframework.cache.CacheManager;  
import org.springframework.data.redis.core.StringRedisTemplate;  
import org.springframework.stereotype.Component;  
  
import java.util.concurrent.ThreadLocalRandom;  
import java.util.concurrent.TimeUnit;  
import java.util.function.Supplier;  
  
import com.fasterxml.jackson.core.type.TypeReference;  
  
/**  
 * 缓存工具类  
 * <p>  
 * 提供 Caffeine 本地缓存 + Redis 分布式缓存的两级缓存能力  
 * </p>  
 */@Slf4j  
@Component  
@RequiredArgsConstructor  
public class CacheUtil {  
  
    private final StringRedisTemplate stringRedisTemplate;  
    private final CacheManager cacheManager;  
  
    // ==================== 查询操作（两级缓存穿透） ====================  
    /**     * 两级缓存查询（穿透模式）  
     * <p>  
     * 流程：Caffeine → Redis → 数据库  
     * </p>  
     *     * @param localCacheName  Caffeine 缓存名称  
     * @param redisKey        Redis 缓存键  
     * @param type            返回类型  
     * @param dbFallback      数据库查询函数  
     * @param time            TTL 时间  
     * @param unit            TTL 时间单位  
     * @return 查询结果  
     */  
    public <T> T queryWithPassThrough(String localCacheName, String redisKey, Class<T> type,  
                                      Supplier<T> dbFallback, Long time, TimeUnit unit) {  
        // 1. 查询 Caffeine 本地缓存  
        Cache localCache = cacheManager.getCache(localCacheName);  
        if (localCache != null) {  
            T localValue = localCache.get(redisKey, type);  
            if (localValue != null) {  
                log.debug("Cache hit (Caffeine): key={}", redisKey);  
                return localValue;  
            }  
        }  
  
        // 2. 查询 Redis 分布式缓存  
        String json = stringRedisTemplate.opsForValue().get(redisKey);  
        if (json != null) {  
            // 命中空值  
            if ("".equals(json)) {  
                return null;  
            }  
            T value = JSONUtil.toBean(json, type);  
            // 回填本地缓存  
            if (localCache != null) {  
                localCache.put(redisKey, value);  
            }  
            log.debug("Cache hit (Redis): key={}", redisKey);  
            return value;  
        }  
  
        // 3. 查询数据库  
        T value = dbFallback.get();  
  
        // 4. 写入缓存  
        if (value != null) {  
            setWithRandomExpire(redisKey, value, time, unit);  
            if (localCache != null) {  
                localCache.put(redisKey, value);  
            }  
        } else {  
            // 防止缓存穿透：写入空值  
            stringRedisTemplate.opsForValue().set(redisKey, "", time, unit);  
        }  
  
        return value;  
    }  
  
    /**  
     * 两级缓存查询（穿透模式，支持泛型类型）  
     * <p>  
     * 流程：Caffeine → Redis → 数据库  
     * </p>  
     *     * @param localCacheName  Caffeine 缓存名称  
     * @param redisKey        Redis 缓存键  
     * @param typeRef         返回类型引用（支持泛型）  
     * @param dbFallback      数据库查询函数  
     * @param time            TTL 时间  
     * @param unit            TTL 时间单位  
     * @return 查询结果  
     */  
    public <T> T queryWithPassThrough(String localCacheName, String redisKey, TypeReference<T> typeRef,  
                                      Supplier<T> dbFallback, Long time, TimeUnit unit) {  
        // 1. 查询 Caffeine 本地缓存  
        Cache localCache = cacheManager.getCache(localCacheName);  
        if (localCache != null) {  
            T localValue = localCache.get(redisKey, (Class<T>) typeRef.getType());  
            if (localValue != null) {  
                log.debug("Cache hit (Caffeine): key={}", redisKey);  
                return localValue;  
            }  
        }  
  
        // 2. 查询 Redis 分布式缓存  
        String json = stringRedisTemplate.opsForValue().get(redisKey);  
        if (json != null) {  
            // 命中空值  
            if ("".equals(json)) {  
                return null;  
            }  
            T value = JSONUtil.toBean(json, typeRef.getType(), false);  
            // 回填本地缓存  
            if (localCache != null) {  
                localCache.put(redisKey, value);  
            }  
            log.debug("Cache hit (Redis): key={}", redisKey);  
            return value;  
        }  
  
        // json == null ⬇️  
        // 3. 查询数据库  
        T value = dbFallback.get();  
  
        // 4. 写入缓存  
        if (value != null) {  
            setWithRandomExpire(redisKey, value, time, unit);  
            if (localCache != null) {  
                localCache.put(redisKey, value);  
            }  
        } else {  
            // 防止缓存穿透：写入空值  
            stringRedisTemplate.opsForValue().set(redisKey, "", time, unit);  
        }  
  
        return value;  
    }  
  
    // ==================== 写入操作 ====================  
    /**     * 写入缓存（随机过期时间，防止缓存雪崩）  
     *  
     * @param key   缓存键  
     * @param value 缓存值  
     * @param time  基础过期时间  
     * @param unit  时间单位  
     */  
    public void setWithRandomExpire(String key, Object value, Long time, TimeUnit unit) {  
        long randomOffset = ThreadLocalRandom.current().nextLong(10);  
        long finalTime = time + randomOffset;  
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value), finalTime, unit);  
        log.debug("Cache write: key={}, ttl={}{}", key, finalTime, unit);  
    }  
  
    // ==================== 删除操作 ====================  
    /**     * 删除缓存（同时删除 Redis 和本地缓存）  
     *  
     * @param localCacheName  Caffeine 缓存名称  
     * @param redisKey         Redis 缓存键  
     */  
    public void delete(String localCacheName, String redisKey) {  
        // 删除 Redis        stringRedisTemplate.delete(redisKey);  
        // 删除本地缓存  
        Cache localCache = cacheManager.getCache(localCacheName);  
        if (localCache != null) {  
            localCache.evict(redisKey);  
        }  
        log.debug("Cache delete: key={}", redisKey);  
    }  
  
    /**  
     * 删除用户相关缓存（同时删除 Redis 和本地缓存）  
     *  
     * @param localCacheName  Caffeine 缓存名称  
     * @param keyPrefix       Redis 缓存键前缀  
     * @param userId          用户ID  
     */    public void deleteWithUserId(String localCacheName, String keyPrefix, Long userId) {  
        String redisKey = keyPrefix + userId;  
        delete(localCacheName, redisKey);  
    }  
}

```