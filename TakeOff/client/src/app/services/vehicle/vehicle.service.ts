import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class VehicleService {

  constructor(private http: HttpClient) { }

  getVehicle(id: number) {
    return this.http.get('/vehicles/' + id);
  }

  addVehicle(vehicle: any) {
    return this.http.post('/vehicles', vehicle);
  }

  updateVehicle(vehicle: any) {
    return this.http.put('/vehicles', vehicle);
  }

  deleteVehicle(id: number) {
    return this.http.delete('/vehicles/' + id);
  }
}
