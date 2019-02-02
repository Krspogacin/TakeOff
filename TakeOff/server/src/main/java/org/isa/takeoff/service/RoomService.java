package org.isa.takeoff.service;

import java.util.List;

import org.isa.takeoff.model.Room;
import org.isa.takeoff.model.RoomPrice;
import org.isa.takeoff.repository.RoomPriceRespository;
import org.isa.takeoff.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class RoomService {
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private RoomPriceRespository roomPriceRepository;
	
	public Room findOne(Long id){
		return roomRepository.getOne(id);
	}
	
	public List<Room> findAll(){
		return roomRepository.findAll();
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Room save(Room room){
		return roomRepository.save(room);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void delete(Long id){
		roomRepository.deleteById(id);
	}
	
	public RoomPrice findOneRoomPrice(Long id){
		return roomPriceRepository.getOne(id);
	}
	
	public List<RoomPrice> findAllRoomPrices(){
		return roomPriceRepository.findAll();
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public RoomPrice save(RoomPrice roomPrice){
		return roomPriceRepository.save(roomPrice);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteRoomPrice(Long id){
		roomPriceRepository.deleteById(id);
	}
}
