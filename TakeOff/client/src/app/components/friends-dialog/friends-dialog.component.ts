import { Component, OnInit, Inject } from '@angular/core';
import { UserService } from 'src/app/services/user/user.service';
import { MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-friends-dialog',
  templateUrl: './friends-dialog.component.html',
  styleUrls: ['./friends-dialog.component.css']
})
export class FriendsDialogComponent implements OnInit {

  user: any;
  friends = [];
  allUsers = [];
  loading = true;
  addedFriends = [];

  constructor(private userService: UserService, @Inject(MAT_DIALOG_DATA) private data: any) { }

  ngOnInit() {
    this.user = this.data.user;
    this.friends = this.data.friends;
    this.userService.getAllUsers().subscribe(
      (users: []) => {
        this.allUsers = users;
        this.allUsers = this.allUsers.filter(u => u.id !== this.user.id);
        this.friends.forEach(element => {
          this.allUsers = this.allUsers.filter(u => u.id !== element.id);
        });
        this.loading = false;
      },
      () => {
        this.loading = false;
      }
    );
  }

  sendFriendRequest(user: any) {
    setTimeout(() => {
      this.addedFriends.push(user);
    }, 200);
  }

  cancelFriendRequest(user: any) {
    setTimeout(() => {
      this.addedFriends.splice(this.addedFriends.indexOf(user), 1);
    }, 200);
  }
}
