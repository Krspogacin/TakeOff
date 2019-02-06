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
  disableLogin: boolean;

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
        if (userType.authority !== 'ROLE_USER' && !userType.enabled) {
          const dialogRef = this.dialog.open(ChangePasswordDialogComponent,
          {
            data: {'enabled': userType.enabled},
            disableClose: true,
            autoFocus: true,
            width: '40%'
          });
          dialogRef.afterClosed().subscribe(
            (newPassword) => {
              if (newPassword) {
                this.disableLogin = true;
                const user = {'username': this.loginForm.get('username').value,
                              'oldPassword': this.loginForm.get('password').value,
                              'newPassword': newPassword};
                this.userService.updatePassword(user).subscribe(
                  () => {
                    const userToLogin = {'username': user.username, 'password': user.newPassword};
                    this.loginUser(userToLogin);
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
