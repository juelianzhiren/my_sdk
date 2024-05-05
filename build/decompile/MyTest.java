/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 */
package com.demo.grammer;

import android.util.Log;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0007R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u0007R\u0011\u0010\u000b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\t\u00a8\u0006\r"}, d2={"Lcom/demo/grammer/MyTest;", "", "name", "", "age", "", "(Ljava/lang/String;I)V", "(Ljava/lang/String;)V", "getName", "()Ljava/lang/String;", "setName", "testName", "getTestName", "kotlinapp_debug"})
public final class MyTest {
    @NotNull
    private String name;
    @NotNull
    private final String testName;

    @NotNull
    public final String getName() {
        return this.name;
    }

    public final void setName(@NotNull String string) {
        Intrinsics.checkParameterIsNotNull((Object)string, (String)"<set-?>");
        this.name = string;
    }

    @NotNull
    public final String getTestName() {
        return this.testName;
    }

    private MyTest(String name) {
        this.name = "";
        this.name = name;
        Log.v((String)"MyTest", (String)("this name is " + this.name));
        Log.v((String)"MyTest", (String)"init 2");
        this.testName = this.name.toString();
    }

    public MyTest(@NotNull String name, int age) {
        Intrinsics.checkParameterIsNotNull((Object)name, (String)"name");
        this(name);
    }
}
