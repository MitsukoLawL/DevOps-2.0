package integration;

import arquillian.AbstractTCFTest;
import fr.unice.polytech.isa.tcf.CartModifier;
import fr.unice.polytech.isa.tcf.CustomerFinder;
import fr.unice.polytech.isa.tcf.CustomerRegistration;
import fr.unice.polytech.isa.tcf.entities.Cookies;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;
import fr.unice.polytech.isa.tcf.entities.Order;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by islamefelipefernandes on 05/04/2016.
 */
@RunWith(Arquillian.class)
public class WebServicesIntegrationTest extends AbstractTCFTest {
    @EJB(name= "cart-stateless") private CartModifier cart;
    @EJB
    private CustomerFinder finder;
    @EJB
    private CustomerRegistration registration;

    private Item item1, item2;

    @Before
    public void setUpContext() {
        memory.flush();
        item1 = new Item(Cookies.CHOCOLALALA, 3);
        item2 = new Item(Cookies.DARK_TEMPTATION, 2);
    }

    @Test
    public void integrationBetweenCartPaymentAndOrders() throws Exception {
        registration.register("john", "1234-896983");
        Customer retrieved = finder.findByName("john").get();
        assertNotNull(retrieved);
        assertEquals("john",retrieved.getName());
        assertTrue(retrieved.getOrders().isEmpty());
        boolean b1 = cart.add(retrieved,item1);
        boolean b2 = cart.add(retrieved,item2);
        assertTrue(b1);
        assertTrue(b2);
        Item[] oracle = new Item[] {new Item(Cookies.CHOCOLALALA, 3),new Item(Cookies.DARK_TEMPTATION, 2)};
        assertEquals(new HashSet<>(Arrays.asList(oracle)), cart.contents(retrieved));
        String id = cart.validate(retrieved);

        //String id = cashier.payOrder(retrieved, items);
        Order order = memory.getOrders().get(id);
        assertTrue(retrieved.getOrders().contains(order));
    }
}
