package ci553.happyshop.catalogue;


/*
   Author: Hannah Virgo
   Changes: An Exception was created for when the quantity amount exceeds the permitted maximum amount.
*/

public class ExceedMaximumQuantity extends RuntimeException {
    public ExceedMaximumQuantity(String message) {
        super(message);
    }
}
