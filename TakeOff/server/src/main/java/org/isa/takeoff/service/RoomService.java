package org.isa.takeoff.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.isa.takeoff.model.Room;
import org.isa.takeoff.model.RoomPrice;
import org.isa.takeoff.model.RoomRating;
import org.isa.takeoff.model.RoomRatingId;
import org.isa.takeoff.repository.RoomPriceRespository;
import org.isa.takeoff.repository.RoomRatingRepository;
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
	
	@Autowired
	private RoomRatingRepository roomRatingRepository;
	
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
	
	public RoomRating findOneRating(RoomRatingId id){
		RoomRating roomRating;
		try {
			roomRating = roomRatingRepository.findById(id).get();
			return roomRating;
		} catch(NoSuchElementException noSuchElementException) {
			return null;
		}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public RoomRating saveRating(RoomRating roomRating) {
		return roomRatingRepository.save(roomRating);
	}
}