package com.my.hotel.service;

import com.my.hotel.dto.request.CreateInvoiceRequestDto;

import javax.transaction.Transactional;

public interface InvoiceService {


    @Transactional
    Integer createInvoiceByRentalSlipDetail(CreateInvoiceRequestDto requestDto) throws Throwable;
}
