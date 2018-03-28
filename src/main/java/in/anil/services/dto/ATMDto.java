package in.anil.services.dto;

import in.anil.model.ATMDenomination;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ATMDto {
    private String message;
    private Map<ATMDenomination, Integer> theMoney = new HashMap<>();

    public ATMDto reset() {
        theMoney = new HashMap<>();
        return this;
    }
}
