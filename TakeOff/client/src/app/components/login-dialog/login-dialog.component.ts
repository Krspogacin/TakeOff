import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { MatDialogRef } from '@angular/material';

@Component({
  selector: 'app-login-dialog',
  templateUrl: './login-dialog.component.html',
  styleUrls: ['./login-dialog.component.css']
})
export class LoginDialogComponent implements OnInit {

  loginForm: FormGroup;
  data: any;

  constructor(private authenticationService: AuthenticationService,
              private dialogRef: MatDialogRef<LoginDialogComponent>,
              private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({ username: [], password: [] });
  }

  submitForm() {
    const usernameControl: AbstractControl = this.loginForm.get('username');
    const passwordControl: AbstractControl = this.loginForm.get('password');
    this.authenticationService.login(this.loginForm.value).subscribe(
      (data) => {
        usernameControl.setErrors(null);
        passwordControl.setErrors(null);
        this.data = data;
        this.loginUserIfExists();
      },
      (error) => {
        usernameControl.setErrors( { loginFailed: true } );
        passwordControl.setErrors( { loginFailed: true } );
      }
    );
  }

  loginUserIfExists() {
    if (this.loginForm.valid) {
      this.dialogRef.close(this.data);
    }
  }
}
