package net.ysq.shiro.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.time.Duration;

/**
 * redis配置类
 */
@Configuration
@EnableCaching // 开启SpringCache注解
public class CacheConfig extends CachingConfigurerSupport {

    /**
     * 配置自己的RedisTemplate。
     * 1、不配置beanName，默认以方法名作为beanName
     * 2、查看RedisAutoConfiguration自动配置类，发现：只要我们添加了名为"redisTemplate"的bean，那么
     * 自动配置类的bean就不会被装配
     * 3、因为springboot2.0中默认是使用Lettuce来集成Redis服务，spring-boot-starter-data-redis默认
     * 只引入了Lettuce连接池的实现，并没有引入jedis连接池的实现
     *
     *
     * @param connectionFactory     注入Redis连接池。有两个实现类，注入的是：LettuceConnectionFactory
     * @return
     */
    /*
    @Bean("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        // key的泛型固定为String
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 设置Redis连接池
        template.setConnectionFactory(connectionFactory);


        Jackson2JsonRedisSerializer jacksonSerializer = jacksonSerializer();
        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(jacksonSerializer);
        template.setHashKeySerializer(stringSerializer); // 注意，shiro使用Hash时的key可能是Object
        template.setHashValueSerializer(jacksonSerializer);

        // 其他采用默认
        template.afterPropertiesSet();

        return template;
    }
    */

    @Bean
    public CacheManager springCacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(60*60))
                .disableCachingNullValues();
        RedisCacheManager redisCacheManager =
                RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .transactionAware()
                .build();
        return redisCacheManager;
    }

    public Jackson2JsonRedisSerializer jacksonSerializer() {
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer<Object> jacksonSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        jacksonSerializer.setObjectMapper(om);
        return jacksonSerializer;
    }


}
