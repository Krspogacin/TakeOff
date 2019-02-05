import { Component, OnInit, Inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { mustMatch } from '../registration-dialog/registration-dialog.component';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-change-password-dialog',
  templateUrl: './change-password-dialog.component.html',
  styleUrls: ['./change-password-dialog.component.css']
})
export class ChangePasswordDialogComponent implements OnInit {

  passwordMinLength = 8;
  passwordMaxLength = 32;
  changePasswordForm: FormGroup;

  constructor(private authService: AuthenticationService,
              private userService: UserService,
              private dialogRef: MatDialogRef<ChangePasswordDialogComponent>,
              private formBuilder: FormBuilder,
              @Inject(MAT_DIALOG_DATA) public enabled: any) { }

  ngOnInit() {
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
                    'oldPassword': this.changePasswordForm.get('oldPassword'),
                    'newPassword': this.changePasswordForm.get('password')};
      this.userService.updatePassword(user).subscribe(
        () => {
          this.dialogRef.close();
        },
        () => {
          this.changePasswordForm.get('oldPassword').setErrors( { wrongOldPassword: true } );
        }
      );
    } else {
      if (this.changePasswordForm.valid) {
        this.dialogRef.close();
      }
    }
  }

  cancel() {
    this.dialogRef.close(null);
  }
}
