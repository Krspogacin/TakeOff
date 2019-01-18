import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { FormGroup, Validators, FormBuilder, AbstractControl } from '@angular/forms';
import { RegistrationService } from 'src/app/services/registration/registration.service';

export interface Avatar {
  filename: string;
  filetype: string;
  value: string;
}

@Component({
  selector: 'app-registration-dialog',
  templateUrl: './registration-dialog.component.html',
  styleUrls: ['./registration-dialog.component.css']
})
export class RegistrationDialogComponent implements OnInit {

  passwordMinLength = 8;
  passwordMaxLength = 32;
  usernameMinLength = 5;
  usernameMaxLength = 25;
  aboutMeMaxLength = 200;
  registrationForm: FormGroup;
  selectedFile: File;
  imgSrc: string;

  constructor(private registrationService: RegistrationService,
              private dialogRef: MatDialogRef<RegistrationDialogComponent>,
              private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.registrationForm = this.formBuilder.group({
      username: ['', [Validators.required,
                      Validators.minLength(this.usernameMinLength),
                      Validators.maxLength(this.usernameMaxLength)]],
      password: ['', [Validators.required,
                      Validators.minLength(this.passwordMinLength),
                      Validators.maxLength(this.passwordMaxLength)]],
      confirmPassword: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      phoneNumber: ['', [Validators.required, Validators.pattern('^[0-9]{3}(-)[0-9]{3}$')]],
      address: ['', Validators.required],
      dateOfBirth: [],
      aboutMe: ['', Validators.maxLength(this.aboutMeMaxLength)]
      }, {
         validator: mustMatch('password', 'confirmPassword')
      });
  }

  onFileSelected(event) {
    if (event.target.files && event.target.files[0]) {
      this.selectedFile = <File>event.target.files[0];
      const reader = new FileReader();
      reader.onload = () => {
        this.imgSrc = reader.result.toString();
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }

  submitForm() {
    const element = document.getElementById('scrollId');
    element.scrollTo(0, 0);
    this.validateByUsername();
  }

  registerUserIfValid() {
    if (this.registrationForm.valid) {
      const userToRegister = this.registrationForm.value;
      userToRegister.image = this.imgSrc;
      delete userToRegister['confirmPassword'];
      this.dialogRef.close(userToRegister);
    }
  }

  validateByUsername() {
      const usernameControl: AbstractControl = this.registrationForm.get('username');
      this.registrationService.checkUserByUsername(usernameControl.value).subscribe(
        (data) => {
          usernameControl.setErrors(null);
        },
        (error) => {
          usernameControl.setErrors({ usernameExists: true });
        },
        () => {
          this.validateByEmail();
        });
  }

  validateByEmail() {
    const emailControl: AbstractControl = this.registrationForm.get('email');
    this.registrationService.checkUserByEmail(emailControl.value).subscribe(
        (data) => {
          emailControl.setErrors(null);
        },
        (error) => {
          emailControl.setErrors({ emailExists: true });
        },
        () => {
          this.registerUserIfValid();
        }
      );
  }
}

// custom validator to check that two fields match
export function mustMatch(controlName: string, matchingControlName: string) {
  return (formGroup: FormGroup) => {
      const control = formGroup.controls[controlName];
      const matchingControl = formGroup.controls[matchingControlName];

      if (matchingControl.errors && !matchingControl.errors.mustMatch) { return; }

      if (control.value !== matchingControl.value) {
          matchingControl.setErrors({ mustMatch: true });
      } else {
          matchingControl.setErrors(null);
      }
  };
}
