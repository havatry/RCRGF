package tests.temp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TestMethoGeneric {
    static <T> void test1(Collection<T> o1, Collection<T> o2) {
        for (T t : o1) {
            o2.add(t);
        }
    }

    static <T> void test(Collection<? extends T> o1, Collection<T> o2) {
        for (T t : o1) {
            o2.add(t);
        }
    }

    public static void main(String[] args) {
        List<Object> as = new ArrayList<>();
        List<String> bs = new ArrayList<>();
        test(bs, as); // right
//        test1(bs, as); // ±¨´í
//        test1(as, bs); // ±¨´í
        test1(bs, bs); // right
    }
}
