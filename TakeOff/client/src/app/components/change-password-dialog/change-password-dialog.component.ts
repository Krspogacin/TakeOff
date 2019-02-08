import { Component, OnInit, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { mustMatch } from '../registration-dialog/registration-dialog.component';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { UserService } from 'src/app/services/user/user.service';
import { AppComponent } from 'src/app/app.component';

@Component({
  selector: 'app-change-password-dialog',
  templateUrl: './change-password-dialog.component.html',
  styleUrls: ['./change-password-dialog.component.css']
})
export class ChangePasswordDialogComponent implements OnInit {

  passwordMinLength = 8;
  passwordMaxLength = 32;
  changePasswordForm: FormGroup;
  enabled: boolean;

  constructor(private authService: AuthenticationService,
              private userService: UserService,
              private dialogRef: MatDialogRef<ChangePasswordDialogComponent>,
              private formBuilder: FormBuilder,
              @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
    this.enabled = this.data.enabled;
    this.changePasswordForm = this.formBuilder.group({
      password: ['', [Validators.required,
                      Validators.minLength(this.passwordMinLength),
                      Validators.maxLength(this.passwordMaxLength)]],
      confirmPassword: ['', Validators.required]
      }, {
         validator: mustMatch('password', 'confirmPassword')
      }
    );

    if (this.enabled) {
      this.changePasswordForm.addControl('oldPassword', this.formBuilder.control('', Validators.required));
    }
  }

  submitForm() {
    if (this.enabled) {
      const user = {'username': this.authService.getUsername(),
                    'oldPassword': this.changePasswordForm.get('oldPassword').value,
                    'newPassword': this.changePasswordForm.get('password').value};
      this.userService.updatePassword(user).subscribe(
        () => {
          this.dialogRef.close();
          this.showSnackBar('You have changed your password successfully!');
        },
        () => {
          this.changePasswordForm.get('oldPassword').setErrors( { wrongOldPassword: true } );
        }
      );
    } else {
      if (this.changePasswordForm.valid) {
        this.dialogRef.close(this.changePasswordForm.get('password').value);
      }
    }
  }

  cancel() {
    this.dialogRef.close(null);
  }

  showSnackBar(message: string) {
    if (!message) {
      return;
    }
    const snackBarRef = this.snackBar.open(message, 'Dissmiss', { verticalPosition: 'top', duration: 4000 });
    snackBarRef.onAction().subscribe(
      () => {
        snackBarRef.dismiss();
      }
    );
  }
}
