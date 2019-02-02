import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RoomService {

  constructor(private http: HttpClient) { }

  getRoom(id: number){
    return this.http.get('/rooms/' + id);
  }

  addRoom(room: any){
    return this.http.post('/rooms', room);
  }

  updateRoom(room: any){
    return this.http.put('/rooms', room);
  }

  deleteRoom(id: number){
    return this.http.delete('/rooms/' + id);
  }

  getRoomPrices(){
    return this.http.get('/rooms/roomPrices');
  }

  addRoomPrice(roomPrices: any){
    return this.http.post('/rooms/addRoomPrice', roomPrices);
  }

  updateRoomPrices(roomPrices: any){
    return this.http.put('/rooms/updateRoomPrices', roomPrices);
  }
}
