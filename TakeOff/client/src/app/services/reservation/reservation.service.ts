import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {

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
}
