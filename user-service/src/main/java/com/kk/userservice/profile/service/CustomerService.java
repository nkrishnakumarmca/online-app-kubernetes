package com.kk.userservice.profile.service;

import com.kk.userservice.errorhandling.exception.CustomerExistsException;
import com.kk.userservice.errorhandling.exception.CustomerNotFoundException;
import com.kk.userservice.profile.model.Customer;
import com.kk.userservice.profile.model.Order;

public interface CustomerService {

    Customer addNewCustomer(Customer customer, String password) throws CustomerExistsException;

    Customer updateCustomerOrder(Order order) throws CustomerNotFoundException;

    Customer getCustomerByCustomerId(String customerId) throws CustomerNotFoundException;
}
