package cli.commands;

import api.TCFPublicAPI;
import cli.framework.Command;
import stubs.cart.Cookies;
import stubs.cart.Item;

import java.util.List;

public class OrderCookie extends CartManagement {

	@Override
	public String identifier() { return "order"; }

	@Override
	public void execute() throws Exception {
		system.carts.addItemToCustomerCart(customerName, item);
	}

	@Override
	public String describe() {
		return "Order some cookies for a given customer (order CUSTOMER QUANTITY RECIPE)";
	}
}
