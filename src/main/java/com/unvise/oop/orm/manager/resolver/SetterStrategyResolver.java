package com.unvise.oop.orm.manager.resolver;

import com.unvise.oop.orm.manager.strategy.*;
import com.unvise.oop.orm.meta.BeanFieldInfo;

public class SetterStrategyResolver {
    public static SetterStrategy resolve(BeanFieldInfo beanFieldInfo) {
        if (beanFieldInfo.isOneToOne()) {
            return new OneToOneSetterStrategy();
        }
        if (beanFieldInfo.isOneToMany()) {
            return new OneToManySetterStrategy();
        }
        if (beanFieldInfo.isManyToOne()) {
            return new EmptyStrategy();
        }
        return new CommonSetterStrategy();
    }
}
