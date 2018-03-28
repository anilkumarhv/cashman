package in.anil.services;

import in.anil.model.ATMBase;
import in.anil.model.ATMDenomination;
import in.anil.services.dto.ATMDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;

@Service
public class ATMDenominations {

    private ATMBase base;

    @Autowired
    public ATMDenominations(final ATMBase base) {
        this.base = base;
    }

    public boolean calculateCombinations(final ATMDto combination, final double amount) {
        return calculateCombinations(combination, amount, 0);
    }

    private boolean calculateCombinations(final ATMDto combination, final double amount, final Integer activeIndex) {

        double effectiveAmount = amount;

        /*
         * Get all the denominations in a descending order in order to step through
         * the the denominations from top down stating at the supplied index
         * */
        ATMDenomination[] denominations = getDecendingSortedDenominations();
        ATMDenomination denomination = denominations[activeIndex];

        /*
         * Start by finding the removing the modulus of the effectiveAmount that is NOT divisible
         * by the denomination, this way we are certain that it will be a whole number of units
         * however at this point we identify the availability of the denomination and use the lower
         * of the two availableUnits vs requiredUnits, after all we can't give out what we don't have.
         * */
        double matchableAmount = effectiveAmount - (effectiveAmount % denomination.getNominalValue());
        int unitsRequired = (int) (matchableAmount / denomination.getNominalValue());
        int unitsAvailable = base.getDenominationQuantity(denomination);
        int effectiveUnits = unitsAvailable < unitsRequired ? unitsAvailable : unitsRequired;

        if (effectiveUnits > 0) {

            /*
             * This is where the magic starts, we will attempt an reducing iteration over the effectiveUnits
             * this will allow us the step back in case of an unsuccessful solution further down.
             * */
            for (int i = effectiveUnits; i > 0; i--) {

                combination.getTheMoney().put(denomination, i);
                effectiveAmount -= denomination.getNominalValue() * i;


                if (effectiveAmount > 0 && activeIndex == denominations.length - 1) {
                    /*
                     * if the effectiveAmount is > 0 and we are at the last denomination,
                     * then we have to fail back up the recursion
                     * */
                    return false;
                } else if (effectiveAmount == 0 || calculateCombinations(combination, effectiveAmount, activeIndex + 1)) {
                    /*
                     * if the effectiveAmount is == 0 then we have fully settled the required amount of cash,
                     * well done now pass the success back up the recursion
                     * OR if effectiveAmount > 0 and there are still denominations available
                     * call the recursive method with the effectiveAmount and index of the next available denomination
                     * if the recursive method returns with a success then pass on the good will back up the recursion
                     * */
                    return true;
                }

                /*
                 * Ah well, it seems the current recipe mix was not successful, thus let us revert the denomination
                 * allocation and reset the effectiveAmount
                 * The effectiveUnits reducing iteration will now reduce the effectiveUnits of the current denomination,
                 * and again run the recursion for the adapted recipe.
                 * */
                combination.getTheMoney().remove(denomination);
                effectiveAmount += denomination.getNominalValue() * i;

            }

            /*
             * If the effectiveUnits reducing iteration finishes without a successful recipe, then
             * we DO NOT have the denominations quantities to settle the required amount, hence we have to fail
             * the request.
             * */
            return false;
        } else {
            /*
             * This scenario will occur if there is no need, if there is no matchable amount for the current
             * denomination, or there is no units left of the current denomination
             * */
            if (effectiveAmount > 0 && activeIndex == denominations.length - 1) {
                /*
                 * if the effectiveAmount is > 0 and we are at the last denomination,
                 * then we have to fail back up the recursion
                 * */
                return false;
            } else {
                /*
                 * There are still denominations available
                 * call the recursive method with the effectiveAmount and index of the next available denomination
                 * if the recursive method returns with a success then pass on the good will back up the recursion
                 * */
                return calculateCombinations(combination, effectiveAmount, activeIndex + 1);
            }
        }

    }

    private ATMDenomination[] getDecendingSortedDenominations() {
        ATMDenomination[] denominations = ATMDenomination.values();
        Arrays.sort(denominations, new DecendingDenominationComparator());
        return denominations;
    }

    class DecendingDenominationComparator implements Comparator<ATMDenomination> {
        @Override
        public int compare(final ATMDenomination a1, final ATMDenomination a2) {
            return Double.compare(a2.getNominalValue(), a1.getNominalValue());
        }
    }

}
