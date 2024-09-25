package com.ecom.client.service;

import com.ecom.client.repository.AddressRepository;
import com.ecom.common.entity.Address;
import com.ecom.common.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    public List<Address> findAddressByCustomer(Customer customer) {
        return addressRepository.findByCustomer(customer);
    }

    public void saveAddress(Address address, Customer customer) {
        List<Address> addressList = findAddressByCustomer(customer);
        if(addressList.isEmpty()) {
            address.setAddressDefault(true);
        }
        addressRepository.save(address);
    }

    public Address findAddressByIdAndCustomer(int id, Customer customer) {
        return addressRepository.findByIdAndCustomer(id, customer);
    }

    public void updateDefaultAddress(Integer addressId, Integer customerId){
        addressRepository.updateDefaultAddress(addressId, customerId);
        addressRepository.updateDefaultOtherAddress(addressId, customerId);
    }

    public Address getCustomerDefaultAddress(Customer customer) {
        int customerId = customer.getId();
        return addressRepository.getAddressDefault(customerId);
    }

    public void deleteAddress(Integer addressId, Integer customerId){
        addressRepository.deleteAddress(addressId, customerId);
    }
}
