package in.anil.base;

import in.anil.ATMMeta;
import in.anil.model.ATMBase;
import in.anil.model.ATMDenomination;

import java.util.Map;

public class ATMTestBase extends ATMBase {
    private static ATMTestBase instance = null;

    private ATMTestBase(Map<ATMDenomination, Integer> quantities) {
        super(quantities);
    }

    public static ATMTestBase getNewInstance(final ATMMeta atmMeta) {
        ATMTestBase.instance = new ATMTestBase(atmMeta.getDefaults());
        return ATMTestBase.instance;
    }
}
