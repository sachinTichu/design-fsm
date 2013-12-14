package design.fsm.events;

import java.util.Date;

import design.fsm.OrderIdentifier;
import design.fsm.OrderStatus;
import design.shared.User;


public class OrderClosedEvent extends AbstractOrderEvent {

	public OrderClosedEvent(Date date, User createdBy, OrderIdentifier identifier, OrderStatus oldStatus,
			OrderStatus newStatus) {
		super(date, createdBy, identifier, oldStatus, newStatus);
	}

}