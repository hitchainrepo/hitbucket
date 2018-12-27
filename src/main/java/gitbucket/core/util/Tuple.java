package gitbucket.core.util;

import java.io.Serializable;

/**
 * Tupe
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-10-26 auto generate by qdp.
 */
public class Tuple implements Serializable {
	public static class Two<F, S> implements Serializable {
		private F first;
		private S second;

		public Two() {
		}

		public Two(F first, S second) {
			this.first = first;
			this.second = second;
		}

		public static <F, S> Two<F, S> empty() {
			return new Two<F, S>();
		}

		public static <F, S> Two<F, S> create(F first, S second) {
			return new Two<F, S>(first, second);
		}

		public F getFirst() {
			return first;
		}

		public void setFirst(F first) {
			this.first = first;
		}

		public S getSecond() {
			return second;
		}

		public void setSecond(S second) {
			this.second = second;
		}

		public boolean isEmpty() {
			return first == null && second == null;
		}
	}

	public static class Three<F, S, T> implements Serializable {
		private F first;
		private S second;
		private T third;

		public Three() {
		}

		public Three(F first, S second, T third) {
			this.first = first;
			this.second = second;
			this.third = third;
		}

		public static <F, S, T> Three<F, S, T> empty() {
			return new Three<F, S, T>();
		}

		public static <F, S, T> Three<F, S, T> create(F first, S second, T third) {
			return new Three<F, S, T>(first, second, third);
		}

		public F getFirst() {
			return first;
		}

		public void setFirst(F first) {
			this.first = first;
		}

		public S getSecond() {
			return second;
		}

		public void setSecond(S second) {
			this.second = second;
		}

		public T getThird() {
			return third;
		}

		public void setThird(T third) {
			this.third = third;
		}

		public boolean isEmpty() {
			return first == null && second == null && third == null;
		}
	}

	public static class Four<A, B, C, D> implements Serializable {
		private A first;
		private B second;
		private C third;
		private D fourth;

		public Four() {
		}

		public Four(A first, B second, C third, D fourth) {
			this.first = first;
			this.second = second;
			this.third = third;
			this.fourth = fourth;
		}

		public static <A, B, C, D> Four<A, B, C, D> empty() {
			return new Four<A, B, C, D>();
		}

		public static <A, B, C, D> Four<A, B, C, D> create(A first, B second, C third, D fourth) {
			return new Four<A, B, C, D>(first, second, third, fourth);
		}

		public A getFirst() {
			return first;
		}

		public void setFirst(A first) {
			this.first = first;
		}

		public B getSecond() {
			return second;
		}

		public void setSecond(B second) {
			this.second = second;
		}

		public C getThird() {
			return third;
		}

		public void setThird(C third) {
			this.third = third;
		}

		public D getFourth() {
			return fourth;
		}

		public void setFourth(D fourth) {
			this.fourth = fourth;
		}

		public boolean isEmpty() {
			return first == null && second == null && third == null && fourth == null;
		}
	}
}
