import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material';
import { Router } from '@angular/router';
import { AppComponent } from 'src/app/app.component';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { UserService } from 'src/app/services/user/user.service';
import { FriendsDialogComponent } from '../friends-dialog/friends-dialog.component';

@Component({
  selector: 'app-friends',
  templateUrl: './friends.component.html',
  styleUrls: ['./friends.component.css']
})
export class FriendsComponent implements OnInit {

  user: any;
  allFriends = [];
  acceptedRequests = [];
  sentRequests = [];
  receivedRequests = [];
  pageSize = 5;
  pageIndex = 0;
  loading = true;

  constructor(private userService: UserService, private authService: AuthenticationService,
    private router: Router, private appComponent: AppComponent, private dialog: MatDialog) { }

  ngOnInit() {
    const username = this.authService.getUsername();
    if (username) {
      this.userService.getUser(username).subscribe(
        (data: any) => {
          this.user = data;

          this.userService.getFriends(username).subscribe(
            (friendData: []) => {
              this.allFriends = friendData;
              // extracting only friend objects
              friendData.forEach((element: any) => {
                if (element.user1.id === this.user.id) {
                  // if he was a sender
                  if (element.accepted) {
                    this.acceptedRequests.push(element.user2);
                  } else {
                    this.sentRequests.push(element.user2);
                  }
                } else {
                  // if he got a request
                  if (element.accepted) {
                    this.acceptedRequests.push(element.user1);
                  } else {
                    this.receivedRequests.push(element.user1);
                  }
                }
              });
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

  acceptRequest(friend: any) {
    const dto = {
      'user1': friend,
      'user2': this.user
    };

    this.userService.acceptFriendRequest(dto).subscribe(
      () => {
        this.receivedRequests.splice(this.receivedRequests.indexOf(friend), 1);
        this.acceptedRequests.push(friend);
        this.appComponent.showSnackBar('You are now friends with \'' + friend.firstName + ' ' + friend.lastName + '\'!');
      },
      () => {
        this.appComponent.showSnackBar('Error while accepting request!');
      }
    );
  }

  deleteRequest(friend: any, type: number) {
    const dto = {
      'user1': null,
      'user2': null
    };

    // check who is sender and who is receiver of the request
    for (const f of this.allFriends) {
      if ((f.user1.id === this.user.id && f.user2.id === friend.id)) {
        dto.user1 = this.user;
        dto.user2 = friend;
        break;
      }

      if (f.user2.id === this.user.id && f.user1.id === friend.id) {
        dto.user1 = friend;
        dto.user2 = this.user;
        break;
      }
    }

    this.userService.deleteFriendRequest(dto).subscribe(
      () => {
        let message;
        const name = friend.firstName + ' ' + friend.lastName;

        if (type === 0) {
          const index = this.acceptedRequests.indexOf(friend);
          if (index >= 0) {
            this.acceptedRequests.splice(index, 1);
          }
          message = 'You are no longer friends with ' + name + '!';

        } else if (type === 1) {
          const index = this.sentRequests.indexOf(friend);
          if (index >= 0) {
            this.sentRequests.splice(index, 1);
          }
          message = 'Request to ' + name + ' canceled!';

        } else {
          const index = this.receivedRequests.indexOf(friend);
          if (index >= 0) {
            this.receivedRequests.splice(index, 1);
          }
          message = 'Request from ' + name + ' canceled!';
        }

        this.appComponent.showSnackBar(message);
      },
      () => {
        this.appComponent.showSnackBar('Error while canceling request!');
      }
    );
  }

  openSearchDialog() {
    const dialogRef = this.dialog.open(FriendsDialogComponent,
      {
        data: {
          'user': this.user,
          'friends': this.acceptedRequests.concat(this.sentRequests)
        },
        disableClose: true,
        autoFocus: true,
        width: '40%'
      });

    dialogRef.afterClosed().subscribe(
      (data: []) => {
        if (data.length) {
          for (const user of data) {
            const friend = {
              'user1': this.user,
              'user2': user,
              'accepted': false
            };
            this.userService.sendFriendRequest(friend).subscribe(
              (friendData) => {
                this.allFriends.push(friend);
                this.sentRequests.push(user);
              },
              () => {

              }
            );
          }
        }
      });
  }

}
