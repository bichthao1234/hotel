package com.my.hotel.dto.request;

import com.my.hotel.utils.Utilities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConvertReservationRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer reservationId;
    private String employeeId;
    private Date arrivalDate;
    private List<OrderedRoom> orderedRooms;
    private String orderedRoomsString;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderedRoom implements Serializable {
        private static final long serialVersionUID = 1L;
        private Integer roomClassId;
        private String roomId;
        private List<String> customerIdList;
        private String customerIdsString;

        public void formatForSql() {
            this.customerIdsString = Utilities.listToCsv(this.customerIdList);
            this.customerIdList = null;
        }
    }


}
