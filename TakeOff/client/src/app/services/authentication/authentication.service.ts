import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  userKey = 'authUser';

  constructor(private http: HttpClient) { }

  setUserState(user: string) {
    localStorage.setItem(this.userKey, JSON.stringify(user));
  }

  removeUserState() {
    localStorage.removeItem(this.userKey);
  }

  getAccessToken(): string {
    if (!JSON.parse(localStorage.getItem(this.userKey))) {
      return null;
    }
    return JSON.parse(localStorage.getItem(this.userKey)).accessToken;
  }

  getUsername(): string {
    if (!JSON.parse(localStorage.getItem(this.userKey))) {
      return null;
    }
    return JSON.parse(localStorage.getItem(this.userKey)).username;
  }

  isUserEnabled(): string {
    if (!JSON.parse(localStorage.getItem(this.userKey))) {
      return null;
    }
    return JSON.parse(localStorage.getItem(this.userKey)).isEnabled;
  }

  getAuthorities(): string {
    if (!JSON.parse(localStorage.getItem(this.userKey))) {
      return null;
    }
    return JSON.parse(localStorage.getItem(this.userKey)).authorities;
  }

  login(authRequest: any) {
    return this.http.post('/login', authRequest);
  }

  logout() {
    return this.http.get('/logout_user');
  }
}
