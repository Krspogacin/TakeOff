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

  getAllUsers() {
    return this.http.get('/users');
  }

  updateUser(user: any) {
    return this.http.put('/users', user);
  }

  getFriends(username: string) {
    return this.http.get('/users/' + username + '/friends');
  }

  sendFriendRequest(friend: any) {
    return this.http.post('/users/friends', friend);
  }

  acceptFriendRequest(friend: any) {
    return this.http.put('/users/friends', friend);
  }

  deleteFriendRequest(friend: any) {
    return this.http.put('/users/friends/delete', friend);
  }
}
