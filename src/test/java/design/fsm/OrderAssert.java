package design.fsm;

import static org.fest.assertions.api.Assertions.assertThat;

import org.fest.assertions.api.AbstractAssert;
import org.fest.assertions.core.Condition;

public class OrderAssert extends AbstractAssert<OrderAssert, Order> {

	public OrderAssert(Order actual) {
		super(actual, OrderAssert.class);
	}

	public OrderAssert isNew() {
		assertThat(actual.getStatus()).isEqualTo(OrderStatus.NEW);
		return this;
	}

	public OrderAssert isOpened() {
		assertThat(actual.getStatus()).isEqualTo(OrderStatus.OPENED);
		return this;
	}

	public OrderAssert isClosed() {
		assertThat(actual.getStatus()).isEqualTo(OrderStatus.CLOSED);
		return this;
	}

	public OrderAssert isCancelled() {
		assertThat(actual.getStatus()).isEqualTo(OrderStatus.CANCELLED);
		return this;
	}

	public OrderAssert hasDetails(OrderDetails details) {
		assertThat(actual.getDetails()).isEqualTo(details);
		return this;
	}

	public OrderAssert hasOrderLineAmendment(AmendOrderLineCommand command) {
		assertThat(actual.getOrderLineAmendments()).contains(command);
		return this;
	}

	public OrderAssert containsOrderLine(final OrderLineIdentifier orderLineIdentifier) {
		assertThat(actual.getDetails().getOrderLines()).areExactly(1, new Condition<OrderLine>() {
			@Override
			public boolean matches(OrderLine orderLine) {
				return orderLine.getIdentifier().equals(orderLineIdentifier);
			}
		});
		return this;
	}

	public OrderAssert doesNotContainOrderLine(final OrderLineIdentifier orderLineIdentifier) {
		assertThat(actual.getDetails().getOrderLines()).areExactly(0, new Condition<OrderLine>() {
			@Override
			public boolean matches(OrderLine orderLine) {
				return orderLine.getIdentifier().equals(orderLineIdentifier);
			}
		});
		return this;
	}

}
