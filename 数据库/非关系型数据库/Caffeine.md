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