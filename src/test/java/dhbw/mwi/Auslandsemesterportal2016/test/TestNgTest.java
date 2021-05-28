
package dhbw.mwi.Auslandsemesterportal2016.test;

import org.testng.annotations.*;

public class TestNgTest {

    @BeforeClass
    public void setUp() {
        // code that will be invoked when this test is instantiated
    }

    @Test(groups = { "fast" })
    public void aFastTest() {
        System.out.println("Fast test");
    }

    @Test(groups = { "slow" })
    public void aSlowTest() {
        System.out.println("Slow test");
        System.out.println("Slow test");
    }

}