package com.amdaris.mentoring.payment.service;

import com.amdaris.mentoring.payment.dto.PaymentMethodDto;
import com.amdaris.mentoring.payment.model.PaymentMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@SpringBootTest
public class PaymentMethodServiceTests {
    @Autowired
    private PaymentMethodService paymentMethodService;

    @BeforeEach
    public void beforeEach() {
        paymentMethodService.clear();
    }

    @DisplayName("Test that all payment methods were returned")
    @Test
    public void testThatAllPaymentMethodsWereReturned() {
        Assertions.assertEquals(0, paymentMethodService.findAll().size());

        PaymentMethodDto cardPaymentMethod = new PaymentMethodDto();
        cardPaymentMethod.setTitle("card");
        cardPaymentMethod.setDetails("pay with visa/mastercard card");

        paymentMethodService.save(cardPaymentMethod);

        PaymentMethodDto paypalPaymentMethod = new PaymentMethodDto();
        paypalPaymentMethod.setTitle("paypal");
        paypalPaymentMethod.setDetails("pay with paypal service");

        paymentMethodService.save(paypalPaymentMethod);

        Assertions.assertEquals(2, paymentMethodService.findAll().size());
    }

    @DisplayName("Test that found by title payment method from database")
    @Test
    public void testThatFindByTitlePaymentMethodWasGot(){
        PaymentMethodDto cardPaymentMethod = new PaymentMethodDto();
        cardPaymentMethod.setTitle("card");
        cardPaymentMethod.setDetails("pay with visa/mastercard card");

        paymentMethodService.save(cardPaymentMethod);

        PaymentMethod foundByTitle = paymentMethodService.findByTitle(cardPaymentMethod.getTitle());

        Assertions.assertEquals(cardPaymentMethod.getTitle(), foundByTitle.getTitle());
        Assertions.assertEquals(cardPaymentMethod.getDetails(), foundByTitle.getDetails());
    }

    @DisplayName("Test that found by title payment method throws error when didn't found any item")
    @Test
    public void testThatFoundByTitlePaymentMethodThrowsAnErrorWhenNotFoundAnyItem() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> paymentMethodService.findByTitle("card"));

        Assertions.assertEquals("Payment method with title - card doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that found by id payment method from database")
    @Test
    public void testThatFindByIdPaymentMethodWasGot(){
        PaymentMethodDto cardPaymentMethod = new PaymentMethodDto();
        cardPaymentMethod.setTitle("card");
        cardPaymentMethod.setDetails("pay with visa/mastercard card");

        paymentMethodService.save(cardPaymentMethod);

        PaymentMethod paymentMethod = paymentMethodService.findByTitle("card");
        PaymentMethodDto foundByTitle = paymentMethodService.findById((paymentMethod.getId()));

        Assertions.assertEquals(cardPaymentMethod.getTitle(), foundByTitle.getTitle());
        Assertions.assertEquals(cardPaymentMethod.getDetails(), foundByTitle.getDetails());
    }

    @DisplayName("Test that found by id payment method throws error when didn't found any item")
    @Test
    public void testThatFoundByIdPaymentMethodThrowsAnErrorWhenNotFoundAnyItem() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> paymentMethodService.findById((short) 1L));

        Assertions.assertEquals("Payment method with id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that payment method was saved in database")
    @Test
    public void testThatPaymentMethodWasSaved() {
        PaymentMethodDto cardPaymentMethod = new PaymentMethodDto();
        cardPaymentMethod.setTitle("card");
        cardPaymentMethod.setDetails("pay with visa/mastercard card");

        paymentMethodService.save(cardPaymentMethod);

        List<PaymentMethodDto> paymentMethods = paymentMethodService.findAll();

        Assertions.assertEquals(1, paymentMethods.size());
        Assertions.assertEquals("card", paymentMethods.get(0).getTitle());

        PaymentMethodDto paypalPaymentMethod = new PaymentMethodDto();
        paypalPaymentMethod.setTitle("paypal");
        paypalPaymentMethod.setDetails("pay with paypal service");

        paymentMethodService.save(paypalPaymentMethod);

        paymentMethods = paymentMethodService.findAll();

        Assertions.assertEquals(2, paymentMethods.size());
        Assertions.assertEquals("paypal", paymentMethods.get(1).getTitle());
    }

    @DisplayName("Test that save payment method throws error when item already exists")
    @Test
    public void testThatSavePaymentMethodThrowsAnErrorWhenItemAlreadyExists() {
        PaymentMethodDto firstPaypalPaymentMethod = new PaymentMethodDto();
        firstPaypalPaymentMethod.setTitle("paypal");
        firstPaypalPaymentMethod.setDetails("pay with paypal service");

        paymentMethodService.save(firstPaypalPaymentMethod);

        PaymentMethodDto secondPaypalPaymentMethod = new PaymentMethodDto();
        secondPaypalPaymentMethod.setTitle("paypal");
        secondPaypalPaymentMethod.setDetails("pay with paypal service");

        EntityExistsException exception = Assertions.assertThrows(EntityExistsException.class,
                () -> paymentMethodService.save(secondPaypalPaymentMethod));

        Assertions.assertEquals("Payment method with title - paypal exists!", exception.getMessage());
    }

    @DisplayName("Test that payment method was updated in database")
    @Test
    public void testThatPaymentMethodWasUpdated() {
        PaymentMethodDto paypalPaymentMethod = new PaymentMethodDto();
        paypalPaymentMethod.setTitle("paypal");
        paypalPaymentMethod.setDetails("pay with paypal service");

        paymentMethodService.save(paypalPaymentMethod);

        PaymentMethodDto updatedPaypalPaymentMethod = new PaymentMethodDto();
        updatedPaypalPaymentMethod.setDetails("pay with paypal service updated");

        short paypalMethodId = paymentMethodService.findByTitle(paypalPaymentMethod.getTitle()).getId();
        paymentMethodService.update(updatedPaypalPaymentMethod, paypalMethodId);

        PaymentMethodDto updatedPaymentMethod = paymentMethodService.findById(paypalMethodId);

        Assertions.assertEquals("pay with paypal service updated", updatedPaymentMethod.getDetails());
    }

    @DisplayName("Test that update payment method throws error when didn't found any item")
    @Test
    public void testThatUpdatePaymentMethodThrowsAnErrorWhenNotFoundAnyItem() {
        PaymentMethodDto paypalPaymentMethod = new PaymentMethodDto();
        paypalPaymentMethod.setTitle("paypal");
        paypalPaymentMethod.setDetails("pay with paypal service");

        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> paymentMethodService.update(paypalPaymentMethod,(short)1));

        Assertions.assertEquals("Payment method with id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that payment method was deleted from database")
    @Test
    public void testThatPaymentMethodWasDeleted() {
        PaymentMethodDto paypalPaymentMethod = new PaymentMethodDto();
        paypalPaymentMethod.setTitle("paypal");
        paypalPaymentMethod.setDetails("pay with paypal service");

        paymentMethodService.save(paypalPaymentMethod);

        Assertions.assertEquals(1, paymentMethodService.findAll().size());
        paymentMethodService.deleteById((short) 1);

        Assertions.assertEquals(0, paymentMethodService.findAll().size());
    }

    @DisplayName("Test that delete payment method throws error when didn't found any item")
    @Test
    public void testThatDeletePaymentMethodThrowsAnErrorWhenNotFoundAnyItem() {
        EntityExistsException exception = Assertions.assertThrows(EntityExistsException.class,
                () -> paymentMethodService.deleteById((short) 1));

        Assertions.assertEquals("Payment method with id - 1 doesn't exist!", exception.getMessage());
    }
}
