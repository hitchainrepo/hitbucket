/*******************************************************************************
 * Copyright (c) 2018-10-26 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.hitchain.hit.util;

import java.io.Serializable;

/**
 * Tupe
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-10-26
 * auto generate by qdp.
 */
public class Tuple<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N> implements Serializable {
    private Object[] args = new Object[]{
            (T) null, (A) null, (B) null, (C) null, (D) null, (E) null, (F) null, (G) null, (H) null, (I) null, (J) null, (K) null, (L) null, (M) null, (N) null
    };

    public Tuple() {
    }

    public Tuple(A arg0) {
        this(arg0, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public Tuple(A arg0, B arg1) {
        this(arg0, arg1, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public Tuple(A arg0, B arg1, C arg2) {
        this(arg0, arg1, arg2, null, null, null, null, null, null, null, null, null, null, null);
    }

    public Tuple(A arg0, B arg1, C arg2, D arg3) {
        this(arg0, arg1, arg2, arg3, null, null, null, null, null, null, null, null, null, null);
    }

    public Tuple(A arg0, B arg1, C arg2, D arg3, E arg4) {
        this(arg0, arg1, arg2, arg3, arg4, null, null, null, null, null, null, null, null, null);
    }

    public Tuple(A arg0, B arg1, C arg2, D arg3, E arg4, F arg5) {
        this(arg0, arg1, arg2, arg3, arg4, arg5, null, null, null, null, null, null, null, null);
    }

    public Tuple(A arg0, B arg1, C arg2, D arg3, E arg4, F arg5, G arg6) {
        this(arg0, arg1, arg2, arg3, arg4, arg5, arg6, null, null, null, null, null, null, null);
    }

    public Tuple(A arg0, B arg1, C arg2, D arg3, E arg4, F arg5, G arg6, H arg7) {
        this(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, null, null, null, null, null, null);
    }

    public Tuple(A arg0, B arg1, C arg2, D arg3, E arg4, F arg5, G arg6, H arg7, I arg8) {
        this(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, null, null, null, null, null);
    }

    public Tuple(A arg0, B arg1, C arg2, D arg3, E arg4, F arg5, G arg6, H arg7, I arg8, J arg9) {
        this(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, null, null, null, null);
    }

    public Tuple(A arg0, B arg1, C arg2, D arg3, E arg4, F arg5, G arg6, H arg7, I arg8, J arg9, K arg10) {
        this(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, null, null, null);
    }

    public Tuple(A arg0, B arg1, C arg2, D arg3, E arg4, F arg5, G arg6, H arg7, I arg8, J arg9, K arg10, L arg11) {
        this(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, null, null);
    }

    public Tuple(A arg0, B arg1, C arg2, D arg3, E arg4, F arg5, G arg6, H arg7, I arg8, J arg9, K arg10, L arg11, M arg12) {
        this(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, null);
    }

    public Tuple(A arg0, B arg1, C arg2, D arg3, E arg4, F arg5, G arg6, H arg7, I arg8, J arg9, K arg10, L arg11, M arg12, N arg13) {
        Object[] src = {arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13};
        System.arraycopy(src, 0, args, 1, src.length);
    }

    public A first() {
        return (A) args[1];
    }

    public B second() {
        return (B) args[2];
    }

    public C third() {
        return (C) args[3];
    }

    public D fourth() {
        return (D) args[4];
    }

    public E fifth() {
        return (E) args[5];
    }

    public F sixth() {
        return (F) args[6];
    }

    public G seventh() {
        return (G) args[7];
    }

    public H eighth() {
        return (H) args[8];
    }

    public I ninth() {
        return (I) args[9];
    }

    public J tenth() {
        return (J) args[10];
    }

    public K eleventh() {
        return (K) args[11];
    }

    public L twelfth() {
        return (L) args[12];
    }

    public M thirteenth() {
        return (M) args[13];
    }

    public N fourteenth() {
        return (N) args[14];
    }

    public boolean isEmpty() {
        for (Object arg : args) {
            if (arg != null) {
                return false;
            }
        }
        return true;
    }

    public boolean hasError() {
        return args[0] instanceof Throwable;
    }

    public Throwable error() {
        return (Throwable) args[0];
    }

    public T result() {
        return (T) args[0];
    }

    public void result(Object result) {
        args[0] = result;
    }

    public static interface Result<T> {
        boolean hasError();

        Throwable error();

        T result();

        void result(Object result);
    }

    public static class One<T, A> extends Tuple<T, A, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> implements Serializable, Result<T> {
        public One() {
        }

        public One(A first) {
            super(first);
        }
    }

    public static class Two<T, A, B> extends Tuple<T, A, B, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> implements Serializable, Result<T> {
        public Two() {
        }

        public Two(A first, B second) {
            super(first, second);
        }
    }

    public static class Three<T, A, B, C> extends Tuple<T, A, B, C, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> implements Serializable, Result<T> {
        public Three() {
        }

        public Three(A first, B second, C third) {
            super(first, second, third);
        }
    }

    public static class Four<T, A, B, C, D> extends Tuple<T, A, B, C, D, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> implements Serializable, Result<T> {
        public Four() {
        }

        public Four(A first, B second, C third, D fourth) {
            super(first, second, third, fourth);
        }
    }

    public static class Five<T, A, B, C, D, E> extends Tuple<T, A, B, C, D, E, Object, Object, Object, Object, Object, Object, Object, Object, Object> implements Serializable, Result<T> {
        public Five() {
        }

        public Five(A first, B second, C third, D fourth, E five) {
            super(first, second, third, fourth, five);
        }
    }

    public static class Six<T, A, B, C, D, E, F> extends Tuple<T, A, B, C, D, E, F, Object, Object, Object, Object, Object, Object, Object, Object> implements Serializable, Result<T> {
        public Six() {
        }

        public Six(A first, B second, C third, D fourth, E five, F six) {
            super(first, second, third, fourth, five, six);
        }
    }

    public static class Seven<T, A, B, C, D, E, F, G> extends Tuple<T, A, B, C, D, E, F, G, Object, Object, Object, Object, Object, Object, Object> implements Serializable, Result<T> {
        public Seven() {
        }

        public Seven(A first, B second, C third, D fourth, E five, F six, G seven) {
            super(first, second, third, fourth, five, six, seven);
        }
    }

    public static class Eight<T, A, B, C, D, E, F, G, H> extends Tuple<T, A, B, C, D, E, F, G, H, Object, Object, Object, Object, Object, Object> implements Serializable, Result<T> {
        public Eight() {
        }

        public Eight(A first, B second, C third, D fourth, E five, F six, G seven, H eight) {
            super(first, second, third, fourth, five, six, seven, eight);
        }
    }

    public static class Nine<T, A, B, C, D, E, F, G, H, I> extends Tuple<T, A, B, C, D, E, F, G, H, I, Object, Object, Object, Object, Object> implements Serializable, Result<T> {
        public Nine() {
        }

        public Nine(A first, B second, C third, D fourth, E five, F six, G seven, H eight, I nine) {
            super(first, second, third, fourth, five, six, seven, eight, nine);
        }
    }

    public static class Ten<T, A, B, C, D, E, F, G, H, I, J> extends Tuple<T, A, B, C, D, E, F, G, H, I, J, Object, Object, Object, Object> implements Serializable, Result<T> {
        public Ten() {
        }

        public Ten(A first, B second, C third, D fourth, E five, F six, G seven, H eight, I nine, J ten) {
            super(first, second, third, fourth, five, six, seven, eight, nine, ten);
        }
    }

    public static class Eleven<T, A, B, C, D, E, F, G, H, I, J, K> extends Tuple<T, A, B, C, D, E, F, G, H, I, J, K, Object, Object, Object> implements Serializable, Result<T> {
        public Eleven() {
        }

        public Eleven(A first, B second, C third, D fourth, E five, F six, G seven, H eight, I nine, J ten, K eleven) {
            super(first, second, third, fourth, five, six, seven, eight, nine, ten, eleven);
        }
    }

    public static class Twelve<T, A, B, C, D, E, F, G, H, I, J, K, L> extends Tuple<T, A, B, C, D, E, F, G, H, I, J, K, L, Object, Object> implements Serializable, Result<T> {
        public Twelve() {
        }

        public Twelve(A first, B second, C third, D fourth, E five, F six, G seven, H eight, I nine, J ten, K eleven, L twelve) {
            super(first, second, third, fourth, five, six, seven, eight, nine, ten, eleven, twelve);
        }
    }

    public static class Thirteen<T, A, B, C, D, E, F, G, H, I, J, K, L, M> extends Tuple<T, A, B, C, D, E, F, G, H, I, J, K, L, M, Object> implements Serializable, Result<T> {
        public Thirteen() {
        }

        public Thirteen(A first, B second, C third, D fourth, E five, F six, G seven, H eight, I nine, J ten, K eleven, L twelve, M thirteen) {
            super(first, second, third, fourth, five, six, seven, eight, nine, ten, eleven, twelve, thirteen);
        }
    }

    public static class Fourteen<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N> extends Tuple<T, A, B, C, D, E, F, G, H, I, J, K, L, M, N> implements Serializable, Result<T> {
        public Fourteen() {
        }

        public Fourteen(A first, B second, C third, D fourth, E five, F six, G seven, H eight, I nine, J ten, K eleven, L twelve, M thirteen, N fourteen) {
            super(first, second, third, fourth, five, six, seven, eight, nine, ten, eleven, twelve, thirteen, fourteen);
        }
    }
}
