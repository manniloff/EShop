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
import java.util.stream.Collectors;

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
    public void findAll_dataIsPresent_returnAllData() {
        PaymentMethodDto cardPaymentMethod = new PaymentMethodDto();
        cardPaymentMethod.setTitle("card1");
        cardPaymentMethod.setDetails("pay with visa/mastercard card");

        paymentMethodService.save(cardPaymentMethod);

        PaymentMethodDto paypalPaymentMethod = new PaymentMethodDto();
        paypalPaymentMethod.setTitle("paypal1");
        paypalPaymentMethod.setDetails("pay with paypal service");

        paymentMethodService.save(paypalPaymentMethod);

        Assertions.assertEquals(2L, paymentMethodService.findAll()
                .stream()
                .filter(paymentMethod -> paymentMethod.getTitle().equals("card1") || paymentMethod.getTitle().equals("paypal1"))
                .count());
    }

    @DisplayName("Test that found by title payment method from database")
    @Test
    public void findByTitle_dataIsPresent_returnExistingData() {
        PaymentMethodDto cardPaymentMethod = new PaymentMethodDto();
        cardPaymentMethod.setTitle("noNamePay");
        cardPaymentMethod.setDetails("pay with noNamePay");

        paymentMethodService.save(cardPaymentMethod);

        PaymentMethod foundByTitle = paymentMethodService.findByTitle(cardPaymentMethod.getTitle());

        Assertions.assertEquals(cardPaymentMethod.getTitle(), foundByTitle.getTitle());
        Assertions.assertEquals(cardPaymentMethod.getDetails(), foundByTitle.getDetails());
    }

    @DisplayName("Test that found by title payment method throws error when didn't found any item")
    @Test
    public void findByTitle_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> paymentMethodService.findByTitle("errorTitle"));

        Assertions.assertEquals("Payment method with title - errorTitle doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that found by id payment method from database")
    @Test
    public void findById_dataIsPresent_returnExistingData() {
        PaymentMethodDto cardPaymentMethod = new PaymentMethodDto();
        cardPaymentMethod.setTitle("binance");
        cardPaymentMethod.setDetails("pay with binance platform");

        paymentMethodService.save(cardPaymentMethod);

        PaymentMethod paymentMethod = paymentMethodService.findByTitle("binance");
        PaymentMethodDto foundByTitle = paymentMethodService.findById((paymentMethod.getId()));

        Assertions.assertEquals(cardPaymentMethod.getTitle(), foundByTitle.getTitle());
        Assertions.assertEquals(cardPaymentMethod.getDetails(), foundByTitle.getDetails());
    }

    @DisplayName("Test that found by id payment method throws error when didn't found any item")
    @Test
    public void findById_dataNoPresent_returnErrorMessage() {
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> paymentMethodService.findById((short) 1L));

        Assertions.assertEquals("Payment method with id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that payment method was saved in database")
    @Test
    public void create_dataNoPresent_returnSavedDataId() {
        PaymentMethodDto cardPaymentMethod = new PaymentMethodDto();
        cardPaymentMethod.setTitle("cash");
        cardPaymentMethod.setDetails("pay with cash");

        paymentMethodService.save(cardPaymentMethod);

        List<PaymentMethodDto> paymentMethods = paymentMethodService.findAll();

        Assertions.assertEquals("cash",paymentMethods.stream()
                .filter(method -> method.getTitle().equals("cash"))
                .findFirst()
                .get()
                .getTitle());

        PaymentMethodDto paypalPaymentMethod = new PaymentMethodDto();
        paypalPaymentMethod.setTitle("yandexpay");
        paypalPaymentMethod.setDetails("pay with yandexpay service");

        paymentMethodService.save(paypalPaymentMethod);

        paymentMethods = paymentMethodService.findAll();

        Assertions.assertEquals("yandexpay", paymentMethods.stream()
                .filter(method -> method.getTitle().equals("yandexpay"))
                .findFirst()
                .get()
                .getTitle());
    }

    @DisplayName("Test that save payment method throws error when item already exists")
    @Test
    public void create_dataIsPresent_returnErrorMessage() {
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
    public void update_dataIsPresent_returnUpdatedDataId() {
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
    public void update_dataNoPresent_returnErrorMessage() {
        PaymentMethodDto paypalPaymentMethod = new PaymentMethodDto();
        paypalPaymentMethod.setTitle("paypal");
        paypalPaymentMethod.setDetails("pay with paypal service");

        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> paymentMethodService.update(paypalPaymentMethod, (short) 1));

        Assertions.assertEquals("Payment method with id - 1 doesn't exist!", exception.getMessage());
    }

    @DisplayName("Test that payment method was deleted from database")
    @Test
    public void deletedById_dataIsPresent_returnDeletedDataId() {
        PaymentMethodDto paypalPaymentMethod = new PaymentMethodDto();
        paypalPaymentMethod.setTitle("paypal");
        paypalPaymentMethod.setDetails("pay with paypal service");

        paymentMethodService.save(paypalPaymentMethod);

        short paymentMethodId = paymentMethodService.findByTitle(paypalPaymentMethod.getTitle()).getId();

        paymentMethodService.deleteById(paymentMethodId);

        Assertions.assertFalse(paymentMethodService.existsByTitle(paypalPaymentMethod.getTitle()));
    }

    @DisplayName("Test that delete payment method throws error when didn't found any item")
    @Test
    public void deletedById_dataNoPresent_returnErrorMessage() {
        EntityExistsException exception = Assertions.assertThrows(EntityExistsException.class,
                () -> paymentMethodService.deleteById((short) 1));

        Assertions.assertEquals("Payment method with id - 1 doesn't exist!", exception.getMessage());
    }
}
