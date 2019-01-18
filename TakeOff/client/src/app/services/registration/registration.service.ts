import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  constructor(private http: HttpClient) { }

  checkUserByUsername(username: string) {
    return this.http.get('/users/checkUsername/' + username);
  }

  checkUserByEmail(email: string) {
    return this.http.get('/users/checkEmail/' + email);
  }

  registerUser(user: any) {
    return this.http.post('/users/register', user);
  }

  verifyUser(token: string) {
    return this.http.get('/users/register/verify?token=' + token);
  }
}
