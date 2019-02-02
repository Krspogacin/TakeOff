import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/services/user/user.service';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { MatSnackBar } from '@angular/material';
import { Observable } from 'rxjs';
import { startWith, map } from 'rxjs/operators';

export interface User {
  id: string;
  password: string;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  address: string;
  dateOfBirth: string;
  aboutMe: string;
  image: string;
  enabled: boolean;
}

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  user: any;
  allUsers: User[] = [];
  friends = [];
  userExists = false;
  loadingUser = true;
  profileForm: FormGroup;
  aboutMeMaxLength = 200;
  changedForm = false;
  initForm: any;
  initDateOfBirth: string;
  message: string;

  constructor(private userService: UserService, private authService: AuthenticationService,
    private formBuilder: FormBuilder, private router: Router, private snackBar: MatSnackBar) { }

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
          // this.allUsers.filter(x => x.id !== this.user.id);

          console.log('all users:');
          console.log(this.allUsers);

          this.userService.getFriends(username).subscribe(
            (friendData: []) => {
              this.userExists = true;
              this.loadingUser = false;
              this.friends = friendData;

              // for (let i = 0; i < this.friends.length; i++) {
              //   const element = this.friends[i];
              //   this.allUsers.filter(x => x.id !== element.user1.id && x.id !== element.user2.id);
              // }

              if (this.user.image) {
                document.getElementById('profileImg').setAttribute('src', this.user.image);
              }
            }
          );


          this.profileForm = this.formBuilder.group({
            firstName: [this.user.firstName, Validators.required],
            lastName: [this.user.lastName, Validators.required],
            email: [{ value: this.user.email, disabled: true }],
            phoneNumber: [this.user.phoneNumber, [Validators.required, Validators.pattern('^[0-9]{3}(-)[0-9]{3}$')]],
            address: [this.user.address, Validators.required],
            dateOfBirth: [{ value: this.user.dateOfBirth, disabled: true }],
            aboutMe: [this.user.aboutMe, Validators.maxLength(this.aboutMeMaxLength)]
          });

          this.initForm = this.profileForm.value;
          // save the date separate because stupid format
          this.initDateOfBirth = new Date(this.user.dateOfBirth).toLocaleDateString();

          this.profileForm.valueChanges.subscribe(
            () => {
              if (JSON.stringify(this.initForm) !== JSON.stringify(this.profileForm.value)
                || this.initDateOfBirth !== new Date(this.profileForm.controls.dateOfBirth.value).toLocaleDateString()) {
                this.changedForm = true;
              } else {
                this.changedForm = false;
              }
            }
          );

        },
        () => {
          this.loadingUser = false;
        }
      );

    } else {
      this.router.navigate(['/']);
    }
  }

  updateUser() {

    const formValue = this.profileForm.value;
    const newUser = JSON.parse(JSON.stringify(this.user));

    newUser.firstName = formValue.firstName;
    newUser.lastName = formValue.lastName;
    newUser.phoneNumber = formValue.phoneNumber;
    newUser.address = formValue.address;
    newUser.aboutMe = formValue.aboutMe;

    // again playing with dates :)
    const date = this.profileForm.controls.dateOfBirth.value;
    // newUser.dateOfBirth = new Date(date.getTime() - (date.getTimezoneOffset() * 60000)).toISOString().split('T')[0];

    if (date) {
      newUser.dateOfBirth = new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate()));

      console.log(new Date(date));
      console.log(newUser.dateOfBirth);
    }

    this.userService.updateUser(newUser).subscribe(
      (data) => {
        this.user = data;
        this.initForm = this.profileForm.value;
        this.initDateOfBirth = new Date(this.user.dateOfBirth).toLocaleDateString();
        this.changedForm = false;
        this.message = 'Changes successfully saved!';
        this.showSnackBar();
      },
      () => {
        this.message = 'Error while saving changes!';
        this.showSnackBar();
      }
    );

  }

  sendFriendRequest(user2: any) {
    const friend = {
      'user1': this.user,
      'user2': user2,
      'accepted': false
    };
    this.userService.sendFriendRequest(friend).subscribe(
      (data) => {
        this.friends.push(data);
        this.message = 'Request successfully sent!';
        this.showSnackBar();
      },
      () => {
        this.message = 'Error while sending request!';
        this.showSnackBar();
      }
    );
  }

  acceptRequest(friend: any) {
    this.userService.acceptFriendRequest(friend).subscribe(
      () => {
        friend.accepted = true;
        this.message = 'You are now friends with \'' + friend.user1.firstName + ' ' + friend.user1.lastName + '\'!';
        this.showSnackBar();
      },
      () => {
        this.message = 'Error while accepting request!';
        this.showSnackBar();
      }
    );
  }

  deleteRequest(friend: any) {
    this.userService.deleteFriendRequest(friend).subscribe(
      () => {
        const index = this.friends.indexOf(friend);
        if (index >= 0) {
          this.friends.splice(index, 1);
        }
        const sender = this.user.id === friend.user1.id;
        const name = sender ? friend.user2.firstName + ' ' + friend.user2.lastName
          : friend.user1.firstName + ' ' + friend.user1.lastName;

        if (friend.accepted) {
          this.message = 'You are no longer friends with \'' + name + '\'!';
        } else {
          this.message = 'Request ' + (sender ? 'to \'' : 'from \'') + name + '\' canceled!';
        }
        this.showSnackBar();
      },
      () => {
        this.message = 'Error while canceling request!';
        this.showSnackBar();
      }
    );
  }

  showSnackBar() {
    if (this.message) {
      const snackBarRef = this.snackBar.open(this.message, 'Dismiss', { duration: 3000 });
      snackBarRef.onAction().subscribe(
        () => {
          snackBarRef.dismiss();
        }
      );
    }
  }

}
