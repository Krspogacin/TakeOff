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

  addVehiclePrices(vehiclePrices: []) {
    return this.http.post('/vehicles/prices/', vehiclePrices);
  }

  updateVehiclePrices(vehiclePrices: []) {
    return this.http.put('/vehicles/prices/', vehiclePrices);
  }

  getFuelTypes() {
    return this.http.get('/vehicles/fuelTypes');
  }

  getTransmissionTypes() {
    return this.http.get('/vehicles/transmissionTypes');
  }
}
