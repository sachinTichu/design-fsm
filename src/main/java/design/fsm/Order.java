package design.fsm;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Order {

	private OrderStatus status;

	private OrderIdentifier identifier;

	private OrderDetails details;

	private Set<AmendOrderLineCommand> orderLineAmendments = new HashSet<>();

	Order(Builder builder) {
		this.status = requireNonNull(builder.status);
		this.identifier = requireNonNull(builder.identifier);
		this.details = requireNonNull(builder.details);
	}

	public OrderStatus getStatus() {
		return status;
	}

	public OrderIdentifier getIdentifier() {
		return identifier;
	}

	public OrderDetails getDetails() {
		return details;
	}

	public Set<AmendOrderLineCommand> getOrderLineAmendments() {
		return Collections.unmodifiableSet(orderLineAmendments);
	}

	public void open() {
		status.open(this);
	}

	void doOpen() {
		status = OrderStatus.OPENED;
	}

	public void close() {
		status.close(this);
	}

	void doClose() {
		status = OrderStatus.CLOSED;
	}

	public void suspend(String reason) {
		status.suspend(this, reason);
	}

	void doSuspend(String reason) {
		requireNonNull(reason);
		status = OrderStatus.SUSPENDED;
	}

	public void resume() {
		status.resume(this);
	}

	void doResume() {
		status = OrderStatus.OPENED;
	}

	public void cancel(String reason) {
		status.cancel(this, reason);
	}

	void doCancel(String reason) {
		requireNonNull(reason);
		status = OrderStatus.CANCELLED;
	}

	public void update(OrderDetails details) {
		status.update(this, details);
	}

	void doUpdate(OrderDetails details) {
		this.details = requireNonNull(details);
	}

	public void amendOrderLine(AmendOrderLineCommand command) {
		status.amendOrderLine(this, command);
	}

	void doAmendOrderLine(AmendOrderLineCommand command) {
		requireNonNull(command);

		if (command.isChanging()) {
			details.changeOrderLine(command.getIdentifier(), command.getNewOrderLine());
		}

		if (command.isAdding()) {
			details.addOrderLine(command.getNewOrderLine());
		}

		if (command.isRemoving()) {
			details.removeOrderLine(command.getIdentifier());
		}

		orderLineAmendments.add(command);
	}

	public static class Builder {

		public OrderStatus status;

		private OrderIdentifier identifier;

		private OrderDetails details;

		public Builder withStatus(OrderStatus status) {
			this.status = status;
			return this;
		}

		public Builder newOrder() {
			return withStatus(OrderStatus.NEW);
		}

		public Builder openedOrder() {
			return withStatus(OrderStatus.OPENED);
		}

		public Builder withIdentifier(OrderIdentifier identifier) {
			this.identifier = identifier;
			return this;
		}

		public Builder withDetails(OrderDetails details) {
			this.details = details;
			return this;
		}

		public Order build() {
			return new Order(this);
		}
	}

	private CurrentDateProvider currentDateProvider;

	private CurrentUserProvider currentUserProvider;
}
