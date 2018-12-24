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
