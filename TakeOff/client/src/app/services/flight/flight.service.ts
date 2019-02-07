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

  setFlightDestinations(id: number, destinations: any) {
    return this.http.put('/flights/' + id + '/destinations', destinations);
  }

  getFlightTickets(id: number) {
    return this.http.get('/flights/' + id + '/tickets');
  }

  updateFlightTickets(tickets: any) {
    return this.http.put('/flights/tickets', tickets);
  }

  updateFlightDiagram(id: number, diagram: any) {
    return this.http.put('/flights/' + id + '/diagram', diagram);
  }

  addFlight(flight: any) {
    return this.http.post('/flights', flight);
  }

  updateFlight(flight: any) {
    return this.http.put('/flights', flight);
  }
}
