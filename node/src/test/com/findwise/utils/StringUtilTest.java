package test.com.findwise.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class StringUtilTest {

    @Test
    public void testApplySha256ToSampleAndToEmpty() {
        // given
        final String sampleString = "af2bdbe1aa9b6ec1e2ade1d694f41fc71a831d0268e9891562113d8a62add1bf";
        final String emptyString = "";

        // when
        final String hashedSampleString = StringUtil.applySha256(sampleString);
        final String hashedEmptyString = StringUtil.applySha256(emptyString);

        // then
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", hashedEmptyString);
        assertEquals("da16214a78272359569580e8887e93f9056a00fd9d3e70aa6807b4fd0e21a284", hashedSampleString);
    }

    @Test(expected = NullPointerException.class)
    public void testApplySha256ToNull(){
        // when
         StringUtil.applySha256(null);

        // then
        fail();
    }
}