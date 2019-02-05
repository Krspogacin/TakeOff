import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AppComponent } from 'src/app/app.component';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  user: any;
  userExists = false;
  loadingUser = true;
  profileForm: FormGroup;
  aboutMeMaxLength = 200;
  changedForm = false;
  initForm: any;
  initDateOfBirth: string;
  message: string;

  constructor(private userService: UserService, private authService: AuthenticationService,
    private formBuilder: FormBuilder, private router: Router, private appComponent: AppComponent) { }

  ngOnInit() {
    const username = this.authService.getUsername();
    if (username) {

      this.userService.getUser(username).subscribe(
        (data: any) => {
          this.user = data;
          this.loadingUser = false;
          this.userExists = true;

          if (this.user.image) {
            document.getElementById('profileImg').setAttribute('src', this.user.image);
          }

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
    if (date instanceof Date) {
      newUser.dateOfBirth = new Date(Date.UTC(date.getFullYear(), date.getMonth(), date.getDate()));
    }

    this.userService.updateUser(newUser).subscribe(
      (data) => {
        this.user = data;
        this.initForm = this.profileForm.value;
        this.initDateOfBirth = new Date(this.user.dateOfBirth).toLocaleDateString();
        this.changedForm = false;
        this.appComponent.showSnackBar('Changes successfully saved!');
      },
      () => {
        this.appComponent.showSnackBar('Error while saving changes!');
      }
    );

  }
}
