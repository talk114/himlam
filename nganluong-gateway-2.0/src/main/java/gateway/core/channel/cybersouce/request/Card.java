package gateway.core.channel.cybersouce.request;

import gateway.core.channel.cybersouce.enu.CardType;

import java.io.Serializable;

/**
 *
 * @author TaiND
 */
public class Card implements Serializable{
    
    private CardType cardType;
    private String cardNumber;
    private String cardExpireYear;
    private String cardExpireMonth;
    private String cardCvv2Code;

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardExpireYear() {
        return cardExpireYear;
    }

    public void setCardExpireYear(String cardExpireYear) {
        this.cardExpireYear = cardExpireYear;
    }

    public String getCardExpireMonth() {
        return cardExpireMonth;
    }

    public void setCardExpireMonth(String cardExpireMonth) {
        this.cardExpireMonth = cardExpireMonth;
    }

    public String getCardCvv2Code() {
        return cardCvv2Code;
    }

    public void setCardCvv2Code(String cardCvv2Code) {
        this.cardCvv2Code = cardCvv2Code;
    }
}