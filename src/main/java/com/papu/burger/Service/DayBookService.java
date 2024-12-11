package com.papu.burger.Service;

import com.papu.burger.Model.DayBook;
import com.papu.burger.Model.Sell;
import com.papu.burger.Repository.DayBookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class DayBookService {
    private final DayBookRepository dayBookRepository;

    public void updateDayBook(List<Sell> sells, String paymentType) {
        if (sells == null || sells.isEmpty() || paymentType == null || paymentType.isEmpty()) {
                throw new IllegalArgumentException("Sells and paymentType must not be null or empty");
        }
        Date now = new Date();
        for (Sell sell : sells) {
                
            
            double totalAmount = sell.getPrice() * sell.getQty();
            double cmvAmount = totalAmount / 3;
            


            // Entry for payment type (CAJA, MERCADO PAGO, BINANCE)
            DayBook paymentEntry = DayBook.builder()
                    .description(paymentType.toUpperCase())
                    .amount(totalAmount)
                    .type(paymentType)
                    .entryType("DEBE")
                    .reference(sell)
                    .createdAt(now)
                    .build();
            dayBookRepository.save(paymentEntry);


            // Entry for VENTAS
            DayBook ventasEntry = DayBook.builder()
                    .description("GANANCIAS")
                    .amount(totalAmount - cmvAmount)
                    .type(paymentType)
                    .entryType("HABER")
                    .reference(sell)
                    .createdAt(now)
                    .build();
            dayBookRepository.save(ventasEntry);

            
            // Entry for MERCADERIAS
            DayBook merchandiseEntry = DayBook.builder()
                    .description("MERCADERIAS")
                    .amount(cmvAmount)
                    .type(paymentType)
                    .entryType("HABER")
                    .reference(sell)
                    .createdAt(now)
                    .build();
            dayBookRepository.save(merchandiseEntry);
        }
    }
}
