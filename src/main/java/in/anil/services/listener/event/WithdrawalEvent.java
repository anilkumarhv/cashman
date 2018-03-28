package in.anil.services.listener.event;

import in.anil.model.ATMDenomination;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class WithdrawalEvent extends ApplicationEvent {

    private ATMDenomination denomination;
    private int quantity;
    private int remainingNotes;

    public WithdrawalEvent(final Object source, final ATMDenomination denomination, final int quantity, final int remainingNotes) {
        super(source);
        this.denomination = denomination;
        this.quantity = quantity;
        this.remainingNotes = remainingNotes;
    }

}
