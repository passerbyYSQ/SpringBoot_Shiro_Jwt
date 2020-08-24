package net.ysq.shiro.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author passerbyYSQ
 * @create 2020-08-21 11:50
 */
@Component // 加入容器
public class ApplicationContextUtil implements ApplicationContextAware {

    // IOC容器
    private static ApplicationContext context;


    /**
     * 将IOC容器回调给我们，我们将它缓存起来
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 从IOC容器中获取组件（bean）
     *
     * @param beanName
     * @return
     */
    public static Object getBean(String beanName) {
        return context.getBean(beanName);
    }
}
