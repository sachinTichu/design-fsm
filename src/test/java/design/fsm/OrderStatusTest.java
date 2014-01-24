package design.fsm;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.EnumSet;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import design.fsm.commands.AmendOrderLineCommand;

@Test
public class OrderStatusTest {

	private static final OrderDetails ANY_ORDER_DETAILS = Mockito.mock(OrderDetails.class);

	private static final AmendOrderLineCommand ANY_AMEND_ORDER_LINE_COMMAND = Mockito.mock(AmendOrderLineCommand.class);

	private static final String ANY_SUSPEND_REASON = "";

	private static final String ANY_CANCEL_REASON = "";

	@Mock
	private Order order;

	@Test(dataProvider = "toOpen")
	public void shouldOpenOrderWhenStatusIs(OrderStatus status) {
		// when
		status.open(order);

		// then
		verify(order).doOpen();
	}

	@Test(dataProvider = "complementToOpen", expectedExceptions = IllegalOrderStateException.class, expectedExceptionsMessageRegExp = ".* open .*")
	public void shouldNotOpenOrderWhenStatusIs(OrderStatus status) {
		// when
		status.open(order);
	}

	@Test(dataProvider = "toClose")
	public void shouldCloseOrderWhenStatusIs(OrderStatus status) {
		// when
		status.close(order);

		// then
		verify(order).doClose();
	}

	@Test(dataProvider = "complementToClose", expectedExceptions = IllegalOrderStateException.class, expectedExceptionsMessageRegExp = ".* close .*")
	public void shouldNotCloseOrderWhenStatusIs(OrderStatus status) {
		// when
		status.close(order);
	}

	@Test(dataProvider = "toSuspend")
	public void shouldSuspendOrderWhenStatusIs(OrderStatus status) {
		// when
		status.suspend(order, ANY_SUSPEND_REASON);

		// then
		verify(order).doSuspend(eq(ANY_SUSPEND_REASON));
	}

	@Test(dataProvider = "complementToSuspend", expectedExceptions = IllegalOrderStateException.class, expectedExceptionsMessageRegExp = ".* suspend .*")
	public void shouldNotSuspendOrderWhenStatusIs(OrderStatus status) {
		// when
		status.suspend(order, ANY_SUSPEND_REASON);
	}

	@Test(dataProvider = "toResume")
	public void shouldResumeOrderWhenStatusIs(OrderStatus status) {
		// when
		status.resume(order);

		// then
		verify(order).doResume();
	}

	@Test(dataProvider = "complementToResume", expectedExceptions = IllegalOrderStateException.class, expectedExceptionsMessageRegExp = ".* resume .*")
	public void shouldNotResumeOrderWhenStatusIs(OrderStatus status) {
		// when
		status.resume(order);
	}

	@Test(dataProvider = "toCancel")
	public void shouldCancelOrderWhenStatusIs(OrderStatus status) {
		// when
		status.cancel(order, ANY_CANCEL_REASON);

		// then
		verify(order).doCancel(eq(ANY_CANCEL_REASON));
	}

	@Test(dataProvider = "complementToCancel", expectedExceptions = IllegalOrderStateException.class, expectedExceptionsMessageRegExp = ".* cancel .*")
	public void shouldNotCancelOrderWhenStatusIs(OrderStatus status) {
		// when
		status.cancel(order, ANY_CANCEL_REASON);
	}

	@Test(dataProvider = "toUpdate")
	public void shouldUpdateOrderWhenStatusIs(OrderStatus status) {
		assertThat(status.canUpdate()).isTrue();

		// when
		status.update(order, ANY_ORDER_DETAILS);

		// then
		verify(order).doUpdate(eq(ANY_ORDER_DETAILS));
	}

	@Test(dataProvider = "complementToUpdate", expectedExceptions = IllegalOrderStateException.class, expectedExceptionsMessageRegExp = ".* update .*")
	public void shouldNotUpdateOrderWhenStatusIs(OrderStatus status) {
		assertThat(status.canUpdate()).isFalse();

		// when
		status.update(order, ANY_ORDER_DETAILS);
	}

	@Test(dataProvider = "toAmend")
	public void shouldAmendOrderLineWhenStatusIs(OrderStatus status) {
		assertThat(status.canAmendOrderLine()).isTrue();

		// when
		status.amendOrderLine(order, ANY_AMEND_ORDER_LINE_COMMAND);

		// then
		verify(order).doAmendOrderLine(eq(ANY_AMEND_ORDER_LINE_COMMAND));
	}

	@Test(dataProvider = "complementToAmend", expectedExceptions = IllegalOrderStateException.class, expectedExceptionsMessageRegExp = ".* amend order line .*")
	public void shouldNotAmendOrderLineWhenStatusIs(OrderStatus status) {
		assertThat(status.canAmendOrderLine()).isFalse();

		// when
		status.amendOrderLine(order, ANY_AMEND_ORDER_LINE_COMMAND);
	}

	@DataProvider
	Object[][] toOpen() {
		return new Object[][] { { OrderStatus.NEW } };
	}

	@DataProvider
	Object[][] complementToOpen() {
		return complement(toOpen());
	}

	@DataProvider
	Object[][] toClose() {
		return new Object[][] { { OrderStatus.OPENED }, { OrderStatus.SUSPENDED } };
	}

	@DataProvider
	Object[][] complementToClose() {
		return complement(toClose());
	}

	@DataProvider
	Object[][] toSuspend() {
		return new Object[][] { { OrderStatus.OPENED } };
	}

	@DataProvider
	Object[][] complementToSuspend() {
		return complement(toSuspend());
	}

	@DataProvider
	Object[][] toResume() {
		return new Object[][] { { OrderStatus.SUSPENDED } };
	}

	@DataProvider
	Object[][] complementToResume() {
		return complement(toResume());
	}

	@DataProvider
	Object[][] toCancel() {
		return new Object[][] { { OrderStatus.OPENED }, { OrderStatus.SUSPENDED } };
	}

	@DataProvider
	Object[][] complementToCancel() {
		return complement(toCancel());
	}

	@DataProvider
	Object[][] toUpdate() {
		return new Object[][] { { OrderStatus.NEW } };
	}

	@DataProvider
	Object[][] complementToUpdate() {
		return complement(toUpdate());
	}

	@DataProvider
	Object[][] toAmend() {
		return new Object[][] { { OrderStatus.OPENED } };
	}

	@DataProvider
	Object[][] complementToAmend() {
		return complement(toAmend());
	}

	@BeforeMethod
	void givenOrder() {
		MockitoAnnotations.initMocks(this);
	}

	private Object[][] complement(Object[][] input) {
		// extract origin statuses from first column
		OrderStatus[] origin = new OrderStatus[input.length];
		for (int i = 0; i < origin.length; i++) {
			origin[i] = (OrderStatus) input[i][0];
		}

		// calculate complemented statuses
		OrderStatus[] complemented = EnumSet.complementOf(EnumSet.copyOf(Arrays.asList(origin))).toArray(
				new OrderStatus[0]);

		// build output for complemented statuses
		OrderStatus[][] output = new OrderStatus[complemented.length][];
		for (int i = 0; i < output.length; i++) {
			output[i] = new OrderStatus[] { complemented[i] };
		}
		return output;
	}

}
