package com.ztq.sdk.utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 弱引用列表，每一个item都为一个弱引用。
 * 不能用index来获取item。
 */
public class WeakList<E> extends LinkedList<E> {

    private LinkedList<WeakReference<E>> linkedList = new LinkedList<WeakReference<E>>();

    @Override
    public Iterator<E> iterator() {
        ArrayList<E> returnList = new ArrayList<E>();
        for (WeakReference<E> item : linkedList) {
            E e = item.get();
            if (e == null) {
                continue;
            }
            returnList.add(e);
        }
        return returnList.iterator();
    }

    @Override
    public boolean add(E e) {
        if (contains(e)) {
            return false;
        }
        return linkedList.add(new WeakReference<E>(e));
    }

    @Deprecated
    @Override
    public void add(int location, E object) {
        super.add(location, object);
    }

    @Override
    public boolean remove(Object object) {
        if (object == null) {
            return false;
        }
        for (WeakReference<E> item : linkedList) {
            E e = item.get();
            if (e == null) {
                continue;
            } else {
                if (object.equals(e)) {
                    linkedList.remove(item);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean contains(Object object) {
        if (object == null) {
            return false;
        }

        for (WeakReference<E> item : linkedList) {
            E e = item.get();
            if (e == null) {
                continue;
            } else {
                if (object.equals(e)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public E get(int location) {
        if (location < linkedList.size()) {
            if (linkedList.get(location) != null) {
                return linkedList.get(location).get();
            }
        }
        return null;
    }

    @Override
    @Deprecated
    public E getFirst() {
        return super.getFirst();
    }

    @Override
    @Deprecated
    public E getLast() {
        return super.getLast();
    }

    /**
     * 清理已经没有引用的item
     */
    public void clearRubbish() {
        for (WeakReference<E> item : linkedList) {
            E e = item.get();
            if (e == null) {
                linkedList.remove(item);
            }
        }
    }

    @Override
    public boolean isEmpty() {
        Iterator<E> iter = iterator();
        return !iter.hasNext();
    }

    @Override
    public int size() {
        return linkedList.size();
    }
}