import { Component, OnInit, Inject} from '@angular/core';
import {MatDialog} from '@angular/material';
import { RegistrationDialogComponent } from 'src/app/registration-dialog/registration-dialog.component';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent {

  constructor(public dialog: MatDialog) { }

  openRegistrationDialog() {
    const dialogRef = this.dialog.open(RegistrationDialogComponent, { disableClose: true });
    dialogRef.afterClosed().subscribe(
      data => console.log('Dialog output:', data)
    );
  }
}
