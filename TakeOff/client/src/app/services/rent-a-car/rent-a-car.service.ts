import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RentACarService {

  constructor(private http: HttpClient) { }

  checkMainServiceName(id: number, name: string) {
    if (isNaN(id)) {
      id = null;
    }
    return this.http.get('/rent-a-cars/checkMainServiceName/' + + id + '/' + name);
  }

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

  getVehiclesOnDiscount(id: number) {
    return this.http.get('/rent-a-cars/' + id + '/vehiclesOnDiscount');
  }

  getAvailableVehicles(parameters: any) {
    return this.http.get('/rent-a-cars/vehicles/availableVehicles?parametersDTO=' + encodeURI(JSON.stringify(parameters)));
  }

  getRentACarRatings() {
    return this.http.get('/rent-a-cars/ratings');
  }

  getRentACarRating(id: number) {
    return this.http.get('/rent-a-cars/' + id + '/rating');
  }

  getVehiclesRatings(id: number) {
    return this.http.get('rent-a-cars/' + id + '/vehicles/ratings');
  }

  addMainService(mainService: any) {
    return this.http.post('/rent-a-cars/mainServices', mainService);
  }

  updateMainService(mainService: any) {
    return this.http.put('/rent-a-cars/mainServices', mainService);
  }

  deleteMainService(id: number) {
    return this.http.delete('/rent-a-cars/mainServices/' + id);
  }

  getMainServices(id: number) {
    return this.http.get('rent-a-cars/' + id + '/mainServices');
  }

  getMainServicesPrices(id: number) {
    return this.http.get('rent-a-cars/' + id + '/mainServicesPrices');
  }

  addOffice(office: any) {
    return this.http.post('/rent-a-cars/offices', office);
  }

  updateOffice(office: any) {
    return this.http.put('/rent-a-cars/offices', office);
  }

  deleteOffice(id: number) {
    return this.http.delete('/rent-a-cars/offices/' + id);
  }

  getOffices(id: number) {
    return this.http.get('rent-a-cars/' + id + '/offices');
  }
}
