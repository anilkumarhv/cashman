package in.anil;

import in.anil.model.ATMDenomination;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Data
public class ATMMeta {

    @Value("${default.denominations.fivecent:0}")
    private int fivecent;
    @Value("${default.denominations.tencent:0}")
    private int tencent;
    @Value("${default.denominations.twentycent:0}")
    private int twentycent;
    @Value("${default.denominations.fiftycent:0}")
    private int fiftycent;
    @Value("${default.denominations.one:0}")
    private int one;
    @Value("${default.denominations.two:0}")
    private int two;
    @Value("${default.denominations.five:0}")
    private int five;
    @Value("${default.denominations.ten:0}")
    private int ten;
    @Value("${default.denominations.twenty:0}")
    private int twenty;
    @Value("${default.denominations.fifty:0}")
    private int fifty;
    @Value("${default.denominations.hundred:0}")
    private int hundred;

    public Map<ATMDenomination, Integer> getDefaults() {
        Map<ATMDenomination, Integer> quantities = new HashMap<>();
        quantities.put(ATMDenomination.FIFTYCENT, getFiftycent());
        quantities.put(ATMDenomination.TENCENT, getTencent());
        quantities.put(ATMDenomination.TWENTYCENT, getTencent());
        quantities.put(ATMDenomination.FIFTYCENT, getFiftycent());
        quantities.put(ATMDenomination.ONE, getOne());
        quantities.put(ATMDenomination.TWO, getTwo());
        quantities.put(ATMDenomination.FIVE, getFive());
        quantities.put(ATMDenomination.TEN, getTen());
        quantities.put(ATMDenomination.TWENTY, getTwenty());
        quantities.put(ATMDenomination.FIFTY, getFifty());
        quantities.put(ATMDenomination.HUNDRED, getHundred());
        return quantities;
    }
}
