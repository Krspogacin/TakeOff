import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material';
import { FormGroup, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { RegistrationService } from 'src/app/services/registration/registration.service';

@Component({
  selector: 'app-sys-admin-dialog',
  templateUrl: './sys-admin-dialog.component.html',
  styleUrls: ['./sys-admin-dialog.component.css']
})
export class SysAdminDialogComponent implements OnInit {

  passwordMinLength = 8;
  passwordMaxLength = 32;
  usernameMinLength = 5;
  usernameMaxLength = 25;
  adminForm: FormGroup;

  constructor(private dialogRef: MatDialogRef<SysAdminDialogComponent>,
              private formBuilder: FormBuilder,
              private registrationService: RegistrationService) { }

  ngOnInit() {
    this.adminForm = this.formBuilder.group({
      username : ['', [Validators.required,
                       Validators.minLength(this.usernameMinLength),
                       Validators.maxLength(this.usernameMaxLength)]],
      password:  ['', [Validators.required,
                       Validators.minLength(this.passwordMinLength),
                       Validators.maxLength(this.passwordMaxLength)]],
    });
  }

  addAdmin(){
    this.validateByUsername();
  }

  validateByUsername() {
    const usernameControl: AbstractControl = this.adminForm.get('username');
    this.registrationService.checkUserByUsername(usernameControl.value).subscribe(
      (data) => {
        usernameControl.setErrors(null);
      },
      (error) => {
        usernameControl.setErrors({ usernameExists: true });
      },
      () => {
        this.addAdminIfValid();
      });
  }

  addAdminIfValid() {
    if (this.adminForm.valid) {
      const admin = this.adminForm.value;
      this.registrationService.addAdmin(admin).subscribe(
      )
      this.dialogRef.close();
    }
  }

}
