import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getUser(username: string) {
    return this.http.get('/users/' + username);
  }

  getFriends(username: string) {
    return this.http.get('/users/' + username + '/friends');
  }
}
