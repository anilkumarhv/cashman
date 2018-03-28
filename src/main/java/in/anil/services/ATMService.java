package in.anil.services;

import in.anil.model.ATMBase;
import in.anil.model.ATMDenomination;
import in.anil.services.dto.ATMDto;
import in.anil.services.exception.ATMInsufficientDenominationsException;
import in.anil.services.listener.event.WithdrawalEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class ATMService {

    private ATMDenominations denominations;
    private ATMBase base;
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public ATMService(final ATMDenominations denominations, final ATMBase base, final ApplicationEventPublisher applicationEventPublisher) {
        this.denominations = denominations;
        this.base = base;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public ATMDto withdrawMoney(final double amount) {
        ATMDto result = new ATMDto();
        synchronized (this) {
            if (denominations.calculateCombinations(result, amount)) {
                updateBase(result, false);
                result.setMessage(String.format("Transaction successful, %s withdrawn.", new Double(ATMUtil.calculateTotalValue(result.getTheMoney())).toString()));
            } else {
                throw new ATMInsufficientDenominationsException(String.format("Transaction failed, %s not available", amount));
            }
        }
        return result;
    }

    public boolean depositMoney(final ATMDto amounts) {
        synchronized (this) {
            updateBase(amounts, true);
        }
        return true;
    }

    public ATMDto getDenominationQuantities() {
        ATMDto dto = new ATMDto();
        dto.setTheMoney(base.getDenominationQuantities());
        return dto;
    }

    public double getTotalHoldings() {
        return base.getTotalHolding();
    }

    private void updateBase(final ATMDto result, final boolean deposit) {
        for (ATMDenomination denomination : result.getTheMoney().keySet()) {
            Integer value = result.getTheMoney().get(denomination);
            if (value != null) {
                if (deposit) {
                    base.addDenominationUnits(denomination, value);
                } else {
                    base.minusDenominationUnits(denomination, value);
                    WithdrawalEvent event = new WithdrawalEvent(this, denomination, value, base.getDenominationQuantity(denomination));
                    applicationEventPublisher.publishEvent(event);
                }
            }
        }
    }

}
