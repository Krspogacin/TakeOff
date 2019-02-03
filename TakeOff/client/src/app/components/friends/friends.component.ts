import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material';
import { UserService } from 'src/app/services/user/user.service';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { Router } from '@angular/router';
import { AppComponent } from 'src/app/app.component';

@Component({
  selector: 'app-friends',
  templateUrl: './friends.component.html',
  styleUrls: ['./friends.component.css']
})
export class FriendsComponent implements OnInit {

  user: any;
  allUsers = [];
  acceptedfriends = [];
  pendingFriends = [];
  pageSize = 5;
  pageIndex = 0;
  loading = true;
  hasFriends = false;

  constructor(private userService: UserService, private authService: AuthenticationService,
    private snackBar: MatSnackBar, private router: Router, private appComponent: AppComponent) { }

  ngOnInit() {
    const username = this.authService.getUsername();
    if (username) {

      this.userService.getAllUsers().subscribe(
        (data: []) => {
          this.allUsers = data;
        }
      );

      this.userService.getUser(username).subscribe(
        (data: any) => {
          this.user = data;

          this.userService.getFriends(username).subscribe(
            (friendData: []) => {
              // extracting only friend objects
              const friends = [];
              friendData.forEach((element: any) => {
                const f = {
                  'friend': null,
                  'accepted': element.accepted
                };
                if (element.user1.id === this.user.id) {
                  f.friend = element.user2;
                } else {
                  f.friend = element.user1;
                }
                friends.push(f);
              });

              this.acceptedfriends = friends.filter((x: any) => x.accepted);
              this.pendingFriends = friends.filter((x: any) => !x.accepted);
              this.hasFriends = this.acceptedfriends.length > 0 ? true : false;
              this.loading = false;
            }
          );
        },
        () => {
          this.loading = false;
        }
      );

    } else {
      this.router.navigate(['/']);
    }
  }


  sendFriendRequest(user2: any) {
    const friend = {
      'user1': this.user,
      'user2': user2,
      'accepted': false
    };
    this.userService.sendFriendRequest(friend).subscribe(
      (data) => {
        this.acceptedfriends.push({ 'friend': data, 'accepted': false });
        this.appComponent.showSnackBar('Request successfully sent!');
      },
      () => {
        this.appComponent.showSnackBar('Error while sending request!');
      }
    );
  }

  acceptRequest(friend: any) {
    this.userService.acceptFriendRequest(friend).subscribe(
      () => {
        friend.accepted = true;
        this.appComponent.showSnackBar('You are now friends with \'' + friend.user1.firstName + ' ' + friend.user1.lastName + '\'!');
      },
      () => {
        this.appComponent.showSnackBar('Error while accepting request!');
      }
    );
  }

  deleteRequest(friend: any) {
    this.userService.deleteFriendRequest(friend).subscribe(
      () => {
        const sender = this.user.id === friend.user1.id;
        const name = sender ? friend.user2.firstName + ' ' + friend.user2.lastName
          : friend.user1.firstName + ' ' + friend.user1.lastName;

        let message;
        if (friend.accepted) {
          const index = this.acceptedfriends.indexOf(friend);
          if (index >= 0) {
            this.acceptedfriends.splice(index, 1);
          }
          message = 'You are no longer friends with \'' + name + '\'!';
        } else {
          const index = this.pendingFriends.indexOf(friend);
          if (index >= 0) {
            this.pendingFriends.splice(index, 1);
          }
          message = 'Request ' + (sender ? 'to \'' : 'from \'') + name + '\' canceled!';
        }
        this.appComponent.showSnackBar(message);
      },
      () => {
        this.appComponent.showSnackBar('Error while canceling request!');
      }
    );
  }


}
