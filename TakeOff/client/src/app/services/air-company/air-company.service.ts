import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AirCompanyService {

  constructor(private http: HttpClient) { }

  getCompanyById(id: number) {
    return this.http.get('/companies/' + id);
  }

  getCompanyDestinations(id: number) {
    return this.http.get('/companies/' + id + '/destinations');
  }

  setCompanyDestinations(id, destinations) {
    return this.http.put('/companies/' + id + '/destinations', destinations);
  }

  getAllDestinations() {
    return this.http.get('companies/destinations');
  }

  getCompanyFlights(id: number) {
    return this.http.get('/companies/' + id + '/flights');
  }

  getCompanies() {
    return this.http.get('/companies');
  }

  updateCompany(company) {
    return this.http.put('/companies', company);
  }
}
