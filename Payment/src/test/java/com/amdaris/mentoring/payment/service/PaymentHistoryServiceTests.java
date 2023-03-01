package com.amdaris.mentoring.payment.service;

import com.amdaris.mentoring.payment.dto.PaymentHistoryDto;
import com.amdaris.mentoring.payment.model.PaymentHistory;
import com.amdaris.mentoring.payment.model.PaymentStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityExistsException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@SpringBootTest
public class PaymentHistoryServiceTests {
    @Autowired
    private PaymentHistoryService paymentHistoryService;

    @BeforeEach
    public void beforeEach() {
        paymentHistoryService.clear();
    }

    @DisplayName("Test that all payment history were returned")
    @Test
    public void testThatAllPaymentHistoriesWereReturned() {
        PaymentHistoryDto firstPaymentHistory = new PaymentHistoryDto();
        firstPaymentHistory.setOrderId(1L);
        firstPaymentHistory.setStatus(PaymentStatus.pending.name());
        firstPaymentHistory.setPaymentMethod("card");
        firstPaymentHistory.setPaymentDate(LocalDateTime.now().withNano(0).minusHours(1));

        paymentHistoryService.save(firstPaymentHistory);

        PaymentHistoryDto secondPaymentHistory = new PaymentHistoryDto();
        secondPaymentHistory.setOrderId(2L);
        secondPaymentHistory.setStatus(PaymentStatus.complete.name());
        secondPaymentHistory.setPaymentMethod("paypal");
        secondPaymentHistory.setPaymentDate(LocalDateTime.now().withNano(0));

        paymentHistoryService.save(secondPaymentHistory);

        Assertions.assertEquals(2, paymentHistoryService.findAll().size());
    }

    @DisplayName("Test that found by order id payment history from database")
    @Test
    public void testThatFindByOrderIdPaymentHistoryWasGot() {
        PaymentHistoryDto firstPaymentHistory = new PaymentHistoryDto();
        firstPaymentHistory.setOrderId(1L);
        firstPaymentHistory.setStatus(PaymentStatus.pending.name());
        firstPaymentHistory.setPaymentMethod("card");
        firstPaymentHistory.setPaymentDate(LocalDateTime.now().withNano(0).minusHours(1));

        paymentHistoryService.save(firstPaymentHistory);

        PaymentHistory foundByOrderId = paymentHistoryService.findByOrderId(firstPaymentHistory.getOrderId());

        Assertions.assertEquals(firstPaymentHistory.getPaymentDate(), foundByOrderId.getPaymentDate());
        Assertions.assertEquals(firstPaymentHistory.getStatus(), foundByOrderId.getStatus().name());
        Assertions.assertEquals(firstPaymentHistory.getPaymentMethod(), foundByOrderId.getPaymentMethod().getTitle());
    }

