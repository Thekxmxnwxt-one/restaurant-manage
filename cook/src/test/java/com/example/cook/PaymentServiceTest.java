package com.example.cook;

import com.example.cook.enums.PaymentMethod;
import com.example.cook.exception.NotFoundException;
import com.example.cook.model.PaymentModel;
import com.example.cook.repository.PaymentRepository;
import com.example.cook.service.PaymentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    public void test_get_all_payment_expect_success() {
        when(paymentRepository.findAllPayments()).thenReturn(List.of(new PaymentModel()));

        List<PaymentModel> result = paymentService.getAllPayments();

        Assertions.assertTrue(result.size()>0);
        verify(paymentRepository).findAllPayments();
    }

    @Test
    public void test_get_all_payment_expect_runtime_exception() {
        when(paymentRepository.findAllPayments()).thenThrow(new RuntimeException("DB connection failed"));

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            paymentService.getAllPayments();
        });

        Assertions.assertTrue(exception.getMessage().contains("Failed to get all payments"));
        verify(paymentRepository).findAllPayments();
    }

    @Test
    public void test_get_payment_by_id_expect_success() {
        PaymentModel mockPayment = new PaymentModel();
        mockPayment.setId(1);

        when(paymentRepository.findPaymentById(1)).thenReturn(mockPayment);

        PaymentModel result = paymentService.getPaymentById(1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getId());
        verify(paymentRepository).findPaymentById(1);
    }

    @Test
    public void test_get_payment_by_id_expect_not_found() {
        when(paymentRepository.findPaymentById(1)).thenReturn(null);

        NotFoundException ex = Assertions.assertThrows(NotFoundException.class,
                () -> paymentService.getPaymentById(1));

        Assertions.assertTrue(ex.getMessage().contains("Payment with id " + 1 + " not found."));
        verify(paymentRepository).findPaymentById(1);
    }

    @Test
    public void test_get_payment_by_id_expect_runtime_exception() {
        int paymentId = 1;

        when(paymentRepository.findPaymentById(paymentId)).thenThrow(new RuntimeException("DB connection failed"));

        RuntimeException ex = Assertions.assertThrows(RuntimeException.class,
                () -> paymentService.getPaymentById(paymentId));

        Assertions.assertTrue(ex.getMessage().contains("Failed to get payment by id"));
        verify(paymentRepository).findPaymentById(paymentId);
    }

    @Test
    public void test_get_payment_id_detail_except_success() {
        PaymentModel mockPayment = new PaymentModel();
        mockPayment.setId(1);

        when(paymentRepository.findPaymentByIdDetail(1)).thenReturn(mockPayment);

        PaymentModel result = paymentService.getPaymentByIdDetail(1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getId());
        verify(paymentRepository).findPaymentByIdDetail(1);
    }

    @Test
    public void test_get_payment_by_id_detail_expect_runtime_exception() {
        int paymentId = 1;

        when(paymentRepository.findPaymentByIdDetail(paymentId)).thenThrow(new RuntimeException("DB connection failed"));

        RuntimeException ex = Assertions.assertThrows(RuntimeException.class,
                () -> paymentService.getPaymentByIdDetail(paymentId));

        Assertions.assertTrue(ex.getMessage().contains("Failed to get payment detail by id:"));
        verify(paymentRepository).findPaymentByIdDetail(paymentId);
    }

    @Test
    public void test_insert_payment_expect_success() {

        PaymentModel paymentModels = new PaymentModel();
        paymentModels.setOrderId(1);
        paymentModels.setAmount(BigDecimal.valueOf(30.00));
        paymentModels.setMethod(PaymentMethod.cash);

        when(paymentRepository.insertPayment(paymentModels)).thenReturn(1);

        int result = paymentService.insertPayment(paymentModels);

        Assertions.assertEquals(1, result);
        verify(paymentRepository).insertPayment(paymentModels);
    }

    @Test
    public void test_insert_payment_expect_runtime_exception() {
        PaymentModel paymentModels = new PaymentModel();
        paymentModels.setOrderId(1);
        paymentModels.setAmount(BigDecimal.valueOf(30.00));
        paymentModels.setMethod(PaymentMethod.cash);

        when(paymentRepository.insertPayment(paymentModels))
                .thenThrow(new RuntimeException("DB connection failed"));

        RuntimeException ex = Assertions.assertThrows(RuntimeException.class, () -> {
            paymentService.insertPayment(paymentModels);
        });

        Assertions.assertTrue(ex.getMessage().contains("Failed to insert payment"));
        verify(paymentRepository).insertPayment(paymentModels);
    }
}
