package integration;

import arquillian.AbstractTCFTest;
import fr.unice.polytech.isa.tcf.CustomerFinder;
import fr.unice.polytech.isa.tcf.entities.Cookies;
import fr.unice.polytech.isa.tcf.entities.Customer;
import fr.unice.polytech.isa.tcf.entities.Item;
import fr.unice.polytech.isa.tcf.entities.Order;
import fr.unice.polytech.isa.tcf.webservice.CartWebService;
import fr.unice.polytech.isa.tcf.webservice.CustomerCareService;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by islamefelipefernandes on 05/04/2016.
 */
@RunWith(Arquillian.class)
public class WebServicesIntegrationTest extends AbstractTCFTest {

    @EJB(name= "CartWS") private CartWebService cartWebService;
    @EJB(name= "CustomerCareWS") private CustomerCareService customerService;
    @EJB private CustomerFinder finder;
    private Item item1, item2;

    @Before
    public void setUpContext() {
        memory.flush();
        item1 = new Item(Cookies.CHOCOLALALA, 3);
        item2 = new Item(Cookies.DARK_TEMPTATION, 2);
    }

    @Test
    public void integrationBetweenTheServicesAndOrders() throws Exception {
        customerService.register("john", "1234-896983");
        cartWebService.addItemToCustomerCart("john",item1);
        cartWebService.addItemToCustomerCart("john",item2);

        Item[] oracle = new Item[] {new Item(Cookies.CHOCOLALALA, 3),new Item(Cookies.DARK_TEMPTATION, 2)};
        assertEquals(new HashSet<>(Arrays.asList(oracle)), cartWebService.getCustomerCartContents("john"));
        String id = cartWebService.validate("john");

        //String id = cashier.payOrder(retrieved, items);
        Order order = memory.getOrders().get(id);
        Customer retrieved = finder.findByName("john").get();
        assertTrue(retrieved.getOrders().contains(order));
    }
}

