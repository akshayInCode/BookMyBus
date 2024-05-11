package com.redBus.operator.service;

import com.redBus.operator.payload.BusOperatorDto;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface BusOperatorService {

     BusOperatorDto scheduleBus(@RequestBody BusOperatorDto busOperatorDto);
     List<BusOperatorDto> getBusDetailsByCompanyName(String busOperatorCompanyName, int pageNo, int pageSize, String sortBy, String sortDir);
     void deleteBusOperator(String busId);
    BusOperatorDto updateBusOperator(String busId, BusOperatorDto busOperatorDto);
}

