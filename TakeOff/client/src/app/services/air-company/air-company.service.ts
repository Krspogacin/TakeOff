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

  getCompanies() {
    return this.http.get('/companies');
  }
}
