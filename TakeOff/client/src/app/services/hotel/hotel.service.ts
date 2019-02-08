import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { jsonpCallbackContext } from '@angular/common/http/src/module';

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

  getHotelRating(id: number){
    return this.http.get('/hotels/' + id + '/getHotelRating');
  }

  getRoomRatings(id: number){
    return this.http.get('/hotels/' + id + '/getRoomRatings')
  }

  getHotelRatings(){
    return this.http.get('/hotels/getHotelRatings')
  }

  getServices(){
    return this.http.get('/hotels/allServices');
  }

  addHotelServices(services: any){
    return this.http.put('/hotels/saveHotelServices', services);
  }

  getHotelServices(id: number){
    return this.http.get('/hotels/' + id + '/getHotelServices');
  }

  getAvailableRooms(object: any){
    return this.http.get('/hotels/getAvailableRooms?parameters='+ encodeURI(JSON.stringify(object)));
  }

  roomsNotOnDiscount(id: number) {
    return this.http.get('/hotels/' + id + '/roomsNotOnDiscount');
  }

  getRoomsOnDiscount(object: any) {
    return this.http.get('/hotels/roomsOnDiscount?parameters='+ encodeURI(JSON.stringify(object)));
  }
}
