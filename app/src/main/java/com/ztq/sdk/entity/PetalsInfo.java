package com.ztq.sdk.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Author: ztq
 * Date: 2019/12/21 13:29
 * Description: 花瓣信息类，包括每片花瓣的名字及花瓣包括的子列表信息
 */
public class PetalsInfo implements Serializable {
    private List<PetalEntity> petalList;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PetalEntity> getPetalList() {
        return petalList;
    }

    public void setPetalList(List<PetalEntity> petalList) {
        this.petalList = petalList;
    }

    public static class PetalEntity implements Serializable {
        /**名字*/
        private String name;
        private List<String> childList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getChildList() {
            return childList;
        }

        public void setChildList(List<String> childList) {
            this.childList = childList;
        }
    }
}