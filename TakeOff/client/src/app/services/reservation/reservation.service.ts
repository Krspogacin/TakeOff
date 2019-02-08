import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {

  reservationsSubject = new Subject<any>();

  constructor(private http: HttpClient) { }

  createReservations(reservations: any) {
    return this.http.post('/reservations', reservations);
  }

  getReservations(username: string) {
    return this.http.get('/reservations/' + username);
  }

  createVehicleReservation(vehicleReservation: any) {
    return this.http.post('/reservations/vehicleReservations', vehicleReservation);
  }

  createRoomReservation(roomReservation: any) {
    return this.http.post('/reservations/roomReservations', roomReservation);
  }

  getNumberOfUsers(id: number){
    return this.http.get('/reservations/getNumberOfUsers/' + id);
  }
  
  rateAirCompany(userRating: any) {
    return this.http.post('/companies/rateAirCompany', userRating);
  }

  rateFlight(userRating: any) {
    return this.http.post('/flights/rateFlight', userRating);
  }

  rateRentACar(userRating: any) {
    return this.http.post('/rent-a-cars/rateRentACar', userRating);
  }

  rateVehicle(userRating: any) {
    return this.http.post('/vehicles/rateVehicle', userRating);
  }

  rateHotel(userRating: any) {
    return this.http.post('/hotels/rateHotel', userRating);
  }

  rateRoom(userRating: any) {
    return this.http.post('/rooms/rateRoom', userRating);
  }
}
