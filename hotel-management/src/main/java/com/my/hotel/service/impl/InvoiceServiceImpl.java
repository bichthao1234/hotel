package com.my.hotel.service.impl;

import com.my.hotel.dto.request.CreateInvoiceRequestDto;
import com.my.hotel.entity.Invoice;
import com.my.hotel.entity.RentalSlip;
import com.my.hotel.entity.RentalSlipDetail;
import com.my.hotel.repo.EmployeeRepo;
import com.my.hotel.repo.InvoiceRepo;
import com.my.hotel.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepo invoiceRepo;

    @Autowired
    private EmployeeRepo employeeRepo;

    @Override
    @Transactional
    public Integer createInvoiceByRentalSlipDetail(CreateInvoiceRequestDto requestDto) throws Throwable {
        try {
            List<RentalSlipDetail> rentalSlipDetailById = invoiceRepo.getRentalSlipDetailById(requestDto.getRentalSlipDetailIdList());
            RentalSlip rentalSlip = rentalSlipDetailById.get(0).getRentalSlip();
            Invoice invoice = new Invoice();
            invoice.setRentalSlip(rentalSlip);
            invoice.setRentalSlipDetails(rentalSlipDetailById);
            for (RentalSlipDetail rentalSlipDetail : rentalSlipDetailById) {
                rentalSlipDetail.setInvoice(invoice);
            }
            invoice.setCreatedBy(employeeRepo.findById(requestDto.getEmployeeId()).orElseThrow());
            invoice.setCreatedDate(new Date());
            Invoice savedInvoice = invoiceRepo.save(invoice);
            return savedInvoice.getId();
        } catch (Exception e) {
            if (e.getCause() != null && e.getCause().getCause() != null) {
                throw e.getCause().getCause();
            }
            throw e;
        }
    }
}
