import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { MatDialogRef, MatDialog } from '@angular/material';
import { ChangePasswordDialogComponent } from '../change-password-dialog/change-password-dialog.component';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-login-dialog',
  templateUrl: './login-dialog.component.html',
  styleUrls: ['./login-dialog.component.css']
})
export class LoginDialogComponent implements OnInit {

  loginForm: FormGroup;

  constructor(private authenticationService: AuthenticationService,
              private dialogRef: MatDialogRef<LoginDialogComponent>,
              private formBuilder: FormBuilder,
              private dialog: MatDialog,
              private userService: UserService) { }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({ username: [], password: [] });
  }

  submitForm() {
    this.userService.checkUserType(this.loginForm.value).subscribe(
      (userType: any) => {
        if (userType.authority !== 'ROLE_USER' && !userType.isEnabled) {
          const dialogRef = this.dialog.open(ChangePasswordDialogComponent,
          {
            data: userType.isEnabled,
            disableClose: true,
            autoFocus: true,
            width: '30%'
          });
          dialogRef.afterClosed().subscribe(
            (newPassword) => {
              if (newPassword) {
                const user = {'username': this.loginForm.get('username'),
                              'oldPassword': this.loginForm.get('password'),
                              'newPassword': newPassword};
                this.userService.updatePassword(user).subscribe(
                  () => {
                    delete user['oldPassword'];
                    this.loginUser(user);
                  },
                  () => {
                    this.loginForm.get('username').setErrors( { loginFailed: true } );
                    this.loginForm.get('password').setErrors( { loginFailed: true } );
                  }
                );
              }
            }
          );
        } else {
          this.loginUser(this.loginForm.value);
        }
      },
      () => {
        this.loginForm.get('username').setErrors( { loginFailed: true } );
        this.loginForm.get('password').setErrors( { loginFailed: true } );
      }
    );
  }

  loginUser(user: any) {
    this.authenticationService.login(user).subscribe(
      (data: any) => {
        this.dialogRef.close(data);
      },
      () => {
        this.loginForm.get('username').setErrors( { loginFailed: true } );
        this.loginForm.get('password').setErrors( { loginFailed: true } );
      }
    );
  }
}
