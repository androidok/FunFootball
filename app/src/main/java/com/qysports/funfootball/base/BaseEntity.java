package com.qysports.funfootball.base;


import com.boredream.bdcodehelper.base.BoreBaseEntity;

/**
 * 数据实体基类
 */
public class BaseEntity extends BoreBaseEntity {

    @Override
    public boolean equals(Object o) {
        if (o instanceof BaseEntity) {
            BaseEntity oEntity = (BaseEntity) o;
            return this.objectId.equals(oEntity.objectId);
        }
        return super.equals(o);
    }
}