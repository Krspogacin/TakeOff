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

  addCompany(company) {
    return this.http.post('/companies', company);
  }

  updateCompany(company) {
    return this.http.put('/companies', company);
  }

  getCompaniesRatings() {
    return this.http.get('/companies/ratings');
  }

  getCompanyRating(id: number) {
    return this.http.get('/companies/' + id + '/rating');
  }

  searchFlights(searchObject) {
    return this.http.get('/companies/flights?parameters=' + encodeURI(JSON.stringify(searchObject)));
  }
}
