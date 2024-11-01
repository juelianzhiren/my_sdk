package com.derry.premissionstudy.permission.aspect;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.derry.premissionstudy.permission.MyPermissionActivity;
import com.derry.premissionstudy.permission.annotation.Permission;
import com.derry.premissionstudy.permission.annotation.PermissionCancel;
import com.derry.premissionstudy.permission.annotation.PermissionDenied;
import com.derry.premissionstudy.permission.core.IPermission;
import com.derry.premissionstudy.permission.util.PermissionUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PermissionAspect {

    // AOP 思维 切面的思维

    // 切点 --- 是注解 @
    // * * 任何函数 都可以使用 此注解
    // AOP 思维 切面的思维
// 切点 --- 是注解 @
// * * 任何函数 都可以使用 此注解
//(..) 我要带参数  带的参数就是后面那个 @annotation(permission)意思就是  参数里是permission。这样我就拿到了Permission注解它里面的参数
//这样通过切点就拿到了下面这个注解
//@Permission(value = Manifest.permission.READ_EXTERNAL_STORAGE, requestCode = 200) 这就是  Permission permission
    @Pointcut
            ("execution(@com.derry.premissionstudy.permission.annotation.Permission * *(..)) && @annotation(permission)")
    //  @Permission == permission
    public void pointActionMethod(Permission permission) {
    } // 切点函数

    // 切面
    @Around("pointActionMethod(permission)")
    // final ProceedingJoinPoint point == AspectJ的API类 （NDK JNI JNIEnv evn）
    public void aProceedingJoinPoint(final ProceedingJoinPoint point, Permission permission) throws Throwable {
        // 我需要拿到  MainActivity this

        // 先定义一个上下文操作创建
        Context context = null;

        // MainActivity this == thisObject
        final Object thisObject = point.getThis();

        // context初始化
        if (thisObject instanceof Context) {
            context = (Context) thisObject; // Activity
        } else if (thisObject instanceof Fragment) {
            context = ((Fragment) thisObject).getActivity(); // Fragment
        } else {
            return;
        }

        // 判断是否为null
        if (null == context || permission == null) {
            throw new IllegalAccessException("null == context || permission == null is null");
        }

        // testRequest 此函数被控制了，不会再执行

        // 动态申请 危险权限  透明的空白的Activity（动态申请 危险权限）

        // 这里一定要得知 ，接口三个状态  已经授权  取消授权 拒绝授权

        // 调用 空白的Activity 开始授权了
        MyPermissionActivity.requestPermissionAction(context, permission.value(), permission.requestCode(), new IPermission() {
            @Override
            public void granted() {
                // 授权成功
                try {
                    point.proceed(); // 允许 该 @Permission 的 函数 执行
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void cancel() {
                // 授权取消

                // 反射 @PermissionCancel 修饰的 函数
                // MainActivity this
                PermissionUtils.invokeAnnotion(thisObject, PermissionCancel.class);
            }

            @Override
            public void denied() {
                // 授权拒绝

                // 反射  @PermissionDenied 修饰的 函数
                // MainActivity this
                PermissionUtils.invokeAnnotion(thisObject, PermissionDenied.class);
            }
        });
    }
}