package com.luolei.pattern.builder.ch2;

/**
 * 英雄角色建造器：具体建造者
 *
 * @author luolei
 * @date 2017-03-30 10:10
 */
public class HeroBuilder extends ActorBuilder {
    public void buildType() {
        actor.setType("英雄");
    }

    public void buildSex() {
        actor.setSex("男");
    }

    public void buildFace() {
        actor.setFace("英俊");
    }

    public void buildCostume() {
        actor.setCostume("盔甲");
    }

    public void buildHairstyle() {
        actor.setHairstyle("飘逸");
    }
}
