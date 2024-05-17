package com.demo.in_out;

import com.demo.in_out.obj.FuClass;
import com.demo.in_out.obj.ZiClass;

import java.util.ArrayList;
import java.util.List;

// 面试：你有没有用过 in out
public class TestOutIn1 {
    FuClass fuClass = new FuClass();
    ZiClass ziClass = new ZiClass();

    public void main(String[] args) {
        // 作用一：可以接收更多的类型
        List<? extends FuClass> list = new ArrayList<ZiClass>(); // 协变解决

        // 由于是 【生产者】

        // 只能获取 == 生产者
        FuClass fuClass = list.get(0);

        // 不能修改 == 不能消费
        /*list.add(fuClass);
        list.add(ziClass);*/


        // TODO =================================


        // 作用一：可以接收更多的类型
        List<? super ZiClass> list2 = new ArrayList<FuClass>(); // 逆变解决

        // 由于是 【消费者】

        // 不能获取 == 不能生产
        // ZiClass zi = list2.get(0);

        // 能修改 == 消费者
        list2.add(ziClass);
    }

    // TODO  ===================  协变
    private void forShow(List<? extends FuClass> lists) {
        // 协变：只能读取（能生产者）， 不能修改（不能消费）

        // 只能读取
        for (FuClass list : lists) {
        }
        FuClass fu = lists.get(0);

        // 你就不能修改人家，不能破坏人家传递进来的数据了
        /*lists.add(fuClass);
        lists.add(ziClass);*/
    }

    void test02() {
        List<ZiClass> ziClasses = new ArrayList<>();
        forShow(ziClasses);
    }


    // TODO  ===================  逆变
    private void updateData(List<? super ZiClass> lists) {
        // 逆变：不能读取（不能生产者）， 能修改（能消费）

        // 只能读取
        /*for (ZiClass list : lists) {}
        ZiClass zi = lists.get(0);*/
        // Object obj = lists.get(0);

        // 我可以修改你 能修改（能消费）
        lists.add(ziClass);
    }

    void test03() {
        List<FuClass> fuClasses = new ArrayList<FuClass>();
        updateData(fuClasses);
    }
}