import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RentACarService {

  constructor(private http: HttpClient) { }

  getRentACarById(id: number) {
    return this.http.get('/rent-a-cars/' + id);
  }

  getRentACars() {
    return this.http.get('/rent-a-cars');
  }

  addRentACar(rentACar: any) {
    return this.http.post('/rent-a-cars', rentACar);
  }

  updateRentACar(rentACar: any) {
    return this.http.put('/rent-a-cars', rentACar);
  }

  getVehicles(id: number) {
    return this.http.get('/rent-a-cars/' + id + '/vehicles');
  }
}
