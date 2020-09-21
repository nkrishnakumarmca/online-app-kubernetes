package com.kk.userservice.profile.commander.service;

import com.kk.userservice.authentication.service.AuthenticationServiceImpl;
import com.kk.userservice.errorhandling.exception.CustomerExistsException;
import com.kk.userservice.errorhandling.exception.CustomerNotFoundException;
import com.kk.userservice.profile.model.Address;
import com.kk.userservice.profile.model.CartProduct;
import com.kk.userservice.profile.model.Customer;
import com.kk.userservice.profile.model.Order;
import com.kk.userservice.profile.repository.CustomerRepository;
import com.kk.userservice.profile.service.CustomerServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTests {

    public static final String EMAIL = "test@test.com";
    public static final String CUSTOMER_ID = "c1";
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AuthenticationServiceImpl authenticationService;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private Optional<Customer> emptyOptional;
    private Order order;

    @BeforeEach
    public void setUp() {
        Address address = new Address("100", "street", "area", "city", "state", 10101, "name", 9999911111l);
        List<CartProduct> products = List.of(new CartProduct("1", "Carrot", 10, 10));
        order = new Order("o1", CUSTOMER_ID, products, LocalDate.now(), address, address, "creditcard", 100, "completed");
        emptyOptional = Optional.empty();
        customer = new Customer(CUSTOMER_ID, "firstname", "lastname", EMAIL, address, new ArrayList<>());
    }

    @AfterEach
    public void tearDown() {
        customer = null;
        order = null;
        emptyOptional = null;
    }

    @Test
    public void givenCustomerEntityWhenAddedThenReturnAddedCustomerObject() throws CustomerExistsException {
        when(customerRepository.findByEmail(EMAIL))
                .thenReturn(emptyOptional);
        when(customerRepository.save(customer))
                .thenReturn(customer);
        Customer addedCustomer = customerService.addNewCustomer(this.customer, "password");
        assertThat(addedCustomer).isEqualTo(customer);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    public void givenCustomerEntityWhenAlreadyExistsThenThrowException() throws CustomerExistsException {
        when(customerRepository.findByEmail(EMAIL))
                .thenReturn(Optional.of(customer));
        assertThatThrownBy(() -> customerService.addNewCustomer(customer, "password")).isInstanceOf(CustomerExistsException.class);
        verify(customerRepository, times(1)).findByEmail(EMAIL);
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    public void givenCustomerEntityWhenUpdatedThenReturnUpdatedCustomerObject() throws CustomerExistsException, CustomerNotFoundException {
        when(customerRepository.findById(CUSTOMER_ID))
                .thenReturn(Optional.of(customer));
        when(customerRepository.save(customer))
                .thenReturn(customer);
        Customer customerOrder = customerService.updateCustomerOrder(order);
        customer.setOrders(List.of(order));
        assertThat(customerOrder).isEqualTo(this.customer);
        verify(customerRepository, times(1)).findById(CUSTOMER_ID);
        verify(customerRepository, times(1)).save(this.customer);
    }

    @Test
    public void givenCustomerIdWhenFoundThenReturnCustomerObject() throws CustomerNotFoundException {
        when(customerRepository.findById(CUSTOMER_ID))
                .thenReturn(Optional.of(customer));
        Customer customerFound = customerService.getCustomerByCustomerId(CUSTOMER_ID);
        assertThat(customerFound).isEqualTo(customer);
        verify(customerRepository, times(1)).findById(CUSTOMER_ID);
    }

    @Test
    public void givenCustomerIdWhenNotFoundThenThrowException() throws CustomerNotFoundException {
        when(customerRepository.findById(CUSTOMER_ID))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> customerService.getCustomerByCustomerId(CUSTOMER_ID))
                .isInstanceOf(CustomerNotFoundException.class);
        verify(customerRepository, times(1)).findById(CUSTOMER_ID);
    }
}