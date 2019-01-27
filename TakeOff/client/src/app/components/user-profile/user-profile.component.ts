import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user/user.service';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  user: any;
  friends = [];
  userExists = false;
  loadingUser = true;

  constructor(private userService: UserService, private authService: AuthenticationService) { }

  ngOnInit() {
    const username = this.authService.getUsername();
    if (username) {
      this.userService.getUser(username).subscribe(
        (data: any) => {
          this.user = data;
          this.userService.getFriends(username).subscribe(
            (friendData: []) => {
              this.friends = friendData;
              this.userExists = true;
              this.loadingUser = false;

              if (this.user.image) {
                document.getElementById('profileImg').setAttribute('src', this.user.image);
              }
            }
          );
        },
        () => {
          this.loadingUser = false;
        }
      );
    }
  }

}
