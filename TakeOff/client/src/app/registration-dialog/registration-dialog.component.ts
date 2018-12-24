import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef } from '@angular/material';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-registration-dialog',
  templateUrl: './registration-dialog.component.html',
  styleUrls: ['./registration-dialog.component.css']
})
export class RegistrationDialogComponent implements OnInit {

  registrationForm: FormGroup;

  constructor(private dialogRef: MatDialogRef<RegistrationDialogComponent>) { }

  ngOnInit() {
    this.registrationForm = new FormGroup({
      username: new FormControl(),
      password: new FormControl(),
      email: new FormControl(),
      firstname: new FormControl(),
      lastname: new FormControl()
   });
  }
}
