package ci553.happyshop.client.customer;

import ci553.happyshop.catalogue.Product;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// JUnit test checks the makeOrgainsedTrolley method.
class CustomerModelTest {

    @Test
    void makeOrganisedTrolley() {
        CustomerModel cm = new CustomerModel();

        Product p = new Product("0001", "TV", "0001.jpg", 12.01, 100);
        cm.setTheProduct(p);

        // User clicking 'add to trolley' 5 times
        cm.makeOrganisedTrolley();
        cm.makeOrganisedTrolley();
        cm.makeOrganisedTrolley();
        cm.makeOrganisedTrolley();
        cm.makeOrganisedTrolley();

        ArrayList<Product> tro = cm.getTrolley();

        // Verify the size list is 1
        assertEquals(1, tro.size(), "The list size should be 1, due to merged items");

        // Verify the quantity has updated to 5 correctly
        assertEquals(5, tro.get(0).getOrderedQuantity(), "The quantity should match 5 items");

        System.out.println("makeOrganisedTrolley has passed!");
    }
}



