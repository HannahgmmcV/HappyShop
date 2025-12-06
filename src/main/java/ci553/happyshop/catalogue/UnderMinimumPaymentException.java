package ci553.happyshop.catalogue;

/*
   Author: Hannah Virgo
   Changes: An Exception was created for when a checkout attempt has a total below the minimum payment.
*/

public class UnderMinimumPaymentException extends RuntimeException {
    public UnderMinimumPaymentException(String message) {
        super(message);
    }
}
