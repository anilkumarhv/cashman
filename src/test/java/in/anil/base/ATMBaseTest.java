package in.anil.base;

import in.anil.ATMTestConfiguration;
import in.anil.model.ATMBase;
import in.anil.model.ATMDenomination;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ATMTestConfiguration.class})
@WebAppConfiguration
@ActiveProfiles("test")
public class ATMBaseTest {

    @Autowired
    private ATMBase base;

    @Before
    public void setup() {
        base = ATMTestBase.getNewInstance(ATMTestConfiguration.getTestMeta());
    }

    @Test
    public void testATMBase() throws Exception {

        Assert.assertNotNull(base);

        Assert.assertEquals(base.getDenominationQuantity(ATMDenomination.TEN), 10);
        Assert.assertEquals(base.getDenominationQuantity(ATMDenomination.TWENTY), 10);
        Assert.assertEquals(base.getDenominationQuantity(ATMDenomination.FIFTY), 10);
        Assert.assertEquals(base.getDenominationQuantity(ATMDenomination.HUNDRED), 10);

        Assert.assertTrue(base.addDenominationUnits(ATMDenomination.TEN, 10));
        Assert.assertEquals(base.getDenominationQuantity(ATMDenomination.TEN), 20);
        Assert.assertTrue(base.minusDenominationUnits(ATMDenomination.TEN, 10));

        Assert.assertFalse(base.minusDenominationUnits(ATMDenomination.HUNDRED, 11));
        Assert.assertEquals(base.getDenominationQuantity(ATMDenomination.HUNDRED), 10);

        Assert.assertEquals(base.getDenominationQuantity(ATMDenomination.TEN), 10);
        Assert.assertEquals(base.getDenominationQuantity(ATMDenomination.TWENTY), 10);
        Assert.assertEquals(base.getDenominationQuantity(ATMDenomination.FIFTY), 10);
        Assert.assertEquals(base.getDenominationQuantity(ATMDenomination.HUNDRED), 10);

        Assert.assertTrue(true);
    }

    @Test
    public void testATMTestBase() throws Exception {
        base = ATMTestBase.getNewInstance(ATMTestConfiguration.getTest12345Meta());
        Assert.assertEquals(base.getDenominationQuantity(ATMDenomination.TEN), 1);
        Assert.assertEquals(base.getDenominationQuantity(ATMDenomination.TWENTY), 2);
        Assert.assertEquals(base.getDenominationQuantity(ATMDenomination.FIFTY), 3);
        Assert.assertEquals(base.getDenominationQuantity(ATMDenomination.HUNDRED), 4);
    }

}
