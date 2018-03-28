package in.anil.services.listener;

import in.anil.services.listener.event.WithdrawalEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class WithdrawalListener {

    @Value("${default.refillThreshold:2}")
    private int refillThreshold;

    @Async
    @EventListener
    public void handleUserEvent(final WithdrawalEvent event) {
        if (refillThreshold > 0 && event.getRemainingNotes() <= refillThreshold) {
            System.out.println("Need To Order More Of: " + event.getDenomination().name());
        }
    }
}
