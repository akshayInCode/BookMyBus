package com.redBus.operator.service;

import com.redBus.operator.entity.BusOperator;
import com.redBus.operator.entity.TicketCost;
import com.redBus.operator.exception.ResourceNotFound;
import com.redBus.operator.payload.BusOperatorDto;
import com.redBus.operator.repository.BusOperatorRepository;
import com.redBus.operator.repository.TicketCostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BusOperatorServiceImpl implements BusOperatorService {

    private final BusOperatorRepository busOperatorRepository;
    private final ModelMapper modelMapper;
    private final TicketCostRepository ticketCostRepository;

    public BusOperatorServiceImpl(BusOperatorRepository busOperatorRepository, TicketCostRepository ticketCostRepository,
                                  ModelMapper modelMapper) {
        this.busOperatorRepository = busOperatorRepository;
        this.ticketCostRepository=ticketCostRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public BusOperatorDto scheduleBus(BusOperatorDto busOperatorDto) {
        BusOperator busOperator = mapToEntity(busOperatorDto);

        //Create a new TicketCost  entity and set its properties
        TicketCost ticketCost= new TicketCost();
        ticketCost.setTicketId(busOperatorDto.getTicketCost().getTicketId());
        ticketCost.setCost(busOperatorDto.getTicketCost().getCost());
        ticketCost.setCode(busOperatorDto.getTicketCost().getCode());
        ticketCost.setDiscountAmount(busOperatorDto.getTicketCost().getDiscountAmount());

        // Calculate the final price after deducting the discount amount
        double price = busOperatorDto.getTicketCost().getCost() - busOperatorDto.getTicketCost().getDiscountAmount();
        ticketCost.setPrice(price); // Set the final price in the TicketCost entity


        //Set the TicketCost entity in the BusOperator entity
        busOperator.setTicketCost(ticketCost);

        String busId = UUID.randomUUID().toString();  // generate unique busId
        busOperator.setBusId(busId);
        BusOperator savedBusSchedule = busOperatorRepository.save(busOperator);
        return mapToDto(savedBusSchedule);
    }
    @Override
    public List<BusOperatorDto> getBusDetailsByCompanyName(String busOperatorCompanyName, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort.Direction direction = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<BusOperator> busOperatorPage = busOperatorRepository.findByBusOperatorCompanyName(busOperatorCompanyName, pageable);
        List<BusOperatorDto> busOperatorDtoList = busOperatorPage.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return busOperatorDtoList;
    }
    @Override
    public void deleteBusOperator(String busId) {
        busOperatorRepository.findById(busId).orElseThrow(
                ()-> new ResourceNotFound("Busoperator not found with BusId:"+busId)
        );
        busOperatorRepository.deleteById(busId);
    }

    @Override
    public BusOperatorDto updateBusOperator(String busId, BusOperatorDto busOperatorDto) {
        BusOperator busOperator= busOperatorRepository.findById(busId).orElseThrow(
                ()->new ResourceNotFound("BusOperator not found with BusId:"+busId)
        );
        busOperator.setTicketCost(busOperatorDto.getTicketCost());
        busOperator.setBusNumber(busOperatorDto.getBusNumber());
        busOperator.setBusOperatorCompanyName(busOperatorDto.getBusOperatorCompanyName());
        busOperator.setDriverName(busOperatorDto.getDriverName());
        busOperator.setSupportStaff(busOperatorDto.getSupportStaff());
        busOperator.setNumberSeats(busOperatorDto.getNumberSeats());
        busOperator.setDepartureCity(busOperatorDto.getDepartureCity());
        busOperator.setArrivalCity(busOperatorDto.getArrivalCity());
        busOperator.setDepartureTime(busOperatorDto.getDepartureTime());
        busOperator.setArrivalTime(busOperatorDto.getArrivalTime());
        busOperator.setDepartureDate(busOperatorDto.getDepartureDate());
        busOperator.setArrivalDate(busOperatorDto.getArrivalDate());
        busOperator.setTotalTravelTime(busOperatorDto.getTotalTravelTime());
        busOperator.setBusType(busOperatorDto.getBusType());
        busOperator.setAmenities(busOperatorDto.getAmenities());
        busOperator.setBusId(busId);
        BusOperator savedBusSchedule = busOperatorRepository.save(busOperator);
        return mapToDto(savedBusSchedule);

    }
    private BusOperator mapToEntity(BusOperatorDto busOperatorDto) {
        return modelMapper.map(busOperatorDto, BusOperator.class);
    }
    private BusOperatorDto mapToDto(BusOperator busOperator) {
        return modelMapper.map(busOperator, BusOperatorDto.class);
    }
}
