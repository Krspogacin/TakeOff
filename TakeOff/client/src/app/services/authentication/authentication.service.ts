import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  userKey = 'authUser';

  constructor(private http: HttpClient) { }

  setUserState(userState: string) {
    localStorage.setItem(this.userKey, JSON.stringify(userState));
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

  getAuthority(): string {
    if (!JSON.parse(localStorage.getItem(this.userKey))) {
      return null;
    }
    return JSON.parse(localStorage.getItem(this.userKey)).authority;
  }

  login(authRequest: any) {
    return this.http.post('/login', authRequest);
  }

  logout() {
    return this.http.get('/logout_user');
  }
}
