import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class FlightService {

  constructor(private http: HttpClient) { }

  getFlightById(id: number) {
    return this.http.get('/flights/' + id);
  }

  getFlightDestinations(id: number) {
    return this.http.get('/flights/' + id + '/destinations');
  }

  setFlightDestinations(id, destinations) {
    return this.http.put('/flights/' + id + '/destinations', destinations);
  }

  getFlightTickets(id: number) {
    return this.http.get('/flights/' + id + '/tickets');
  }

  addFlight(flight) {
    return this.http.post('/flights', flight);
  }

  updateFlight(flight) {
    return this.http.put('/flights', flight);
  }
}
