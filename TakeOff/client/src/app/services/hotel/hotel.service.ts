import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class HotelService {

  constructor(private http: HttpClient) { }

  getHotelById(id: number){
    return this.http.get('/hotels/' + id);
  }

  getHotels() {
    return this.http.get('/hotels/all');
  }
  
  addHotel(hotel: {}){
    return this.http.post('/hotels',hotel);
  }

  updateHotel(hotel: {}){
    return this.http.put('/hotels',hotel);
  }

  deleteHotel(hotel: {}){
    return this.http.delete('/hotels',hotel);
  }

  getRooms(id: number){
    return this.http.get('/hotels/' + id + '/rooms');
  }


}
