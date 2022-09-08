package arrayTests;

import arraypack.Arraymain;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArraymainTest {

    @Test
    void throwException(){
        Assertions.assertThrows(RuntimeException.class, ()->Arraymain.convertArray(new int[]{10, 45}));
    }

    @Test
    void test1(){
        Arraymain.convertArray(new int[]{5,6,8,10});
    }

    @Test
    void test2(){
        Assertions.assertArrayEquals(new int[]{5,10},Arraymain.convertArray(new int[]{5,7,9,6,4,8,6,4,5,10}));
    }

    @Test
    void test3(){
        Assertions.assertFalse(Arraymain.matchersOneAndFour(new int[]{4,5,8,1,3,10}));
    }

    @Test
    void test4(){
        Assertions.assertTrue(Arraymain.matchersOneAndFour(new int[]{4,1,1,4,4,4,1}));
    }

}
