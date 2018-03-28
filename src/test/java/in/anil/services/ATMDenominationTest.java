package in.anil.services;

import in.anil.ATMTestConfiguration;
import in.anil.base.ATMTestBase;
import in.anil.model.ATMBase;
import in.anil.model.ATMDenomination;
import in.anil.services.dto.ATMDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ATMTestConfiguration.class})
@WebAppConfiguration
@ActiveProfiles("test")
public class ATMDenominationTest {

    private static final int TWENTY = 20;
    private static final int FORTY = 40;
    private static final int FIFTY = 50;
    private static final int SIXTY = 60;
    private static final int SEVENTY = 70;
    private static final int EIGHTY = 80;
    private static final int HUNDRED = 100;
    private static final int HUNDREDTEN = 110;
    private static final int HUNDREDFIFTY = 150;
    private static final int TWOHUNDRED = 200;

    private ATMDenominations denominations;
    private ATMBase base;

    @Before
    public void setup() {
        base = ATMTestBase.getNewInstance(ATMTestConfiguration.getTestMeta());
        denominations = new ATMDenominations(base);
    }

    @Test
    public void testATMDenominations() throws Exception {
        ATMDto dto = new ATMDto();

        Assert.assertTrue(denominations.calculateCombinations(dto.reset(), TWENTY));
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.TEN) == null);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.TWENTY) == 1);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.FIFTY) == null);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.HUNDRED) == null);

        Assert.assertTrue(denominations.calculateCombinations(dto.reset(), FORTY));
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.TEN) == null);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.TWENTY) == 2);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.FIFTY) == null);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.HUNDRED) == null);

        Assert.assertTrue(denominations.calculateCombinations(dto.reset(), FIFTY));
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.TEN) == null);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.TWENTY) == null);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.FIFTY) == 1);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.HUNDRED) == null);

        Assert.assertTrue(denominations.calculateCombinations(dto.reset(), SEVENTY));
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.TEN) == null);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.TWENTY) == 1);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.FIFTY) == 1);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.HUNDRED) == null);

        Assert.assertTrue(denominations.calculateCombinations(dto.reset(), EIGHTY));
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.TEN) == 1);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.TWENTY) == 1);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.FIFTY) == 1);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.HUNDRED) == null);

        Assert.assertTrue(denominations.calculateCombinations(dto.reset(), HUNDRED));
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.TEN) == null);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.TWENTY) == null);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.FIFTY) == null);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.HUNDRED) == 1);

        Assert.assertTrue(denominations.calculateCombinations(dto.reset(), HUNDREDFIFTY));
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.TEN) == null);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.TWENTY) == null);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.FIFTY) == 1);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.HUNDRED) == 1);

        Assert.assertTrue(denominations.calculateCombinations(dto.reset(), SIXTY));
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.TEN) == 1);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.TWENTY) == null);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.FIFTY) == 1);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.HUNDRED) == null);

        Assert.assertTrue(denominations.calculateCombinations(dto.reset(), HUNDREDTEN));
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.TEN) == 1);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.TWENTY) == null);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.FIFTY) == null);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.HUNDRED) == 1);

        Assert.assertTrue(denominations.calculateCombinations(dto.reset(), TWOHUNDRED));
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.TEN) == null);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.TWENTY) == null);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.FIFTY) == null);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.HUNDRED) == 2);
    }

    @Test
    public void testATMDenominations200Special() throws Exception {

        ATMDto dto = new ATMDto();

        base = ATMTestBase.getNewInstance(ATMTestConfiguration.getTest08300Meta());

        denominations = new ATMDenominations(base);

        Assert.assertFalse(denominations.calculateCombinations(dto.reset(), EIGHTY));

        Assert.assertTrue(denominations.calculateCombinations(dto.reset(), TWOHUNDRED));
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.TEN) == null);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.TWENTY) == 5);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.FIFTY) == 2);
        Assert.assertTrue(dto.getTheMoney().get(ATMDenomination.HUNDRED) == null);

    }

}