    @DisplayName("Test that found by order id payment history throws error when didn't found any item")
    @Test
    public void testThatFoundByOrderIdPaymentHistoryThrowsAnErrorWhenNotFoundAnyItem() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> paymentHistoryService.findByOrderId(1L));

        Assertions.assertEquals("Payment history with order id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that found by id payment history from database")
    @Test
    public void testThatFindByIdPaymentHistoryWasGot() {
        PaymentHistoryDto firstPaymentHistory = new PaymentHistoryDto();
        firstPaymentHistory.setOrderId(1L);
        firstPaymentHistory.setStatus(PaymentStatus.pending.name());
        firstPaymentHistory.setPaymentMethod("card");
        firstPaymentHistory.setPaymentDate(LocalDateTime.now().withNano(0).minusHours(1));

        paymentHistoryService.save(firstPaymentHistory);

        PaymentHistory paymentHistory = paymentHistoryService.findByOrderId(1L);

        Assertions.assertEquals(firstPaymentHistory.getPaymentDate(), paymentHistory.getPaymentDate());
        Assertions.assertEquals(firstPaymentHistory.getStatus(), paymentHistory.getStatus().name());
        Assertions.assertEquals(firstPaymentHistory.getPaymentMethod(), paymentHistory.getPaymentMethod().getTitle());
    }

    @DisplayName("Test that found by id payment history throws error when didn't found any item")
    @Test
    public void testThatFoundByIdPaymentHistoryThrowsAnErrorWhenNotFoundAnyItem() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> paymentHistoryService.findById(1L));

        Assertions.assertEquals("Payment history with id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that payment history was saved in database")
    @Test
    public void testThatPaymentHistoryWasSaved() {
        PaymentHistoryDto firstPaymentHistory = new PaymentHistoryDto();
        firstPaymentHistory.setOrderId(1L);
        firstPaymentHistory.setStatus(PaymentStatus.pending.name());
        firstPaymentHistory.setPaymentMethod("card");
        firstPaymentHistory.setPaymentDate(LocalDateTime.now().withNano(0).minusHours(1));


        paymentHistoryService.save(firstPaymentHistory);

        PaymentHistory paymentHistory = paymentHistoryService.findByOrderId(1L);

        Assertions.assertEquals(firstPaymentHistory.getOrderId(), paymentHistory.getOrderId());
        Assertions.assertEquals(firstPaymentHistory.getPaymentDate(), paymentHistory.getPaymentDate());
        Assertions.assertEquals(firstPaymentHistory.getPaymentMethod(), paymentHistory.getPaymentMethod().getTitle());
    }

    @DisplayName("Test that save payment history throws error when item already exists")
    @Test
    public void testThatSavePaymentHistoryThrowsAnErrorWhenItemAlreadyExists() {
        PaymentHistoryDto firstPaymentHistory = new PaymentHistoryDto();
        firstPaymentHistory.setOrderId(1L);
        firstPaymentHistory.setStatus(PaymentStatus.pending.name());
        firstPaymentHistory.setPaymentMethod("card");
        firstPaymentHistory.setPaymentDate(LocalDateTime.now().withNano(0).minusHours(1));

        paymentHistoryService.save(firstPaymentHistory);

        firstPaymentHistory.setStatus(PaymentStatus.complete.name());

        EntityExistsException exception = Assertions.assertThrows(EntityExistsException.class,
                () -> paymentHistoryService.save(firstPaymentHistory));

        Assertions.assertEquals("Payment history for order id - 1 exists!", exception.getMessage());
    }

    @DisplayName("Test that payment history was updated in database")
    @Test
    public void testThatPaymentHistoryWasUpdated() {
        PaymentHistoryDto firstPaymentHistory = new PaymentHistoryDto();
        firstPaymentHistory.setOrderId(1L);
        firstPaymentHistory.setStatus(PaymentStatus.pending.name());
        firstPaymentHistory.setPaymentMethod("card");
        firstPaymentHistory.setPaymentDate(LocalDateTime.now().withNano(0).minusHours(1));

        paymentHistoryService.save(firstPaymentHistory);

        PaymentHistory paymentHistoryBeforeUpdate = paymentHistoryService.findByOrderId(1L);

        Assertions.assertEquals(firstPaymentHistory.getPaymentMethod(), paymentHistoryBeforeUpdate.getPaymentMethod().getTitle());

        firstPaymentHistory.setStatus(PaymentStatus.complete.name());

        paymentHistoryService.update(firstPaymentHistory, paymentHistoryBeforeUpdate.getId());

        PaymentHistory paymentHistoryAfterUpdate = paymentHistoryService.findByOrderId(1L);

        Assertions.assertEquals(firstPaymentHistory.getPaymentMethod(), paymentHistoryAfterUpdate.getPaymentMethod().getTitle());
    }

    @DisplayName("Test that update payment history throws error when didn't found any item")
    @Test
    public void testThatUpdatePaymentHistoryThrowsAnErrorWhenNotFoundAnyItem() {
        PaymentHistoryDto firstPaymentHistory = new PaymentHistoryDto();
        firstPaymentHistory.setOrderId(1L);
        firstPaymentHistory.setStatus(PaymentStatus.pending.name());
        firstPaymentHistory.setPaymentMethod("card");
        firstPaymentHistory.setPaymentDate(LocalDateTime.now().withNano(0).minusHours(1));

        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> paymentHistoryService.update(firstPaymentHistory, (short) 1));

        Assertions.assertEquals("Payment history with id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that payment history was deleted from database")
    @Test
    public void testThatPaymentHistoryWasDeleted() {
        PaymentHistoryDto firstPaymentHistory = new PaymentHistoryDto();
        firstPaymentHistory.setOrderId(1L);
        firstPaymentHistory.setStatus(PaymentStatus.pending.name());
        firstPaymentHistory.setPaymentMethod("card");
        firstPaymentHistory.setPaymentDate(LocalDateTime.now().withNano(0).minusHours(1));

        paymentHistoryService.save(firstPaymentHistory);

        PaymentHistory byOrderId = paymentHistoryService.findByOrderId(1L);

        Assertions.assertEquals(1, paymentHistoryService.findAll().size());

        paymentHistoryService.deleteById(byOrderId.getId());

        Assertions.assertEquals(0, paymentHistoryService.findAll().size());
    }

    @DisplayName("Test that delete payment history throws error when didn't found any item")
    @Test
    public void testThatDeletePaymentHistoryThrowsAnErrorWhenNotFoundAnyItem() {
        EntityExistsException exception = Assertions.assertThrows(EntityExistsException.class,
                () -> paymentHistoryService.deleteById(1L));

        Assertions.assertEquals("Payment history with id - 1 doesn't exist!", exception.getMessage());
    }
}
